package com.oldp1e.sfp.dto;

import com.oldp1e.sfp.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private UUID id;
    private String email;
    private String fullName;
    private Boolean isActive;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
}
