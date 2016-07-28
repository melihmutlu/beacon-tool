package com.example.melih.beacon_tool;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class DotView extends View implements View.OnTouchListener {

    private Point mPoint;
    private Paint paint;
    private Beacon b;

    public DotView(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GREEN);
        if (b != null) {
            canvas.drawCircle((float) b.getX(), (float) b.getY(), 15, paint);
        }

        if (LeScanner.beaconList != null && !LeScanner.beaconList.isEmpty()) {
            paint.setColor(Color.RED);
            int a = 1;
            for(Beacon b : LeScanner.beaconList.values()){
                canvas.drawCircle((float) b.getX(), (float) b.getY(), 15, paint);
                canvas.drawText(b.getAddress(),30,10*a,paint);
                a++;
            }
        } else {
            paint.setColor(Color.RED);
            canvas.drawText("no beacon", 10, 10, paint);
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
                LeScanner.currentConfig.add(b.getAddress());

                invalidate();
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