package mbeans;

import javax.management.NotificationEmitter;

public interface PointCounterMBean {
    long getTotalPoints();
    long getPointsOutside();
}