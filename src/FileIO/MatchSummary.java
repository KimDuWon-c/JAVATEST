package FileIO;

public class MatchSummary {
    private int totalComparableCount;
    private int matchedCount;
    private int unmatchedCount;

    public MatchSummary() {
    }

    public MatchSummary(int totalComparableCount, int matchedCount, int unmatchedCount) {
        this.totalComparableCount = totalComparableCount;
        this.matchedCount = matchedCount;
        this.unmatchedCount = unmatchedCount;
    }

    public int getTotalComparableCount() {
        return totalComparableCount;
    }

    public int getMatchedCount() {
        return matchedCount;
    }

    public int getUnmatchedCount() {
        return unmatchedCount;
    }

    public void setTotalComparableCount(int totalComparableCount) {
        this.totalComparableCount = totalComparableCount;
    }

    public void setMatchedCount(int matchedCount) {
        this.matchedCount = matchedCount;
    }

    public void setUnmatchedCount(int unmatchedCount) {
        this.unmatchedCount = unmatchedCount;
    }

    @Override
    public String toString() {
        return "MatchSummary{totalComparableCount=" + totalComparableCount + ", matchedCount=" + matchedCount
                + ", unmatchedCount=" + unmatchedCount + "}";
    }
}
