package com.user.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private List<UserPreferenceDTO> preferences;
}
