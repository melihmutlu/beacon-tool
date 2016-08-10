package com.example.melih.beacon_tool;

/**
 * Created by alper on 08/08/16.
 */
public class WeightedDirection {

    private double dx;
    private double dy;
    private double weight;

    public WeightedDirection(double dx, double dy, double weight) {
        this.dx = dx;
        this.dy = dy;
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getWeight() {
        return weight;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }
}
