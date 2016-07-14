package com.example.melih.beacon_tool;

import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Melih on 13.7.2016.
 */
public class BeaconMap extends LeScanner {

    private DotView dotView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        dotView = (DotView) findViewById(R.id.dotView);
        textView = (TextView) findViewById(R.id.beaconName);
        //startScan();
        //Beacon b = getNeaerest();

        dotView.setOnTouchListener(dotView);
        //b.setX(dotView.getX());
        //b.setY(dotView.getY());

    }


}
