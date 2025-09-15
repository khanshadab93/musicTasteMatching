package com.user.matching.service;

import com.user.matching.MatchConfig;
import com.user.matching.dto.UserDTO;
import com.user.matching.dto.UserPreferenceDTO;
import com.user.matching.entity.User;
import com.user.matching.entity.UserPreference;
import com.user.matching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepository;
    private final MatchConfig matchConfig;

    public List<UserDTO> findMatches(UUID userId) {
        User currentUser = userRepository.findById(userId).orElseThrow();

        // Precompute current user's genres
        Set<String> currentGenres = currentUser.getPreferences()
                .stream()
                .map(UserPreference::getGenre)
                .collect(Collectors.toSet());

        // Fetch all users with preferences
        List<User> allUsers = userRepository.findAllWithPreferences();

        // Optional: custom parallelism level
        ForkJoinPool customThreadPool = new ForkJoinPool(matchConfig.getThreads());
        try {
            return customThreadPool.submit(() ->
                    allUsers.parallelStream()
                            .filter(u -> !u.getId().equals(userId))
                            .map(u -> new UserScore(u, calculateScore(currentGenres, u.getPreferences())))
                            .sorted((a, b) -> Integer.compare(b.score, a.score)) // descending
                            .map(us -> new UserDTO(
                                    us.user.getId().toString(),
                                    us.user.getUsername(),
                                    us.user.getEmail(),
                                    us.user.getPreferences().stream()
                                            .map(p -> new UserPreferenceDTO(p.getGenre(), p.getArtist(), p.getRating()))
                                            .toList()
                            ))
                            .toList()
            ).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            customThreadPool.shutdown();
        }
    }

    private int calculateScore(Set<String> currentGenres, List<UserPreference> otherPrefs) {
        Set<String> otherGenres = otherPrefs.stream()
                .map(UserPreference::getGenre)
                .collect(Collectors.toSet()); // convert list to set
        otherGenres.retainAll(currentGenres);
        return otherGenres.size();
    }

    // Helper class to store user + score
    private static class UserScore {
        private final User user;
        private final int score;

        private UserScore(User user, int score) {
            this.user = user;
            this.score = score;
        }
    }
}
