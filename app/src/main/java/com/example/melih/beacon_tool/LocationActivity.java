package com.example.melih.beacon_tool;

import android.graphics.Point;
import android.os.Bundle;

import org.apache.commons.math3.distribution.NormalDistribution;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Melih on 14.7.2016.
 */
public class LocationActivity extends LeScanner {

    private static Point point;
    private static NormalDistribution Z;
    private static Map<Point, Double> estimationMap;
    private MapView map ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loc_layout);
        point = new Point();
        Z = new NormalDistribution(0, 1);
        estimationMap = new HashMap<Point, Double>();
        map = (MapView) findViewById(R.id.map);

        while(isScanning){
            for(Beacon b : beaconList){
                Point pos = new Point();
                pos.x = (int) b.getX();
                pos.y = (int) b.getY();
                estimationMap.put(pos , getDistance(b.getTx(), b.getRssi().peek()));
                map.setBeaconList(estimationMap);
                double a = -1;
                double avgX = 0;
                double avgY = 0;
                double avgZ = 0;
                List<Point> dotList = new LinkedList<Point>();

                for(int j=0; j<1000; j++) {
                    a = getPosition(estimationMap);
                    Point temp = new Point();
                    temp.x = point.x;
                    temp.y = point.y;
                    dotList.add(temp);
                    avgX = avgX + point.x;
                    avgY = avgY + point.y;
                }
                map.setDots(dotList);

                point.x = (int) avgX / 1000;
                point.y = (int) avgY / 1000;
            }
        }



    }

    protected static double getDistance(int tx, double rssi) {
        double n = 2;
        return Math.pow(10d, ((double) tx - rssi) / (10 * n));
    }

    protected static double getPosition(Map<Point, Double> m){

        double pr_new = 1;
        double pr_old = 1;

        Random r = new Random();
        double x = point.x + r.nextGaussian()*2;
        double y = point.y + r.nextGaussian()*2;

        for(Point t : m.keySet()){
            double d_new = Math.sqrt( Math.pow(t.x - x,2) + Math.pow(t.y - y,2));
            double d = m.get(t);
            pr_new = pr_new * Z.cumulativeProbability(d-d_new);
        }

        for(Point t : m.keySet()){
            double d_old = Math.sqrt( Math.pow(t.x - point.x,2) + Math.pow(t.y - point.y,2));
            double d = m.get(t);
            pr_old = pr_old * Z.cumulativeProbability(d-d_old);
        }

        Random r2 = new Random();
        double a = r2.nextDouble();

        if ( a < pr_new / pr_old) {
            point.x = (int) x;
            point.y = (int) y;
        }

        return pr_new/pr_old;
    }

}
