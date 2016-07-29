package com.example.melih.beacon_tool;


import android.graphics.Point;
import android.util.Log;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by alper on 14/07/16.
 */
public class MathHelper {

    private static Random r = new Random();
    public static NormalDistribution Z = new NormalDistribution(null,0, 1);

    // return distance estimation with respect to
    // calibration value tx and measured rssi, with
    // n = 2, path loss exponent.

    public static double getDistance(int tx, double rssi) {
        double n = 2;
        return Math.pow(10d, ((double) tx - rssi) / (10 * n));

    }

    // return a new point by Metropolis-Hastings algorithm
    // with respect to prior point and beacon set.
    protected static Point mhRollout(Set<Beacon> beaconSet, Point prior){

        // 1 for normal calculations
        // 0 for logarithm-based calculations

        double scaleConstant = LocationActivity.getScaleConstant();
        double pr_new = 0;
        double pr_old = 0;

        double x = prior.x/scaleConstant + r.nextGaussian() * 0.5;
        double y = prior.y/scaleConstant + r.nextGaussian() * 0.5;

        for(Beacon b : beaconSet){
            if(b != null) {
                double d_new = Math.sqrt(Math.pow(b.getX() / scaleConstant - x, 2) + Math.pow(b.getY() / scaleConstant - y, 2));
                double d_old = Math.sqrt(Math.pow((b.getX() - prior.x) / scaleConstant, 2) + Math.pow((b.getY() - prior.y) / scaleConstant, 2));
                double d = b.getAverageDistance();

                // normal calculations
                //////////////////////
//                pr_new = pr_new * Z.cumulativeProbability(d - d_new);
//                pr_old = pr_old * Z.cumulativeProbability(d - d_old);

                double v = Math.log10(Z.cumulativeProbability(d - d_new));
                Log.d("INFO", "p: " + v);

                // logarithm-based calculation
                //////////////////////
                pr_new = pr_new + Math.log10(Z.cumulativeProbability(d - d_new));
                pr_old = pr_old + Math.log10(Z.cumulativeProbability(d - d_old));
            }
        }

        double a = r.nextDouble();

        // normal calculations
        // (pr_new / pr_old)
        //////////////////////
        // logarithm-based calculations
        // Math.pow(10, pr_new - pr_old)

        if ( a < Math.pow(10d, pr_new - pr_old)) {
            prior.x = (int) (x * scaleConstant);
            prior.y = (int) (y * scaleConstant);
        }

        return prior;
    }

    protected static List<Point> filterOut(Set<Beacon> beaconSet, List<Point> particleList) {

        double scaleConstant = LocationActivity.getScaleConstant();
        List<Point> filtered = new LinkedList<>();
        Map<Point, Double> weights = new HashMap<>();

        double proposed_likelihood = 0;

        for(Point p : particleList) {

            double theta = getRandomAngle();
            double rho = r.nextDouble();

            // transform polar coordinates to euclidean coordinates

            double x = p.x/scaleConstant + 0.5 * rho * Math.cos(theta);
            double y = p.y/scaleConstant + 0.5 * rho * Math.sin(theta);

            for(Beacon b : beaconSet) {
                if(b != null) {

                    double d_new = Math.sqrt(Math.pow(b.getX() / scaleConstant - x, 2) + Math.pow(b.getY() / scaleConstant - y, 2));
                    double d = b.getAverageDistance();
                    proposed_likelihood = proposed_likelihood + Math.log(Z.cumulativeProbability(d_new-d));

                    Point m = new Point((int) (x * scaleConstant),(int) (y * scaleConstant));
                    weights.put(m, proposed_likelihood);

                }
            }

            weights = normaliseWeights(weights);


        }
        return filtered;
    }

    // return a random angle in [0, 2*pi]

    public static double getRandomAngle() {

        return r.nextDouble() * 2 * Math.PI;

    }

    public static Map<Point, Double> normaliseWeights(Map<Point, Double> m) {

        double Z = 0;
        Map<Point, Double> normalisedMap = new HashMap<>();

        for(Double d : m.values()){
            Z = Z + d;
        }
        for(Point e : m.keySet()) {
            double normalisedWeight = m.get(e) / Z;
            normalisedMap.put(e, normalisedWeight);
        }

        return normalisedMap;
    }
}
