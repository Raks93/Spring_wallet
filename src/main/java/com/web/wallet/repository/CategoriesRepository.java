package com.web.wallet.repository;

import com.web.wallet.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long> {

    Categories findByName(String name);

    @Query("SELECT c FROM Categories c ORDER BY c.id DESC")
    List<Categories> findCategoriesOrderByIdDescInDB();

    @Query("SELECT c.income FROM Categories c WHERE c.id = ?1")
    Boolean findIncomeById(Long id);
}
