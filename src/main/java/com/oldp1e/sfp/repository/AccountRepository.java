package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findByUserId(UUID userId);

    Optional<Account> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT a FROM Account a WHERE a.userId = :userId ORDER BY a.createdAt DESC")
    List<Account> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

}
