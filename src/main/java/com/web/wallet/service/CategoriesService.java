package com.web.wallet.service;

import com.web.wallet.entity.Categories;
import com.web.wallet.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public void checkStandardCategoriesInDb() {
        String[] standardCategoriesNames = {"Еда", "Здоровье", "Одежда", "Развлечения"};
        AddArrayCategories(standardCategoriesNames, false);

        String[] standardIncomeCategoriesNames = {"Зарплата", "Аванс"};
        AddArrayCategories(standardIncomeCategoriesNames, true);
    }

    public void AddArrayCategories(String[] arrayNames, boolean income) {
        for (String name : arrayNames) {
            if (categoriesRepository.findByName(name) == null)
            {
                categoriesRepository.save(new Categories(name, income));
            }
        }
    }

    public List<Categories> findStandardCategories() {
        return categoriesRepository.findStandardCategoriesInDb();
    }

    public void saveCategory(Categories categories) {
        categoriesRepository.save(categories);
    }
}
