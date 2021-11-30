package com.web.wallet.service;

import com.web.wallet.entity.Categories;
import com.web.wallet.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public void createStandardCategoriesInDb() {
        String[] standardCategoriesNames = {"Еда", "Здоровье", "Одежда", "Развлечения"};
        AddArrayCategories(standardCategoriesNames, false);

        String[] standardIncomeCategoriesNames = {"Зарплата", "Аванс"};
        AddArrayCategories(standardIncomeCategoriesNames, true);
    }

    public void AddArrayCategories(String[] arrayNames, boolean income) {
        for (String name : arrayNames) {
                categoriesRepository.save(new Categories(name, income));
        }
    }

    public List<Categories> findLastSixCategories() {
        List<Categories> allCategories = categoriesRepository.findCategoriesOrderByIdDescInDB();
        List<Categories> lastSixCategoriesInDB = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lastSixCategoriesInDB.add(allCategories.get(i));
        }
        return lastSixCategoriesInDB;
    }

    public void saveCategory(Categories categories) {
        categoriesRepository.save(categories);
    }

    public Boolean findIncomeById(long id) {
        return categoriesRepository.findIncomeById(id);
    }

    public Categories findCategoryById(long id) {
        return categoriesRepository.findById(id).orElse(new Categories());
    }
}
