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
import java.util.Set;

/**
 * Created by Melih on 13.7.2016.
 */
public class LeScanner extends AppCompatActivity{

    protected static Map<String, Beacon> beaconList;
    private static Set<BluetoothEventListener> listeners = new HashSet<>();
    private BluetoothAdapter adapter;
    protected Beacon nearestBeacon;

    protected void startScan(){
        beaconList = new HashMap<>();
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
                        if(beaconList.containsKey(b.getAddress())) {
                            for (BluetoothEventListener l : listeners) {
                                l.onUpdate(b);
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

        if(beaconAddress == null || beaconAddress.isEmpty()) return null;

        List<Beacon> beacons = new LinkedList<>();
        for(String key : beaconAddress){
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

}
