package TullingGest.classes;

import java.text.NumberFormat;
import java.util.Locale;

public class Constantes {
    static Locale locale = new Locale("pt", "AO");
    public static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
}
