package com.web.wallet.controller;

import com.web.wallet.service.CategoriesService;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    private final CategoriesService categoriesService;

    public MainController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/")
    public String start(Model model) {
        categoriesService.checkStandardCategoriesInDb();

        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            return "start";


//        List<Categories> standardCategories = categoriesService.findStandardCategories();
//
//        List<Users> users = usersService.findAllUsers();
//
//        for (Categories standardCategory : standardCategories) {
//            standardCategory.setUsersList(users);
//            categoriesService.saveCategory(standardCategory);
//            System.out.println(standardCategory);
//        }

//        Iterable<Users> users = usersService.findAllUsers();
//        for (Users user : users) {
//            user.setCategoriesList(standardCategories);
//            usersService.saveUser(user);
//            System.out.println(user);
//        }
//        System.out.println("redirect:/home/" + usersService.findUserByName(username).getId());
        return "redirect:/home/";
    }

//    @PostMapping("/")
//    public String startPost(@RequestParam String calendar, Model model) {
//        Date date = java.sql.Date.valueOf(calendar);
//
//        return "start";
//    }

}