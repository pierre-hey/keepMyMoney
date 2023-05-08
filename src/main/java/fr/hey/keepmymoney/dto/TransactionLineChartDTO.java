package fr.hey.keepmymoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransactionLineChartDTO {

    private String categoryLabel;

    private Double transactionTotalAmountOfMonth;

    private Integer transactionMonth;

}
