package com.web.wallet.controller;

import com.web.wallet.service.UsersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("title", "Пользователи");
        model.addAttribute("users", usersService.findAllUsers());
        return "users";
    }
}
