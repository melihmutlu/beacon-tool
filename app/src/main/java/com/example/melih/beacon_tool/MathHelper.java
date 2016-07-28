package com.example.melih.beacon_tool;


import android.graphics.Point;
import android.util.Log;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by alper on 14/07/16.
 */
public class MathHelper {

    private static Random r = new Random();
    public static NormalDistribution Z = new NormalDistribution(null,0, 1);

    public static String lol(){
        return "Hello World!";
    }

    public static double getDistance(int tx, double rssi) {
        double n = 2;
        return Math.pow(10d, ((double) tx - rssi) / (10 * n));

    }

    // return a new point by Metropolis-Hastings algorithm
    // with respect to prior point and beacon set.
    protected static Point mhRollout(Set<Beacon> beaconSet, Point prior){

        // 1 for normal calculations
        // 0 for logarithm-based calculations

        double pr_new = 0;
        double pr_old = 0;

        double x = prior.x/LocationActivity.getScaleConstant() + r.nextGaussian() * 0.5;
        double y = prior.y/LocationActivity.getScaleConstant() + r.nextGaussian() * 0.5;

        for(Beacon b : beaconSet){
            if(b != null) {
                double d_new = Math.sqrt(Math.pow(b.getX() / LocationActivity.getScaleConstant() - x, 2) + Math.pow(b.getY() / LocationActivity.getScaleConstant() - y, 2));
                double d_old = Math.sqrt(Math.pow((b.getX() - prior.x) / LocationActivity.getScaleConstant(), 2) + Math.pow((b.getY() - prior.y) / LocationActivity.getScaleConstant(), 2));
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
            prior.x = (int) (x * LocationActivity.getScaleConstant());
            prior.y = (int) (y * LocationActivity.getScaleConstant());
        }

        return prior;
    }

    protected static List<Point> filterOut(Set<Beacon> beaconSet, List<Point> particleList) {


        List<Point> filtered = new LinkedList<>();

        // 1 for normal calculations
        // 0 for logarithm-based calculations

        double proposed_likelihood = 1;
        double prior_likelihood = 1;

        for(Point p : particleList) {

            double theta = getRandomAngle();
            double rho = r.nextDouble();
            double x = p.x/LocationActivity.getScaleConstant() + 0.5 * rho * Math.cos(theta);
            double y = p.y/LocationActivity.getScaleConstant() + 0.5 * rho * Math.sin(theta);

            for(Beacon b : beaconSet) {
                if(b != null) {
                    double d_new = Math.sqrt(Math.pow(b.getX() / LocationActivity.getScaleConstant() - x, 2) + Math.pow(b.getY() / LocationActivity.getScaleConstant() - y, 2));
                    double d_old = Math.sqrt(Math.pow((b.getX() - p.x) / LocationActivity.getScaleConstant(), 2) + Math.pow((b.getY() - p.y) / LocationActivity.getScaleConstant(), 2));
                    double d = b.getAverageDistance();

                    // normal calculations
                    //////////////////////
                    proposed_likelihood = proposed_likelihood * Z.cumulativeProbability(d_new - d);
                    prior_likelihood = prior_likelihood * Z.cumulativeProbability(d_old - d);

                    // logarithm-based calculation
                    //////////////////////
//                    proposed_likelihood = proposed_likelihood + Math.log(Z.cumulativeProbability(d_new-d));
//                    prior_likelihood = prior_likelihood + Math.log(Z.cumulativeProbability(d_old-d));

                }
            }

            double a = r.nextDouble();

            if ( a < (proposed_likelihood / prior_likelihood)) {
                filtered.add(new Point((int) (x * LocationActivity.getScaleConstant()), (int) (y * LocationActivity.getScaleConstant())));
            }

        }
        return filtered;
    }

    public static double getRandomAngle() {

        return r.nextDouble() * 2 * Math.PI;

    }
}
