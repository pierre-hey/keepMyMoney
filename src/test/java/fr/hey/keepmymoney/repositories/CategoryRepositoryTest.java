package fr.hey.keepmymoney.repositories;

import fr.hey.keepmymoney.entities.Category;
import fr.hey.keepmymoney.entities.enumerations.EPeriod;
import fr.hey.keepmymoney.entities.enumerations.EType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CategoryRepositoryTest {



    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryRepositoryTest(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Test
    @Transactional
     void testSave() {

        List<Category> categoryListBefore = categoryRepository.findAll();
        Integer categorySize = categoryListBefore.size();

        Category category = new Category();
        category.setLabel("Salaire");
        category.setType(EType.INCOME);
        category.setPeriod(EPeriod.MONTHLY);
        categorySize++;

        categoryRepository.save(category);

        List<Category> categoryListAfter = categoryRepository.findAll();

        assertEquals(categorySize ,  categoryListAfter.size());

    }


    @Test
    @Transactional
    void testFindById() {
        Category c1 = new Category();
        c1.setLabel("Salaire");
        c1.setType(EType.INCOME);
        c1.setPeriod(EPeriod.MONTHLY);
        categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setLabel("Courses");
        c2.setType(EType.SPENT);
        c2.setPeriod(EPeriod.PUNCTUAL);
        categoryRepository.save(c2);

        List<Category> categories = categoryRepository.findAll();

        Random random = new Random();
        Category categoryPicked = categories.get(random.nextInt(categories.size()));

        Category categoryFinded = categoryRepository.findById(categoryPicked.getId()).orElse(null);

        assertEquals(categoryPicked, categoryFinded);
    }
}
