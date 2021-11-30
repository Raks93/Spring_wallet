package com.web.wallet.controller;

import com.web.wallet.entity.Cards;
import com.web.wallet.entity.Journal;
import com.web.wallet.entity.Users;
import com.web.wallet.service.CardsService;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Controller
public class CardsController {

    private final CardsService cardsService;

    private final UsersService usersService;

    public CardsController(CardsService cardsService, UsersService usersService) {
        this.cardsService = cardsService;
        this.usersService = usersService;
    }

    @GetMapping("/cards")
    public String home(Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        user.getCardsList().sort(Comparator.comparing(Cards::getName));

        model.addAttribute("cards", user.getCardsList());
        return "cards";
    }

    @GetMapping("/cards/edit/{id}")
    public String editCard(@PathVariable(value = "id") long id, Model model) {
        Optional<Cards> optionalCards = cardsService.findCardById(id);

        if (!optionalCards.isPresent()) {
            return "redirect:/cards";
        }

        model.addAttribute("cards", optionalCards.get());

        return "cards_edit";
    }

    @PostMapping("/cards/edit/{id}")
    public String updateCard(@PathVariable(value = "id") long id, @RequestParam long balance, @RequestParam String name, Model model) {
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
    public String deleteCard(@PathVariable(value = "id") long id, @PathVariable(value = "size") long size, Model model) {

        if (size == 1) {
            model.addAttribute("message", "Нельзя удалить последний счет");
            model.addAttribute("cards", cardsService.findCardById(id).orElse(new Cards()));
            return "cards";

        }

        cardsService.deleteCardById(id);

        return "redirect:/cards";
    }

    @GetMapping("/cards/replace/{id}/{size}")
    public String replaceCard(@PathVariable(value = "id") long id, @PathVariable(value = "size") long size, Model model) {
        if (size == 1) {
            model.addAttribute("message", "Нельзя перенести, если счёт один");
            model.addAttribute("cards", cardsService.findCardById(id).orElse(new Cards()));
            return "cards";
        }

        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        System.out.println(user);

        model.addAttribute("fromCategory", id);
        model.addAttribute("cards", user.getCardsList());

        return "cards_replace";
    }

}
