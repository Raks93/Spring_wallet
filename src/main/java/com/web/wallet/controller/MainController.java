package com.web.wallet.controller;

import com.web.wallet.service.CategoriesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@Controller
public class MainController {

    private final CategoriesService categoriesService;

    public MainController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping("/")
    public String start(Model model) {
        categoriesService.checkStandardCategoriesInDb();
        model.addAttribute("title", "Главная страница");
        return "start";
    }

    @PostMapping("/")
    public String startPost(@RequestParam String calendar, Model model) {
        Date date = java.sql.Date.valueOf(calendar);

        return "start";
    }

}