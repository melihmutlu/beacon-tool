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
    private int floor;

    public Beacon(BluetoothDevice device, byte[] sr){
        this.sr = sr;
        this.device = device;
        this.floor = -100;
        rssiList = new LinkedList<>();
        decodeFromMajorAndMinor();
    }

    public String getAddress(){
        return device.getAddress();
    }

    public String getName(){
        return device.getName();
    }

    public void addRssi(int rssi){
        if(rssiList.size() == 5){
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

    private void setX(double x){
        this.x = x/1024;
    }

    private void setFloor(int floor){
        this.floor = floor;
    }

    private void setY(double y){
        this.y = y/1024;
    }

    public double getX(){
        return x;
    }

    public int getFloor(){
        return floor;
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

    public void decodeFromMajorAndMinor() {

        String major = "";
        String minor = "";
        try{
            major = String.valueOf((sr[25] & 0xff) * 0x100 + (sr[26] & 0xff));
            minor = String.valueOf((sr[27] & 0xff) * 0x100 + (sr[28] & 0xff));

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        int majorMinor = (Integer.parseInt(major) << 16) | (Integer.parseInt(minor) & 0xFFFF);

        setX((majorMinor >> 22) & 0x03FF);
        setY((majorMinor >> 12) & 0x03FF);

        int sign = ((majorMinor >> 11) & 1);
        if (sign == 0) {
            //Positive floor
            setFloor((short) ((majorMinor >> 4) & 0xFF));
        } else {
            setFloor((short) ((majorMinor >> 4) | 0xFFFFFF80));
        }

    }

    public void clearRssiList() {
        rssiList.clear();
    }
}
