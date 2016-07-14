package com.example.melih.beacon_tool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Melih on 13.7.2016.
 */
public class LeScanner extends AppCompatActivity{

    protected static ArrayList<Beacon> beaconList;
    private BluetoothAdapter adapter;
    private BluetoothLeScanner leScanner;
    protected Beacon nearestBeacon;

    protected void startScan(){
        beaconList = new ArrayList<>();
        adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device,final int rssi, final byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO
                        Beacon b = new Beacon(device, scanRecord);
                        b.addRssi(rssi);
                        if(nearestBeacon == null){
                            nearestBeacon = b;
                        }else if(rssi > nearestBeacon.getRssi().peek())
                            nearestBeacon = b;
                        beaconList.add(b);
                        Log.d("Scanner" , b.getAddress() + " " + b.getRssi().peek() );
                    }
                });
            }
        });
    }

    protected Beacon getNeaerest(){
        return nearestBeacon;
    }

}
