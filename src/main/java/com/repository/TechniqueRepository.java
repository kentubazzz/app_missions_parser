package com.repository;

import com.model.entity.TechniqueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TechniqueRepository extends JpaRepository<TechniqueEntity, Long> {
    Optional<TechniqueEntity> findByNameAndTypeAndOwner(String name, String type, String owner);
}