package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.CreditCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditCardTransactionRepository extends JpaRepository<CreditCardTransaction, UUID> {

    List<CreditCardTransaction> findByUserId(UUID userId);

    Optional<CreditCardTransaction> findByIdAndUserId(UUID id, UUID userId);

    List<CreditCardTransaction> findByCreditCardId(UUID creditCardId);

    @Query("SELECT cct FROM CreditCardTransaction cct WHERE cct.creditCardId = :creditCardId AND cct.userId = :userId ORDER BY cct.purchaseDate DESC")
    List<CreditCardTransaction> findByCreditCardIdAndUserId(@Param("creditCardId") UUID creditCardId, @Param("userId") UUID userId);

    @Query("SELECT cct FROM CreditCardTransaction cct WHERE cct.userId = :userId AND EXTRACT(YEAR FROM cct.firstDueDate) = :year AND EXTRACT(MONTH FROM cct.firstDueDate) = :month ORDER BY cct.purchaseDate DESC")
    List<CreditCardTransaction> findByUserIdAndYearAndMonth(@Param("userId") UUID userId, @Param("year") Integer year, @Param("month") Integer month);

}
