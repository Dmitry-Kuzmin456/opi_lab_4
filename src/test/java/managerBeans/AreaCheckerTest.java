package managerBeans;

import org.junit.Assert;
import org.junit.Test;

public class AreaCheckerTest {
    private final AreaChecker areaChecker = new AreaChecker();

    @Test
    public void pointInsideRectangleShouldBeTrue() {
        Assert.assertTrue(areaChecker.isInTheSpot(1, 1, 2));
    }

    @Test
    public void negativeRadiusShouldBeFalse() {
        Assert.assertFalse(areaChecker.isInTheSpot(1, 1, -2));
    }
}
