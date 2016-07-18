package com.example.melih.beacon_tool;


import android.graphics.Point;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.Random;
import java.util.Set;

/**
 * Created by alper on 14/07/16.
 */
public class MathHelper {

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

        double pr_new = 1;
        double pr_old = 1;

        Random r = new Random();
        double x = prior.x/LocationActivity.scaleConstant + r.nextGaussian();
        double y = prior.y/LocationActivity.scaleConstant + r.nextGaussian();

        for(Beacon b : beaconSet){
            double d_new = Math.sqrt(Math.pow(b.getX()/LocationActivity.scaleConstant - x,2) + Math.pow(b.getY()/LocationActivity.scaleConstant - y,2));
            double d = b.getAverageDistance();
            pr_new = pr_new * Z.cumulativeProbability(d-d_new);
        }

        for(Beacon b : beaconSet){
            double d_old = Math.sqrt(Math.pow((b.getX() - prior.x)/LocationActivity.scaleConstant, 2) + Math.pow((b.getY() - prior.y)/LocationActivity.scaleConstant,2));
            double d = b.getAverageDistance();
            pr_old = pr_old * Z.cumulativeProbability(d-d_old);
        }

        Random r2 = new Random();
        double a = r2.nextDouble();

        if ( a < (pr_new / pr_old)) {
            prior.x = (int) (x * LocationActivity.scaleConstant);
            prior.y = (int) (y * LocationActivity.scaleConstant);
        }

        return prior;
    }
}
