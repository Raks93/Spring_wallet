package com.web.wallet.service;

import com.web.wallet.entity.Role;
import com.web.wallet.entity.Users;
import com.web.wallet.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    public Users findUserByName(String username) {
        return usersRepository.findByUsername(username);
    }

    public void saveUser(Users user) {
        usersRepository.save(user);
    }

    public void generateFewUsers() {
        if (usersRepository.findAll().isEmpty()) {
            for (int i = 1; i < 10; i++) {
                Users user = new Users(String.valueOf(i), String.valueOf(i), true, Collections.singleton(Role.USER));
                usersRepository.save(user);
            }
        }
    }

}
