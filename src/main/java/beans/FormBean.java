package beans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import managerBeans.DataBaseManager;
import managerBeans.Validator;
import models.Point;
import java.io.Serializable;
import mbeans.MBeanRegistry;


@Named("formBean")
@SessionScoped
public class FormBean implements Serializable {

    @Inject
    private PointsContainer pointsContainer;

    @Inject
    private Validator validator;

    @EJB
    private DataBaseManager database;

    @EJB
    private MBeanRegistry mBeanRegistry;

    private Double x = 0.0;
    private Double y = 0.0;
    private Double r = 1.0;

    private Double lastX;
    private Double lastY;
    private Double lastR;
    private Boolean lastHit;
    private String status;

    public Double getX() { return x; }
    public void setX(Double x) { this.x = x; }
    public Double getY() { return y; }
    public void setY(Double y) { this.y = y; }
    public Double getR() { return r; }
    public void setR(Double r) { this.r = r; }

    public Double getLastX() { return lastX; }
    public void setLastX(Double lastX) { this.lastX = lastX; }
    public Double getLastY() { return lastY; }
    public void setLastY(Double lastY) { this.lastY = lastY; }
    public Double getLastR() { return lastR; }
    public void setLastR(Double lastR) { this.lastR = lastR; }
    public Boolean getLastHit() { return lastHit; }
    public void setLastHit(Boolean lastHit) { this.lastHit = lastHit; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void submit() {
        setStatus(null);

        if (!isValuesValid()) {
            setStatus("All fields (X, Y, R) must be provided");
            return;
        }

        Validator.ValidationResult result = validator.validateForm(x, y, r);
        if (!result.isValid()) {
            setStatus(result.getErrorMessage());
            return;
        }

        try {
            Point newPoint = database.checkAndSavePoint(x, y, r);
            pointsContainer.addPoint(newPoint);

            if (mBeanRegistry != null) {
                mBeanRegistry.getPointCounter().incrementPoints(newPoint.isStatus());

                mBeanRegistry.getHitPercentage().update(newPoint.isStatus());
            }

            setLastX(newPoint.getX());
            setLastY(newPoint.getY());
            setLastR(newPoint.getR());
            setLastHit(newPoint.isStatus());
            setStatus("Success");
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Ошибка при сохранении точки: " + e.getMessage());
        }
    }


    private boolean isValuesValid() {
        return x != null && y != null && r != null && r > 0;
    }
}