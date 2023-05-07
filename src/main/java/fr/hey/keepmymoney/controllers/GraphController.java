package fr.hey.keepmymoney.controllers;

import fr.hey.keepmymoney.KeepMyMoneyUtils;
import fr.hey.keepmymoney.controllers.helpers.GraphHelper;
import fr.hey.keepmymoney.dto.CategoryChartDTO;
import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.User;
import fr.hey.keepmymoney.entities.enumerations.EType;
import fr.hey.keepmymoney.services.TransactionService;
import fr.hey.keepmymoney.services.security.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/graphs")
public class GraphController {

    private final TransactionService transactionService;

    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public GraphController(TransactionService transactionService, IAuthenticationFacade authenticationFacade) {
        this.transactionService = transactionService;

        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping()
    public ModelAndView showGraphs(@RequestParam(required = false, name = "month") Integer monthFilter) {
        User user = this.authenticationFacade.getUserAuth();
        ModelAndView modelAndView;
        if (!ObjectUtils.isEmpty(user)) {

            modelAndView = new ModelAndView("charts/chart");

            // Liste pour la sélection du mois
            final List<String> MONTHS_SELECTOR_LIST = KeepMyMoneyUtils.monthList();

            List<Transaction> expenseTransactions;
            List<Transaction> incomeTransactions;

            // Vérification du filtre de mois
            if (ObjectUtils.isEmpty(monthFilter) || monthFilter < 1 || monthFilter > 12) {
                expenseTransactions = transactionService
                        .findTransactionsWithCategoryTypeAndUser(EType.SPENT, user.getId());
                incomeTransactions = transactionService
                        .findTransactionsWithCategoryTypeAndUser(EType.INCOME, user.getId());
            } else {
                expenseTransactions = transactionService
                        .findTransactionsWithUserAndCategoryAndMonth(user.getId(), monthFilter, EType.SPENT);
                incomeTransactions = transactionService
                        .findTransactionsWithUserAndCategoryAndMonth(user.getId(), monthFilter, EType.INCOME);
            }

            List<CategoryChartDTO> chartDataExpenseTransactions = GraphHelper.createCategoryChart(expenseTransactions);
            List<CategoryChartDTO> chartDataIncomeTransactions = GraphHelper.createCategoryChart(incomeTransactions);

            modelAndView.addObject("monthsSelectorList", MONTHS_SELECTOR_LIST);
            modelAndView.addObject("chartDataExpenseTransactions", chartDataExpenseTransactions);
            modelAndView.addObject("chartDataIncomeTransactions", chartDataIncomeTransactions);

            return modelAndView;
        }

        // Si login absent/incorrect retourne à la page login
        modelAndView = new ModelAndView("login");

        return modelAndView;
    }

    @PostMapping()
    public String createMonthFilterForGraph(@ModelAttribute("monthFilter") String monthFilter,
                                            RedirectAttributes redirectAttributes) {

        if (!ObjectUtils.isEmpty(monthFilter)) {
            redirectAttributes.addAttribute("month", monthFilter);
        }

        return "redirect:/graphs";
    }

}
