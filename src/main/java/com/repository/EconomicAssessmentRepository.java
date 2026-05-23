package com.repository;

import com.model.entity.EconomicAssessmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EconomicAssessmentRepository extends JpaRepository<EconomicAssessmentEntity, Long> {
}
