package com.oldp1e.sfp.repository;

import com.oldp1e.sfp.entity.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SimulationRepository extends JpaRepository<Simulation, UUID> {

    List<Simulation> findByUserId(UUID userId);

    Optional<Simulation> findByIdAndUserId(UUID id, UUID userId);

    @Query("SELECT s FROM Simulation s WHERE s.userId = :userId ORDER BY s.createdAt DESC")
    List<Simulation> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

}
