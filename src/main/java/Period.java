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

        Period another = new Period(budget.firstDay(), budget.lastDay());
        LocalDate firstDay = another.getStart();
        LocalDate lastDay = another.getEnd();

        LocalDate overlappingStart = getStart().isAfter(firstDay) ? getStart() : firstDay;
        LocalDate overlappingEnd = getEnd().isBefore(lastDay) ? getEnd() : lastDay;
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
