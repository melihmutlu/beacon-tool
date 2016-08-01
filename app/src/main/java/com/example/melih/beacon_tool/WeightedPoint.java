package com.example.melih.beacon_tool;

import android.graphics.Point;

/**
 * Created by alper on 29/07/16.
 */
public class WeightedPoint {
    private Point point;
    private double weight;

    public WeightedPoint(Point p, double w) {
        point = new Point(p);
        weight = w;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public double getWeight() {
        return weight;
    }

    public Point getPoint() {
        return point;
    }
}
