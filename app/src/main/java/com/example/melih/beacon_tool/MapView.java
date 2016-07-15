package com.example.melih.beacon_tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by alper on 11/07/16.
 */
public class MapView extends ImageView {

    private Canvas C = null;
    Context context;
    private Paint mPaint;
    private double absWidth = 460;
    private double absHeigth = 460;
    private List<Point> dots;

    private Map<Point, Double> beaconList;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
        this.context = c;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);

        beaconList = new HashMap<>();
        dots = new LinkedList<>();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,460,460,mPaint);
        for (Point t : beaconList.keySet()) {
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setColor(Color.CYAN);
            canvas.drawCircle((float) t.x*30, (float) t.y*30, 10, mPaint);

            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(Color.RED);
            canvas.drawCircle((float) t.x*30, (float) t.y*30, (float) beaconList.get(t).doubleValue()*30, mPaint);
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha(20);
        for (Point t : dots) {
            canvas.drawCircle((float) t.x*30, (float) t.y*30, 5, mPaint);
        }
    }

    public void setBeaconList(Map<Point, Double> m) {
        beaconList = m;
        invalidate();
    }

    public void setDots(List<Point> dots) {
        this.dots = dots;
        invalidate();
    }
}