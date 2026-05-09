package mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@Singleton
@Startup
public class MBeanRegistry {
    private PointCounter pointCounter;
    private HitPercentage hitPercentage;
    private ObjectName counterName;
    private ObjectName percentageName;

    @PostConstruct
    public void init() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            pointCounter = new PointCounter();
            counterName = new ObjectName("WebLab4:type=PointCounter");
            mbs.registerMBean(pointCounter, counterName);

            hitPercentage = new HitPercentage();
            percentageName = new ObjectName("WebLab4:type=HitPercentage");
            mbs.registerMBean(hitPercentage, percentageName);

            System.out.println("MBeans registered successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            mbs.unregisterMBean(counterName);
            mbs.unregisterMBean(percentageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PointCounter getPointCounter() { return pointCounter; }
    public HitPercentage getHitPercentage() { return hitPercentage; }
}