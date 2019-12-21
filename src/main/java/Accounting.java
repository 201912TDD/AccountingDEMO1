import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

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

                long overlappingDays = getOverlappingDays(new Period(start, end), budget);

                totalAmount += overlappingDays * budget.dailyAmount();
            }

            return totalAmount;
        }
    }

    private long getOverlappingDays(Period period, Budget budget) {
        long overlappingDays;
        if (budget.getYearMonth().equals(period.getStart().format(DateTimeFormatter.ofPattern("yyyyMM")))) {
            overlappingDays = DAYS.between(period.getStart(), budget.lastDay()) + 1;
        }
        else if (budget.getYearMonth().equals(period.getEnd().format(DateTimeFormatter.ofPattern("yyyyMM")))) {
            overlappingDays = DAYS.between(budget.firstDay(), period.getEnd()) + 1;
//            overlappingDays = period.getEnd().getDayOfMonth();
        }
        else {
            overlappingDays = budget.dayCount();
        }
        return overlappingDays;
    }
}
