package com.web.wallet.controller;

import com.web.wallet.entity.Cards;
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

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
public class MainController {

    private final CategoriesService categoriesService;

    private final CardsService cardsService;

    private final UsersService usersService;

    private final JournalService journalService;

    public MainController(CategoriesService categoriesService, CardsService cardsService, UsersService usersService, JournalService  journalService) {
        this.categoriesService = categoriesService;
        this.cardsService = cardsService;
        this.usersService = usersService;
        this.journalService = journalService;
    }

    @GetMapping("/")
    public String start(Model model) {
        generateValuesDb();

        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            return "start";

        return "redirect:/home";
    }

//    @PostMapping("/")
//    public String startPost(@RequestParam String calendar, Model model) {
//        Date date = java.sql.Date.valueOf(calendar);
//
//        return "start";
//    }

    private void generateValuesDb() {
        categoriesService.checkStandardCategoriesInDb();
        usersService.generateFewUsers();

        List<Categories> standardCategories = categoriesService.findStandardCategories();

        List<Users> users = usersService.findAllUsers();

        for (Categories standardCategory : standardCategories) {
            standardCategory.setUsersList(users);
        }

        for (Users user : users) {
            user.setCategoriesList(standardCategories);
            user.setCardsList(cardsService.generatedStandardCards(user.getId()));
        }


        LocalDate date = LocalDate.now();

        for (Users user : users) {
            List<Cards> cardsList = user.getCardsList();
            for (Cards card : cardsList) {
                List<Categories> categoriesList = user.getCategoriesList();
                for (Categories category : categoriesList) {
                    Journal journal = new Journal();
                    journal.setAmount((long) ((Math.random() * 11) + 1)* 100);
                    journal.setDate(Date.valueOf(date.minusDays((long) ((Math.random() * 11) + 1) * 35)));
                    journal.setInOutMoney(category.getIncome());
                    journal.setPurchase("Что-то");
                    journalService.saveJournal(journal);
                    journal.setUsers(user);
                    journal.setCards(card);
                    journal.setCategories(category);
                    Long balance = category.getIncome() ? card.getBalance() + journal.getAmount() :  card.getBalance() - journal.getAmount();
                    card.setBalance(balance);
                    cardsService.saveCard(card);
                }
            }




        }
    }
}