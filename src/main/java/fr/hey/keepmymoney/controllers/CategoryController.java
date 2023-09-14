package fr.hey.keepmymoney.controllers;

import fr.hey.keepmymoney.entities.Category;
import fr.hey.keepmymoney.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView showAllView() {
        List<Category> categoryList = categoryService.findAllCategories();
        return new ModelAndView("category/list", "categoryList", categoryList);
    }

    @GetMapping("/detail")
    public ModelAndView showCategoryDetailView(@RequestParam("id") Integer id) {
        Category category = categoryService.findCategoryById(id);

        return new ModelAndView("category/detail", "category", category);
    }

    @PostMapping("/detail")
    public String updateCategory(@RequestParam("id") Integer id,
                                       @Valid Category category, BindingResult result, Model model) {

        /*
            TODO :
             - QUE FAIRE SI CATEGORIE DEJA EN BASE ?
             - QUE FAIRE SI LA CATEGORIE N'EXISTE PAS ?
             - AFFICHER UN MESSAGE FLASH POUR INDIQUER QUE LA CATEGORIE A ETE CREEE
         */
        category.setId(id);
        if (result.hasErrors()) {
            model.addAttribute(category);
            return "category/detail";
        } else {
            categoryService.updateCategory(category);
        }
        return "redirect:/categories";
    }

    @GetMapping("/add")
    public ModelAndView addCategoryView() {
        return new ModelAndView("category/add", "category", new Category());
    }

    @PostMapping("/add")
    public String addCategory(@Valid Category category, BindingResult result, Model model) {

        /*
            TODO :
             - QUE FAIRE SI CATEGORIE DEJA EN BASE ?
             - AFFICHER UN MESSAGE FLASH POUR INDIQUER QUE LA CATEGORIE A ETE CREEE
         */

        if (result.hasErrors()) {
            model.addAttribute(category);
            return "category/add";
        } else {
            categoryService.createCategory(category);
        }
        return "redirect:/categories";

    }




}
