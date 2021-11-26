package com.web.wallet.controller;

import com.web.wallet.entity.Users;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UsersController {

    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

//    @GetMapping("/home/{id}")
//    public String currentUser(@PathVariable(value = "id") long id, Model model) {
//        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());
//        if (user.getId() != id)
//            return "redirect:/";
//
//        model.addAttribute("id", String.valueOf(id));
//        return "home";
//    }

//    @GetMapping("/users")
//    public String users(Model model) {
//        model.addAttribute("title", "Пользователи");
//        model.addAttribute("users", usersService.findAllUsers());
//        return "users";
//    }
}
