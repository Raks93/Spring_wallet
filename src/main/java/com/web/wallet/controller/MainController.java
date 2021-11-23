package com.web.wallet.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Slf4j
@Controller
public class MainController {

    @GetMapping("/")
    public String start(Model model) {
        model.addAttribute("title", "Главная страница");
        return "start";
    }

    @PostMapping("/")
    public String homePost(@RequestParam String calendar, Model model) {
        Date date = java.sql.Date.valueOf(calendar);
        log.info(date.toString());

        return "home";
    }

}