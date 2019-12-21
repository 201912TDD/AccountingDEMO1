import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.DAYS;

public class Period {
    private final LocalDate start;
    private final LocalDate end;

    Period(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public long getOverlappingDays(Budget budget) {
        LocalDate overlappingStart = getStart().isAfter(budget.firstDay()) ? getStart() : budget.firstDay();
        LocalDate overlappingEnd = getEnd().isBefore(budget.lastDay()) ? getEnd() : budget.lastDay();
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
