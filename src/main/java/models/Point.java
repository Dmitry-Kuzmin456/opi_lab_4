package models;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "points_results")
public class Point implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "x_coord", nullable = false)
    private double x;

    @Column(name = "y_coord", nullable = false)
    private double y;

    @Column(name = "r_value", nullable = false)
    private double r;

    @Column(name = "status", nullable = false)
    private boolean status;


    public Point() {}

    public Point(double x, double y, double r, boolean status) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }
    public double getY() { return y; }
    public void setY(double y) { this.y = y; }
    public double getR() { return r; }
    public void setR(double r) { this.r = r; }
    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
