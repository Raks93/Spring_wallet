package com.web.wallet.controller;

import com.web.wallet.entity.Categories;
import com.web.wallet.entity.Role;
import com.web.wallet.entity.Users;
import com.web.wallet.service.CardsService;
import com.web.wallet.service.CategoriesService;
import com.web.wallet.service.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
public class RegistrationController {

    private final CardsService cardsService;

    private final CategoriesService categoriesService;

    private final UsersService usersService;

    public RegistrationController(CardsService cardsService, CategoriesService categoriesService, UsersService usersService) {
        this.cardsService = cardsService;
        this.categoriesService = categoriesService;
        this.usersService = usersService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Users user, Model model) {
        if (user.getUsername().equals("anonymousUser")) {
            model.addAttribute("message", "Запрещенное имя пользователя!");
            return "registration";
        }

        Users userFromDb = usersService.findUserByName(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "Пользователь существует!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        usersService.saveUser(user);

        categoriesService.createStandardCategoriesInDb();
        List<Categories> standardCategories = categoriesService.findLastSixCategories();
        for (Categories standardCategory : standardCategories) {
            standardCategory.setUsers(user);
        }

        user.setCategoriesList(standardCategories);
        user.setCardsList(cardsService.generatedStandardCards(user.getId()));


        return "redirect:/login";
    }
}
