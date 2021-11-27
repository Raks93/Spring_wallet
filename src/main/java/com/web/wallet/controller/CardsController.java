package com.web.wallet.controller;

import com.web.wallet.entity.Users;
import com.web.wallet.service.UsersService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CardsController {

    private final UsersService usersService;

    public CardsController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/cards")
    public String home(Model model) {
        Users user = usersService.findUserByName(SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("cards", user.getCardsList());
        return "cards";
    }


//    @PostMapping("/home")
//    public String homePost( Model model) {
////        Date date = java.sql.Date.valueOf(calendar);
//
//        return "home";
//    }

}
