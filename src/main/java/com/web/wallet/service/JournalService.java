package com.web.wallet.service;

import com.web.wallet.repository.JournalRepository;
import org.springframework.stereotype.Service;

@Service
public class JournalService {

    private final JournalRepository journalRepository;


    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }
}
