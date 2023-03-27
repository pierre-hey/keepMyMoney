package fr.hey.keepmymoney.repositories;

import fr.hey.keepmymoney.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
