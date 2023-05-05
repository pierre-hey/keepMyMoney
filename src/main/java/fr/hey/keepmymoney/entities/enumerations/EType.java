package fr.hey.keepmymoney.entities.enumerations;

public enum EType {
    SPENT("Dépense"),
    INCOME("Revenu");

    private final String displayValue;

    EType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
