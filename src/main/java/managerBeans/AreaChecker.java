package managerBeans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("areaChecker")
@ApplicationScoped
public class AreaChecker {

    public boolean isInTheSpot(double x, double y, double r) {
        if (r <= 0) return false;

        if (x >= 0 && y >= 0) {
            return x <= r && y <= r;
        }

        if (x <= 0 && y >= 0) {
            return false;
        }

        if (x <= 0 && y <= 0) {
            return x >= -r && y >= -r/2 && y >= (-x/2 - r/2);
        }

        if (x >= 0 && y <= 0) {
            return (x * x + y * y) <= (r/2 * r/2);
        }

        return false;
    }
}