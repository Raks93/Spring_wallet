package com.web.wallet.service;

import com.web.wallet.entity.Cards;
import com.web.wallet.entity.Users;
import com.web.wallet.repository.CardsRepository;
import com.web.wallet.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardsService {

    private final CardsRepository cardsRepository;

    private final UsersRepository usersRepository;

    public CardsService(CardsRepository cardsRepository, UsersRepository usersRepository) {
        this.cardsRepository = cardsRepository;
        this.usersRepository = usersRepository;
    }

    public List<Cards> generatedStandardCards(Long userId) {
        String[] standardCardsNames = {"Наличные", "Карта Сбербанка"};
        List<Cards> cardsList = new ArrayList<>();

        Optional<Users> user = usersRepository.findById(userId);

        if (!user.isPresent() ) return null;

        for (String standardCardsName : standardCardsNames) {
            if (user.get().getCardsList() == null || user.get().getCardsList().isEmpty()) {
                Cards card = new Cards(standardCardsName, 0L);
                cardsList.add(card);
                card.setUsers(user.get());
                cardsRepository.save(card);
            }
            else {
                List<Cards> existCards = user.get().getCardsList();
                boolean checkExist = true;
                for (Cards existCard : existCards) {
                    if (existCard.getName().equals(standardCardsName)) {
                        checkExist = false;
                        break;
                    }
                }
                if (checkExist) {
                    cardsList = existCards;
                    Cards card = new Cards(standardCardsName, 0L);
                    cardsList.add(card);
                    card.setUsers(user.get());
                    cardsRepository.save(card);
                }
            }
        }

//        for (String standardCardsName : standardCardsNames) {
//              if (user.isPresent() && !user.get().getCardsList().isEmpty()) {
//                Cards card = new Cards(standardCardsName, 0L);
//                cardsList.add(card);
//                card.setUsers(usersRepository.findById(userId).get());
//                cardsRepository.save(card);
//                System.out.println(card);
//            }
//            System.out.println(user.get().getCardsList());
//        }

        return cardsList;
    }

    public List<Cards> findAllCards() {
        return cardsRepository.findAll();
    }

    public void saveCard(Cards cards) {
        cardsRepository.save(cards);
    }

}
