package com.oldp1e.sfp.service;

import com.oldp1e.sfp.dto.account.AccountInputDTO;
import com.oldp1e.sfp.dto.account.AccountResponseDTO;
import com.oldp1e.sfp.entity.Account;
import com.oldp1e.sfp.repository.AccountRepository;
import com.oldp1e.sfp.util.AuthUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    public AccountService(AccountRepository accountRepository, AuthUtil authUtil) {
        this.accountRepository = accountRepository;
        this.authUtil = authUtil;
    }

    public AccountResponseDTO create(AccountInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();

        Account account = Account.builder()
            .userId(userId)
            .name(request.name())
            .initialBalance(request.initialBalance())
            .build();

        Account saved = accountRepository.save(account);
        return toResponseDTO(saved);
    }

    public AccountResponseDTO getById(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Account account = accountRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        return toResponseDTO(account);
    }

    public List<AccountResponseDTO> getAll() {
        UUID userId = authUtil.getCurrentUserId();
        return accountRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
            .stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public AccountResponseDTO update(UUID id, AccountInputDTO request) {
        UUID userId = authUtil.getCurrentUserId();
        Account account = accountRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        account.setName(request.name());
        account.setInitialBalance(request.initialBalance());

        Account updated = accountRepository.save(account);
        return toResponseDTO(updated);
    }

    public void delete(UUID id) {
        UUID userId = authUtil.getCurrentUserId();
        Account account = accountRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        accountRepository.delete(account);
    }

    private AccountResponseDTO toResponseDTO(Account account) {
        return new AccountResponseDTO(
            account.getId(),
            account.getName(),
            account.getInitialBalance(),
            account.getCreatedAt()
        );
    }
}
