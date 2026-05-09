package mbeans;

public class HitPercentage implements HitPercentageMBean {
    private long totalPoints = 0;
    private long hits = 0;

    @Override
    public double getHitPercentage() {
        if (totalPoints == 0) return 0.0;
        return ((double) hits / totalPoints) * 100.0;
    }

    public void update(boolean isHit) {
        totalPoints++;
        if (isHit) {
            hits++;
        }
    }
}