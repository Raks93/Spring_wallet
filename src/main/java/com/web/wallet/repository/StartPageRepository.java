package com.web.wallet.repository;

import com.web.wallet.entity.StartPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StartPageRepository extends JpaRepository<StartPage, Long> {

    StartPage findByName(String name);
}
