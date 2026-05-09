package mbeans;

import javax.management.AttributeChangeNotification;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

public class PointCounter extends NotificationBroadcasterSupport implements PointCounterMBean {
    private long totalPoints = 0;
    private long pointsOutside = 0;
    private long sequenceNumber = 1;

    @Override
    public long getTotalPoints() {
        return totalPoints;
    }

    @Override
    public long getPointsOutside() {
        return pointsOutside;
    }

    public void incrementPoints(boolean isHit) {
        totalPoints++;
        if (!isHit) {
            pointsOutside++;
        }

        if (totalPoints % 5 == 0) {
            Notification notification = new Notification(
                    "points.multipleOfFive",
                    this,
                    sequenceNumber++,
                    System.currentTimeMillis(),
                    "Общее количество точек кратно 5: " + totalPoints
            );
            sendNotification(notification);
        }
    }
}