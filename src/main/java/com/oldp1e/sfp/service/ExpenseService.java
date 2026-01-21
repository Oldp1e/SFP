package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.expense.ExpenseInputDTO;
import com.oldp1e.sfp.dto.expense.ExpenseResponseDTO;
import com.oldp1e.sfp.entity.Expense;
import com.oldp1e.sfp.entity.Expense.ExpenseType;
import com.oldp1e.sfp.repository.ExpenseRepository;
import com.oldp1e.sfp.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final AuthUtil authUtil;

    public ExpenseService(ExpenseRepository expenseRepository, AuthUtil authUtil) {
        this.expenseRepository = expenseRepository;
        this.authUtil = authUtil;
    }

    public ExpenseResponseDTO create(ExpenseInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();
        LocalDate referenceDate = request.referenceDate();

        Expense expense = Expense.builder()
            .userId(userId)
            .accountId(request.accountId())
            .description(request.description())
            .amount(request.amount())
            .expenseType(ExpenseType.valueOf(request.expenseType()))
            .referenceDate(referenceDate)
            .referenceMonth(referenceDate.getMonthValue())
            .referenceYear(referenceDate.getYear())
            .build();

        Expense saved = expenseRepository.save(expense);
        return toResponseDTO(saved);
    }

    public ExpenseResponseDTO getById(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Expense expense = expenseRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Despesa não encontrada"));
        return toResponseDTO(expense);
    }

    public List<ExpenseResponseDTO> getAll(Integer month, Integer year, String type) {
        UUID userId = authUtil.getCurrentUserId();

        List<Expense> expenses;
        if (month != null && year != null && type != null) {
            ExpenseType expenseType = ExpenseType.valueOf(type);
            expenses = expenseRepository.findByUserIdAndYearAndMonthAndType(userId, year, month, expenseType);
        } else if (month != null && year != null) {
            expenses = expenseRepository.findByUserIdAndYearAndMonth(userId, year, month);
        } else if (year != null) {
            expenses = expenseRepository.findByUserIdAndYear(userId, year);
        } else {
            expenses = expenseRepository.findAllByUserIdOrderByReferenceDateDesc(userId);
        }

        return expenses.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public ExpenseResponseDTO update(UUID id, ExpenseInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();
        Expense expense = expenseRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Despesa não encontrada"));

        LocalDate referenceDate = request.referenceDate();
        expense.setDescription(request.description());
        expense.setAmount(request.amount());
        expense.setExpenseType(ExpenseType.valueOf(request.expenseType()));
        expense.setReferenceDate(referenceDate);
        expense.setReferenceMonth(referenceDate.getMonthValue());
        expense.setReferenceYear(referenceDate.getYear());

        Expense updated = expenseRepository.save(expense);
        return toResponseDTO(updated);
    }

    public void delete(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Expense expense = expenseRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Despesa não encontrada"));
        expenseRepository.delete(expense);
    }

    private ExpenseResponseDTO toResponseDTO(Expense expense) {
        return new ExpenseResponseDTO(
            expense.getId(),
            expense.getAccountId(),
            expense.getDescription(),
            expense.getAmount(),
            expense.getExpenseType().name(),
            expense.getReferenceDate(),
            expense.getReferenceMonth(),
            expense.getReferenceYear(),
            expense.getCreatedAt()
        );
    }
}
