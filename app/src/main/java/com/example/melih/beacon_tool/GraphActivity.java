package com.example.melih.beacon_tool;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by alper on 14/07/16.
 */
public class GraphActivity extends AppCompatActivity {

    private static GraphView graphView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        graphView = (GraphView) findViewById(R.id.graph);
    }

    public static void updateView(){
        if (graphView == null) return;
        graphView.invalidate();
    }
}
