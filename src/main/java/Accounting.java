import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Accounting {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
    private IBudgetRepo db;

    public Accounting(IBudgetRepo db) {
        this.db = db;
    }

    public double QueryBudget(LocalDate start, LocalDate end) {

//        List<Budget> budgets = db.GetAll().stream().filter(bd ->
//        {
//            YearMonth d = YearMonth.parse(bd.yearMonth, formatter);
//            YearMonth startYM = YearMonth.from(start);
//            YearMonth endYM = YearMonth.from(end);
//            return (d.equals(startYM) || d.isAfter(startYM)) && (d.equals(endYM) || d.isBefore(endYM));
//        }).collect(Collectors.toList());

        Period period = new Period(start, end);

        return db.GetAll().stream()
                .mapToDouble(budget -> budget.getTotalAmount(period))
                .sum();
    }
}
