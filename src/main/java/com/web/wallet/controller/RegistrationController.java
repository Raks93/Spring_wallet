package com.web.wallet.controller;

import com.web.wallet.entity.Users;
import com.web.wallet.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class RegistrationController {
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(Users user, Model model) {
        Users userFromDb = usersRepository.findByLoginAndPassword(user.getLogin(), user.getPassword());

        log.info(user.toString());

        if (userFromDb != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        usersRepository.save(user);

        return  "redirect:/login";
    }
}
