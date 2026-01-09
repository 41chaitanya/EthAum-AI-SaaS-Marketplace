package com.ethaum.deal.repository;

import com.ethaum.deal.model.DealApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealApplicationRepository
        extends JpaRepository<DealApplication, Long> {

    List<DealApplication> findByDealId(Long dealId);

    List<DealApplication> findByDealIdOrderByMatchScoreDesc(Long dealId);

    List<DealApplication> findByEnterpriseEmail(String email);
}
