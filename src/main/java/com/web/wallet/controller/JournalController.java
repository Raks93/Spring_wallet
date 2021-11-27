package com.web.wallet.controller;

import com.web.wallet.entity.Users;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.Comparator;

@Controller
public class JournalController {

    private final UsersService usersService;

    public JournalController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/journal")
    public String journal(Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        System.out.println(user.getJournalList());
        user.getJournalList().sort(Collections.reverseOrder(Comparator.comparing(j -> j.getDate().toString())));

        model.addAttribute("journal", user.getJournalList());

        return "journal";
    }
}
