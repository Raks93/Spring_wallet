package com.web.wallet.service;

import com.web.wallet.entity.Cards;
import com.web.wallet.entity.Categories;
import com.web.wallet.entity.Journal;
import com.web.wallet.entity.Users;
import com.web.wallet.repository.CardsRepository;
import com.web.wallet.repository.CategoriesRepository;
import com.web.wallet.repository.JournalRepository;
import com.web.wallet.repository.UsersRepository;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.apache.poi.ss.usermodel.CellType.*;


@Service
public class JournalService {

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final JournalRepository journalRepository;

    private final CardsRepository cardsRepository;

    private final CategoriesRepository categoriesRepository;

    private final UsersRepository usersRepository;

    public JournalService(JournalRepository journalRepository, CardsRepository cardsRepository, CategoriesRepository categoriesRepository, UsersRepository usersRepository) {
        this.journalRepository = journalRepository;
        this.cardsRepository = cardsRepository;
        this.categoriesRepository = categoriesRepository;
        this.usersRepository = usersRepository;
    }

    public void saveJournal(Journal journal) {
        journalRepository.save(journal);
    }

    public Optional<Journal> findJournalById(long id) {
        return journalRepository.findById(id);
    }

    public void deleteJournalById(long id) {
        journalRepository.deleteById(id);
    }

    public void deleteAllRecords() {
        journalRepository.deleteAll();
    }

    public void readFile(Workbook wb) {
        Users user = usersRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<Categories> categoriesList = user.getCategoriesList();
        List<Cards> cardsList = user.getCardsList();
        List<Journal> journalList = user.getJournalList();

        System.out.println("Пустая строка" + wb.getSheetAt(0).getRow(0).getCell(5).getRichStringCellValue().getString().trim().equals(""));
        System.out.println("Тип булен" + (wb.getSheetAt(0).getRow(1).getCell(5).getCellType() == BOOLEAN));

        for (Row row : wb.getSheetAt(0)) {
            if ((row.getCell(0) != null) && (row.getCell(3) != null) && (row.getCell(4) != null) &&
                    (row.getCell(0).getCellType() == NUMERIC) && (!DateUtil.isCellDateFormatted(row.getCell(0))) &&
                    (row.getCell(3).getCellType() == STRING) && (row.getCell(4).getCellType() == STRING) &&
                    !String.valueOf(row.getCell(0).getNumericCellValue()).trim().equals("")  &&
                    !row.getCell(3).getRichStringCellValue().getString().trim().equals("") &&
                    !row.getCell(4).getRichStringCellValue().getString().trim().equals("")) {
                Cards card = new Cards();
                Categories categories = new Categories();

                boolean checkExist = false;
                for (Cards cards : cardsList) {
                    if (cards.getName().equals(row.getCell(3).getRichStringCellValue().getString())) {
                        checkExist = true;
                        card = cards;
                        break;
                    }
                }
                if (!checkExist) {
                    card = new Cards(row.getCell(3).getRichStringCellValue().getString(), 0L);
                    card.setUsers(user);
                    cardsRepository.save(card);
                    cardsList.add(card);
                }
                user.setCardsList(cardsList);
                checkExist = false;

                for (Categories category : categoriesList) {
                    if (category.getName().equals(row.getCell(4).getRichStringCellValue().getString())) {
                        categories = category;
                        checkExist = true;
                        break;
                    }
                }
                if (!checkExist) {
                    if (row.getCell(5) != null && row.getCell(5).getCellType() == BOOLEAN && row.getCell(5).getBooleanCellValue() == Boolean.TRUE) {
                        categories = new Categories(row.getCell(4).getRichStringCellValue().getString(), row.getCell(5).getBooleanCellValue());
                    }
                    else {
                        categories = new Categories(row.getCell(4).getRichStringCellValue().getString(), false);
                    }
                    categories.setUsers(user);
                    categoriesRepository.save(categories);
                    categoriesList.add(categories);
                }
                user.setCategoriesList(categoriesList);
                usersRepository.save(user);


                Journal journal = new Journal();
                journal.setAmount(Math.abs(Double.valueOf(row.getCell(0).getNumericCellValue()).longValue()));
                journal.setInOutMoney(categories.getIncome());
                if (row.getCell(1) != null && row.getCell(1).getCellType() == NUMERIC && DateUtil.isCellDateFormatted(row.getCell(1))) {
                    journal.setDate(LocalDate.parse(simpleDateFormat.format(row.getCell(1).getDateCellValue())));
                }
                else {
                    journal.setDate(LocalDate.now());
                }
                if (row.getCell(2) != null && row.getCell(2).getCellType() == STRING) {
                    journal.setPurchase(row.getCell(2).getRichStringCellValue().getString());
                }
                else {
                    journal.setPurchase("Что-то");
                }

                journal.setCategories(categories);
                journal.setCards(card);
                journal.setUsers(user);
                journalRepository.save(journal);

                Long balance = card.getBalance() + (journal.getInOutMoney() ? journal.getAmount() : -journal.getAmount());
                card.setBalance(balance);
                cardsRepository.save(card);
            }

        }
    }
}
