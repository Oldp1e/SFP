package com.oldp1e.sfp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;

/**
 * Configuração centralizada de OpenAPI/Swagger.
 * Define metadados, esquemas de segurança e agrupamentos de endpoints.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

    @Value("${api.doc.title:SFP - Sistema Financeiro Particular}")
    private String title;

    @Value("${api.doc.description:API REST para gerenciamento de finanças pessoais com autenticação JWT}")
    private String description;

    @Value("${api.doc.version:1.0.0}")
    private String version;

    @Value("${api.doc.contact-name:Suporte SFP}")
    private String contactName;

    @Value("${api.doc.contact-email:support@sfp.com}")
    private String contactEmail;

    @Value("${api.doc.contact-url:https://sfp.com}")
    private String contactUrl;

    @Value("${api.doc.license-name:Apache 2.0}")
    private String licenseName;

    @Value("${api.doc.license-url:https://www.apache.org/licenses/LICENSE-2.0.html}")
    private String licenseUrl;

    /**
     * Configuração principal do OpenAPI com metadados dinâmicos e segurança JWT.
     */
    @Bean
    public OpenAPI sfpOpenAPI() {
        Contact contact = new Contact()
                .name(contactName)
                .email(contactEmail)
                .url(contactUrl);

        License license = new License()
                .name(licenseName)
                .url(licenseUrl);

        Info info = new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(contact)
                .license(license);

        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Token JWT gerado no endpoint de login. " +
                        "Use o formato: Authorization: Bearer {token}");

        return new OpenAPI()
                .info(info)
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", securityScheme));
    }

    /**
     * Grupo de documentação para endpoints de Autenticação.
     * Endpoints: /auth/login, /auth/register
     */
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Autenticação")
                .displayName("Endpoints de Autenticação")
                .pathsToMatch("/auth/**")
                .build();
    }

    /**
     * Grupo de documentação para endpoints de Usuários.
     * Endpoints: /users/me, /users/{id}
     */
    @Bean
    public GroupedOpenApi usersApi() {
        return GroupedOpenApi.builder()
                .group("Usuários")
                .displayName("Endpoints de Gerenciamento de Usuários")
                .pathsToMatch("/users/**")
                .build();
    }

    /**
     * Grupo de documentação para Contas.
     * Endpoints: /accounts/**
     */
    @Bean
    public GroupedOpenApi accountsApi() {
        return GroupedOpenApi.builder()
                .group("Contas")
                .displayName("Gerenciar Contas Bancárias e Carteiras")
                .pathsToMatch("/accounts/**")
                .build();
    }

    /**
     * Grupo de documentação para Receitas.
     * Endpoints: /incomes/**
     */
    @Bean
    public GroupedOpenApi incomesApi() {
        return GroupedOpenApi.builder()
                .group("Receitas")
                .displayName("Gerenciar Receitas e Entradas")
                .pathsToMatch("/incomes/**")
                .build();
    }

    /**
     * Grupo de documentação para Despesas.
     * Endpoints: /expenses/**
     */
    @Bean
    public GroupedOpenApi expensesApi() {
        return GroupedOpenApi.builder()
                .group("Despesas")
                .displayName("Gerenciar Despesas e Saídas")
                .pathsToMatch("/expenses/**")
                .build();
    }

    /**
     * Grupo de documentação para Cartões de Crédito.
     * Endpoints: /credit-cards/**
     */
    @Bean
    public GroupedOpenApi creditCardsApi() {
        return GroupedOpenApi.builder()
                .group("Cartões de Crédito")
                .displayName("Gerenciar Cartões de Crédito e Transações")
                .pathsToMatch("/credit-cards/**")
                .build();
    }

    /**
     * Grupo de documentação para Investimentos.
     * Endpoints: /investments/**
     */
    @Bean
    public GroupedOpenApi investmentsApi() {
        return GroupedOpenApi.builder()
                .group("Investimentos")
                .displayName("Gerenciar Investimentos e Aplicações")
                .pathsToMatch("/investments/**")
                .build();
    }

    /**
     * Grupo de documentação para Balanço.
     * Endpoints: /balance/**
     */
    @Bean
    public GroupedOpenApi balanceApi() {
        return GroupedOpenApi.builder()
                .group("Balanço")
                .displayName("Visualizar Balanço e Resumos Financeiros")
                .pathsToMatch("/balance/**")
                .build();
    }

    /**
     * Grupo de documentação para Simulações.
     * Endpoints: /simulations/**
     */
    @Bean
    public GroupedOpenApi simulationsApi() {
        return GroupedOpenApi.builder()
                .group("Simulações")
                .displayName("Simular Cenários Financeiros e Compras")
                .pathsToMatch("/simulations/**")
                .build();
    }

}
