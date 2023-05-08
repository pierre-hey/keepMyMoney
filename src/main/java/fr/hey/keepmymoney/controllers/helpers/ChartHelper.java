package fr.hey.keepmymoney.controllers.helpers;

import fr.hey.keepmymoney.dto.CategoryPieChartDTO;
import fr.hey.keepmymoney.dto.TotalBarChartDTO;
import fr.hey.keepmymoney.dto.TransactionLineChartDTO;
import fr.hey.keepmymoney.entities.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartHelper {

    public static List<CategoryPieChartDTO> createTransactionsByCategoryChart(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.summingDouble(Transaction::getAmount)
                ))
                .entrySet()
                .stream()
                .map(entry -> new CategoryPieChartDTO(entry.getKey().getLabel(), entry.getValue()))
                .collect(Collectors.toList());
    }

    /*
    Map<String, Map<String, List<Person>>> peopleByStateAndCity      =
        personStream.collect(Collectors.groupingBy(Person::getState,
                                            Collectors.groupingBy(Person::getCity)));
     */

    public static List<TransactionLineChartDTO> createTransactionChartLine(List<Transaction> transactions) {
        Map<String, Map<Integer, Double>> transactionsByCategoryAndMonth = new HashMap<>();

        // Grouper les transactions par catégorie et mois
        groupTransactionByCategoryAndMonth(transactions, transactionsByCategoryAndMonth);

        // Créer les transactions à partir de la map de transaction par catégorie et mois
        List<TransactionLineChartDTO> transactionLineChartDataList = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Double>> entry : transactionsByCategoryAndMonth.entrySet()) {
            String categoryLabel = entry.getKey();
            Map<Integer, Double> transactionsByMonth = entry.getValue();
            for (Map.Entry<Integer, Double> monthEntry : transactionsByMonth.entrySet()) {
                Integer transactionMonth = monthEntry.getKey();
                Double transactionTotalAmountOfMonth = monthEntry.getValue();
                TransactionLineChartDTO transactionLineChartData = new TransactionLineChartDTO(categoryLabel, transactionTotalAmountOfMonth, transactionMonth);
                transactionLineChartDataList.add(transactionLineChartData);
            }
        }

        return transactionLineChartDataList;
    }

    public static List<TotalBarChartDTO> createTransactionChartBar(List<Transaction> transactions) {
        // Type & Mois/Total
        Map<String, Map<Integer, Double>> transactionsByTypeAndMonth = new HashMap<>();

        // Grouper les transactions par type et mois
        groupTransactionByTypeAndMonth(transactions, transactionsByTypeAndMonth);

        // Créer les transactions à partir de la map de transaction par type et mois
        List<TotalBarChartDTO> transactionLineChartDataList = new ArrayList<>();
        for (Map.Entry<String, Map<Integer, Double>> entry : transactionsByTypeAndMonth.entrySet()) {
            String transactionType = entry.getKey();
            Map<Integer, Double> transactionsByMonth = entry.getValue();
            for (Map.Entry<Integer, Double> monthEntry : transactionsByMonth.entrySet()) {
                Integer transactionMonth = monthEntry.getKey();
                Double transactionTotalAmountOfMonth = monthEntry.getValue();
                TotalBarChartDTO transactionLineChartData =
                        new TotalBarChartDTO(transactionType, transactionTotalAmountOfMonth, transactionMonth);
                transactionLineChartDataList.add(transactionLineChartData);
            }
        }

        return transactionLineChartDataList;
    }

    private static void groupTransactionByCategoryAndMonth(List<Transaction> transactions,
                                                           Map<String, Map<Integer, Double>> transactionsByCategoryAndMonth) {
        for (Transaction transaction : transactions) {
            String categoryLabel = transaction.getCategory().getLabel();
            int transactionMonth = transaction.getTransactionDate().getMonthValue();
            double transactionAmount = transaction.getAmount();

            if (transactionsByCategoryAndMonth.containsKey(categoryLabel)) {
                Map<Integer, Double> transactionsByMonth = transactionsByCategoryAndMonth.get(categoryLabel);
                if (transactionsByMonth.containsKey(transactionMonth)) {
                    transactionsByMonth.put(transactionMonth, transactionsByMonth.get(transactionMonth) + transactionAmount);
                } else {
                    transactionsByMonth.put(transactionMonth, transactionAmount);
                }
            } else {
                Map<Integer, Double> transactionsByMonth = new HashMap<>();
                transactionsByMonth.put(transactionMonth, transactionAmount);
                transactionsByCategoryAndMonth.put(categoryLabel, transactionsByMonth);
            }
        }
    }

    private static void groupTransactionByTypeAndMonth(List<Transaction> transactions,
                                                       Map<String, Map<Integer, Double>> transactionsByTypeAndMonth) {
        for (Transaction transaction : transactions) {
            //String categoryLabel = transaction.getCategory().getLabel();
            String transactionType = String.valueOf(transaction.getCategory().getType());

            int transactionMonth = transaction.getTransactionDate().getMonthValue();
            double transactionAmount = transaction.getAmount();

            if (transactionsByTypeAndMonth.containsKey(transactionType)) {
                Map<Integer, Double> transactionsByMonth = transactionsByTypeAndMonth.get(transactionType);
                if (transactionsByMonth.containsKey(transactionMonth)) {
                    transactionsByMonth.put(transactionMonth, transactionsByMonth.get(transactionMonth) + transactionAmount);
                } else {
                    transactionsByMonth.put(transactionMonth, transactionAmount);
                }
            } else {
                Map<Integer, Double> transactionsByMonth = new HashMap<>();
                transactionsByMonth.put(transactionMonth, transactionAmount);
                transactionsByTypeAndMonth.put(transactionType, transactionsByMonth);
            }
        }
    }
}
