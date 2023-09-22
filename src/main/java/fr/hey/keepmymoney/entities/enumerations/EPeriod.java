package fr.hey.keepmymoney.entities.enumerations;

public enum EPeriod {
    PUNCTUAL("Ponctuelle"),
    WEEKLY("Hebdomadaire"),
    MONTHLY("Mensuelle"),
    QUARTERLY("Trimestrielle"),
    ANNUAL("Annuelle"),
    BIANNUAL("Semestrielle"),
    ;

    private final String displayValue;

    EPeriod(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

