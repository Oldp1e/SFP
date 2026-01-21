package com.oldp1e.sfp.controller;

import com.oldp1e.sfp.dto.simulation.SimulationInputDTO;
import com.oldp1e.sfp.dto.simulation.SimulationResponseDTO;
import com.oldp1e.sfp.service.SimulationService;
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
@RequestMapping("/simulations")
@PreAuthorize("hasRole('USER')")
@Tag(name = "Simulações", description = "Simular cenários financeiros e compras")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping
    @Operation(
        summary = "Criar nova simulação",
        description = "Cria uma simulação de cenário financeiro (parcelamento ou orçamento)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Simulação criada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<SimulationResponseDTO> create(@Valid @RequestBody SimulationInputDTO request) {
        SimulationResponseDTO response = simulationService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar simulações",
        description = "Lista todas as simulações do usuário autenticado"
    )
    @ApiResponse(responseCode = "200", description = "Lista de simulações")
    public ResponseEntity<List<SimulationResponseDTO>> getAll() {
        List<SimulationResponseDTO> response = simulationService.getAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter detalhes de uma simulação")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Simulação encontrada"),
        @ApiResponse(responseCode = "404", description = "Simulação não encontrada")
    })
    public ResponseEntity<SimulationResponseDTO> getById(@PathVariable UUID id) {
        SimulationResponseDTO response = simulationService.getById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar simulação")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Simulação deletada"),
        @ApiResponse(responseCode = "404", description = "Simulação não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        simulationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
