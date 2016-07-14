package com.example.melih.beacon_tool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by alper on 14/07/16.
 */
public class GraphActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText(MathHelper.lol());
    }
}
