package com.oldp1e.sfp.config;

import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Customizações globais de OpenAPI.
 * Adiciona respostas de erro padronizadas e exemplos comuns.
 *
 * @author SFP Team
 * @version 1.0.0
 */
@Configuration
public class OpenApiCustomizerConfig {

    /**
     * Customizador para adicionar respostas de erro padrão em todos os endpoints.
     */
    @Bean
    public OpenApiCustomizer openApiCustomizer() {
        return openApi -> {
            // Aqui você pode adicionar customizações globais se desejar
            // Por exemplo: adicionar respostas padrão 5xx, cabeçalhos comuns, etc.

            // Exemplo: adicionar header X-RateLimit em todas as respostas
            if (openApi.getPaths() != null) {
                openApi.getPaths().values().forEach(pathItem -> {
                    pathItem.readOperations().forEach(operation -> {
                        // Customizações por operação podem ser feitas aqui
                        // Ex: adicionar exemplos padrão, documentação extra, etc.
                    });
                });
            }
        };
    }

    /**
     * Cria uma resposta de erro padrão para 400 Bad Request.
     */
    public static ApiResponse createBadRequestResponse() {
        ObjectSchema schema = new ObjectSchema();
        schema.addProperty("status", new IntegerSchema().example(400));
        schema.addProperty("message", new StringSchema().example("Requisição inválida"));
        schema.addProperty("timestamp", new StringSchema().example("2025-01-20T10:30:00"));

        MediaType mediaType = new MediaType().schema(schema);
        return new ApiResponse()
                .description("Requisição inválida - verifique os parâmetros enviados")
                .content(new Content().addMediaType("application/json", mediaType));
    }

    /**
     * Cria uma resposta de erro padrão para 401 Unauthorized.
     */
    public static ApiResponse createUnauthorizedResponse() {
        ObjectSchema schema = new ObjectSchema();
        schema.addProperty("status", new IntegerSchema().example(401));
        schema.addProperty("message", new StringSchema().example("Token JWT ausente ou inválido"));
        schema.addProperty("timestamp", new StringSchema().example("2025-01-20T10:30:00"));

        MediaType mediaType = new MediaType().schema(schema);
        return new ApiResponse()
                .description("Não autorizado - token JWT ausente, inválido ou credenciais incorretas")
                .content(new Content().addMediaType("application/json", mediaType));
    }

    /**
     * Cria uma resposta de erro padrão para 403 Forbidden.
     */
    public static ApiResponse createForbiddenResponse() {
        ObjectSchema schema = new ObjectSchema();
        schema.addProperty("status", new IntegerSchema().example(403));
        schema.addProperty("message", new StringSchema().example("Token expirado"));
        schema.addProperty("timestamp", new StringSchema().example("2025-01-20T10:30:00"));

        MediaType mediaType = new MediaType().schema(schema);
        return new ApiResponse()
                .description("Acesso proibido - token expirado ou sem permissão suficiente")
                .content(new Content().addMediaType("application/json", mediaType));
    }

    /**
     * Cria uma resposta de erro padrão para 500 Internal Server Error.
     */
    public static ApiResponse createInternalServerErrorResponse() {
        ObjectSchema schema = new ObjectSchema();
        schema.addProperty("status", new IntegerSchema().example(500));
        schema.addProperty("message", new StringSchema().example("Erro interno do servidor"));
        schema.addProperty("timestamp", new StringSchema().example("2025-01-20T10:30:00"));

        MediaType mediaType = new MediaType().schema(schema);
        return new ApiResponse()
                .description("Erro interno do servidor - contate o suporte")
                .content(new Content().addMediaType("application/json", mediaType));
    }

}
