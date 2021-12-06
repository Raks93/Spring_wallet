package com.web.wallet.controller;

import com.web.wallet.entity.Cards;
import com.web.wallet.entity.Journal;
import com.web.wallet.entity.Users;
import com.web.wallet.service.CardsService;
import com.web.wallet.service.CategoriesService;
import com.web.wallet.service.JournalService;
import com.web.wallet.service.UsersService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.time.LocalDate;
import java.util.*;

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

    @PostMapping("/journal")
    public String loadJournal(@RequestParam MultipartFile file, Model model) throws IOException {

        InputStream inputStream =  new BufferedInputStream(file.getInputStream());
        StringBuilder stringBuilder = new StringBuilder(file.getOriginalFilename());
        stringBuilder.delete(0, stringBuilder.lastIndexOf(".") + 1);
        if (stringBuilder.toString().equals("xls")) {
            journalService.readFile(new HSSFWorkbook(inputStream));

        }
        else if (stringBuilder.toString().equals("xlsx")) {
            journalService.readFile(new XSSFWorkbook(inputStream));
        }
        else {
            Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
            user.getJournalList().sort(Collections.reverseOrder(Comparator.comparing(j -> j.getDate().toString())));
            model.addAttribute("journal", user.getJournalList());
            model.addAttribute("message", "Ошибка выбора файла");
            return "journal";
        }

        inputStream.close();


        return "redirect:/journal";
    }

    @GetMapping("/journal/add")
    public String addJournal(Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        Journal journal = new Journal(0L, LocalDate.now(), "", true);

        model.addAttribute("journal", journal);
        model.addAttribute("categories", user.getCategoriesList());
        model.addAttribute("cards", user.getCardsList());

        return "journal_add";
    }



    @PostMapping("/journal/add")
    public String postAddJournal(@RequestParam Long amount, @RequestParam String purchase, @RequestParam String date,
                                 @RequestParam Long chooseCategory, @RequestParam Long chooseCard, Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        Optional<Cards> optionalCards = cardsService.findCardById(chooseCard);

        if (!optionalCards.isPresent()) {
            return "redirect:/journal";
        }

        Cards cards = optionalCards.get();

        Journal journal = new Journal(amount, LocalDate.parse(date), purchase, categoriesService.findIncomeById(chooseCategory));
        journal.setCategories(categoriesService.findCategoryById(chooseCategory).orElse(null));
        journal.setCards(cards);
        journal.setUsers(user);
        journalService.saveJournal(journal);

        Long balance = cards.getBalance() + (journal.getInOutMoney() ? journal.getAmount() : -journal.getAmount());
        cards.setBalance(balance);
        cardsService.saveCard(cards);

        return "redirect:/journal";
    }

    @GetMapping("/journal/edit/{id}")
    public String editJournal(@PathVariable(value = "id") Long id, Model model) {
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
    public String updateJournal(@PathVariable(value = "id") Long id, @RequestParam Long amount, @RequestParam String purchase,
                                @RequestParam String date, @RequestParam Long chooseCategory, @RequestParam Long chooseCard, Model model) {
        Optional<Journal> optionalJournal = journalService.findJournalById(id);
        if (!optionalJournal.isPresent()) {
            return "redirect:/journal";
        }

        Journal oldJournal = optionalJournal.get();
        Journal newJournal = new Journal(amount, LocalDate.parse(date), purchase, categoriesService.findIncomeById(chooseCategory));
        newJournal.setCategories(categoriesService.findCategoryById(chooseCategory).orElse(null));
        newJournal.setCards(cardsService.findCardById(chooseCard).orElse(null));
        newJournal.setUsers(oldJournal.getUsers());
        newJournal.setId(oldJournal.getId());

        newJournal = cardsService.editCard(oldJournal, newJournal);

        journalService.saveJournal(newJournal);

        return "redirect:/journal";
    }

    @GetMapping("/journal/delete/{id}")
    public String deleteJournal(@PathVariable(value = "id") Long id, Model model) {
        Optional<Journal> optionalJournal = journalService.findJournalById(id);
        if (!optionalJournal.isPresent()) {
            return "redirect:/journal";
        }

        cardsService.rollBackBalance(optionalJournal.get());
        journalService.deleteJournalById(id);

        return "redirect:/journal";
    }


}
