package com.web.wallet.service;

import com.web.wallet.entity.Journal;
import com.web.wallet.repository.JournalRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JournalService {

    private final JournalRepository journalRepository;

    public JournalService(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    public void saveJournal(Journal journal) {
        journalRepository.save(journal);
    }

    public Optional<Journal> findJournalById(long id) {
        return journalRepository.findById(id);
    }

    public boolean existJournalById(long id) {
        return journalRepository.existsById(id);
    }

    public void deleteJournalById(long id) {
        journalRepository.deleteById(id);
    }
}
