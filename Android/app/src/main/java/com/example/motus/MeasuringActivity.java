package com.example.motus;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MeasuringActivity extends NavigationMenu {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    boolean connected = true;
    int teller = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_measuring, null, false);
        drawer.addView(contentView, 0);

        mAuth = FirebaseAuth.getInstance();

        final Button cancelMeasurement = findViewById(R.id.stop_measuring_button);
        cancelMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Pressed cancel button");
                EndMeasuring(v);
            }
        });

        CheckInjuries();
        teller =0;

        if (!connected) {
            BuildDialog();
        }
    }

    public void EndMeasuring(View view) {
        //Log.d(LOG_TAG, "Stopping Measurements.");
        //finish();
        ShowResults();
    }

    public void BuildDialog() {
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

    public void BuildInjuryAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MeasuringActivity.this);

        builder.setCancelable(false);
        builder.setTitle("Injuries Added!");
        builder.setMessage("You have added injuries, please be careful during your exercise.\nContact the injuries tab for more information.");

        builder.setNegativeButton("Injuries", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShowInjuries();
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

    public void CheckInjuries() {
        final DatabaseReference myRef = database.getReference("injuries");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i = 1; i < 10; i++) {
                    String name = dataSnapshot.child(Integer.toString(i)).child("name").getValue().toString();
                    if (dataSnapshot.child(Integer.toString(i)).hasChild("users")) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.child(Integer.toString(i)).child("users").getChildren()) {
                            try {
                                String user = dataSnapshot.child(Integer.toString(i)).child("users").child(mAuth.getCurrentUser().getUid()).getKey();
                                if (dataSnapshot2.getKey().equals(mAuth.getCurrentUser().getUid())) {
                                    if (teller < 1) {
                                        BuildInjuryAlert();
                                    }
                                    teller = teller + 1;
                                }
                            }catch (Exception e){

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void ShowInjuries() {
        Intent intent = new Intent(this, InjuryActivity.class);
        startActivity(intent);
    }

    public void ShowResults() {
        Intent intent = new Intent(this, GetDataActivity.class);
        startActivity(intent);
    }
}
