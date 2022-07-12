package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.PromotionCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionCodeRepository extends JpaRepository<PromotionCode, Long> {
}
