package com.web.wallet.controller;

import com.web.wallet.entity.Categories;
import com.web.wallet.entity.Journal;
import com.web.wallet.entity.Users;
import com.web.wallet.service.CardsService;
import com.web.wallet.service.CategoriesService;
import com.web.wallet.service.JournalService;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoriesController {

    private final UsersService usersService;

    private final CategoriesService categoriesService;

    private final JournalService journalService;

    private final CardsService cardsService;

    public CategoriesController(UsersService usersService, CategoriesService categoriesService, JournalService journalService, CardsService cardsService) {
        this.usersService = usersService;
        this.categoriesService = categoriesService;
        this.journalService = journalService;
        this.cardsService = cardsService;
    }

    @GetMapping("/categories")
    public String categories(Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("categories", user.getCategoriesList());
        return "categories";
    }

    @GetMapping("/categories/add")
    public String addCategories(Model model) {

        model.addAttribute("nameCategory", "");
        return "categories_add";
    }

    @PostMapping("/categories/add")
    public String addCategories(@RequestParam String name, @RequestParam Boolean incomeCategory, Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Categories> categoriesList = user.getCategoriesList();
        Categories category = new Categories(name, incomeCategory);

        for (Categories categories : categoriesList) {
            if (categories.getName().equals(name)) {
                model.addAttribute("nameCategory", name);
                model.addAttribute("message", "Данная категория существует");
                return "categories_add";
            }
        }

        category.setUsers(user);
        categoriesService.saveCategory(category);

        categoriesList.add(category);
        user.setCategoriesList(categoriesList);

        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategories(@PathVariable(value = "id") Long id, Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Categories> optionalCategory = categoriesService.findCategoryById(id);
        if (!optionalCategory.isPresent()) {
            model.addAttribute("categories", user.getCategoriesList());
            return "redirect:/categories";
        }
        model.addAttribute("nameCategory", optionalCategory.get().getName());
        model.addAttribute("categorySelected", optionalCategory.get().getIncome());

        return "categories_edit";
    }

    @PostMapping("/categories/edit/{id}")
    public String postEditCategories(@PathVariable(value = "id") Long id, @RequestParam String name, @RequestParam Boolean incomeCategory, Model model) {
        Optional<Categories> optionalCategory = categoriesService.findCategoryById(id);
        if (!optionalCategory.isPresent())
        {
            model.addAttribute("nameCategory", optionalCategory.get().getName());
            model.addAttribute("message", "Пользователь не существует");
            model.addAttribute("categorySelected", optionalCategory.get().getIncome());
            return "categories_edit";
        }

        Categories categories = optionalCategory.get();
        categories.setName(name);
        categories.setIncome(incomeCategory);
        categoriesService.saveCategory(categories);

        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Journal> journalList = user.getJournalList();
        for (Journal journal : journalList) {
            if (journal.getCategories().getId().equals(id)) {
                Journal newJournal = new Journal(journal.getAmount(), journal.getDate(), journal.getPurchase(), categories.getIncome());
                newJournal.setId(journal.getId());
                newJournal.setCategories(categories);
                newJournal.setUsers(journal.getUsers());
                newJournal.setCards(journal.getCards());
                newJournal = cardsService.editCard(journal, newJournal);
                journalService.saveJournal(newJournal);
            }
        }

        return "redirect:/categories";
    }

    @GetMapping("/categories/delete/{id}/{size}")
    public String deleteCategories(@PathVariable(value = "id") Long id, @PathVariable(value = "size") Long size, Model model) {
        if (size == 1) {
            model.addAttribute("message", "Нельзя удалить последний счет");
            model.addAttribute("categories", categoriesService.findCategoryById(id).orElse(null));
            return "categories";
        }

        categoriesService.deleteCategoryById(id);

        return "redirect:/categories";
    }
}
