package fr.hey.keepmymoney.dto;

import lombok.Data;

@Data
public class TotalBarChartDTO {

    private String type;
    private Double transactionTotalAmountOfMonth;
    private Integer transactionMonth;


    public TotalBarChartDTO(String type, Double transactionTotalAmountOfMonth, Integer transactionMonth) {
        this.type = type;
        this.transactionTotalAmountOfMonth = transactionTotalAmountOfMonth;
        this.transactionMonth = transactionMonth;
    }
}
