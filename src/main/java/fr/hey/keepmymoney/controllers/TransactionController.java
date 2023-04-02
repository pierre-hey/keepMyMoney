package fr.hey.keepmymoney.controllers;

import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.User;
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

    @GetMapping
    public ModelAndView showAllView() {

        List<Transaction> transactionList;
        // Récupérer l'utilisateur connecté
        User user = authenticationFacade.getUserAuth();

        if (!ObjectUtils.isEmpty(user)) {
            transactionList = transactionService.findAllTransactionsByUserId(user.getId());
        } else {
            return new ModelAndView("login");
        }

        return new ModelAndView("transaction/list", "transactionList", transactionList);
    }

    @GetMapping("/page")
    public ModelAndView showAllViewWithPagination(@RequestParam(defaultValue = "1") Integer pageNo,
                                                  @RequestParam(defaultValue = "10") Integer pageSize,
                                                  @RequestParam(required = false, name = "label") String labelFilter,
                                                  @RequestParam(required = false, name = "date") LocalDate dateFilter,
                                                  @RequestParam(required = false, name = "year") Integer yearFilter,
                                                  @RequestParam(required = false, name = "month") Integer monthFilter,
                                                  @RequestParam(defaultValue = "id") String sortBy
    ) {

//        http://localhost:8080/transactions/page?pageNo=1&pageSize=10&label=co&year=2021&dateFilter=


        // Récupérer l'utilisateur connecté
        User user = authenticationFacade.getUserAuth();
        ModelAndView modelAndView;
        if (!ObjectUtils.isEmpty(user)) {
            // Soustrait 1 à la pagination, dans les paramètres de la requête, on veut pas de pageNo=0 pour la 1ère page
            Pageable paging = PageRequest.of(pageNo - 1, pageSize, Sort.by(sortBy).ascending());

            Page<Transaction> transactionList;
            modelAndView = new ModelAndView("transaction/list");

            transactionList = transactionService.findTransactionWithSpec(labelFilter, dateFilter, monthFilter, yearFilter, user.getId(), paging);

            modelAndView.addObject("transactionList", transactionList);
            modelAndView.addObject("activePage", pageNo);
            modelAndView.addObject("categoryList",categoryService.findAllCategories());
            // Redéfinis le nombre de pages à afficher dans le footer du tableau
            int totalPages = transactionList.getTotalPages();
            modelAndView.addObject("totalPages", totalPages);
            if (!ObjectUtils.isEmpty(totalPages) && totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                        .boxed()
                        .collect(Collectors.toList());
                modelAndView.addObject("pageNumbers", pageNumbers);
            }
        } else {
            return new ModelAndView("login");
        }
        return modelAndView;
    }

    @PostMapping("/page")
    public String testFormFilter(@ModelAttribute("pageSizeFilter") String pageSizeFilter,
                                 @ModelAttribute("labelFilter") String labelFilter,
                                 @ModelAttribute("dateFilter") String dateFilter,
                                 @ModelAttribute("yearFilter") String yearFilter,
                                 RedirectAttributes redirectAttributes
    ) {

        System.out.println("######################################################");
        System.out.println("TransactionController.testFormFilter");
        System.out.println(pageSizeFilter);
        System.out.println(labelFilter);
        System.out.println(dateFilter);
        System.out.println(yearFilter);
        System.out.println("######################################################");

        // Récupère les attributs du formulaire en post pour les renvoyer au get
        redirectAttributes.addAttribute("pageNo", 1);
        redirectAttributes.addAttribute("pageSize", pageSizeFilter);
        redirectAttributes.addAttribute("label", labelFilter);
        redirectAttributes.addAttribute("year", yearFilter);
        redirectAttributes.addAttribute("dateFilter", dateFilter);


        return "redirect:/transactions/page";
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
