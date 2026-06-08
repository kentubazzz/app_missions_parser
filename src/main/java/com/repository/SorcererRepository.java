package com.repository;

import com.model.entity.SorcererEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SorcererRepository extends JpaRepository<SorcererEntity, Long> {
    Optional<SorcererEntity> findByNameAndRank(String name, String rank);
}