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

    public long getOverlappingDays(Period another) {

        LocalDate overlappingStart = getStart().isAfter(another.getStart()) ? getStart() : another.getStart();
        LocalDate overlappingEnd = getEnd().isBefore(another.getEnd()) ? getEnd() : another.getEnd();
        return DAYS.between(overlappingStart, overlappingEnd) + 1;
    }
}
