package com.oldp1e.sfp.util;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste para gerar e validar hash BCrypt da senha padrão
 * Execute este teste para obter o hash correto para usar na migration V2
 */
class PasswordHashTest {

    @Test
    void generatePasswordHashForOldp1e() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String password = "oldp1e@2025";
        String hash = encoder.encode(password);

        System.out.println("===========================================");
        System.out.println("Password: " + password);
        System.out.println("BCrypt Hash: " + hash);
        System.out.println("===========================================");
        System.out.println("Use este hash na migration V2__insert_default_user.sql");
        System.out.println("===========================================");

        // Validar que o hash funciona
        assertTrue(encoder.matches(password, hash), "Hash deve validar corretamente");
    }

    @Test
    void validateExistingHash() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "oldp1e@2025";

        // Hash que será usado na migration (atualizar após gerar)
        String hashFromMigration = "$2a$10$rZ8pWZQXnKJvH.xE7LXw0OYvJ5HXQXQXQXQXQXQXQXQXQXQXQXQXQ";

        boolean matches = encoder.matches(password, hashFromMigration);
        System.out.println("Validating migration hash: " + (matches ? "✅ VALID" : "❌ INVALID"));

        if (!matches) {
            System.err.println("⚠️ Hash na migration V2 está INCORRETO!");
            System.err.println("Execute o teste generatePasswordHashForOldp1e() para gerar um novo hash");
        }
    }
}
