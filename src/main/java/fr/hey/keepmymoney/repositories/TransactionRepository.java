package fr.hey.keepmymoney.repositories;

import fr.hey.keepmymoney.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findAllByUserId(Integer userId);

    List<Transaction> findAllByCategoryIdAndUserId(Integer categoryId, Integer userId);

    List<Transaction> findAllByDateBetweenAndUserId(LocalDate startDate, LocalDate endDate, Integer userId);


  @Query("SELECT t FROM Transaction t " +
          "WHERE MONTH(date)=:dateMonth " +
          "AND YEAR(date)=:dateYear " +
          "AND t.user.id=:userId "
  )
  List<Transaction> findAllByDateMonthAndDateYearAndUserId(@Param("dateMonth") short dateMonth, @Param("dateYear")int dateYear, Integer userId);



    //	@Query("SELECT DISTINCT YEAR(date) FROM Mouvement m")
    @Query("SELECT t FROM Transaction t " +
            "WHERE YEAR(date)=:dateYear " +
            "AND t.user.id=:userId "
            )
    List<Transaction> findByDateYearAndUserId(@Param("dateYear")int dateYear, @Param("userId")Integer userId);

    List<Transaction> findAllByLabelLikeAndUserId(String label, Integer userId);
}
