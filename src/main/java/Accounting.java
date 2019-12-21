import java.time.LocalDate;

public class Accounting {

    private IBudgetRepo db;

    public Accounting(IBudgetRepo db) {
        this.db = db;
    }

    public double QueryBudget(LocalDate start, LocalDate end) {

        Period period = new Period(start, end);
        return db.GetAll().stream()
                .mapToDouble(budget -> budget.getTotalAmount(period))
                .sum();
    }
}
