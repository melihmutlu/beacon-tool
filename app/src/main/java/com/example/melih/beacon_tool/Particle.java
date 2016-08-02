package com.example.melih.beacon_tool;

/**
 * Created by Melih on 21.7.2016.
 */
public class Particle {
    public double x, y, weight;
    private double width , height;

    public Particle(){
        this.x = 0;
        this.y = 0;
        this.weight = 1.0;
    }

    public Particle(double x, double y, double weight){
        this.x = x;
        this.y = y ;
        this.weight = weight;
    }

    public void randomize(){
        this.x = Math.floor(Math.random() * width);
        this.y = Math.floor(Math.random() * height);
    }

    public void degrade(double weight){
        this.weight *= weight;
    }

    public void normalize(double maxWeight){
        this.weight /= maxWeight;
    }

    public Particle clone(){
        return new Particle(x, y, weight);
    }
}
