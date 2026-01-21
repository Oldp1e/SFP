package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.investment.InvestmentInputDTO;
import com.oldp1e.sfp.dto.investment.InvestmentResponseDTO;
import com.oldp1e.sfp.service.InvestmentService;
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
@RequestMapping("/investments")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Investimentos", description = "Gerenciar investimentos e aplicações")
public class InvestmentController {

    private final InvestmentService investmentService;

    public InvestmentController(InvestmentService investmentService) {
        this.investmentService = investmentService;
    }

    @PostMapping
    @Operation(
        summary = "Registrar novo investimento",
        description = "Registra um novo investimento para o usuário autenticado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Investimento registrado"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<InvestmentResponseDTO> create(@Valid @RequestBody InvestmentInputDTO request) {
        InvestmentResponseDTO response = investmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar investimentos",
        description = "Lista todos os investimentos do usuário autenticado"
    )
    @ApiResponse(responseCode = "200", description = "Lista de investimentos")
    public ResponseEntity<List<InvestmentResponseDTO>> getAll() {
        List<InvestmentResponseDTO> response = investmentService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um investimento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Investimento encontrado"),
        @ApiResponse(responseCode = "404", description = "Investimento não encontrado")
    })
    public ResponseEntity<InvestmentResponseDTO> getById(@PathVariable UUID id) {
        InvestmentResponseDTO response = investmentService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar investimento")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Investimento atualizado"),
        @ApiResponse(responseCode = "404", description = "Investimento não encontrado")
    })
    public ResponseEntity<InvestmentResponseDTO> update(
        @PathVariable UUID id,
        @Valid @RequestBody InvestmentInputDTO request
    ) {
        InvestmentResponseDTO response = investmentService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar investimento")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Investimento deletado"),
        @ApiResponse(responseCode = "404", description = "Investimento não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        investmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
