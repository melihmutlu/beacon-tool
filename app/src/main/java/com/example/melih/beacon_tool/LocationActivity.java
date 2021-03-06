package com.example.melih.beacon_tool;

import android.graphics.Point;
import android.os.Bundle;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Melih on 14.7.2016.
 */
public class LocationActivity extends LeScanner implements BluetoothEventListener{

    private static final int NUMBER_OF_ROLLOUTS = 300;
    private static long lastUpdateTime = 0;
    private static Point currentPoint;
    private static List<Point> particles;
    private static MapView map;
    private static Set<Beacon> beaconSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loc_layout);

        particles = new LinkedList<>();
        currentPoint = new Point();
        currentPoint.set(300,300);
        map = (MapView) findViewById(R.id.map);


        LeScanner.addListener(this);
        startScan();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LeScanner.removeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LeScanner.addListener(this);
    }

    @Override
    public void onUpdate(Beacon beacon) {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastUpdateTime > 500) {
            lastUpdateTime = currentTime;
            updateBeaconSet();
//          getEstimation();
            getEstimationByFilter();
        }
    }

    private List<Point> getEstimation() {

        double avgX = 0;
        double avgY = 0;
        List<Point> dotList = new LinkedList<Point>();

        //burn out time

        for(int j=0; j<NUMBER_OF_ROLLOUTS/10; j++) {
            MathHelper.mhRollout(beaconSet, currentPoint);
        }

        for(int j=0; j<NUMBER_OF_ROLLOUTS; j++) {
            Point temp = new Point();
            MathHelper.mhRollout(beaconSet, currentPoint);
            temp.set(currentPoint.x, currentPoint.y);
            dotList.add(temp);
            avgX = avgX + temp.x;
            avgY = avgY + temp.y;
        }
        map.setDots(dotList);

        currentPoint.x = (int) (avgX / NUMBER_OF_ROLLOUTS);
        currentPoint.y = (int) (avgY / NUMBER_OF_ROLLOUTS);

        return dotList;

    }

    private void getEstimationByFilter() {

        if(particles.isEmpty()) {
            particles = getEstimation();
        }

        particles = MathHelper.filterOut(beaconSet,particles);
        map.setDots(particles);

    }

    private void updateBeaconSet() {

        Set<String> m = LeScanner.getValidBeacons();
        beaconSet.clear();

        for(String s : m){
            if(chosenBeacons.contains(s)) {
                beaconSet.add(beaconList.get(s));
            }
        }
        map.setBeaconList(beaconSet);

    }


}
