package fr.hey.keepmymoney.dto;

import lombok.Data;

@Data
public class CategoryPieChartDTO {
    private String label;
    private Double amount;

    public CategoryPieChartDTO(String label, Double amount) {
        this.label = label;
        this.amount = amount;
    }
}
