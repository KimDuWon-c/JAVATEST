package monitoring.model;

import java.time.LocalDateTime;

public class TimeRange {
    private LocalDateTime startInclusive;
    private LocalDateTime endInclusive;

    public TimeRange() {
    }

    public TimeRange(LocalDateTime startInclusive, LocalDateTime endInclusive) {
        this.startInclusive = startInclusive;
        this.endInclusive = endInclusive;
    }

    public LocalDateTime getStartInclusive() {
        return startInclusive;
    }

    public LocalDateTime getEndInclusive() {
        return endInclusive;
    }

    public void setStartInclusive(LocalDateTime startInclusive) {
        this.startInclusive = startInclusive;
    }

    public void setEndInclusive(LocalDateTime endInclusive) {
        this.endInclusive = endInclusive;
    }

    public boolean contains(LocalDateTime target) {
        return !target.isBefore(startInclusive) && !target.isAfter(endInclusive);
    }

    @Override
    public String toString() {
        return "TimeRange{startInclusive=" + startInclusive + ", endInclusive=" + endInclusive + "}";
    }
}
