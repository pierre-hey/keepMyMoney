package fr.hey.keepmymoney.controllers;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

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
    public ModelAndView showGraphs() {
        User user = this.authenticationFacade.getUserAuth();
        ModelAndView modelAndView;
        modelAndView = new ModelAndView("charts/chart");
        if (!ObjectUtils.isEmpty(user)) {

            List<Transaction> outcomeTransactions = transactionService.findTransactionsWithCategoryTypeAndUser(EType.SPENT, user.getId());
            List<Transaction> incomeTransactions = transactionService.findTransactionsWithCategoryTypeAndUser(EType.INCOME, user.getId());

            List<CategoryChartDTO> chartDataOutcomeTransactions = outcomeTransactions
                    .stream()
                    .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)))
                    .entrySet()
                    .stream()
                    .map(entry -> new CategoryChartDTO(entry.getKey().getLabel(), entry.getValue()))
                    .collect(Collectors.toList());

            List<CategoryChartDTO> chartDataIncomeTransactions = incomeTransactions
                    .stream()
                    .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.summingDouble(Transaction::getAmount)))
                    .entrySet()
                    .stream()
                    .map(entry -> new CategoryChartDTO(entry.getKey().getLabel(), entry.getValue()))
                    .collect(Collectors.toList());

            modelAndView.addObject("chartDataOutcomeTransactions", chartDataOutcomeTransactions);
            modelAndView.addObject("chartDataIncomeTransactions", chartDataIncomeTransactions);

        }

        return modelAndView;
    }

}
