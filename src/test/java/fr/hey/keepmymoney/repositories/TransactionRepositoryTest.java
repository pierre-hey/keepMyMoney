package fr.hey.keepmymoney.repositories;

import fr.hey.keepmymoney.dto.UserDto;
import fr.hey.keepmymoney.entities.Category;
import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.User;
import fr.hey.keepmymoney.entities.enumerations.EPeriod;
import fr.hey.keepmymoney.entities.enumerations.EType;
import fr.hey.keepmymoney.services.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TransactionRepositoryTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final  TransactionRepository transactionRepository;
    private final  CategoryRepository categoryRepository;
    private final UserService userService;

    @Autowired
    public TransactionRepositoryTest(TransactionRepository transactionRepository, CategoryRepository categoryRepository, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
    }



    @Test
    @Transactional
     void testSave() {
        logger.info("####### testSave() #######");
        // Récupère la liste des transactions avant l'insertion d'une nouvelle
        List<Transaction> transactionsBefore = transactionRepository.findAll();
        Integer transactionsSize = transactionsBefore.size();

        // Ajouter un utilisateur pour le test
        UserDto userDto = new UserDto();
        userDto.setLogin("UserTest");
        userDto.setPassword("UserTest");
        userDto.setMatchingPassword("UserTest");
        userService.saveUser(userDto);
        User user = userService.findByLogin(userDto.getLogin());
        logger.info(String.format("Utilisateur ajouté : %s", user));

        // Ajouter une catégorie pour le test
        Category category = new Category();
        category.setLabel("Salaire");
        category.setType(EType.INCOME);
        category.setPeriod(EPeriod.MONTHLY);
        categoryRepository.save(category);
        logger.info(String.format("Catégorie ajoutée : %s", category));

        // Ajouter la transaction pour le test
        Transaction transaction = new Transaction();
        transaction.setCategory(category);
        transaction.setLabel("Salaire du mois de mars");
        transaction.setUser(user);
        transaction.setAmount(1800.00);
        transaction.setTransactionDate(LocalDate.now());
        transactionRepository.save(transaction);
        logger.info(String.format("Transaction ajoutée : %s", transaction));
        transactionsSize++;

        List<Transaction> transactionsAfter = transactionRepository.findAll();

        assertEquals(transactionsSize, transactionsAfter.size());
        logger.info("##########################");
    }


    @Test
    @Transactional
    void testFindById() {

    }
}
