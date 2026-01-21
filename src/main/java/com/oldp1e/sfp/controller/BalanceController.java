package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.balance.MonthlyBalanceResponseDTO;
import com.oldp1e.sfp.service.BalanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Balanço", description = "Visualizar balanço e resumos financeiros")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/monthly")
    @Operation(
        summary = "Obter balanço mensal",
        description = "Retorna o balanço consolidado para um mês e ano específicos"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Balanço encontrado"),
        @ApiResponse(responseCode = "404", description = "Balanço não encontrado para o período")
    })
    public ResponseEntity<MonthlyBalanceResponseDTO> getMonthlyBalance(
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        MonthlyBalanceResponseDTO response = balanceService.getMonthlyBalance(month, year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/yearly")
    @Operation(
        summary = "Listar balanços do ano",
        description = "Lista os balanços mensais para um ano específico"
    )
    @ApiResponse(responseCode = "200", description = "Balanços do ano")
    public ResponseEntity<List<MonthlyBalanceResponseDTO>> getYearlyBalances(
        @RequestParam(required = false) Integer year
    ) {
        List<MonthlyBalanceResponseDTO> response = balanceService.getYearlyBalances(year);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/current")
    @Operation(
        summary = "Obter balanço do mês atual",
        description = "Retorna o balanço consolidado do mês e ano atuais"
    )
    @ApiResponse(responseCode = "200", description = "Balanço atual")
    public ResponseEntity<MonthlyBalanceResponseDTO> getCurrentBalance() {
        MonthlyBalanceResponseDTO response = balanceService.getCurrentBalance();
        return ResponseEntity.ok(response);
    }
}
