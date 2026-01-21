package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    List<Expense> findByUserId(UUID userId);

    Optional<Expense> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND e.referenceYear = :year AND e.referenceMonth = :month ORDER BY e.referenceDate DESC")
    List<Expense> findByUserIdAndYearAndMonth(@Param("userId") UUID userId, @Param("year") Integer year, @Param("month") Integer month);

    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND e.referenceYear = :year AND e.referenceMonth = :month AND e.expenseType = :type ORDER BY e.referenceDate DESC")
    List<Expense> findByUserIdAndYearAndMonthAndType(@Param("userId") UUID userId, @Param("year") Integer year, @Param("month") Integer month, @Param("type") Expense.ExpenseType type);

    @Query("SELECT e FROM Expense e WHERE e.userId = :userId AND e.referenceYear = :year ORDER BY e.referenceDate DESC")
    List<Expense> findByUserIdAndYear(@Param("userId") UUID userId, @Param("year") Integer year);

    @Query("SELECT e FROM Expense e WHERE e.userId = :userId ORDER BY e.referenceDate DESC")
    List<Expense> findAllByUserIdOrderByReferenceDateDesc(@Param("userId") UUID userId);

}
