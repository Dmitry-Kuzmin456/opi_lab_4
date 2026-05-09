package beans;

import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import managerBeans.DataBaseManager;
import models.Point;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Named("points")
@ApplicationScoped
public class PointsContainer implements Serializable {

    private List<Point> points;

    @EJB
    private DataBaseManager dataBaseManager;

    @PostConstruct
    @Transactional
    public void init() {
        points = new CopyOnWriteArrayList<Point>(dataBaseManager.getPoints());
    }

    public void addPoint(Point point) {
        this.points.add(0, point);
    }

    public List<Point> getPoints() {
        return this.points;
    }

    public void setPoints(List<Point> points) {
        this.points = new CopyOnWriteArrayList<Point>(points);
    }

    public String getPointsJson() {
        Gson gson = new Gson();
        String json = gson.toJson(points.stream()
                .map(point -> Map.of(
                        "x", point.getX(),
                        "y", point.getY(),
                        "r", point.getR(),
                        "hit", point.isStatus()
                ))
                .collect(Collectors.toList()));
        return json;
    }

    public void setPointsJson(String pointsJson) {
    }
}