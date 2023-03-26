package fr.hey.keepmymoney.entities;

import fr.hey.keepmymoney.entities.enumerations.EType;
import fr.hey.keepmymoney.entities.enumerations.EPeriod;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Ne doit pas être vide")
    private String label;

    @NotNull(message = "Le type de la catégorie est obligatoire")
    private EType type;

    @NotNull(message = "La périodicité de la catégorie est obligatoire")
    private EPeriod period;


    public Category(String label, EType type, EPeriod period) {
        this.label = label;
        this.type = type;
        this.period = period;
    }
}
