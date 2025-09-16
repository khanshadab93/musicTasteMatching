package com.user.matching.controller;

import com.user.matching.dto.UserDTO;
import com.user.matching.entity.User;
import com.user.matching.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
@Slf4j
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getMatches(@RequestParam UUID userId) {
        long startTime = System.currentTimeMillis();
        List<UserDTO> matches = matchService.findMatches(userId);
        return ResponseEntity.ok(matches);
    }
}
