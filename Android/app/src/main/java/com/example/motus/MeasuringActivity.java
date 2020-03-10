package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MeasuringActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measuring);

        final Button cancelMeasurement = findViewById(R.id.stop_measuring_button);
        cancelMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Pressed cancel button");
                EndMeasuring(v);
            }
        });
    }
    public void EndMeasuring(View view){
        Log.d(LOG_TAG, "Stopping Measurements.");
        finish();

    }
}
