package fr.hey.keepmymoney.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String label;

    private Double amount;

    @ManyToOne
    private Category category;

    @ManyToOne
    private User user;


    public Transaction(LocalDate date, String label, Double amount, Category category, User user) {
        this.date = date;
        this.label = label;
        this.amount = amount;
        this.category = category;
        this.user = user;
    }
}
