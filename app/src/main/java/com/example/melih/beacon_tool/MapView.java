package com.example.melih.beacon_tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by alper on 11/07/16.
 */
public class MapView extends ImageView {

    private Canvas C = null;
    Context context;
    private Paint mPaint;
    private List<Point> dots;
    private Set<Beacon> beaconList;
    private static double scaleConstant = 1;
    public static int width , height;

    public MapView(Context context) {
        this(context, null);
    }

    public MapView(Context c, AttributeSet attrs) {
        this(c, attrs, 0);
        this.context = c;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        beaconList = new HashSet<>();
        dots = new LinkedList<>();
    }

    public MapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // initialization
        super.onDraw(canvas);
        scaleConstant = this.getWidth() / 31;
        width = getWidth();
        height = getHeight();
        Log.d("TEST" , width + " : " + height);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,getWidth(),getHeight(),mPaint);
        int a = 1;

        // draw beacons and distance circles
        for (Beacon b : beaconList) {
            if (b != null) {
                // beacon position
                mPaint.setColor(Color.MAGENTA);
                canvas.drawText("x: " + b.getX()*getWidth() + ", y: " + b.getY()*getHeight(), 30, 10 * a, mPaint);
                a++;

                // beacon circle
                mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
                mPaint.setColor(Color.CYAN);
                canvas.drawCircle((float) b.getX()*getWidth(), (float) b.getY()*getHeight(), 10, mPaint);

                // distance circle
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(Color.RED);
                canvas.drawCircle((float) b.getX()*getWidth(), (float) b.getY()*getHeight(), (float) (b.getAverageDistance() * getScaleConstant()), mPaint);
            }
        }

        // draw compass
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(getWidth()-100,0,getWidth(),100,mPaint);
        mPaint.setColor(Color.RED);
        double theta = Math.atan(MathHelper.getDeltaY() / MathHelper.getDeltaX());
        canvas.drawLine(getWidth()-50,50, getWidth() - 50 + 50 * (float) Math.cos(theta),50 + 50 * (float) Math.sin(theta),mPaint);

        // draw point cloud
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setAlpha(20);
        for (Point t : dots) {
            canvas.drawCircle((float) t.x, (float) t.y, 5, mPaint);
        }

    }

    public void setBeaconList(Set<Beacon> m) {
        beaconList = m;
        invalidate();
    }

    public void setDots(List<Point> dots) {
        this.dots = dots;
        invalidate();
    }

    public static double getScaleConstant() {
        return scaleConstant;
    }
}