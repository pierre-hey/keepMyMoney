package fr.hey.keepmymoney.services.impl;

import fr.hey.keepmymoney.entities.Category;
import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.User;
import fr.hey.keepmymoney.entities.enumerations.EPeriod;
import fr.hey.keepmymoney.entities.enumerations.EType;
import fr.hey.keepmymoney.repositories.TransactionRepository;
import fr.hey.keepmymoney.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    @Override
    public void createTransactionWithPeriod(Transaction transaction) {
        // A la création d'une transaction, en fonction de la périodicité du type,
        // il faut créer les transactions des périodes suivantes sur 1 année
        final int NUMBER_MONTH_IN_YEAR = 12;
        final int NUMBER_WEEK_IN_YEAR = 52;
        final int NUMBER_QUARTERLY = 4;
        final int NUMBER_ANNUAL = 1;
        final int NUMBER_BIANNUAL = 2;
        // Enregistre la 1ère transaction (identique au ponctuel/annuel)


        switch (transaction.getCategory().getPeriod()) {
            case PUNCTUAL -> transactionRepository.save(transaction);
            // Par semaine - 52 fois par an
            case WEEKLY -> createTransactionsWithDuration(transaction, NUMBER_WEEK_IN_YEAR);
            // Mensuelle - 12 fois par an
            case MONTHLY -> createTransactionsWithDuration(transaction, NUMBER_MONTH_IN_YEAR);
            // Trimestrielle - 3 fois par an
            case QUARTERLY -> createTransactionsWithDuration(transaction, NUMBER_QUARTERLY);
            // Annuelle - 1 fois par an
            case ANNUAL -> createTransactionsWithDuration(transaction, NUMBER_ANNUAL);
            // Semestrielle - 2 fois par an
            case BIANNUAL -> createTransactionsWithDuration(transaction, NUMBER_BIANNUAL);
        }

    }

    private void createTransactionsWithDuration(Transaction transaction, int duration) {
        List<Transaction> transactionList = new ArrayList<>();
        LocalDate initialTransactionDate = transaction.getTransactionDate();
        LocalDate startDate = transaction.getTransactionDate();

        for (int i = 1; i < duration + 1; i++) {

            Transaction nextTransaction = new Transaction();
            LocalDate dateToCreate = startDate;
            nextTransaction.setTransactionDate(dateToCreate);
            if (transaction.getCategory().getPeriod().equals(EPeriod.MONTHLY)) {
                String nameOfMonth = startDate.getMonth().getDisplayName(TextStyle.FULL, Locale.FRANCE);
                nameOfMonth = nameOfMonth.substring(0, 1).toUpperCase() + nameOfMonth.substring(1).toLowerCase();
                nextTransaction.setLabel(transaction.getLabel() + " - " + nameOfMonth + " - " + i);
            } else {
                nextTransaction.setLabel(transaction.getLabel() + " - " + i);
            }
            nextTransaction.setAmount(transaction.getAmount());
            nextTransaction.setCategory(transaction.getCategory());
            nextTransaction.setUser(transaction.getUser());

            transactionList.add(nextTransaction);

            // Si la transaction est hebdomadaire, on ajoute 1 semaine à la date de la prochaine transaction
            if (transaction.getCategory().getPeriod().equals(EPeriod.WEEKLY)) {
                startDate = initialTransactionDate.plusWeeks(i);
            }
            // Si la transaction est mensuelle, on ajoute 1 mois à la date de la prochaine transaction
            if (transaction.getCategory().getPeriod().equals(EPeriod.MONTHLY)) {
                startDate = initialTransactionDate.plusMonths(i);
            }
            // Si la transaction est trimestrielle, on ajoute 3 mois à la date de la prochaine transaction
            if (transaction.getCategory().getPeriod().equals(EPeriod.QUARTERLY)) {
                startDate = dateToCreate.plusMonths(3);
            }
            // Si la transaction est annuelle, on ajoute 12 mois à la date de la prochaine transaction
            if (transaction.getCategory().getPeriod().equals(EPeriod.ANNUAL)) {
                startDate = dateToCreate.plusMonths(12);
            }
            // Si la transaction est semestrielle, on ajoute 6 mois à la date de la prochaine transaction
            if (transaction.getCategory().getPeriod().equals(EPeriod.BIANNUAL)) {
                startDate = dateToCreate.plusMonths(6);
            }
        }
        transactionRepository.saveAll(transactionList);
    }

    @Override
    public void createAllTransactions(List<Transaction> transactionList) {

    }

    @Override
    public Transaction findTransactionById(Integer id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public Transaction findTransactionByIdAndUserId(Integer transactionId, Integer userId) {
        return transactionRepository.findByIdAndUserId(transactionId, userId);
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
//        return transactionRepository.findAllByTransactionDateBetweenAndUserId(startDate, endDate, userId);
        return null;
    }

    @Override
    public List<Transaction> findAllTransactionsByMonthAndDateAndUserId(short dateMonth, int dateYear, Integer userId) {
//        return transactionRepository.findAllByTransactionDateMonthAndDateYearAndUserId(dateMonth, dateYear, userId);
        return null;
    }

    @Override
    public List<Transaction> findAllTransactionsByYearAndUserId(int dateYear, Integer userId) {
        return transactionRepository.findByDateYearAndUserId(dateYear, userId);
    }

    @Override
    public List<Transaction> findAllByLabelAndUserId(String label, Integer userId) {
        return transactionRepository.findAllByLabelLikeAndUserId(label, userId);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        /*
        TODO pour les transaction avec période != ponctuelle => on modifie les transactions ultérieures à la date du jour
            ajouter un bool pour détecter si on modifie uniquement la valeur de la transaction N ou si c'est pour celles du futurs ?
         */
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Transaction transaction) {
        transactionRepository.delete(transaction);
    }

    @Override
    public Page<Transaction> findTransactionWithSpec(String label, Category category, EType type,
                                                     LocalDate transactionDate, Integer dateMonth, Integer dateYear,
                                                     User user,
                                                     Pageable pageable) {

        Specification<Transaction> spec = Specification.where((root, query, builder) -> builder.equal(root.get("user"), user));

        if (!ObjectUtils.isEmpty(label)) {
            spec = spec.and((root, query, builder)
                    -> builder.like(root.get("label"), "%" + label + "%"));
        }
        if (!ObjectUtils.isEmpty(transactionDate)) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(root.get("transactionDate"), transactionDate));
        }
        spec = getTransactionSpecification(dateMonth, dateYear, type, spec);
        if (!ObjectUtils.isEmpty(category)) {
            spec = spec.and(((root, query, builder)
                    -> builder.equal(root.get("category"), category)));
        }


        return transactionRepository.findAll(spec, pageable);
    }

    @Override
    public List<Transaction> findTransactionsWithCategoryTypeAndUser(EType type, Integer userId) {
        return transactionRepository.findAllByCategory_TypeAndUserId(type, userId);
    }

    @Override
    public List<Transaction> findTransactionsWithUserAndCategoryAndMonth(Integer userId, Integer dateMonth, EType category_type) {
        return transactionRepository.findTransactionByUserAndMonthAndCategoryType(userId, dateMonth, category_type);
    }

    @Override
    public List<Transaction> findTransactionsWithUserAndMonthAndYearAndCategory(Integer userId, Integer dateMonth, Integer dateYear, EType category_type) {
        return transactionRepository.findTransactionByUserAndMonthAndYearAndCategoryType(userId, dateMonth, dateYear, category_type);
    }

    @Override
    public List<Transaction> findTransactionWithCriteria(User user, Integer dateMonth, Integer dateYear) {

        Specification<Transaction> spec = Specification.where((root, query, builder) -> builder.equal(root.get("user"), user));

        spec = getTransactionSpecificationWithMonthAndYear(dateMonth, dateYear, spec);

        return transactionRepository.findAll(spec);
    }

    private Specification<Transaction> getTransactionSpecificationWithMonthAndYear(Integer dateMonth, Integer dateYear, Specification<Transaction> spec) {
        if (!ObjectUtils.isEmpty(dateYear)) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(builder.function("year", Integer.class, root.get("transactionDate")), dateYear));
        }
        if (!ObjectUtils.isEmpty(dateMonth)) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(builder.function("month", Integer.class, root.get("transactionDate")), dateMonth));
        }
        return spec;
    }

    private Specification<Transaction> getTransactionSpecification(Integer dateMonth, Integer dateYear, EType type, Specification<Transaction> spec) {
        if (!ObjectUtils.isEmpty(dateYear)) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(builder.function("year", Integer.class, root.get("transactionDate")), dateYear));
        }
        if (!ObjectUtils.isEmpty(dateMonth)) {
            spec = spec.and((root, query, builder)
                    -> builder.equal(builder.function("month", Integer.class, root.get("transactionDate")), dateMonth));
        }

        if (!ObjectUtils.isEmpty(type)) {
            spec = spec.and(((root, query, builder)
                    -> builder.equal(root.get("category").get("type"), type)));
        }
        return spec;
    }

}
