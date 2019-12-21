import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class Budget {
    String yearMonth;
    int amount;

    public Budget(String yearMonth, int amount) {
        this.yearMonth = yearMonth;
        this.amount = amount;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int dayCount() {
        YearMonth month = getBudgetMonth();
        return month.lengthOfMonth();
    }

    public double dailyAmount() {
        return (double) getAmount() / dayCount();
    }

    public LocalDate lastDay() {

        return getBudgetMonth().atEndOfMonth();
    }

    public LocalDate firstDay() {
        return getBudgetMonth().atDay(1);
    }

    private YearMonth getBudgetMonth() {
        return YearMonth.parse(yearMonth, DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
