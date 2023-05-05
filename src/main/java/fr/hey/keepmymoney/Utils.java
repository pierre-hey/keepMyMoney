package fr.hey.keepmymoney;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static List<String> monthList(){
        // Liste pour la sélection du mois
        List<String> monthTextList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM", Locale.FRENCH);
        for (int i = 1; i <= 12; i++) {
            LocalDate date = LocalDate.of(2000, i, 1);
            String month = formatter.format(date);
            month = month.substring(0, 1).toUpperCase() + month.substring(1).toLowerCase();

            monthTextList.add(month);
        }
        return monthTextList;
    }

}
