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

    /**
     * Créé une liste de données (transactions par catégorie) pour le graphique "Pie"
     *
     * @param transactions liste de transactions à mapper
     * @return liste des valeurs du graphique
     */
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

    /**
     * Créé une liDste de données (transaction par catégorie et par mois) pour le graphique "Line"
     *
     * @param transactions liste de transactions à mapper
     * @return liste des valeurs du graphique
     */
    public static List<TransactionLineChartDTO> createTransactionChartLine(List<Transaction> transactions) {
        Map<String, Map<Integer, Double>> transactionsByCategoryAndMonth = new HashMap<>();

        // Grouper les transactions par catégorie et par mois
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

    /**
     * Créé une liste de données (transaction par type et par mois) pour le graphique "Bar"
     *
     * @param transactions liste de transaction
     * @return liste transactions mappées par type et par mois
     */
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

    /**
     * Grouper les transactions par catégorie et par mois
     *
     * @param transactions                   liste de transactions
     * @param transactionsByCategoryAndMonth transactions mappées par catégorie et par mois
     */
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

    /**
     * Grouper les transactions par type et par mois
     *
     * @param transactions               liste de transactions
     * @param transactionsByTypeAndMonth transactions mappées
     */
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
