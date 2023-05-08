package fr.hey.keepmymoney.controllers;

import fr.hey.keepmymoney.KeepMyMoneyUtils;
import fr.hey.keepmymoney.controllers.helpers.ChartHelper;
import fr.hey.keepmymoney.dto.TotalBarChartDTO;
import fr.hey.keepmymoney.dto.TransactionLineChartDTO;
import fr.hey.keepmymoney.entities.Transaction;
import fr.hey.keepmymoney.entities.User;
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
@RequestMapping({"index", "/"})
public class HomeController {

    private final TransactionService transactionService;

    private final IAuthenticationFacade authenticationFacade;

    @Autowired
    public HomeController(TransactionService transactionService, IAuthenticationFacade authenticationFacade) {
        this.transactionService = transactionService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping()
    public ModelAndView home(@RequestParam(required = false, name = "year") Integer yearFilter) {
        // Récupération de l'utilisateur connecté
        User user = this.authenticationFacade.getUserAuth();

        ModelAndView modelAndView = new ModelAndView("index");
        List<Transaction> transactionList;

        // Liste pour la sélection du mois
        final List<String> MONTHS_SELECTOR_LIST = KeepMyMoneyUtils.monthList();
        // Année et mois par défaut
        final int TODAY_YEAR = LocalDate.now().getYear();


        if (ObjectUtils.isEmpty(yearFilter)) {
            transactionList = transactionService
                    .findAllTransactionsByYearAndUserId(TODAY_YEAR, user.getId());
        } else {
            transactionList = transactionService
                    .findAllTransactionsByYearAndUserId(yearFilter, user.getId());
        }
        List<TransactionLineChartDTO> chartLineData = ChartHelper.createTransactionChartLine(transactionList);
        List<TotalBarChartDTO> chartBarData = ChartHelper.createTransactionChartBar(transactionList);


        System.out.println("###############################");
        chartBarData.forEach(c -> System.out.println(c.toString()));
        System.out.println("###############################");


        modelAndView.addObject("monthsSelectorList", MONTHS_SELECTOR_LIST);
        modelAndView.addObject("defaultYear", TODAY_YEAR);
        modelAndView.addObject("chartLineData", chartLineData);
        modelAndView.addObject("chartBarData", chartBarData);


        return modelAndView;
    }


    @PostMapping()
    public String createYearFilterForGraph(@ModelAttribute("yearFilter") String yearFilter,
                                           RedirectAttributes redirectAttributes) {

        if (!ObjectUtils.isEmpty(yearFilter)) {
            redirectAttributes.addAttribute("year", yearFilter);
        }

        return "redirect:/";
    }
}
