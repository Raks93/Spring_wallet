package com.web.wallet.repository;

import com.web.wallet.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardsRepository extends JpaRepository<Cards, Long> {

    Cards findByName(String name);
}
