package com.ethaum.deal.repository;

import com.ethaum.deal.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DealRepository extends JpaRepository<Deal, Long> {

    List<Deal> findByStatus(String status);
}
