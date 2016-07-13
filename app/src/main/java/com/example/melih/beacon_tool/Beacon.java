package com.example.melih.beacon_tool;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanRecord;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Melih on 13.7.2016.
 */
public class Beacon {

    private ScanRecord sr;
    private BluetoothDevice device;
    private Queue<Integer> rssiList;

    public Beacon(BluetoothDevice device, ScanRecord sr){
        this.sr = sr;
        this.device = device;
        rssiList = new LinkedList<>();
    }

    public String getAddress(){
        return device.getAddress();
    }

    public int getTxLevel(){
        return sr.getTxPowerLevel();
    }

    public String getName(){
        return device.getName();
    }

    public void addRssi(int rssi){
        if(rssiList.size() == 10){
            rssiList.poll();
            rssiList.add(rssi);
        }else{
            rssiList.add(rssi);
        }
    }

    public Queue<Integer> getRssi(){
        return rssiList;
    }

    public int getTx(){
        byte[] b = sr.getBytes();
        int tx = -1;
        try{
            String temp = String.format("%02x ", b[29]);
            tx = -(256 - Integer.parseInt(temp.substring(0,temp.length()-1),16));
        }catch (NullPointerException e){
           e.printStackTrace();
        }
        return tx;
    }

}
