package fr.hey.keepmymoney.dto;

import lombok.Data;

@Data
public class TransactionLineChartDTO {

    private String categoryLabel;
    private Double transactionTotalAmountOfMonth;
    private Integer transactionMonth;


    public TransactionLineChartDTO(String categoryLabel,Double transactionTotalAmountOfMonth, Integer transactionMonth) {
        this.categoryLabel = categoryLabel;
        this.transactionTotalAmountOfMonth = transactionTotalAmountOfMonth;
        this.transactionMonth = transactionMonth;
    }
}
