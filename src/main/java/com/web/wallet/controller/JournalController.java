package com.web.wallet.controller;

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

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

@Controller
public class JournalController {

    private final UsersService usersService;

    private final JournalService journalService;

    private final CategoriesService categoriesService;

    private final CardsService cardsService;

    public JournalController(UsersService usersService, JournalService journalService, CategoriesService categoriesService, CardsService cardsService) {
        this.usersService = usersService;
        this.journalService = journalService;
        this.categoriesService = categoriesService;
        this.cardsService = cardsService;
    }

    @GetMapping("/journal")
    public String journal(Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        user.getJournalList().sort(Collections.reverseOrder(Comparator.comparing(j -> j.getDate().toString())));

        model.addAttribute("journal", user.getJournalList());

        return "journal";
    }

    @GetMapping("/journal/edit/{id}")
    public String editJournal(@PathVariable(value = "id") long id, Model model) {
        Optional<Journal> optionalJournal = journalService.findJournalById(id);

        if (!optionalJournal.isPresent()) {
            return "redirect:/journal";
        }

        Users user = optionalJournal.get().getUsers();

        model.addAttribute("journal", optionalJournal.get());
        model.addAttribute("categories", user.getCategoriesList());
        model.addAttribute("cards", user.getCardsList());
        model.addAttribute("categoryIndex", optionalJournal.get().getCategories().getId());
        model.addAttribute("cardIndex", optionalJournal.get().getCards().getId());

        return "journal_edit";
    }

    @PostMapping("/journal/edit/{id}")
    public String updateJournal(@PathVariable(value = "id") long id, @RequestParam long amount, @RequestParam String purchase,
                                 @RequestParam String date, @RequestParam String chooseCategory, @RequestParam String chooseCard, Model model) {
        Optional<Journal> optionalJournal = journalService.findJournalById(id);
        if (!optionalJournal.isPresent()) {
            return "redirect:/journal";
        }

        Journal oldJournal = optionalJournal.get();
        Journal newJournal = new Journal(amount, LocalDate.parse(date), purchase, categoriesService.findIncomeById(Long.parseLong(chooseCategory)));
        newJournal.setCategories(categoriesService.findCategoryById(Long.parseLong(chooseCategory)));
        newJournal.setCards(cardsService.findCardById(Long.parseLong(chooseCard)).orElse(null));
        newJournal.setUsers(oldJournal.getUsers());
        newJournal.setId(oldJournal.getId());

        newJournal = cardsService.editCard(oldJournal, newJournal);


        journalService.saveJournal(newJournal);

        return "redirect:/journal";
    }

    @GetMapping("/journal/delete/{id}")
    public String deleteJournal(@PathVariable(value = "id") long id, Model model) {
        Optional<Journal> optionalJournal = journalService.findJournalById(id);
        if (!optionalJournal.isPresent()) {
            return "redirect:/journal";
        }

        cardsService.rollBackBalance(optionalJournal.get());
        journalService.deleteJournalById(id);

        return "redirect:/journal";
    }


}
