package fr.hey.keepmymoney.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "La date est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate transactionDate;

    @Column(nullable = false)
    @NotBlank(message = "La désignation de la transaction est obligatoire")
    private String label;

    @Column(nullable = false)
    @NotNull(message = "Ne doit pas être vide")
    @Positive(message = "Doit être positif")
    private Double amount;

    @NotNull(message = "La catégorie est obligatoire")
    @ManyToOne
    private Category category;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;
}
/*    public Transaction(LocalDate transactionDate, String label, Double amount, Category category, User user) {
        this.transactionDate = transactionDate;
        this.label = label;
        this.amount = amount;
        this.category = category;
        this.user = user;
    }*/
