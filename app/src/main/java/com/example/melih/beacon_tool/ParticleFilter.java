package com.example.melih.beacon_tool;

import java.util.ArrayList;

/**
 * Created by Melih on 21.7.2016.
 */
public class ParticleFilter {

    private ArrayList<Particle> particles;

    //Set particles
    public ParticleFilter(int n){
        for(int i=0 ; i<n; i++){
            particles.add(new Particle());
        }
    }

    // 0. approximate robot position using current particles
    // 1. if  the "agent" moves , update all particles by the same amount
    // 2. do a random walk if on random walk frame
    // 3. estimate weights of every particle
    // 4. normalize weights
    // 5. resample: pick each particle based on probability

}