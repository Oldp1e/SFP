package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, UUID> {

    List<Investment> findByUserId(UUID userId);

    Optional<Investment> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT i FROM Investment i WHERE i.userId = :userId ORDER BY i.referenceDate DESC")
    List<Investment> findAllByUserIdOrderByReferenceDateDesc(@Param("userId") UUID userId);

}
