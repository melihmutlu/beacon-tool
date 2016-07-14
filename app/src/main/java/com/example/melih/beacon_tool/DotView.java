package com.example.melih.beacon_tool;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DotView extends View implements View.OnTouchListener {

    private ArrayList<Point> pointList;
    private Point mPoint;
    private Paint paint;
    private Beacon b;

    public DotView(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        pointList = new ArrayList<>();
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GREEN);
        for(Point p: pointList){
            canvas.drawCircle(p.x, p.y, 15, paint);
        }
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch(event.getAction()){
            default:
                Point mPoint = new Point();
                mPoint.x = (int)event.getX();
                mPoint.y = (int)event.getY();
                b.setX(mPoint.x);
                b.setY(mPoint.y);
                pointList.add(mPoint);
        }
        return true;
    }

    public float getX(){
        return mPoint.x;
    }

    public float getY(){
        return mPoint.y;
    }

    public void setBeacon(Beacon b){
        this.b = b ;
    }
}