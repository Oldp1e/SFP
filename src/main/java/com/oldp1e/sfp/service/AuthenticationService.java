package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.LoginRequestDTO;
import com.oldp1e.sfp.dto.LoginResponseDTO;
import com.oldp1e.sfp.dto.RegisterRequestDTO;
import com.oldp1e.sfp.dto.UserResponseDTO;
import com.oldp1e.sfp.entity.User;
import com.oldp1e.sfp.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }

        User user = (User) userService.loadUserByUsername(request.getEmail());
        String jwtToken = jwtService.generateToken(user);

        // Update last login
        userService.updateLastLogin(request.getEmail());

        UserResponseDTO userResponse = userService.getUserByEmail(request.getEmail());

        return new LoginResponseDTO(jwtToken, userResponse);
    }

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO request) {
        return userService.createUser(request);
    }
}
