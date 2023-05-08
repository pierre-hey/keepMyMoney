package fr.hey.keepmymoney.controllers;

import fr.hey.keepmymoney.KeepMyMoneyUtils;
import fr.hey.keepmymoney.controllers.helpers.ChartHelper;
import fr.hey.keepmymoney.dto.CategoryPieChartDTO;
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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/graphs")
public class ChartController {

    private final TransactionService transactionService;

    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public ChartController(TransactionService transactionService, IAuthenticationFacade authenticationFacade) {
        this.transactionService = transactionService;

        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping()
    public ModelAndView showGraphs(@RequestParam(required = false, name = "month") Integer monthFilter,
                                   @RequestParam(required = false, name = "year") Integer yearFilter) {

        // Récupération de l'utilisateur connecté
        User user = this.authenticationFacade.getUserAuth();

        if (!ObjectUtils.isEmpty(user)) {

            ModelAndView modelAndView = new ModelAndView("charts/chart");

            // Liste pour la sélection du mois
            final List<String> MONTHS_SELECTOR_LIST = KeepMyMoneyUtils.monthList();
            // Année et mois par défaut
            final int TODAY_YEAR = LocalDate.now().getYear();
            final int TODAY_MONTH = LocalDate.now().getMonthValue();

            // Vérification du mois et de l'année, si le mois est vide/null : on remonte toute l'année
            if (!ObjectUtils.isEmpty(monthFilter)) {
                monthFilter = monthFilter < 1 || monthFilter > 12 ? TODAY_MONTH : monthFilter;
            }
            yearFilter = ObjectUtils.isEmpty(yearFilter) || yearFilter < 2000 || yearFilter > 2100 ? TODAY_YEAR : yearFilter;


            // Récupération des transactions
            List<Transaction> expenseTransactions = transactionService
                    .findTransactionWithCriteria(user.getId(), monthFilter, yearFilter, EType.SPENT);
            List<Transaction> incomeTransactions = transactionService
                    .findTransactionWithCriteria(user.getId(), monthFilter, yearFilter, EType.INCOME);

            // Création des données du graphique
            List<CategoryPieChartDTO> chartDataExpenseTransactions = ChartHelper.createTransactionsByCategoryChart(expenseTransactions);
            List<CategoryPieChartDTO> chartDataIncomeTransactions = ChartHelper.createTransactionsByCategoryChart(incomeTransactions);


            Double totalExpense = generateTotal(expenseTransactions);
            Double totalIncome = generateTotal(incomeTransactions);


            // Mapping des variables pour la vue
            modelAndView.addObject("defaultYear", TODAY_YEAR);
            modelAndView.addObject("monthsSelectorList", MONTHS_SELECTOR_LIST);
            modelAndView.addObject("chartDataExpenseTransactions", chartDataExpenseTransactions);
            modelAndView.addObject("chartDataIncomeTransactions", chartDataIncomeTransactions);
            modelAndView.addObject("totalExpense",totalExpense);
            modelAndView.addObject("totalIncome",totalIncome);

            return modelAndView;
        }

        // Si login absent/incorrect retourne à la page login
        return new ModelAndView("login");
    }

    private Double generateTotal(List<Transaction> transactionList) {
        // Calcul du total de dépense/revenu et solde pour affichage
        Double total = 0d;

        for (Transaction transaction : transactionList) {
            total += transaction.getAmount();
        }
        return total;
    }

    @PostMapping()
    public String createMonthYearFilterForGraph(@ModelAttribute("monthFilter") String monthFilter,
                                                @ModelAttribute("yearFilter") String yearFilter,
                                                RedirectAttributes redirectAttributes) {

        if (!ObjectUtils.isEmpty(monthFilter)) {
            redirectAttributes.addAttribute("month", monthFilter);
        }

        if (!ObjectUtils.isEmpty(yearFilter)) {
            redirectAttributes.addAttribute("year", yearFilter);
        }

        return "redirect:/graphs";
    }

}
