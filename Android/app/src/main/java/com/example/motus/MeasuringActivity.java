package com.example.motus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class MeasuringActivity extends NavigationMenu {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    boolean connected = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_measuring, null, false);
        drawer.addView(contentView, 0);

        final Button cancelMeasurement = findViewById(R.id.stop_measuring_button);
        cancelMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Pressed cancel button");
                EndMeasuring(v);
            }
        });

        if(!connected){
            BuildDialog();
        }
    }
    public void EndMeasuring(View view){
        Log.d(LOG_TAG, "Stopping Measurements.");
        finish();

    }

    public void BuildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MeasuringActivity.this);

        builder.setCancelable(false);
        builder.setTitle("No connection found!");
        builder.setMessage("Please check your connection with the device and try again.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void BuildInjuryAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MeasuringActivity.this);

        builder.setCancelable(false);
        builder.setTitle("Injuries Added!");
        builder.setMessage("You have Added injuries, please be careful during your exercise. Contact the injuries tab for more information.");

        builder.setNegativeButton("Injuries", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
