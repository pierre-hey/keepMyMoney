package fr.hey.keepmymoney.repositories;

import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.enumerations.EType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {

    Transaction findByIdAndUserId(Integer transactionId, Integer userId);

    List<Transaction> findAllByUserId(Integer userId);

    List<Transaction> findAllByCategoryIdAndUserId(Integer categoryId, Integer userId);

//    List<Transaction> findAllByTransactionDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Integer userId);


/*  @Query("SELECT t FROM Transaction t " +
          "WHERE MONTH(date)=:dateMonth " +
          "AND YEAR(date)=:dateYear " +
          "AND t.user.id=:userId "
  )
  List<Transaction> findAllByTransactionDateMonthAndDateYearAndUserId(@Param("dateMonth") short dateMonth, @Param("dateYear")int dateYear, Integer userId);*/



    @Query("SELECT t FROM Transaction t " +
            "WHERE YEAR(t.transactionDate)=:dateYear " +
            "AND t.user.id=:userId "
            )
    List<Transaction> findByDateYearAndUserId(@Param("dateYear")int dateYear, @Param("userId")Integer userId);

    List<Transaction> findAllByLabelLikeAndUserId(String label, Integer userId);

    List<Transaction> findAllByCategory_TypeAndUserId(EType category_type, Integer userId);

    @Query( "SELECT t FROM Transaction t " +
            "WHERE t.user.id=:userId " +
            "AND MONTH(t.transactionDate)=:dateMonth " +
            "AND t.category.type=:category_type "
    )
    List<Transaction> findTransactionByUserAndMonthAndCategoryType(@Param("userId")Integer userId,
                                                            @Param("dateMonth")Integer dateMonth,
                                                            @Param("category_type")EType category_type);


    @Query( "SELECT t FROM Transaction t " +
            "WHERE t.user.id=:userId " +
            "AND MONTH(t.transactionDate)=:dateMonth " +
            "AND YEAR(t.transactionDate)=:dateYear " +
            "AND t.category.type=:category_type "
    )
    List<Transaction> findTransactionByUserAndMonthAndYearAndCategoryType(@Param("userId")Integer userId,
                                                                   @Param("dateMonth")Integer dateMonth,
                                                                   @Param("dateYear")Integer dateYear,
                                                                   @Param("category_type")EType category_type);
}
