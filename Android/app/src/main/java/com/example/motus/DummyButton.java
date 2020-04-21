package com.example.motus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DummyButton extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_button);

        final Button startMeasureButton = findViewById(R.id.start_measuring_button);

        startMeasureButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(LOG_TAG, "Pressed Dummy measuring Button");
                launchMeasuringActivity(view);
            }
        });
    }

    public void launchMeasuringActivity(View view){
        Log.d(LOG_TAG, "Launching MeasuringActivity");

        Intent intent = new Intent(this, MeasuringActivity.class);
        startActivity(intent);
    }
}
