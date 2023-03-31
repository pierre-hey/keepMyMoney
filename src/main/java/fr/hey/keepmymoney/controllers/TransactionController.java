package fr.hey.keepmymoney.controllers;

import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.User;
import fr.hey.keepmymoney.services.CategoryService;
import fr.hey.keepmymoney.services.TransactionService;
import fr.hey.keepmymoney.services.UserService;
import fr.hey.keepmymoney.services.security.IAuthenticationFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final IAuthenticationFacade authenticationFacade;
    private final UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService, CategoryService categoryService, IAuthenticationFacade authenticationFacade, UserService userService) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.authenticationFacade = authenticationFacade;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView showAllView() {

        List<Transaction> transactionList;
        // Récupérer l'utilisateur connecté
        Authentication authentication = authenticationFacade.getAuthentication();
        User user = userService.findByLogin(authentication.getName());
        if (!ObjectUtils.isEmpty(user)) {
            transactionList = transactionService.findAllTransactionsByUserId(user.getId());
        } else {
            return new ModelAndView("login");
        }

        return new ModelAndView("transaction/list", "transactionList", transactionList);
    }

    @GetMapping("/detail")
    public ModelAndView showTransactionDetailView(@RequestParam("id") Integer id, RedirectAttributes redirAttrs) {
        // Récupérer l'utilisateur connecté
        User user = authenticationFacade.getUserAuth();

        if (!ObjectUtils.isEmpty(user)) {
            Transaction transaction = transactionService.findTransactionByIdAndUserId(id, user.getId());
            // Si la transaction existe est que son utilisateur est l'utilisateur connecté
            if (!ObjectUtils.isEmpty(transaction) && transaction.getUser().equals(user)) {
                ModelAndView modelAndView = new ModelAndView("transaction/detail", "transaction", transaction);
                modelAndView.addObject("categoryList", categoryService.findAllCategories());

                return modelAndView;
            } else {

                redirAttrs.addFlashAttribute("msgFlash","Cette transaction n'existe pas ou ne vous appartient pas");
                return new ModelAndView("redirect:/transactions");
            }
        }
        return new ModelAndView("login");
    }

    @PostMapping("/detail")
    public String updateCategory(@RequestParam("id") Integer id,
                                 @Valid Transaction transaction, BindingResult result,
                                 Model model, RedirectAttributes redirAttrs) {

        User user = authenticationFacade.getUserAuth();

        // Vérifier l'utilisateur
        if (!ObjectUtils.isEmpty(user)) {
            Transaction transactionToUpdate = transactionService.findTransactionById(id);
            // Vérifier la transaction
            if (!ObjectUtils.isEmpty(transaction) && transactionToUpdate.getUser().equals(user)) {
                // Vérifier les contraintes de validations
                if (!result.hasErrors()) {
                    // L'user n'est pas mappé côté front, on le rattache après avoir vérifié qu'il s'agit de l'utilisateur de la transaction
                    transaction.setUser(user);
                    transactionService.updateTransaction(transaction);
                    redirAttrs.addFlashAttribute("msgFlash","Modification de la transaction réalisée avec succès");
                } else {
                    model.addAttribute("transaction", transaction);
                    model.addAttribute("categoryList", categoryService.findAllCategories());

                    return "transaction/detail";
                }
            }
        }
        redirAttrs.addFlashAttribute("msgFlash","Essayez-vous de modifier une transaction qui ne vous appartient pas ?");
        return "/transaction/list";
    }

    @GetMapping("/add")
    public ModelAndView addTransactionView() {

        ModelAndView modelAndView = new ModelAndView("transaction/add", "transaction", new Transaction());
        modelAndView.addObject("categoryList", categoryService.findAllCategories());

        return modelAndView;
    }

    @PostMapping("/add")
    public String addTransaction(@Valid Transaction transaction, BindingResult result, Model model, RedirectAttributes redirAttrs) {

        // Vérifier les erreurs liées au formulaire avant de faire des requêtes
        if (result.hasErrors()) {
            model.addAttribute(transaction);
            model.addAttribute("categoryList", categoryService.findAllCategories());

            return "transaction/add";
        } else {

            // Récupérer l'utilisateur et rattacher lié la transaction à l'utilisateur
            User user = authenticationFacade.getUserAuth();

            if (!ObjectUtils.isEmpty(user)) {
                transaction.setUser(user);
            }

            // TODO check contrainte bdd try/catch - exception bll
            redirAttrs.addFlashAttribute("msgFlash","Création de la transaction réalisée avec succès");
            transactionService.createTransaction(transaction);
        }

        return "redirect:/transactions";
    }

}
