package com.web.wallet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        return "home";
    }


//    @PostMapping("/home")
//    public String homePost( Model model) {
////        Date date = java.sql.Date.valueOf(calendar);
//
//        return "home";
//    }

}
