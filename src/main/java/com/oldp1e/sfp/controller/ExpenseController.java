package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.expense.ExpenseInputDTO;
import com.oldp1e.sfp.dto.expense.ExpenseResponseDTO;
import com.oldp1e.sfp.service.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/expenses")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Despesas", description = "Gerenciar despesas e saídas")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    @Operation(
        summary = "Criar nova despesa",
        description = "Registra uma nova despesa/saída para o usuário autenticado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Despesa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<ExpenseResponseDTO> create(@Valid @RequestBody ExpenseInputDTO request) {
        ExpenseResponseDTO response = expenseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar despesas",
        description = "Lista despesas do usuário autenticado, com filtros opcionais por mês, ano e tipo"
    )
    @ApiResponse(responseCode = "200", description = "Lista de despesas")
    public ResponseEntity<List<ExpenseResponseDTO>> getAll(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) String type
    ) {
        List<ExpenseResponseDTO> response = expenseService.getAll(month, year, type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obter detalhes de uma despesa",
        description = "Retorna os detalhes de uma despesa específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Despesa encontrada"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    public ResponseEntity<ExpenseResponseDTO> getById(@PathVariable UUID id) {
        ExpenseResponseDTO response = expenseService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar despesa")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Despesa atualizada"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    public ResponseEntity<ExpenseResponseDTO> update(
        @PathVariable UUID id,
        @Valid @RequestBody ExpenseInputDTO request
    ) {
        ExpenseResponseDTO response = expenseService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar despesa")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Despesa deletada"),
        @ApiResponse(responseCode = "404", description = "Despesa não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        expenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
