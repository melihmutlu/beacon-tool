package com.example.melih.beacon_tool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by Melih on 13.7.2016.
 */
public class LeScanner extends AppCompatActivity{

    private class Signal{
        public Beacon beacon;
        public long time;
        public Signal(Beacon beacon, long time ){
            this.beacon = beacon ;
            this.time = time ;
        }
    }


    protected static Map<String, Beacon> beaconList;
    private static Set<BluetoothEventListener> listeners = new HashSet<>();
    private static Queue<Signal> lastBeacons = new LinkedList<>();
    private static Set<String> validBeacons = new HashSet<>();
    private BluetoothAdapter adapter;
    public static Set<String> currentConfig = new HashSet<>();
    public static ArrayList<String> nearThree = new ArrayList<>();
    private static int mFloor = 0;
    public static ArrayList<String> chosenBeacons = new ArrayList<>();

    protected Beacon nearestBeacon;

    protected void startScan(){
        beaconList = new HashMap<>();
        // level 11 beacons
        //currentConfig.add("D0:30:AD:84:07:40");
        //currentConfig.add("E0:2E:E2:ED:86:64");
        //currentConfig.add("FC:73:08:31:50:42");
        //currentConfig.add("D6:31:D4:6D:42:DF");

        adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device,final int rssi, final byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO
                        Beacon b;
                        if(beaconList.containsKey(device.getAddress())) {
                            b = beaconList.get(device.getAddress());
                        } else {
                            b = new Beacon(device, scanRecord);
                            beaconList.put(b.getAddress(), b);
                        }
                        b.addRssi(rssi);

                        if(b.getFloor() == mFloor){
                            currentConfig.add(b.getAddress());
                        }

                        if(currentConfig.contains(b.getAddress())) {

                            if(chosenBeacons.size()<3 ) {
//                                b.clearRssiList();
//                                b.addRssi(rssi);
                                beaconList.put(b.getAddress(), b);
                                chosenBeacons.add(b.getAddress());
                            } else {
                                String max = chosenBeacons.get(0);
                                int index = 0;
                                for (String s : chosenBeacons) {
                                    if (beaconList.get(s).getAverageDistance() > beaconList.get(max).getAverageDistance()) {
                                        max = s;
                                    }
                                }
                                if (b.getAverageDistance() < beaconList.get(max).getAverageDistance() && !chosenBeacons.contains(b.getAddress())){
                                    chosenBeacons.remove(chosenBeacons.indexOf(max));
//                                    b.clearRssiList();
//                                    b.addRssi(rssi);
                                    beaconList.put(b.getAddress(), b);
                                    chosenBeacons.add(b.getAddress());
                                }
                            }


                            for (BluetoothEventListener l : listeners) {
                                l.onUpdate(b);
                            }
                            lastBeacons.add(new Signal(b,System.currentTimeMillis()));
                            while (lastBeacons.size() != 1 && (lastBeacons.peek().time < System.currentTimeMillis()-1000)){
                                lastBeacons.poll();
                            }
                            setValidBeacons();
                        }
                    }
                });
            }
        });
    }

    protected Beacon getNearest(){
        return nearestBeacon;
    }

    public static List<Beacon> getBeaconsByAddress(List<String> beaconAddress) {

        if (beaconAddress == null || beaconAddress.isEmpty()) return null;

        List<Beacon> beacons = new LinkedList<>();
        for (String key : beaconAddress) {
            beacons.add(LeScanner.beaconList.get(key));
        }
        return beacons;
    }

    public static void addListener(BluetoothEventListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(BluetoothEventListener listener) {
        listeners.remove(listener);
    }

    public static Queue<Signal> getLastBeacons() {
        return lastBeacons;
    }

    public static void setValidBeacons() {
        validBeacons.clear();
        for(Signal s : lastBeacons) {
            validBeacons.add(s.beacon.getAddress());
        }
    }

    public static Set<String> getValidBeacons() {
        return validBeacons;
    }

}
