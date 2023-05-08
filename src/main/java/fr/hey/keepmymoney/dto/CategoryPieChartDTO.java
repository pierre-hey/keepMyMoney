package fr.hey.keepmymoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryPieChartDTO {

    private String label;

    private Double amount;

}
