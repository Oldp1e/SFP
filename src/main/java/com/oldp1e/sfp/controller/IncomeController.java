package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.income.IncomeInputDTO;
import com.oldp1e.sfp.dto.income.IncomeResponseDTO;
import com.oldp1e.sfp.service.IncomeService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/incomes")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Receitas", description = "Gerenciar receitas e entradas")
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    @Operation(
        summary = "Criar nova receita",
        description = "Registra uma nova receita/entrada para o usuário autenticado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Receita criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<IncomeResponseDTO> create(@Valid @RequestBody IncomeInputDTO request) {
        IncomeResponseDTO response = incomeService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar receitas",
        description = "Lista receitas do usuário autenticado, com filtros opcionais por mês e ano"
    )
    @ApiResponse(responseCode = "200", description = "Lista de receitas")
    public ResponseEntity<List<IncomeResponseDTO>> getAll(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        List<IncomeResponseDTO> response = incomeService.getAll(month, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter detalhes de uma receita",
        description = "Retorna os detalhes de uma receita específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Receita encontrada"),
        @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    public ResponseEntity<IncomeResponseDTO> getById(@PathVariable UUID id) {
        IncomeResponseDTO response = incomeService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar receita")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Receita atualizada"),
        @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    public ResponseEntity<IncomeResponseDTO> update(
        @PathVariable UUID id,
        @Valid @RequestBody IncomeInputDTO request
    ) {
        IncomeResponseDTO response = incomeService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar receita")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Receita deletada"),
        @ApiResponse(responseCode = "404", description = "Receita não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        incomeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
