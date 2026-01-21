package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.income.IncomeInputDTO;
import com.oldp1e.sfp.dto.income.IncomeResponseDTO;
import com.oldp1e.sfp.entity.Income;
import com.oldp1e.sfp.entity.Income.IncomeType;
import com.oldp1e.sfp.repository.IncomeRepository;
import com.oldp1e.sfp.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final AuthUtil authUtil;

    public IncomeService(IncomeRepository incomeRepository, AuthUtil authUtil) {
        this.incomeRepository = incomeRepository;
        this.authUtil = authUtil;
    }

    public IncomeResponseDTO create(IncomeInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();
        LocalDate referenceDate = request.referenceDate();

        Income income = Income.builder()
            .userId(userId)
            .accountId(request.accountId())
            .description(request.description())
            .amount(request.amount())
            .incomeType(IncomeType.valueOf(request.incomeType()))
            .referenceDate(referenceDate)
            .referenceMonth(referenceDate.getMonthValue())
            .referenceYear(referenceDate.getYear())
            .build();

        Income saved = incomeRepository.save(income);
        return toResponseDTO(saved);
    }

    public IncomeResponseDTO getById(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Income income = incomeRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada"));
        return toResponseDTO(income);
    }

    public List<IncomeResponseDTO> getAll(Integer month, Integer year) {
        UUID userId = authUtil.getCurrentUserId();

        List<Income> incomes;
        if (month != null && year != null) {
            incomes = incomeRepository.findByUserIdAndYearAndMonth(userId, year, month);
        } else if (year != null) {
            incomes = incomeRepository.findByUserIdAndYear(userId, year);
        } else {
            incomes = incomeRepository.findAllByUserIdOrderByReferenceDateDesc(userId);
        }

        return incomes.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public IncomeResponseDTO update(UUID id, IncomeInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();
        Income income = incomeRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada"));

        LocalDate referenceDate = request.referenceDate();
        income.setDescription(request.description());
        income.setAmount(request.amount());
        income.setIncomeType(IncomeType.valueOf(request.incomeType()));
        income.setReferenceDate(referenceDate);
        income.setReferenceMonth(referenceDate.getMonthValue());
        income.setReferenceYear(referenceDate.getYear());

        Income updated = incomeRepository.save(income);
        return toResponseDTO(updated);
    }

    public void delete(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Income income = incomeRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Receita não encontrada"));
        incomeRepository.delete(income);
    }

    private IncomeResponseDTO toResponseDTO(Income income) {
        return new IncomeResponseDTO(
            income.getId(),
            income.getAccountId(),
            income.getDescription(),
            income.getAmount(),
            income.getIncomeType().name(),
            income.getReferenceDate(),
            income.getReferenceMonth(),
            income.getReferenceYear(),
            income.getCreatedAt()
        );
    }
}
