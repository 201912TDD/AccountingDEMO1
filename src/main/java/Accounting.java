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
                return diff * budget.dailyAmount();
            }).sum();

            return totalBudget;
        }
        else {
            double totalAmount = 0;

            for (Budget budget : budgets) {

                int overlappingDays = getOverlappingDays(new Period(start, end), budget);

                totalAmount += overlappingDays * budget.dailyAmount();
            }

            return totalAmount;
        }
    }

    private int getOverlappingDays(Period period, Budget budget) {
        int overlappingDays;
        if (budget.getYearMonth().equals(period.getStart().format(DateTimeFormatter.ofPattern("yyyyMM")))) {
            overlappingDays = period.getStart().lengthOfMonth() - period.getStart().getDayOfMonth() + 1;
        }
        else if (budget.getYearMonth().equals(period.getEnd().format(DateTimeFormatter.ofPattern("yyyyMM")))) {
            overlappingDays = period.getEnd().getDayOfMonth();
        }
        else {
            overlappingDays = budget.dayCount();
        }
        return overlappingDays;
    }
}
