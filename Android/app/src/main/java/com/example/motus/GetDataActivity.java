package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.AdapterView.OnItemSelectedListener;

public class GetDataActivity extends AppCompatActivity implements OnItemSelectedListener{

    private Spinner userChoice;
    private String TAG = "firebase";
    private TextView DBData;
    private String DatabaseDataReference = "data";
    private String dataPoint1 = "angle";
    private String dataPoint2 = "time";
    private String dataPoint3 = "uid";
    private DatabaseReference currRef;
    private DatabaseReference dataRef;
    private ValueEventListener event;
    private int dataLength = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        DBData = findViewById(R.id.liveData);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dataRef = database.getReference(DatabaseDataReference).child("dummy_data");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String,String> data = (HashMap<String,String>) dataSnapshot.getValue();
                dataLength = data.size();
                initialiseSpinner();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
        currRef = dataRef.child("");
        initialiseSpinner();
        event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String,String> data = (HashMap<String,String>) dataSnapshot.getValue();
                String value = "";
                value += data.get(dataPoint1) + "\n";
                value += data.get(dataPoint2) + "\n";
                value += data.get(dataPoint3) + "\n";
                DBData.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

    }

    private void initialiseSpinner() {
        userChoice = null;
        userChoice = findViewById(R.id.spinner);
        userChoice.setOnItemSelectedListener(this);

        Log.d(TAG,"initialise called");

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        for(int i = 1;i<dataLength+1;i++){
            String toAdd = "testdata";
            categories.add(toAdd+i);
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        userChoice.setAdapter(dataAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        currRef.removeEventListener(event);
        currRef = dataRef.child(item);
        event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String,String> data = (HashMap<String,String>) dataSnapshot.getValue();
                String value = "";
                value += data.get(dataPoint1) + "\n";
                value += data.get(dataPoint2) + "\n";
                value += data.get(dataPoint3) + "\n";
                DBData.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        currRef.addValueEventListener(event);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
