package com.web.wallet.service;

import com.web.wallet.entity.Users;
import com.web.wallet.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public Iterable<Users> findAllUsers() {
        return usersRepository.findAll();
    }

}
