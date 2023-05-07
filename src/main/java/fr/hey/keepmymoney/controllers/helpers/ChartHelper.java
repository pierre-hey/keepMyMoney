package fr.hey.keepmymoney.controllers.helpers;

import fr.hey.keepmymoney.dto.CategoryChartDTO;
import fr.hey.keepmymoney.entities.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class ChartHelper {

    public static List<CategoryChartDTO> createTransactionsByCategoryChart(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ))
                .entrySet()
                .stream()
                .map(entry -> new CategoryChartDTO(entry.getKey().getLabel(), entry.getValue()))
                .collect(Collectors.toList());
    }

}
