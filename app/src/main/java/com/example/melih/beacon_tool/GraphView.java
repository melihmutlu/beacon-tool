package com.example.melih.beacon_tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


/**
 * Created by alper on 14/07/16.
 */
public class GraphView extends View{

    Context context;
    Paint p = new Paint();

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
        this.context = c;
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        List<String> beaconAddresses = new LinkedList<>();
        for (String s : LeScanner.beaconList.keySet()){
            beaconAddresses.add(s);
        }

        Random r = new Random();

        List<Beacon> beacons = LeScanner.getBeaconsByAddress(beaconAddresses);
        //canvas.drawLine((float) r.nextInt(700),(float) r.nextInt(700),(float) r.nextInt(700),(float) r.nextInt(700),p);
        int old = 0;
        int count = 0;
        for(Beacon b : beacons) {
            //canvas.drawLine((float) r.nextInt(700),(float) r.nextInt(700),(float) r.nextInt(700),(float) r.nextInt(700),p);
            Queue<Integer> rssiValues = b.getRssi();
            Log.d("INFO", "Size: " + rssiValues.size());

            p.setColor(r.nextInt(256));

            // BURASI OLMADI

            for(int a : rssiValues) {

            }
        }
    }

    public void updateView() {
        invalidate();
    }
}
