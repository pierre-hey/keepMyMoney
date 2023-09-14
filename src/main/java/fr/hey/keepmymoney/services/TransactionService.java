package fr.hey.keepmymoney.services;

import fr.hey.keepmymoney.entities.Category;
import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.enumerations.EType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Service de la classe Transaction
 */
public interface TransactionService {

    /**
     * Créé des transactions en fonction de leurs périodes
     *
     * @param transaction transaction
     */
    void createTransactionWithPeriod(Transaction transaction);

    /**
     * Créé plusieurs transactions
     */
    void createAllTransactions(List<Transaction> transactionList);

    /**
     * Recherche une transaction par son id
     *
     * @param id id
     * @return Transaction
     */
    Transaction findTransactionById(Integer id);

    /**
     * Recherche une transaction par son id et l'id de l'utilisateur
     *
     * @param transactionId id transaction
     * @param userId        id utilisateur
     * @return Transaction
     */
    Transaction findTransactionByIdAndUserId(Integer transactionId, Integer userId);

    /**
     * Recherche toutes les transactions pour un utilisateur
     *
     * @param userId id utilisateur
     * @return Liste de transactions
     */
    List<Transaction> findAllTransactionsByUserId(Integer userId);

    /**
     * Recherche toutes les transactions d'une catégorie pour un utilisateur
     *
     * @param categoryId id catégorie
     * @param userId     id utilisateur
     * @return Liste de transactions
     */
    List<Transaction> findAllTransactionsByCategoryIdAndUserId(Integer categoryId, Integer userId);


    /**
     * Recherche les transactions d'un utilisateur entre deux dates
     *
     * @param startDate date de début
     * @param endDate   date de fin
     * @param userId    id utilisateur
     * @return Liste de transactions
     */
    List<Transaction> findAllTransactionsBetweenDateAndUserId(LocalDate startDate, LocalDate endDate, Integer userId);

    /**
     * Recherche les transactions d'un utilisateur pour un mois d'une année
     *
     * @param dateMonth mois
     * @param dateYear  année
     * @param userId    id utilisateur
     * @return Liste de transactions
     */
    List<Transaction> findAllTransactionsByMonthAndDateAndUserId(short dateMonth, int dateYear, Integer userId);

    /**
     * Recherche les transactions d'un utilisateur sur une année
     *
     * @param dateYear année
     * @param userId   id utilisateur
     * @return Liste de transactions
     */
    List<Transaction> findAllTransactionsByYearAndUserId(int dateYear, Integer userId);

    /**
     * Recherche les transactions d'un utilisateur à partir d'un label
     *
     * @param label  label
     * @param userId id utilisateur
     * @return Liste de transactions
     */
    List<Transaction> findAllByLabelAndUserId(String label, Integer userId);


    /**
     * Met à jour une transaction
     *
     * @param transaction transaction
     * @return Transaction
     */
    Transaction updateTransaction(Transaction transaction);

    /**
     * Supprime une transaction
     *
     * @param transaction transaction
     */
    void deleteTransaction(Transaction transaction);


    /**
     * Recherche des transactions d'un utilisateur en fonction de différents critères
     *
     * @param label           filtre label
     * @param category        filtre catégorie
     * @param transactionDate filtre date
     * @param dateMonth       filtre mois
     * @param dateYear        filtre année
     * @param userId          filtre utilisateur
     * @return Page de transaction
     */
    Page<Transaction> findTransactionWithSpec(String label, Category category, EType type, LocalDate transactionDate, Integer dateMonth, Integer dateYear, Integer userId, Pageable pageable);


    /**
     * Recherche des transactions par type de leurs catégories et par id user
     *
     * @param type   type de la catégorie
     * @param userId utilisateur id
     * @return Liste de transactions
     */
    List<Transaction> findTransactionsWithCategoryTypeAndUser(EType type, Integer userId);

    /**
     * Recherche des transactions d'un utilisateur par type de catégorie pour un mois donné
     *
     * @param userId        utilisateur id
     * @param dateMonth     mois
     * @param category_type type de la catégorie des transactions
     * @return Liste de transactions
     */
    List<Transaction> findTransactionsWithUserAndCategoryAndMonth(Integer userId, Integer dateMonth, EType category_type);


    /**
     * Recherche des transactions d'un utilisateur par type de catégorie pour un mois et une année donnés
     *
     * @param userId        utilisateur id
     * @param dateMonth     mois
     * @param dateYear      année
     * @param category_type type de la catégorie des transactions
     * @return Liste de transactions
     */
    List<Transaction> findTransactionsWithUserAndMonthAndYearAndCategory(Integer userId, Integer dateMonth, Integer dateYear,
                                                                         EType category_type);


    /**
     * @param userId    utilisateur id
     * @param dateMonth mois
     * @param dateYear  année
     * @return Liste de transactions
     */
    List<Transaction> findTransactionWithCriteria(Integer userId, Integer dateMonth, Integer dateYear);
}
