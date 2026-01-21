package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.account.AccountInputDTO;
import com.oldp1e.sfp.dto.account.AccountResponseDTO;
import com.oldp1e.sfp.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Contas", description = "Gerenciar contas bancárias e carteiras")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @Operation(
        summary = "Criar nova conta",
        description = "Cria uma nova conta bancária ou carteira para o usuário autenticado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Conta criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<AccountResponseDTO> create(@Valid @RequestBody AccountInputDTO request) {
        AccountResponseDTO response = accountService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar todas as contas",
        description = "Lista todas as contas do usuário autenticado"
    )
    @ApiResponse(responseCode = "200", description = "Lista de contas")
    public ResponseEntity<List<AccountResponseDTO>> getAll() {
        List<AccountResponseDTO> response = accountService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter detalhes de uma conta",
        description = "Retorna os detalhes de uma conta específica do usuário autenticado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conta encontrada"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<AccountResponseDTO> getById(@PathVariable UUID id) {
        AccountResponseDTO response = accountService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Atualizar conta",
        description = "Atualiza os dados de uma conta específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conta atualizada"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<AccountResponseDTO> update(
        @PathVariable UUID id,
        @Valid @RequestBody AccountInputDTO request
    ) {
        AccountResponseDTO response = accountService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Deletar conta",
        description = "Remove uma conta do sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Conta deletada"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
