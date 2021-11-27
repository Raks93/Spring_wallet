package com.web.wallet.repository;

import com.web.wallet.entity.Cards;
import com.web.wallet.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardsRepository extends JpaRepository<Cards, Long> {

    Cards findByName(String name);
}
