/*
package fr.hey.keepmymoney.repositories;

import fr.hey.keepmymoney.entities.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.OptionalValueBinding;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TransactionRepositoryPagination extends PagingAndSortingRepository<Transaction, Integer>, JpaSpecificationExecutor<Transaction> {

    //  Transaction findByIdAndUserId(Integer transactionId, Integer userId);

    Page<Transaction> findAllByUserId(Integer userId, Pageable pageable);


    @Query("SELECT t FROM Transaction t " +
            "WHERE YEAR(t.transactionDate) = :dateYear " +
            "AND t.user.id= :userId "
    )
    Page<Transaction> findByYearAndUser(@Param("dateYear") Integer dateYear, @Param("userId") Integer userId, Pageable pageable);

    Page<Transaction> findAllByLabelContainsAndUserId(String label, Integer userId, Pageable pageable);



 */
/*   @Query("SELECT t FROM Transaction t " +
            "WHERE (t.label IS NOT NULL OR t.label like :label) " +
            "AND (t.transactionDate IS NOT NULL OR  t.transactionDate= :transactionDate) " +
            "AND (MONTH(t.transactionDate) IS NOT NULL OR MONTH(t.transactionDate) = :dateMonth) " +
            "AND (YEAR(t.transactionDate) IS NOT NULL OR YEAR(t.transactionDate) = :dateYear) " +
            "AND t.user.id= :userId")*//*


    @Query("SELECT t FROM Transaction t " +
            "WHERE (t.label like %:label%) " +
            "AND (t.transactionDate= :transactionDate) " +
            "AND (MONTH(t.transactionDate) = :dateMonth) " +
            "AND (YEAR(t.transactionDate) = :dateYear) " +
            "AND t.user.id= :userId")
    Page<Transaction> findByMultipleCriteria(
            @NonNull @Param("label") String label,
            @NonNull @Param("transactionDate") LocalDate transactionDate,
            @NonNull @Param("dateMonth") Integer dateMonth,
            @NonNull @Param("dateYear") Integer dateYear,
            @Param("userId") Integer userId,
            Pageable pageable);


//    Page<Transaction> findWithSpecAndPage(Specification<Transaction> spec, Pageable pageable);



*/
/*    @Query("SELECT t FROM Transaction t WHERE YEAR(t.transactionDate) = :dateYear AND t.user.id = :userId")
    Page<Transaction> findByYearAndUserId(@Param("dateYear") Integer dateYear, @Param("userId")Integer userId, Pageable pageable);*//*


//Page<Transaction> findByUserIdAndTransactionDateYear(Integer user_id, int transactionDate_year, Pageable pageable);

    // Page<Transaction> findByUserIdAndDateYear(Integer user_id, int date_year, Pageable pageable);

    //  List<Transaction> findAllByUserId(Integer userId);

    //   List<Transaction> findAllByCategoryIdAndUserId(Integer categoryId, Integer userId);




*/
/*  @Query("SELECT t FROM Transaction t " +
          "WHERE MONTH(date)=:dateMonth " +
          "AND YEAR(date)=:dateYear " +
          "AND t.user.id=:userId "
  )
  List<Transaction> findAllByTransactionDateMonthAndDateYearAndUserId(@Param("dateMonth") short dateMonth, @Param("dateYear")int dateYear, Integer userId);*//*



}
*/
