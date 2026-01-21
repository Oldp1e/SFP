package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.investment.InvestmentInputDTO;
import com.oldp1e.sfp.dto.investment.InvestmentResponseDTO;
import com.oldp1e.sfp.entity.Investment;
import com.oldp1e.sfp.repository.InvestmentRepository;
import com.oldp1e.sfp.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final AuthUtil authUtil;

    public InvestmentService(InvestmentRepository investmentRepository, AuthUtil authUtil) {
        this.investmentRepository = investmentRepository;
        this.authUtil = authUtil;
    }

    public InvestmentResponseDTO create(InvestmentInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();

        Investment investment = Investment.builder()
            .userId(userId)
            .description(request.description())
            .amount(request.amount())
            .investmentType(request.investmentType())
            .referenceDate(request.referenceDate())
            .build();

        Investment saved = investmentRepository.save(investment);
        return toResponseDTO(saved);
    }

    public InvestmentResponseDTO getById(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Investment investment = investmentRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Investimento não encontrado"));
        return toResponseDTO(investment);
    }

    public List<InvestmentResponseDTO> getAll() {
        UUID userId = authUtil.getCurrentUserId();
        return investmentRepository.findAllByUserIdOrderByReferenceDateDesc(userId)
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public InvestmentResponseDTO update(UUID id, InvestmentInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();
        Investment investment = investmentRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Investimento não encontrado"));

        investment.setDescription(request.description());
        investment.setAmount(request.amount());
        investment.setInvestmentType(request.investmentType());
        investment.setReferenceDate(request.referenceDate());

        Investment updated = investmentRepository.save(investment);
        return toResponseDTO(updated);
    }

    public void delete(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Investment investment = investmentRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Investimento não encontrado"));
        investmentRepository.delete(investment);
    }

    private InvestmentResponseDTO toResponseDTO(Investment investment) {
        return new InvestmentResponseDTO(
            investment.getId(),
            investment.getDescription(),
            investment.getAmount(),
            investment.getInvestmentType(),
            investment.getReferenceDate(),
            investment.getCreatedAt()
        );
    }
}
