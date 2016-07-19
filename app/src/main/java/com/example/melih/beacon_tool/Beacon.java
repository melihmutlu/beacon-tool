package com.example.melih.beacon_tool;

import android.bluetooth.BluetoothDevice;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Melih on 13.7.2016.
 */
public class Beacon {

    private byte[] sr;
    private BluetoothDevice device;
    private Queue<Integer> rssiList;
    private double x,y;

    public Beacon(BluetoothDevice device, byte[] sr){
        this.sr = sr;
        this.device = device;
        rssiList = new LinkedList<>();
    }

    public String getAddress(){
        return device.getAddress();
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
        int tx = -1;
        try{
            String temp = String.format("%02x ", sr[29]);
            tx = -(256 - Integer.parseInt(temp.substring(0,temp.length()-1),16));
        }catch (NullPointerException e){
           e.printStackTrace();
        }
        return tx;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getAverageDistance() {
        double mean = 0;
        for (int i : this.rssiList) {
            mean = mean + i;

        }
        mean = mean / rssiList.size();
        return MathHelper.getDistance(this.getTx(), mean);
    }
}
