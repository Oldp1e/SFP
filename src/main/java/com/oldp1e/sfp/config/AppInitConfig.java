package com.oldp1e.sfp.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração de inicialização da aplicação
 * Útil para logs e validações no startup
 */
@Configuration
@Slf4j
public class AppInitConfig {

    /**
     * Runner que exibe informações úteis no startup (apenas DEV)
     */
    @Bean
    @Profile("dev")
    public CommandLineRunner logStartupInfo(PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("=".repeat(60));
            log.info("🚀 SFP - Sistema Financeiro Pessoal");
            log.info("=".repeat(60));
            log.info("📌 Perfil Ativo: DEV");
            log.info("💾 Banco de Dados: H2 in-memory");
            log.info("🔄 Flyway: Migrations executadas automaticamente");
            log.info("=".repeat(60));
            log.info("👤 Usuário Padrão:");
            log.info("   Email: oldp1e@sfp.com");
            log.info("   Senha: oldp1e@2025");
            log.info("   Role: ADMIN");
            log.info("=".repeat(60));
            log.info("🌐 Console H2: http://localhost:8080/h2");
            log.info("   JDBC URL: jdbc:h2:mem:sfp_dev");
            log.info("   Username: sa");
            log.info("   Password: (vazio)");
            log.info("=".repeat(60));
            log.info("📡 API Endpoints:");
            log.info("   POST /auth/login      - Login");
            log.info("   POST /auth/register   - Registro");
            log.info("   GET  /users/me        - Dados do usuário (protegido)");
            log.info("=".repeat(60));

            // Gerar hash correto para a senha oldp1e@2025
            String correctHash = passwordEncoder.encode("oldp1e@2025");
            log.info("🔐 BCrypt Hash para 'oldp1e@2025':");
            log.info("   {}", correctHash);
            log.info("   (Use este hash na migration V2 se necessário)");
            log.info("=".repeat(60));
        };
    }
}
