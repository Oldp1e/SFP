package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.creditcard.CreditCardInputDTO;
import com.oldp1e.sfp.dto.creditcard.CreditCardResponseDTO;
import com.oldp1e.sfp.dto.creditcard.CreditCardTransactionInputDTO;
import com.oldp1e.sfp.dto.creditcard.CreditCardTransactionResponseDTO;
import com.oldp1e.sfp.entity.CreditCard;
import com.oldp1e.sfp.entity.CreditCardTransaction;
import com.oldp1e.sfp.repository.CreditCardRepository;
import com.oldp1e.sfp.repository.CreditCardTransactionRepository;
import com.oldp1e.sfp.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final CreditCardTransactionRepository transactionRepository;
    private final AuthUtil authUtil;

    public CreditCardService(
        CreditCardRepository creditCardRepository,
        CreditCardTransactionRepository transactionRepository,
        AuthUtil authUtil
    ) {
        this.creditCardRepository = creditCardRepository;
        this.transactionRepository = transactionRepository;
        this.authUtil = authUtil;
    }

    public CreditCardResponseDTO create(CreditCardInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();

        CreditCard creditCard = CreditCard.builder()
            .userId(userId)
            .name(request.name())
            .limitAmount(request.limitAmount())
            .closingDay(request.closingDay())
            .dueDay(request.dueDay())
            .build();

        CreditCard saved = creditCardRepository.save(creditCard);
        return toCreditCardResponseDTO(saved);
    }

    public CreditCardResponseDTO getById(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        CreditCard creditCard = creditCardRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado"));
        return toCreditCardResponseDTO(creditCard);
    }

    public List<CreditCardResponseDTO> getAll() {
        UUID userId = authUtil.getCurrentUserId();
        return creditCardRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(this::toCreditCardResponseDTO)
            .collect(Collectors.toList());
    }

    public CreditCardResponseDTO update(UUID id, CreditCardInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();
        CreditCard creditCard = creditCardRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado"));

        creditCard.setName(request.name());
        creditCard.setLimitAmount(request.limitAmount());
        creditCard.setClosingDay(request.closingDay());
        creditCard.setDueDay(request.dueDay());

        CreditCard updated = creditCardRepository.save(creditCard);
        return toCreditCardResponseDTO(updated);
    }

    public void delete(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        CreditCard creditCard = creditCardRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado"));
        creditCardRepository.delete(creditCard);
    }

    // Transações
    public CreditCardTransactionResponseDTO createTransaction(UUID creditCardId, CreditCardTransactionInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();

        CreditCard creditCard = creditCardRepository.findByIdAndUserId(creditCardId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado"));

        BigDecimal installmentValue = request.totalAmount()
            .divide(new BigDecimal(request.installments()), 2, RoundingMode.HALF_UP);

        LocalDate purchaseDate = request.purchaseDate();
        LocalDate firstDueDate = calculateFirstDueDate(creditCard, purchaseDate);

        CreditCardTransaction transaction = CreditCardTransaction.builder()
            .creditCardId(creditCardId)
            .userId(userId)
            .description(request.description())
            .totalAmount(request.totalAmount())
            .installments(request.installments())
            .installmentValue(installmentValue)
            .currentInstallment(1)
            .purchaseDate(purchaseDate)
            .firstDueDate(firstDueDate)
            .build();

        CreditCardTransaction saved = transactionRepository.save(transaction);
        return toTransactionResponseDTO(saved);
    }

    public CreditCardTransactionResponseDTO getTransaction(UUID creditCardId, UUID transactionId) {
        UUID userId = authUtil.getCurrentUserId();

        creditCardRepository.findByIdAndUserId(creditCardId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado"));

        CreditCardTransaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Transação não encontrada"));

        return toTransactionResponseDTO(transaction);
    }

    public List<CreditCardTransactionResponseDTO> getTransactions(UUID creditCardId) {
        UUID userId = authUtil.getCurrentUserId();

        creditCardRepository.findByIdAndUserId(creditCardId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Cartão não encontrado"));

        return transactionRepository.findByCreditCardIdAndUserId(creditCardId, userId)
            .stream()
            .map(this::toTransactionResponseDTO)
            .collect(Collectors.toList());
    }

    private LocalDate calculateFirstDueDate(CreditCard creditCard, LocalDate purchaseDate) {
        int dayOfMonth = purchaseDate.getDayOfMonth();
        int closingDay = creditCard.getClosingDay();

        LocalDate dueDate;
        if (dayOfMonth <= closingDay) {
            dueDate = purchaseDate.plusMonths(1).withDayOfMonth(creditCard.getDueDay());
        } else {
            dueDate = purchaseDate.plusMonths(2).withDayOfMonth(creditCard.getDueDay());
        }

        return dueDate;
    }

    private CreditCardResponseDTO toCreditCardResponseDTO(CreditCard creditCard) {
        return new CreditCardResponseDTO(
            creditCard.getId(),
            creditCard.getName(),
            creditCard.getLimitAmount(),
            creditCard.getClosingDay(),
            creditCard.getDueDay(),
            creditCard.getCreatedAt()
        );
    }

    private CreditCardTransactionResponseDTO toTransactionResponseDTO(CreditCardTransaction transaction) {
        return new CreditCardTransactionResponseDTO(
            transaction.getId(),
            transaction.getCreditCardId(),
            transaction.getDescription(),
            transaction.getTotalAmount(),
            transaction.getInstallments(),
            transaction.getInstallmentValue(),
            transaction.getCurrentInstallment(),
            transaction.getPurchaseDate(),
            transaction.getFirstDueDate(),
            transaction.getCreatedAt()
        );
    }
}
