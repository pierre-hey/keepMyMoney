package fr.hey.keepmymoney.services;

import fr.hey.keepmymoney.entities.Category;

import java.util.List;

/**
 * Service de la classe Category
 */
public interface CategoryService {

    /**
     * Créé une catégorie
     * @param category catégorie
     */
    void createCategory(Category category);

    /**
     * Recherche une catégorie par son id
     * @param id id catégorie
     * @return Catégorie
     */
    Category findCategoryById(Integer id);

    /**
     * Recherche une catégorie par son nom
     * @param label label
     * @return Catégorie
     */
    Category findCategoryByLabel(String label);

    /**
     * Recherche toutes les catégories
     * @return Liste de catégories
     */
    List<Category> findAllCategories();

    /**
     * Met à jour une catégorie
     * @param category catégorie
     * @return Catégorie
     */
    Category updateCategory(Category category);

    /**
     * Supprime une catégorie
     * @param category catégorie
     */
    void deleteCategory(Category category);

}
