package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, UUID> {

    List<CreditCard> findByUserId(UUID userId);

    Optional<CreditCard> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT cc FROM CreditCard cc WHERE cc.userId = :userId ORDER BY cc.createdAt DESC")
    List<CreditCard> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

}
