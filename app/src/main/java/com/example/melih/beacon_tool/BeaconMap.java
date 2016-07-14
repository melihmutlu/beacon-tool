package com.example.melih.beacon_tool;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Melih on 13.7.2016.
 */
public class BeaconMap extends LeScanner {

    private DotView dotView;
    private TextView textView;
    private Button scanBtn;
    private Beacon b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
        dotView = (DotView) findViewById(R.id.dotView);
        textView = (TextView) findViewById(R.id.beaconName);
        scanBtn = (Button) findViewById(R.id.scanBtn);
        startScan();

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b = getNeaerest();
                dotView.setBeacon(b);

            }
        });

        dotView.setOnTouchListener(dotView);

    }


}
