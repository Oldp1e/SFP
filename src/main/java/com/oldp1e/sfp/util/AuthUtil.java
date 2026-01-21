package com.oldp1e.sfp.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthUtil {

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // O principal é o User entity que tem um getId() method
            Object principal = authentication.getPrincipal();
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                // Spring Security User - extrair do username (que é o email)
                // Neste caso, vamos recuperar do contexto de autenticação
                // Você pode armazenar o UUID no token ou no UserDetails customizado
                throw new IllegalStateException("User ID não disponível no contexto de autenticação");
            }
            // Se tiver um UserDetails customizado com getId()
            if (principal instanceof com.oldp1e.sfp.entity.User) {
                return ((com.oldp1e.sfp.entity.User) principal).getId();
            }
        }
        throw new IllegalStateException("Usuário não autenticado");
    }
}
