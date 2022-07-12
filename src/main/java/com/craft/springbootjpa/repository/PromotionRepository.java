package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
