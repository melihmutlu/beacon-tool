package com.example.melih.beacon_tool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;

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

    protected static Map<String, Beacon> beaconList;
    private static Set<BluetoothEventListener> listeners = new HashSet<>();
    private static Queue<Beacon> lastBeacons = new LinkedList<>();
    private static Set<String> validBeacons = new HashSet<>();
    private BluetoothAdapter adapter;
    private static Set<String> currentConfig = new HashSet<>();

    protected Beacon nearestBeacon;

    protected void startScan(){
        beaconList = new HashMap<>();
        // level 11 beacons
        currentConfig.add("D0:30:AD:84:07:40");
        currentConfig.add("E0:2E:E2:ED:86:64");
        currentConfig.add("FC:73:08:31:50:42");
        currentConfig.add("D6:31:D4:6D:42:DF");

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
                        }
                        b.addRssi(rssi);
                        if(nearestBeacon == null){
                            nearestBeacon = b;
                        }else if(rssi > nearestBeacon.getRssi().peek()) {
                            nearestBeacon = b;
                        }
                        beaconList.put(nearestBeacon.getAddress(), nearestBeacon);
                        if(currentConfig.contains(b.getAddress())) {
                            for (BluetoothEventListener l : listeners) {
                                l.onUpdate(b);
                            }
                            if (lastBeacons.size() < 15) {
                                lastBeacons.add(b);
                                setValidBeacons();
                            } else {
                                lastBeacons.poll();
                                lastBeacons.add(b);
                                setValidBeacons();
                            }
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

    public static Queue<Beacon> getLastBeacons() {
        return lastBeacons;
    }

    public static void setValidBeacons() {
        validBeacons.clear();
        for(Beacon b : lastBeacons) {
            validBeacons.add(b.getAddress());
        }
    }

    public static Set<String> getValidBeacons() {
        return validBeacons;
    }
}
