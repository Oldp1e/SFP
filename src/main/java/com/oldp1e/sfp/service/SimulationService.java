package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.simulation.SimulationInputDTO;
import com.oldp1e.sfp.dto.simulation.SimulationResponseDTO;
import com.oldp1e.sfp.entity.Simulation;
import com.oldp1e.sfp.entity.Simulation.SimulationType;
import com.oldp1e.sfp.repository.SimulationRepository;
import com.oldp1e.sfp.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SimulationService {

    private final SimulationRepository simulationRepository;
    private final AuthUtil authUtil;

    public SimulationService(SimulationRepository simulationRepository, AuthUtil authUtil) {
        this.simulationRepository = simulationRepository;
        this.authUtil = authUtil;
    }

    public SimulationResponseDTO create(SimulationInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();

        BigDecimal monthlyValue = request.totalValue()
            .divide(new BigDecimal(request.installments()), 2, RoundingMode.HALF_UP);

        BigDecimal impactOnBalance = request.totalValue().negate();

        Simulation simulation = Simulation.builder()
            .userId(userId)
            .simulationType(SimulationType.valueOf(request.simulationType()))
            .totalValue(request.totalValue())
            .installments(request.installments())
            .monthlyValue(monthlyValue)
            .startDate(request.startDate())
            .impactOnBalance(impactOnBalance)
            .build();

        Simulation saved = simulationRepository.save(simulation);
        return toResponseDTO(saved);
    }

    public SimulationResponseDTO getById(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Simulation simulation = simulationRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Simulação não encontrada"));
        return toResponseDTO(simulation);
    }

    public List<SimulationResponseDTO> getAll() {
        UUID userId = authUtil.getCurrentUserId();
        return simulationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public void delete(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Simulation simulation = simulationRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Simulação não encontrada"));
        simulationRepository.delete(simulation);
    }

    private SimulationResponseDTO toResponseDTO(Simulation simulation) {
        return new SimulationResponseDTO(
            simulation.getId(),
            simulation.getSimulationType().name(),
            simulation.getTotalValue(),
            simulation.getInstallments(),
            simulation.getMonthlyValue(),
            simulation.getStartDate(),
            simulation.getImpactOnBalance(),
            simulation.getCreatedAt()
        );
    }
}
