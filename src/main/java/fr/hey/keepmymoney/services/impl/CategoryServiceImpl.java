package fr.hey.keepmymoney.services.impl;

import fr.hey.keepmymoney.entities.Category;
import fr.hey.keepmymoney.repositories.CategoryRepository;
import fr.hey.keepmymoney.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public Category findCategoryById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category findCategoryByLabel(String label) {
        return categoryRepository.findByLabelLike(label);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }
}
