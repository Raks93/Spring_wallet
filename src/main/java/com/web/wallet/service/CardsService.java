package com.web.wallet.service;

import com.web.wallet.entity.Cards;
import com.web.wallet.entity.Journal;
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

        return cardsList;
    }

    public List<Cards> findAllCards() {
        return cardsRepository.findAll();
    }

    public void saveCard(Cards cards) {
        cardsRepository.save(cards);
    }

    public Optional<Cards> findCardById(long id) {
        return cardsRepository.findById(id);
    }

    public Journal editCard(Journal oldJournal, Journal newJournal) {
        long oldJournalAmount = oldJournal.getInOutMoney() ? -oldJournal.getAmount() : oldJournal.getAmount();
        long newJournalAmount = newJournal.getInOutMoney() ? newJournal.getAmount() : -newJournal.getAmount();

        if (oldJournal.getCards().equals(newJournal.getCards())) {
            newJournal.getCards().setBalance(newJournal.getCards().getBalance() + oldJournalAmount + newJournalAmount);
        }
        else {
            oldJournal.getCards().setBalance(oldJournal.getCards().getBalance() + oldJournalAmount);
            newJournal.getCards().setBalance(newJournal.getCards().getBalance() + newJournalAmount);
            cardsRepository.save(oldJournal.getCards());
        }

        cardsRepository.save(newJournal.getCards());

        return newJournal;
    }

    public void rollBackBalance(Journal journal) {
        long amount = journal.getInOutMoney() ? -journal.getAmount() : journal.getAmount();
        journal.getCards().setBalance(journal.getCards().getBalance() + amount);
        cardsRepository.save(journal.getCards());
    }

    public void deleteCardById(long id) {
        cardsRepository.deleteById(id);
    }

    public Cards findCardByName(String name) {
        return cardsRepository.findByName(name);
    }
}
