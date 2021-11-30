package com.web.wallet.controller;

import com.web.wallet.entity.Categories;
import com.web.wallet.entity.Role;
import com.web.wallet.entity.Users;
import com.web.wallet.repository.CardsRepository;
import com.web.wallet.repository.CategoriesRepository;
import com.web.wallet.repository.UsersRepository;
import com.web.wallet.service.CardsService;
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

    private final UsersRepository usersRepository;

    private final CategoriesRepository categoriesRepository;

    private final CardsService cardsService;

    public RegistrationController(UsersRepository usersRepository, CategoriesRepository categoriesRepository, CardsService cardsService) {
        this.usersRepository = usersRepository;
        this.categoriesRepository = categoriesRepository;
        this.cardsService = cardsService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Users user, Model model) {
        Users userFromDb = usersRepository.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        List<Categories> standardCategoriesInDb = categoriesRepository.findCategoriesOrderByIdDescInDB();

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        usersRepository.save(user);

        for (Categories categories : standardCategoriesInDb) {
            categories.setUsers(user);
        }

        user.setCategoriesList(standardCategoriesInDb);
        user.setCardsList(cardsService.generatedStandardCards(user.getId()));

        return "redirect:/login";
    }
}
