package com.web.wallet.controller;

import com.web.wallet.entity.Users;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class JournalUserController {

    @GetMapping("/journal")
    public String about(Model model) {

        return "journal";
    }
}
