package com.web.wallet.repository;

import com.web.wallet.entity.StartPage;
import com.web.wallet.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartPageRepository extends JpaRepository<StartPage, Long> {

    StartPage findByName(String name);
}
