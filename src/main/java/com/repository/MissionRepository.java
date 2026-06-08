package com.repository;

import com.model.entity.MissionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {

    Optional<MissionEntity> findByMissionId(String missionId);

    boolean existsByMissionId(String missionId);


    // Решение 4: Разделить на два запроса (рекомендуемый подход)
    @Query("SELECT DISTINCT m FROM MissionEntity m " +
            "LEFT JOIN FETCH m.curse " +
            "LEFT JOIN FETCH m.economicAssessment")
    List<MissionEntity> findAllWithBasicData();

    @Query("SELECT m FROM MissionEntity m LEFT JOIN FETCH m.sorcerers WHERE m.id = :id")
    Optional<MissionEntity> findByIdWithSorcerers(@Param("id") Long id);

    @Query("SELECT m FROM MissionEntity m LEFT JOIN FETCH m.techniques WHERE m.id = :id")
    Optional<MissionEntity> findByIdWithTechniques(@Param("id") Long id);

    // Для поиска по missionId
    @Query("SELECT m FROM MissionEntity m LEFT JOIN FETCH m.sorcerers WHERE m.missionId = :missionId")
    Optional<MissionEntity> findByMissionIdWithSorcerers(@Param("missionId") String missionId);

    @Query("SELECT m FROM MissionEntity m LEFT JOIN FETCH m.techniques WHERE m.missionId = :missionId")
    Optional<MissionEntity> findByMissionIdWithTechniques(@Param("missionId") String missionId);
}