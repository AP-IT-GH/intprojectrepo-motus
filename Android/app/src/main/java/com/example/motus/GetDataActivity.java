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
        dataRef = database.getReference(DatabaseDataReference); //referentie naar de database

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //De snapshot van de referentie
                ArrayList<String> data = (ArrayList<String>) dataSnapshot.getValue();
                //De lijst van alle data
                dataLength = data.size();
                //De grootte van de lijst opvragen
                initialiseSpinner();
                //De menu initialiseren met de grootte van de lijst (default=5)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        currRef = dataRef; //De referentie die momenteel wordt gebruikt
        initialiseSpinner(); // Moet ik doen anders crasht da denk ik
        event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //Data: {0:(time:x,angle:y,uid:z),...}
                HashMap<String,String> data = (HashMap<String,String>) dataSnapshot.getValue();

                //Data omzetten naar string
                String value = "";
                value += data.get(dataPoint1) + "\n";
                value += data.get(dataPoint2) + "\n";
                value += data.get(dataPoint3) + "\n";
                //string zetten in de textview
                DBData.setText(value);

                //controle
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
        //reset de 'spinner'
        userChoice = null;
        userChoice = findViewById(R.id.spinner);
        userChoice.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        //Aangezien da van 0-x gaat gewoon ne lijst van 0-x maken
        for(int i = 0;i<dataLength;i++){
            String toAdd = "";
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

        //referentie resetten
        currRef.removeEventListener(event);

        //referentie leggen naar het nieuw geselecteerde item (0,1,2, etc.)
        currRef = dataRef.child(item);
        event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //Basically hetzelfde als hierboven
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
        //eventlistener aan referentie toevoegen, als die verandert moet ge da terug doen.
        currRef.addValueEventListener(event);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
