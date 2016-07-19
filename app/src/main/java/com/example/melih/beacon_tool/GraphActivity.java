package com.example.melih.beacon_tool;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

/**
 * Created by alper on 14/07/16.
 */
public class GraphActivity extends LeScanner implements BluetoothEventListener {

    private GraphView graph;
    private Spinner spinner;
    private ArrayAdapter<Beacon> adapter;
    private Beacon mBeacon ;
    private int time = 0 ;
    private ArrayList<DataPoint> dataPoints;
    private boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        addListener(this);
        startScan();
        graph = (GraphView) findViewById(R.id.graph);
        spinner = (Spinner) findViewById(R.id.spinner);

        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(20);
        graph.getViewport().setMaxY(100);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter = new ArrayAdapter(GraphActivity.this, R.layout.support_simple_spinner_dropdown_item, beaconList.keySet().toArray());
                spinner.setAdapter(adapter);
                mBeacon = beaconList.get(parent.getItemAtPosition(position));
                isSelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isSelected = false;
            }
        });

    }

    @Override
    public void onUpdate(Beacon b) {
        dataPoints = new ArrayList<>();
        Queue<Integer> graphValues ;
        if(isSelected){
            if(b.getAddress().equals("C1:F8:CB:2B:90:4A")){
                graphValues = b.getRssi();
                for(Integer i : graphValues) {
                    dataPoints.add(new DataPoint(time, -1*i));
                    time++;
                }
                DataPoint[] arr = Arrays.copyOf(dataPoints.toArray(), dataPoints.toArray().length, DataPoint[].class);
                LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(arr);
                graph.removeAllSeries();
                graph.addSeries(series);
                dataPoints.clear();
            }
        }else{
            adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, beaconList.keySet().toArray());
            spinner.setAdapter(adapter);
            Log.d("TEST" , "spinner");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeListener(this);
    }
}
