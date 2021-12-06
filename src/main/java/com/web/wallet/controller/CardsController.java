package com.web.wallet.controller;

import com.web.wallet.entity.Cards;
import com.web.wallet.entity.Journal;
import com.web.wallet.entity.Users;
import com.web.wallet.service.CardsService;
import com.web.wallet.service.JournalService;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class CardsController {

    private final CardsService cardsService;

    private final UsersService usersService;

    private final JournalService journalService;

    public CardsController(CardsService cardsService, UsersService usersService, JournalService journalService) {
        this.cardsService = cardsService;
        this.usersService = usersService;
        this.journalService = journalService;
    }

    @GetMapping("/cards")
    public String home(Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        user.getCardsList().sort(Comparator.comparing(Cards::getName));

        model.addAttribute("cards", user.getCardsList());
        return "cards";
    }

    @GetMapping("/cards/add")
    public String addCard(Model model) {

        Cards card = new Cards("", 0L);

        model.addAttribute("cards", card);
        return "cards_add";
    }

    @PostMapping("/cards/add")
    public String postAddCard(@RequestParam Long balance, @RequestParam String name, Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Cards> cardsList = user.getCardsList();
        Cards card = new Cards(name, balance);

        for (Cards cards : cardsList) {
            if (cards.getName().equals(name)) {
                model.addAttribute("cards", card);
                model.addAttribute("message", "Такой счёт существует!");
                return "cards_add";
            }
        }

        card.setUsers(user);
        cardsService.saveCard(card);

        cardsList.add(card);
        user.setCardsList(cardsList);

        return "redirect:/cards";
    }

    @GetMapping("/cards/edit/{id}")
    public String editCard(@PathVariable(value = "id") Long id, Model model) {
        Optional<Cards> optionalCards = cardsService.findCardById(id);

        if (!optionalCards.isPresent()) {
            return "redirect:/cards";
        }

        model.addAttribute("cards", optionalCards.get());

        return "cards_edit";
    }

    @PostMapping("/cards/edit/{id}")
    public String updateCard(@PathVariable(value = "id") Long id, @RequestParam Long balance, @RequestParam String name, Model model) {
        Optional<Cards> optionalCards = cardsService.findCardById(id);

        if (!optionalCards.isPresent()) {
            return "redirect:/cards";
        }

        optionalCards.get().setBalance(balance);
        optionalCards.get().setName(name);
        cardsService.saveCard(optionalCards.get());

        return "redirect:/cards";
    }

    @GetMapping("/cards/delete/{id}/{size}")
    public String deleteCard(@PathVariable(value = "id") Long id, @PathVariable(value = "size") Long size, Model model) {

        if (size == 1) {
            model.addAttribute("message", "Нельзя удалить последний счет");
            model.addAttribute("cards", cardsService.findCardById(id).orElse(new Cards()));
            return "cards";

        }

        cardsService.deleteCardById(id);

        return "redirect:/cards";
    }

    @GetMapping("/cards/replace/{id}/{size}")
    public String replaceCard(@PathVariable(value = "id") Long id, @PathVariable(value = "size") Long size, Model model) {
        if (size == 1) {
            model.addAttribute("message", "Нельзя перенести, если счёт один");
            model.addAttribute("cards", cardsService.findCardById(id).orElse(new Cards()));
            return "cards";
        }

        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("fromCategory", id);
        model.addAttribute("cards", user.getCardsList());

        return "cards_replace";
    }

    @PostMapping("/cards/replace/{id}/{size}")
    public String postReplaceCard(@PathVariable(value = "id") Long id, @PathVariable(value = "size") Long size, @RequestParam Long chooseCard, Model model) {

        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        Optional<Cards> cardById = cardsService.findCardById(chooseCard);

        if (!cardById.isPresent()) {
            return "redirect:/cards";
        }

        List<Journal> journalList = user.getJournalList();
        for (Journal journal : journalList) {
            if (journal.getCards().getId().equals(id)) {
                Journal newJournal = new Journal(journal.getAmount(), journal.getDate(), journal.getPurchase(), journal.getInOutMoney());
                newJournal.setId(journal.getId());
                newJournal.setCategories(journal.getCategories());
                newJournal.setUsers(journal.getUsers());
                newJournal.setCards(cardById.get());
                newJournal = cardsService.editCard(journal, newJournal);
                journalService.saveJournal(newJournal);
            }
        }

        return "redirect:/cards";
    }

}
