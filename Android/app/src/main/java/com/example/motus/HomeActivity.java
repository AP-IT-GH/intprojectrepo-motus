package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends NavigationMenu {
    private Button btnMeasure, btnRecords, btnInjuries, btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawer.addView(contentView, 0);

        btnMeasure = findViewById(R.id.measure);
        btnRecords = findViewById(R.id.records);
        btnInjuries = findViewById(R.id.injuries);
        btnProfile = findViewById(R.id.profile);

        btnMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMeasure();
            }
        });
        btnRecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowResults();
            }
        });
        btnInjuries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInjuries();
            }
        });
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInfo();
            }
        });
    }

    public void ShowInfo() {
        Intent intent = new Intent(this, ProfileInfo.class);
        startActivity(intent);
    }

    public void ShowResults() {
        //Intent intent = new Intent(this, ResultsActivity.class);
        //startActivity(intent);
    }

    public void ShowMeasure() {
        //Intent intent = new Intent(this, MeasuringActivity.class);
        //startActivity(intent);
    }

    public void ShowInjuries() {
        //Intent intent = new Intent(this, InjuryActivity.class);
        //startActivity(intent);
    }
}
