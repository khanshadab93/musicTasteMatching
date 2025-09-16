package com.user.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPreferenceDTO {
    private String genre;
    private String artist;
    private int rating;
}
