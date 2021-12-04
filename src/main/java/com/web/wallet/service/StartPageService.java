package com.web.wallet.service;

import com.web.wallet.entity.StartPage;
import com.web.wallet.repository.StartPageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StartPageService {

    private final StartPageRepository startPageRepository;

    public StartPageService(StartPageRepository startPageRepository) {
        this.startPageRepository = startPageRepository;
    }

    public void savePage(StartPage startPage) {
        startPageRepository.save(startPage);
    }

    public List<StartPage> getAll() {
        return startPageRepository.findAll();
    }

    public StartPage findRecordByName(String name) {
        return startPageRepository.findByName(name);
    }

    public void deleteAllRecords() {
        startPageRepository.deleteAll();
    }
}
