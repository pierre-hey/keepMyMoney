package fr.hey.keepmymoney.services.impl;

import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.repositories.TransactionRepository;
import fr.hey.keepmymoney.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public Transaction findTransactionById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> findAllTransactionsByUserId(Integer userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    @Override
    public List<Transaction> findAllTransactionsByCategoryIdAndUserId(Integer categoryId, Integer userId) {
        return transactionRepository.findAllByCategoryIdAndUserId(categoryId, userId);
    }

    @Override
    public List<Transaction> findAllTransactionsBetweenDateAndUserId(LocalDate startDate, LocalDate endDate, Integer userId) {
        return transactionRepository.findAllByDateBetweenAndUserId(startDate, endDate, userId);
    }

    @Override
    public List<Transaction> findAllTransactionsByMonthAndDateAndUserId(short dateMonth, int dateYear, Integer userId) {
       return transactionRepository.findAllByDateMonthAndDateYearAndUserId(dateMonth, dateYear, userId);
     //   return null;
    }

    @Override
    public List<Transaction> findAllTransactionsByYearAndUserId(int dateYear, Integer userId) {
        return transactionRepository.findByDateYearAndUserId(dateYear, userId);
//        return null;
    }

    @Override
    public List<Transaction> findAllByLabelAndUserId(String label, Integer userId) {
        return transactionRepository.findAllByLabelLikeAndUserId(label, userId);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }
}
