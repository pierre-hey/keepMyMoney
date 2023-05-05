package fr.hey.keepmymoney.controllers;

import fr.hey.keepmymoney.Utils;
import fr.hey.keepmymoney.entities.Category;
import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.User;
import fr.hey.keepmymoney.entities.enumerations.EType;
import fr.hey.keepmymoney.services.CategoryService;
import fr.hey.keepmymoney.services.TransactionService;
import fr.hey.keepmymoney.services.security.IAuthenticationFacade;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public TransactionController(TransactionService transactionService, CategoryService categoryService,
                                 IAuthenticationFacade authenticationFacade) {
        this.transactionService = transactionService;
        this.categoryService = categoryService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping()
    public ModelAndView showAllViewWithPagination(@RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false, name = "label") String labelFilter,
                                                  @RequestParam(required = false, name = "category") Category categoryFilter,
                                                  @RequestParam(required = false, name = "type") EType typeFilter,
                                                  @RequestParam(required = false, name = "date") LocalDate dateFilter,
                                                  @RequestParam(required = false, name = "month") Integer monthFilter,
                                                  @RequestParam(required = false, name = "year") Integer yearFilter,
                                                  @RequestParam(defaultValue = "transactionDate") String sortBy) {

//        http://localhost:8080/transactions/page?pageNo=1&pageSize=10&label=co&year=2021&dateFilter=


        // Liste pour la sélection du nombre d'éléments par page à afficher
        final List<Integer> PAGE_SIZE_SELECTOR = List.of(5, 10, 15, 20);
        final Integer MAX_PAGE_SIZE = Collections.max(PAGE_SIZE_SELECTOR);
        final Integer MIN_PAGE_SIZE = Collections.min(PAGE_SIZE_SELECTOR);
        // Liste pour la sélection du mois
        final List<String> monthsSelectorList = Utils.monthList();

        // Récupérer l'utilisateur connecté
        User user = authenticationFacade.getUserAuth();
        ModelAndView modelAndView;
        if (!ObjectUtils.isEmpty(user)) {
            // Vérification des valeurs du paging
            if (pageSize > MAX_PAGE_SIZE) {
                pageSize = MAX_PAGE_SIZE;
            }
            if (pageSize < MIN_PAGE_SIZE) {
                pageSize = MIN_PAGE_SIZE;
            }

            // Soustrait 1 à la pagination, dans les paramètres de la requête, on ne veut pas de "pageNo=0" dans l'URL pour la 1ère page
            Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy).ascending());

            Page<Transaction> transactionList;
            modelAndView = new ModelAndView("transaction/list");

            // Recherche des transactions avec critères
            transactionList = transactionService.findTransactionWithSpec(
                    labelFilter, categoryFilter, typeFilter,
                    dateFilter, monthFilter, yearFilter,
                    user.getId(),
                    paging);

            // Redéfinis le nombre de pages à afficher dans le footer du tableau
            int totalPages = transactionList.getTotalPages();
            modelAndView.addObject("totalPages", totalPages);

            if (!ObjectUtils.isEmpty(totalPages) && totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                modelAndView.addObject("pageNumbers", pageNumbers);
            }

            modelAndView.addObject("transactionList", transactionList);
            modelAndView.addObject("activePage", pageNo);
            modelAndView.addObject("categoryList", categoryService.findAllCategories());
            modelAndView.addObject("pageSizeSelectorList", PAGE_SIZE_SELECTOR);
            modelAndView.addObject("monthsSelectorList", monthsSelectorList);

            return modelAndView;
        }
        // Si login absent/incorrect retourne à la page login
        modelAndView = new ModelAndView("login");

        return modelAndView;
    }

    @PostMapping()
    public String createTransactionFilter(@ModelAttribute("pageSizeFilter") String pageSizeFilter,
                                          @ModelAttribute("labelFilter") String labelFilter,
                                          @ModelAttribute("categoryFilter") Category categoryFilter,
                                          @ModelAttribute("typeFilter") String typeFilter,
                                          @ModelAttribute("dateFilter") String dateFilter,
                                          @ModelAttribute("monthFilter") String monthFilter,
                                          @ModelAttribute("yearFilter") String yearFilter,
                                          RedirectAttributes redirectAttributes
    ) {

        System.out.println("######################################################");
        System.out.println("TransactionController.testFormFilter");
        System.out.println("pageSizeFilter : " + pageSizeFilter);
        System.out.println("labelFilter : " + labelFilter);
        System.out.println("dateFilter : " + dateFilter);
        System.out.println("categoryFilter : " + categoryFilter);
        System.out.println("monthFilter : " + monthFilter);
        System.out.println("yearFilter : " + yearFilter);
        System.out.println("######################################################");

        // Récupère les attributs du formulaire en post pour les renvoyer au get
        redirectAttributes.addAttribute("pageNo", 1);
        redirectAttributes.addAttribute("pageSize", pageSizeFilter);

        if (!ObjectUtils.isEmpty(labelFilter)) {
            redirectAttributes.addAttribute("label", labelFilter);
        }
        if (!ObjectUtils.isEmpty(monthFilter)) {
            redirectAttributes.addAttribute("month", monthFilter);
        }
        if (!ObjectUtils.isEmpty(yearFilter)) {
            redirectAttributes.addAttribute("year", yearFilter);
        }
        if (!ObjectUtils.isEmpty(dateFilter)) {
            redirectAttributes.addAttribute("date", dateFilter);
        }
        if (!ObjectUtils.isEmpty(categoryFilter)) {
            redirectAttributes.addAttribute("category", categoryFilter);
        }
        if (!ObjectUtils.isEmpty(typeFilter)) {
            redirectAttributes.addAttribute("type", EType.valueOf(typeFilter));
        }

        return "redirect:/transactions";
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

                redirAttrs.addFlashAttribute("msgFlash", "Cette transaction n'existe pas ou ne vous appartient pas");
                return new ModelAndView("redirect:/transactions");
            }
        }
        return new ModelAndView("login");
    }

    @PostMapping("/detail")
    public String updateTransaction(@RequestParam("id") Integer id,
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
                    redirAttrs.addFlashAttribute("msgFlash", "Modification de la transaction réalisée avec succès");
                    return "redirect:/transactions";
                } else {
                    model.addAttribute("transaction", transaction);
                    model.addAttribute("categoryList", categoryService.findAllCategories());

                    return "transaction/detail";
                }
            }
        }
        redirAttrs.addFlashAttribute("msgFlash", "Essayez-vous de modifier une transaction qui ne vous appartient pas ?");
        return "redirect:/transactions";
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
            redirAttrs.addFlashAttribute("msgFlash", "Création de la transaction réalisée avec succès");
            transactionService.createTransactionWithPeriod(transaction);
        }

        return "redirect:/transactions";
    }

}
