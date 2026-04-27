package FileIO;

import java.time.LocalDateTime;

public class DateTimeRange {
    private final LocalDateTime startInclusive;
    private final LocalDateTime endInclusive;

    public DateTimeRange(LocalDateTime startInclusive, LocalDateTime endInclusive) {
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive;
    }

    public LocalDateTime getStartInclusive() {
        return startInclusive;
    }

    public LocalDateTime getEndInclusive() {
        return endInclusive;
    }

    public boolean contains(LocalDateTime target) {
        return !target.isBefore(startInclusive) && !target.isAfter(endInclusive);
    }

    @Override
    public String toString() {
        return "DateTimeRange{startInclusive=" + startInclusive + ", endInclusive=" + endInclusive + "}";
    }
}
