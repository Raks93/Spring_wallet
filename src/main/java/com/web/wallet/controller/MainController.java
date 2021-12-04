package com.web.wallet.controller;

import com.web.wallet.entity.*;
import com.web.wallet.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private final CategoriesService categoriesService;

    private final CardsService cardsService;

    private final UsersService usersService;

    private final JournalService journalService;

    private final StartPageService startPageService;

    public MainController(CategoriesService categoriesService, CardsService cardsService, UsersService usersService, JournalService journalService, StartPageService startPageService) {
        this.categoriesService = categoriesService;
        this.cardsService = cardsService;
        this.usersService = usersService;
        this.journalService = journalService;
        this.startPageService = startPageService;
    }

    @GetMapping("/")
    public String start(Model model) {

        generateValuesDb();

        model.addAttribute("sumDayI", startPageService.findRecordByName("Средний доход за день"));
        model.addAttribute("sumMonthI", startPageService.findRecordByName("Средний доход за месяц"));
        model.addAttribute("sumYearI", startPageService.findRecordByName("Средний доход за год"));
        model.addAttribute("sumDayS", startPageService.findRecordByName("Средний расход за день"));
        model.addAttribute("sumMonthS", startPageService.findRecordByName("Средний расход за месяц"));
        model.addAttribute("sumYearS", startPageService.findRecordByName("Средний расход за год"));

        if (SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"))
            return "start";

        return "home";
    }

    private void generateValuesDb() {
        usersService.generateFewUsers();

        List<Users> users = usersService.findAllUsers();

        LocalDate date = LocalDate.now();

        if (users.get(0).getJournalList() == null || users.get(0).getJournalList().isEmpty() || users.get(0).getJournalList().size() == 0) {

            for (Users user : users) {
                categoriesService.createStandardCategoriesInDb();
                List<Categories> standardCategories = categoriesService.findLastSixCategories();
                for (Categories standardCategory : standardCategories) {
                    standardCategory.setUsers(user);
                }
                user.setCategoriesList(standardCategories);
                user.setCardsList(cardsService.generatedStandardCards(user.getId()));
            }


            for (Users user : users) {
                List<Journal> journalList = new ArrayList<>();
                List<Cards> cardsList = user.getCardsList();
                for (Cards card : cardsList) {
                    List<Categories> categoriesList = user.getCategoriesList();
                    for (Categories category : categoriesList) {
                        Journal journal = new Journal();
                        journal.setAmount((long) ((Math.random() * 11) + 1) * 100);
                        journal.setDate(date.minusDays((long) ((Math.random() * 60) + 1)));
                        journal.setInOutMoney(category.getIncome());
                        journal.setPurchase("Что-то");
                        journalService.saveJournal(journal);
                        journal.setUsers(user);
                        journal.setCards(card);
                        journal.setCategories(category);

                        Long balance = card.getBalance() + (category.getIncome() ? journal.getAmount() : -journal.getAmount());
                        card.setBalance(balance);
                        cardsService.saveCard(card);

                        journalList.add(journal);
                    }
                }
                user.setJournalList(journalList);
            }
        }

        if (startPageService.getAll() == null || startPageService.getAll().isEmpty()) {

            Long sumDayIncome = 0L, sumMonthIncome = 0L, sumYearIncome = 0L, countDayI = 0L, userDayIncome = 0L;
            Long sumDaySpend = 0L, sumMonthSpend = 0L, sumYearSpend = 0L, countDayS = 0L, userDaySpend = 0L;

            for (Users user : users) {
                List<Journal> journalList = user.getJournalList();
                for (Journal journal : journalList) {
                    if (journal.getInOutMoney()) {
                        countDayI++;
                        userDayIncome += journal.getAmount();
                        if (journal.getDate().isAfter(date.minusMonths(1))) sumMonthIncome += journal.getAmount();
                        if (journal.getDate().isAfter(date.minusYears(1))) sumYearIncome += journal.getAmount();
                    } else {
                        countDayS++;
                        userDaySpend += journal.getAmount();
                        if (journal.getDate().isAfter(date.minusMonths(1))) sumMonthSpend += journal.getAmount();
                        if (journal.getDate().isAfter(date.minusYears(1))) sumYearSpend += journal.getAmount();
                    }
                }
                sumDayIncome += userDayIncome/countDayI;
                sumDaySpend += userDaySpend/countDayS;
                countDayI = 0L;
                countDayS = 0L;
                userDayIncome = 0L;
                userDaySpend = 0L;
            }

            StartPage startPage = new StartPage("Средний доход за день", sumDayIncome / users.size());
            startPageService.savePage(startPage);
            startPage = new StartPage("Средний доход за месяц", sumMonthIncome / users.size());
            startPageService.savePage(startPage);
            startPage = new StartPage("Средний доход за год", sumYearIncome / users.size());
            startPageService.savePage(startPage);
            startPage = new StartPage("Средний расход за день", sumDaySpend / users.size());
            startPageService.savePage(startPage);
            startPage = new StartPage("Средний расход за месяц", sumMonthSpend / users.size());
            startPageService.savePage(startPage);
            startPage = new StartPage("Средний расход за год", sumYearSpend / users.size());
            startPageService.savePage(startPage);
        }
    }
}