import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Budget {
    private String yearMonth;
    private int amount;

    public Budget(String yearMonth, int amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public double getTotalAmount(Period period) {
        return period.getOverlappingDays(createPeriod()) * dailyAmount();
    }

    private Period createPeriod() {
        return new Period(firstDay(), lastDay());
    }

    private double dailyAmount() {
        return (double) amount / dayCount();
    }

    private int dayCount() {
        YearMonth month = getBudgetMonth();
        return month.lengthOfMonth();
    }

    private LocalDate firstDay() {
        return getBudgetMonth().atDay(1);
    }

    private YearMonth getBudgetMonth() {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private LocalDate lastDay() {

        return getBudgetMonth().atEndOfMonth();
    }
}
