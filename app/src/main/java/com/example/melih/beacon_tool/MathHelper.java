package com.example.melih.beacon_tool;


import android.graphics.Point;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by alper on 14/07/16.
 */
public class MathHelper {

    private static Random r = new Random();
    private static double deltaX = 1;
    private static double deltaY = 1;
    public static NormalDistribution Z = new NormalDistribution(null,0, 2.5);

    // return distance estimation with respect to
    // calibration value tx and measured rssi, with
    // n = 2, path loss exponent.

    public static double getDistance(int tx, double rssi) {
        double n = 2;
        return Math.pow(10d, ((double) tx - rssi) / (10 * n));

    }

    // return a new point by Metropolis-Hastings algorithm
    // with respect to prior point and beacon set.
    protected static Point mhRollout(Set<Beacon> beaconSet, Point prior) {

        double scaleConstant = MapView.getScaleConstant();
        double pr_new = 0;
        double pr_old = 0;

        double x = prior.x / MapView.getScaleConstant() + r.nextGaussian() * 0.5;
        double y = prior.y / MapView.getScaleConstant() + r.nextGaussian() * 0.5;

        for (Beacon b : beaconSet) {
            if (b != null) {
                double d_new = Math.sqrt(Math.pow(b.getX() * MapView.width / MapView.getScaleConstant() - x, 2) + Math.pow(b.getY() * MapView.height / MapView.getScaleConstant() - y, 2));
                double d_old = Math.sqrt(Math.pow((b.getX() * MapView.width - prior.x) / MapView.getScaleConstant(), 2) + Math.pow((b.getY() * MapView.height - prior.y) / MapView.getScaleConstant(), 2));

                double d = b.getAverageDistance();
                pr_new = pr_new + Math.log10(Z.cumulativeProbability(d - d_new));
                pr_old = pr_old + Math.log10(Z.cumulativeProbability(d - d_old));

            }
        }

        double a = r.nextDouble();

        if (a < Math.pow(10, pr_new - pr_old)) {
            prior.x = (int) (x * MapView.getScaleConstant());
            prior.y = (int) (y * MapView.getScaleConstant());
        }
        return prior;
    }

    protected static List<Point> filterOut(Set<Beacon> beaconSet, List<Point> particleList ){

        double scaleConstant = MapView.getScaleConstant();
        List<Point> filtered = new LinkedList<>();
        ArrayList<WeightedPoint> weights = new ArrayList<>();
//        ArrayList<WeightedDirection> direcs = new ArrayList<>();
        deltaX = 0;
        deltaY = 0;

        for(Point p : particleList) {

            double proposed_likelihood = 1;
            double theta = getRandomAngle();
            double rho = r.nextDouble();

            // transform polar coordinates to euclidean coordinates

            double dx = rho * Math.cos(theta);
            double dy = rho * Math.sin(theta);
            double x = p.x/MapView.getScaleConstant() + dx;
            double y = p.y/MapView.getScaleConstant() + dy;

            for(Beacon b : beaconSet) {
                if(b != null) {
                    double d_new = Math.sqrt(Math.pow(b.getX()* MapView.width / MapView.getScaleConstant() - x, 2) + Math.pow(b.getY()*MapView.height / MapView.getScaleConstant() - y, 2));
                    double d = b.getAverageDistance();
                    proposed_likelihood = proposed_likelihood * 2 * Z.cumulativeProbability(-Math.abs(d_new - d));
                }
            }
            Point m = new Point((int) (x * scaleConstant),(int) (y * scaleConstant));
            weights.add(new WeightedPoint(m, proposed_likelihood));
//            direcs.add(new WeightedDirection(dx, dy, proposed_likelihood));
            deltaX = deltaX + dx * proposed_likelihood;
            deltaY = deltaY + dy * proposed_likelihood;
        }

        weights = normaliseWeights(weights);

        // Resampling procedure

        for(int i=0; i<weights.size(); i++) {
            double a = r.nextDouble();
            double it = 0;
            int pos = -1;
            while(it < a) {
                pos++;
                it = it + weights.get(pos).getWeight();
            }
            filtered.add(weights.get(pos).getPoint());
        }

        return filtered;
    }

    // return a random angle in [0, 2*pi]

    public static double getRandomAngle() {

        return r.nextDouble() * 2 * Math.PI;

    }

    public static ArrayList<WeightedPoint> normaliseWeights(ArrayList<WeightedPoint> m) {

        double Z = 0;
        ArrayList<WeightedPoint> normalisedMap = new ArrayList<>();

        for(WeightedPoint d : m){
            Z = Z + d.getWeight();
        }

        for(WeightedPoint e : m) {
            double normalisedWeight = e.getWeight() / Z;
            WeightedPoint p = new WeightedPoint(e.getPoint(), normalisedWeight);
            normalisedMap.add(p);
        }

        return normalisedMap;
    }

    public static double getDeltaX() {
        return deltaX;
    }

    public static double getDeltaY() {
        return deltaY;
    }
}
