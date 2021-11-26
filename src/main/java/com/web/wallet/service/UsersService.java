package com.web.wallet.service;

import com.web.wallet.entity.Users;
import com.web.wallet.repository.UsersRepository;
import org.springframework.stereotype.Service;

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

}
