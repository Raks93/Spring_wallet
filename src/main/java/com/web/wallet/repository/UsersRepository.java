package com.web.wallet.repository;

import com.web.wallet.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);

}
