
import java.util.*;
class CurrencyRegistry {
    private final Map<String, Double> rates = new LinkedHashMap<>();
    private final Map<String, String> names = new LinkedHashMap<>();
public class GlobalCurrencyConverter {
    public static void main(String[] args) {
        
        System.out.println("Hello guys"); 
        
    }
}

    public CurrencyRegistry() {
        addCurrency("USD", "US Dollar",                 1.0000);
        addCurrency("EUR", "Euro",                      0.9200);
        addCurrency("GBP", "British Pound Sterling",    0.7900);
        addCurrency("INR", "Indian Rupee",             83.5000);
        addCurrency("JPY", "Japanese Yen",            156.7000);
        addCurrency("CAD", "Canadian Dollar",           1.3700);
        addCurrency("AUD", "Australian Dollar",         1.5400);
        addCurrency("CHF", "Swiss Franc",               0.8980);
        addCurrency("CNY", "Chinese Yuan Renminbi",     7.2400);
        addCurrency("SGD", "Singapore Dollar",          1.3500);
        addCurrency("HKD", "Hong Kong Dollar",          7.8200);
        addCurrency("MXN", "Mexican Peso",             17.1500);
        addCurrency("BRL", "Brazilian Real",            4.9700);
        addCurrency("ZAR", "South African Rand",       18.6000);
        addCurrency("KRW", "South Korean Won",       1345.0000);
        addCurrency("SAR", "Saudi Riyal",               3.7500);
        addCurrency("AED", "UAE Dirham",                3.6700);
        addCurrency("SEK", "Swedish Krona",            10.5000);
        addCurrency("NOK", "Norwegian Krone",          10.7000);
        addCurrency("NZD", "New Zealand Dollar",        1.6300);
    }

    private void addCurrency(String code, String name, double rateToUSD) {
        rates.put(code, rateToUSD);
        names.put(code, name);
    }
    public Double getRate(String code) {
        return rates.get(code.toUpperCase());
    }
    public String getName(String code) {
        return names.get(code.toUpperCase());
    }
    public Set<String> getAllCodes() {
        return Collections.unmodifiableSet(rates.keySet());
    }
    public boolean isSupported(String code) {
        return rates.containsKey(code.toUpperCase());
    }
}
abstract class CurrencyOperation {

    protected final CurrencyRegistry registry;

    public CurrencyOperation(CurrencyRegistry registry) {
        this.registry = registry;
    }

    public abstract double convert(double amount, String fromCurrency, String toCurrency);
    public abstract String getFormattedResult(double amount,
                                               String fromCurrency,
                                               String toCurrency);
    public abstract void displayAllCurrencies();
}
class CurrencyConverter extends CurrencyOperation {

    public CurrencyConverter(CurrencyRegistry registry) {
        super(registry);
    }
    @Override
    public double convert(double amount, String fromCurrency, String toCurrency) {
        String from = fromCurrency.toUpperCase();
        String to   = toCurrency.toUpperCase();

        if (!registry.isSupported(from)) {
            throw new IllegalArgumentException(
                "Unsupported source currency: " + from);
        }
        if (!registry.isSupported(to)) {
            throw new IllegalArgumentException(
                "Unsupported target currency: " + to);
        }
        if (amount < 0) {
            throw new IllegalArgumentException(
                "Amount cannot be negative.");
        }
        double rateFrom = registry.getRate(from);
        double rateTo   = registry.getRate(to);
        double inUSD = amount / rateFrom;
        return inUSD * rateTo;
    }

    @Override
    public String getFormattedResult(double amount,
                                      String fromCurrency,
                                      String toCurrency) {
        double result = convert(amount, fromCurrency, toCurrency);
        String fromName = registry.getName(fromCurrency.toUpperCase());
        String toName   = registry.getName(toCurrency.toUpperCase());

        return String.format(
            "\n  %-8s  %-26s  %,.4f"  +
            "\n  %-8s  %-26s  %,.4f"  +
            "\n  ─────────────────────────────────────────────\n",
            fromCurrency.toUpperCase(), fromName, amount,
            toCurrency.toUpperCase(),   toName,   result
        );
    }

