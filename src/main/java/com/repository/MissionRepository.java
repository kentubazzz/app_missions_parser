package com.repository;

import com.model.entity.MissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MissionRepository extends JpaRepository<MissionEntity, Long> {
    Optional<MissionEntity> findByMissionId(String missionId);
    boolean existsByMissionId(String missionId);
}
