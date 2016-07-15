package com.example.melih.beacon_tool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by alper on 14/07/16.
 */
public class GraphActivity extends LeScanner {

    private Spinner spinner;
    private ArrayAdapter<Beacon> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<Beacon>( this, R.layout.support_simple_spinner_dropdown_item, beaconList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



    }
}
