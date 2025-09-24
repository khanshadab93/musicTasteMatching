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
        List<User> similarUsers = userRepository.findUsersRankedByGenreMatches(userId);

        return similarUsers.stream()
                .map(u -> new UserDTO(
                        u.getId().toString(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getPreferences().stream()
                                .map(p -> new UserPreferenceDTO(p.getGenre(), p.getArtist(), p.getRating()))
                                .toList()
                ))
                .toList();
    }
}
