package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.MonthlyBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MonthlyBalanceRepository extends JpaRepository<MonthlyBalance, UUID> {

    List<MonthlyBalance> findByUserId(UUID userId);

    Optional<MonthlyBalance> findByUserIdAndReferenceYearAndReferenceMonth(UUID userId, Integer referenceYear, Integer referenceMonth);

    @Query("SELECT mb FROM MonthlyBalance mb WHERE mb.userId = :userId AND mb.referenceYear = :year ORDER BY mb.referenceMonth DESC")
    List<MonthlyBalance> findByUserIdAndYear(@Param("userId") UUID userId, @Param("year") Integer year);

    @Query("SELECT mb FROM MonthlyBalance mb WHERE mb.userId = :userId ORDER BY mb.referenceYear DESC, mb.referenceMonth DESC")
    List<MonthlyBalance> findAllByUserIdOrderByYearAndMonthDesc(@Param("userId") UUID userId);

}
