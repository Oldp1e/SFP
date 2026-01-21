package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.UserResponseDTO;
import com.oldp1e.sfp.entity.User;
import com.oldp1e.sfp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }
        User user = (User) authentication.getPrincipal();
        UserResponseDTO response = userService.getUserById(user.getId());
        return ResponseEntity.ok(response);
    }
}
