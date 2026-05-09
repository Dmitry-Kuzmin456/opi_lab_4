package managerBeans;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import models.Point;

import java.util.Collections;
import java.util.List;

@Stateless
public class DataBaseManager {

    @Inject
    private AreaChecker areaChecker;

    public Point checkAndSavePoint(double x, double y, double r) {
        if (r <= 0) {
            throw new RuntimeException("R must be positive");
        }

        boolean status = areaChecker.isInTheSpot(x, y, r);
        // In-memory mode: just compute the status and return the point.
        return new Point(x, y, r, status);
    }

    public List<Point> getPoints() {
        // No DB backing: initial state is empty, points are stored in-memory in `PointsContainer`.
        return Collections.emptyList();
    }
}