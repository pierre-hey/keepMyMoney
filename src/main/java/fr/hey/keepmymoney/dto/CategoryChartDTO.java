package fr.hey.keepmymoney.dto;

import lombok.Data;

@Data
public class CategoryChartDTO {
    private String label;
    private Double amount;

    public CategoryChartDTO(String label, Double amount) {
        this.label = label;
        this.amount = amount;
    }
}
