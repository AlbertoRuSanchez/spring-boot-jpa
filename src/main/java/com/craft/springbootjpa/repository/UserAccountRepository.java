package com.craft.springbootjpa.repository;

import com.craft.springbootjpa.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
