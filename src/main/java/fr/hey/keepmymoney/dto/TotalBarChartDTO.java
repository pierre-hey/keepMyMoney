package fr.hey.keepmymoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalBarChartDTO {

    private String type;

    private Double transactionTotalAmountOfMonth;

    private Integer transactionMonth;

}
