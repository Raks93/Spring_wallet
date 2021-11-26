package com.web.wallet.controller;

import com.web.wallet.entity.Categories;
import com.web.wallet.entity.Users;
import com.web.wallet.service.CategoriesService;
import com.web.wallet.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Controller
public class MainController {

    private final CategoriesService categoriesService;

    private final UsersService usersService;

    public MainController(CategoriesService categoriesService, UsersService usersService) {
        this.categoriesService = categoriesService;
        this.usersService = usersService;
    }

    @GetMapping("/")
    public String start(Model model) {
        categoriesService.checkStandardCategoriesInDb();

        List<Categories> standardCategories = categoriesService.findStandardCategories();

        List<Users> users = usersService.findAllUsers();

        for (Categories standardCategory : standardCategories) {
            standardCategory.setUsersList(users);
            categoriesService.saveCategory(standardCategory);
            System.out.println(standardCategory);
        }

//        Iterable<Users> users = usersService.findAllUsers();
//        for (Users user : users) {
//            user.setCategoriesList(standardCategories);
//            usersService.saveUser(user);
//            System.out.println(user);
//        }

        model.addAttribute("title", "Главная страница");
        return "start";
    }

    @PostMapping("/")
    public String startPost(@RequestParam String calendar, Model model) {
        Date date = java.sql.Date.valueOf(calendar);

        return "start";
    }

}