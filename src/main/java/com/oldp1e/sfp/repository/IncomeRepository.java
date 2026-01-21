package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncomeRepository extends JpaRepository<Income, UUID> {

    List<Income> findByUserId(UUID userId);

    Optional<Income> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT i FROM Income i WHERE i.userId = :userId AND i.referenceYear = :year AND i.referenceMonth = :month ORDER BY i.referenceDate DESC")
    List<Income> findByUserIdAndYearAndMonth(@Param("userId") UUID userId, @Param("year") Integer year, @Param("month") Integer month);

    @Query("SELECT i FROM Income i WHERE i.userId = :userId AND i.referenceYear = :year ORDER BY i.referenceDate DESC")
    List<Income> findByUserIdAndYear(@Param("userId") UUID userId, @Param("year") Integer year);

    @Query("SELECT i FROM Income i WHERE i.userId = :userId ORDER BY i.referenceDate DESC")
    List<Income> findAllByUserIdOrderByReferenceDateDesc(@Param("userId") UUID userId);

}
