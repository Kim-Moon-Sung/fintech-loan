package com.loan.loan.repository;

import com.loan.loan.domain.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JudgementRepository extends JpaRepository<Judgment, Long> {
}
