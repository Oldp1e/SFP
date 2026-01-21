package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.creditcard.CreditCardInputDTO;
import com.oldp1e.sfp.dto.creditcard.CreditCardResponseDTO;
import com.oldp1e.sfp.dto.creditcard.CreditCardTransactionInputDTO;
import com.oldp1e.sfp.dto.creditcard.CreditCardTransactionResponseDTO;
import com.oldp1e.sfp.service.CreditCardService;
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
@RequestMapping("/credit-cards")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Cartões de Crédito", description = "Gerenciar cartões de crédito e suas transações")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @PostMapping
    @Operation(
        summary = "Criar novo cartão de crédito",
        description = "Registra um novo cartão de crédito para o usuário autenticado"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cartão criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<CreditCardResponseDTO> create(@Valid @RequestBody CreditCardInputDTO request) {
        CreditCardResponseDTO response = creditCardService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar cartões de crédito",
        description = "Lista todos os cartões do usuário autenticado"
    )
    @ApiResponse(responseCode = "200", description = "Lista de cartões")
    public ResponseEntity<List<CreditCardResponseDTO>> getAll() {
        List<CreditCardResponseDTO> response = creditCardService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de um cartão")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cartão encontrado"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    public ResponseEntity<CreditCardResponseDTO> getById(@PathVariable UUID id) {
        CreditCardResponseDTO response = creditCardService.getById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cartão de crédito")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cartão atualizado"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    public ResponseEntity<CreditCardResponseDTO> update(
        @PathVariable UUID id,
        @Valid @RequestBody CreditCardInputDTO request
    ) {
        CreditCardResponseDTO response = creditCardService.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cartão de crédito")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cartão deletado"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        creditCardService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Transações de cartão
    @PostMapping("/{creditCardId}/transactions")
    @Operation(
        summary = "Registrar compra no cartão",
        description = "Registra uma nova transação/compra no cartão de crédito"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transação registrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cartão não encontrado")
    })
    public ResponseEntity<CreditCardTransactionResponseDTO> createTransaction(
        @PathVariable UUID creditCardId,
        @Valid @RequestBody CreditCardTransactionInputDTO request
    ) {
        CreditCardTransactionResponseDTO response = creditCardService.createTransaction(creditCardId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{creditCardId}/transactions")
    @Operation(
        summary = "Listar transações do cartão",
        description = "Lista todas as transações de um cartão de crédito específico"
    )
    @ApiResponse(responseCode = "200", description = "Lista de transações")
    public ResponseEntity<List<CreditCardTransactionResponseDTO>> getTransactions(
        @PathVariable UUID creditCardId
    ) {
        List<CreditCardTransactionResponseDTO> response = creditCardService.getTransactions(creditCardId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{creditCardId}/transactions/{transactionId}")
    @Operation(summary = "Obter detalhes de uma transação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transação encontrada"),
        @ApiResponse(responseCode = "404", description = "Transação não encontrada")
    })
    public ResponseEntity<CreditCardTransactionResponseDTO> getTransaction(
        @PathVariable UUID creditCardId,
        @PathVariable UUID transactionId
    ) {
        CreditCardTransactionResponseDTO response = creditCardService.getTransaction(creditCardId, transactionId);
        return ResponseEntity.ok(response);
    }
}
