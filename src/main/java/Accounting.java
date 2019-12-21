import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class Accounting {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    private IBudgetRepo db;

    public Accounting(IBudgetRepo db) {
        this.db = db;
    }

    public double QueryBudget(LocalDate start, LocalDate end) {

        List<Budget> budgets = db.GetAll().stream().filter(bd ->
        {
            YearMonth d = YearMonth.parse(bd.yearMonth, formatter);
            YearMonth startYM = YearMonth.from(start);
            YearMonth endYM = YearMonth.from(end);
            return (d.equals(startYM) || d.isAfter(startYM)) && (d.equals(endYM) || d.isBefore(endYM));
        }).collect(Collectors.toList());

        if (YearMonth.from(start).equals(YearMonth.from(end))) {
            double totalBudget = budgets.stream().mapToDouble(budget -> {
                int diff = end.getDayOfMonth() - start.getDayOfMonth() + 1;
                return diff * dailyAmount(budget);
            }).sum();

            return totalBudget;
        }
        else {
            //firstMonth
            List<Budget> startMonthBudget = budgets.stream().filter(bd -> {
                YearMonth d = YearMonth.parse(bd.yearMonth, formatter);
                YearMonth startYM = YearMonth.from(start);
                return startYM.equals(d);
            }).collect(Collectors.toList());

            double startMonthAmount = startMonthBudget.stream().mapToDouble(budget -> {
                int diff = start.lengthOfMonth() - start.getDayOfMonth() + 1;
                return budget.amount * (diff) / start.lengthOfMonth();
            }).sum();

            //last month
            List<Budget> endMonthBudget = budgets.stream().filter(bd -> {
                YearMonth d = YearMonth.parse(bd.yearMonth, formatter);
                YearMonth endYM = YearMonth.from(end);
                return endYM.equals(d);
            }).collect(Collectors.toList());

            double endMonthAmount = endMonthBudget.stream().mapToDouble(budget -> {
                int diff = end.getDayOfMonth();
                return budget.amount * (diff) / end.lengthOfMonth();
            }).sum();

            // middle
            List<Budget> middleBudgets = budgets;
            middleBudgets.removeAll(startMonthBudget);
            middleBudgets.removeAll(endMonthBudget);

            double middleMonthAmount = middleBudgets.stream().mapToDouble(budget -> budget.amount).sum();

            return startMonthAmount + endMonthAmount + middleMonthAmount;
        }
    }

    private double dailyAmount(Budget budget) {
        return (double) budget.getAmount() / budget.dayCount();
    }
}
