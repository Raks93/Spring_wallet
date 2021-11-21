package com.web.wallet.service;

import com.web.wallet.repository.CardsRepository;
import org.springframework.stereotype.Service;

@Service
public class CardsService {

    private final CardsRepository cardsRepository;

    public CardsService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }
}
