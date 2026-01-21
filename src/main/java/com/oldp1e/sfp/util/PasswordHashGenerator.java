package com.oldp1e.sfp.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilitário para gerar hash BCrypt de senhas
 * Usado para criar senhas dos usuários seed
 */
public class PasswordHashGenerator {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

        // Gerar hash para senha: oldp1e@2025
        String password = "oldp1e@2025";
        String hash = encoder.encode(password);

        System.out.println("===========================================");
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("===========================================");

        // Validar que o hash funciona
        boolean matches = encoder.matches(password, hash);
        System.out.println("Validation: " + (matches ? "✅ VALID" : "❌ INVALID"));
    }
}
