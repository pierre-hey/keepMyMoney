package fr.hey.keepmymoney.services;

import fr.hey.keepmymoney.entities.Transaction;

import java.time.LocalDate;
import java.util.List;

/**
 * Service de la classe Transaction
 */
public interface TransactionService {

    /**
     * Créé une transaction
     *
     * @param transaction transaction
     */
    void createTransaction(Transaction transaction);

    /**
     * Recherche un transaction par son id
     *
     * @param id id
     * @return Transaction
     */
    Transaction findTransactionById(Integer id);

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
     * @return List de transactions
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
     * @param label label
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


}
