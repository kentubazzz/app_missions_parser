package com.repository;

import com.model.entity.SorcererEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SorcererRepository extends JpaRepository<SorcererEntity, Long> {
}
