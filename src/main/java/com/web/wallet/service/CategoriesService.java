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
        for (String standardCategoriesName : standardCategoriesNames) {
            if (categoriesRepository.findByName(standardCategoriesName) == null)
            {
                Categories categories = new Categories();
                categories.setName(standardCategoriesName);
                categoriesRepository.save(categories);
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
