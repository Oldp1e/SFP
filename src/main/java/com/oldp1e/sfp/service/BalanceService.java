package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.balance.MonthlyBalanceResponseDTO;
import com.oldp1e.sfp.entity.MonthlyBalance;
import com.oldp1e.sfp.repository.IncomeRepository;
import com.oldp1e.sfp.repository.ExpenseRepository;
import com.oldp1e.sfp.repository.MonthlyBalanceRepository;
import com.oldp1e.sfp.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class BalanceService {

    private final MonthlyBalanceRepository monthlyBalanceRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final AuthUtil authUtil;

    public BalanceService(
        MonthlyBalanceRepository monthlyBalanceRepository,
        IncomeRepository incomeRepository,
        ExpenseRepository expenseRepository,
        AuthUtil authUtil
    ) {
        this.monthlyBalanceRepository = monthlyBalanceRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.authUtil = authUtil;
    }

    public MonthlyBalanceResponseDTO getCurrentBalance() {
        LocalDate now = LocalDate.now();
        return getMonthlyBalance(now.getMonthValue(), now.getYear());
    }

    public MonthlyBalanceResponseDTO getMonthlyBalance(Integer month, Integer year) {
        UUID userId = authUtil.getCurrentUserId();

        final Integer finalMonth;
        final Integer finalYear;

        if (month == null || year == null) {
            LocalDate now = LocalDate.now();
            finalMonth = now.getMonthValue();
            finalYear = now.getYear();
        } else {
            finalMonth = month;
            finalYear = year;
        }

        return monthlyBalanceRepository.findByUserIdAndReferenceYearAndReferenceMonth(userId, finalYear, finalMonth)
            .map(this::toResponseDTO)
            .orElseGet(() -> createCalculatedBalance(userId, finalMonth, finalYear));
    }

    public List<MonthlyBalanceResponseDTO> getYearlyBalances(Integer year) {
        UUID userId = authUtil.getCurrentUserId();

        final Integer finalYear = year != null ? year : LocalDate.now().getYear();

        return monthlyBalanceRepository.findByUserIdAndYear(userId, finalYear)
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    private MonthlyBalanceResponseDTO createCalculatedBalance(UUID userId, Integer month, Integer year) {
        BigDecimal totalIncome = incomeRepository.findByUserIdAndYearAndMonth(userId, year, month)
            .stream()
            .map(income -> income.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenseRepository.findByUserIdAndYearAndMonth(userId, year, month)
            .stream()
            .map(expense -> expense.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        return new MonthlyBalanceResponseDTO(
            null,
            month,
            year,
            totalIncome,
            totalExpense,
            balance,
            null
        );
    }

    private MonthlyBalanceResponseDTO toResponseDTO(MonthlyBalance balance) {
        return new MonthlyBalanceResponseDTO(
            balance.getId(),
            balance.getReferenceMonth(),
            balance.getReferenceYear(),
            balance.getTotalIncome(),
            balance.getTotalExpense(),
            balance.getBalance(),
            balance.getCreatedAt()
        );
    }
}