    @Override
    public void displayAllCurrencies() {
        System.out.println("\n  ┌──────┬────────────────────────────────┬──────────────────┐");
        System.out.println("  │ Code │ Currency Name                  │  Rate (per USD)  │");
        System.out.println("  ├──────┼────────────────────────────────┼──────────────────┤");
        for (String code : registry.getAllCodes()) {
            System.out.printf("  │ %-4s │ %-30s │  %,14.4f  │%n",
                code,
                registry.getName(code),
                registry.getRate(code));
        }
        System.out.println("  └──────┴────────────────────────────────┴──────────────────┘\n");
    }
}
class ConversionHistory {
    private final List<String> history = new ArrayList<>();

    public void add(String entry) { history.add(entry); }

    public void display() {
        if (history.isEmpty()) {
            System.out.println("\n  No conversions performed yet.\n");
            return;
        }
        System.out.println("\n  ── Conversion History ──────────────────────────────");
        for (int i = 0; i < history.size(); i++) {
            System.out.printf("  [%d] %s%n", i + 1, history.get(i));
        }
        System.out.println();
    }
}
public class GlobalCurrencyConverter {

    private static final String DIVIDER =
        "  ═══════════════════════════════════════════════════════";

    public static void main(String[] args) {
        Scanner scanner           = new Scanner(System.in);
        CurrencyRegistry registry = new CurrencyRegistry();
        CurrencyConverter conv    = new CurrencyConverter(registry);
        ConversionHistory hist    = new ConversionHistory();

        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("  Enter your choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> performConversion(scanner, conv, hist, registry);
                case "2" -> conv.displayAllCurrencies();
                case "3" -> hist.display();
                case "4" -> {
                    System.out.println("\n  Thank you for using Global Currency Converter!\n");
                    running = false;
                }
                default  -> System.out.println("\n  ⚠  Invalid choice. Please enter 1–4.\n");
            }
        }
        scanner.close();
    }
    private static void performConversion(Scanner sc,
                                           CurrencyConverter conv,
                                           ConversionHistory hist,
                                           CurrencyRegistry registry) {
        try {
            System.out.print("\n  Enter source currency code (e.g. USD, INR, EUR): ");
            String from = sc.nextLine().trim().toUpperCase();

            System.out.print("  Enter target currency code (e.g. JPY, GBP, AUD): ");
            String to = sc.nextLine().trim().toUpperCase();

            System.out.print("  Enter amount to convert: ");
            double amount = Double.parseDouble(sc.nextLine().trim());

            System.out.println(DIVIDER);
            System.out.println("  CONVERSION RESULT");
            System.out.println(DIVIDER);
            String result = conv.getFormattedResult(amount, from, to);
            System.out.println(result);
            double converted = conv.convert(amount, from, to);
            hist.add(String.format("%.4f %s → %.4f %s", amount, from, converted, to));

        } catch (NumberFormatException e) {
            System.out.println("\n  ⚠  Invalid amount. Please enter a numeric value.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("\n  ⚠  " + e.getMessage() + "\n");
        }
    }
    private static void printBanner() {
        System.out.println();
        System.out.println("  ╔═════════════════════════════════════════════════════╗");
        System.out.println("  ║         GLOBAL CURRENCY CONVERTER  v1.0             ║");
        System.out.println("  ║      Supporting 20 World Currencies (USD base)      ║");
        System.out.println("  ╚═════════════════════════════════════════════════════╝");
        System.out.println();
    }
    private static void printMenu() {
        System.out.println("  ┌─────────────────────────────────┐");
        System.out.println("  │           MAIN MENU             │");
        System.out.println("  ├─────────────────────────────────┤");
        System.out.println("  │  1. Convert Currency            │");
        System.out.println("  │  2. List All Currencies         │");
        System.out.println("  │  3. View Conversion History     │");
        System.out.println("  │  4. Exit                        │");
        System.out.println("  └─────────────────────────────────┘");
    }
}

