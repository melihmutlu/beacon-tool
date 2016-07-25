package com.example.melih.beacon_tool;

import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Melih on 14.7.2016.
 */
public class LocationActivity extends LeScanner implements BluetoothEventListener{

    private static final int NUMBER_OF_ROLLOUTS = 100;
    private static Point currentPoint;
    private static MapView map;
    private static Set<Beacon> beaconSet = new HashSet<>();
    private static double scaleConstant = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loc_layout);

        currentPoint = new Point();
        currentPoint.set(300,300);
        map = (MapView) findViewById(R.id.map);
        // EGENİN TELEFONUNA GÖRE = 1360, ALPERİN TELEFONUNA GÖRE = 510, İGALİN NOTE 3 = 680, BİLGE = 1020
        // bunu nasıl ayarlayacağımı bulamadım, getWidth() 0 döndürüyordu.
        // level 11 = 31 metre
        // car park = 100 metre
        scaleConstant = 1360 / 31;

        LeScanner.addListener(this);
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
        updateBeaconSet();
        getEstimation();
    }

    private void getEstimation() {

        double avgX = 0;
        double avgY = 0;
        List<Point> dotList = new LinkedList<Point>();

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

    }

    private void updateBeaconSet() {

        /*/
        beaconSet.clear();
        for(Beacon i : LeScanner.beaconList.values()){
            if(i.getY() != 0.0 && i.getX() !=0.0) {
                beaconSet.add(i);
            }
        }
        //*/
        Set<String> m = LeScanner.getValidBeacons();
        beaconSet.clear();
        for(String s : m){
            beaconSet.add(beaconList.get(s));
        }
        map.setBeaconList(beaconSet);

    }

    public static double getScaleConstant() {
        return scaleConstant;
    }
}
