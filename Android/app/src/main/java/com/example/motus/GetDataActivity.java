package com.example.motus;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.widget.AdapterView.OnItemSelectedListener;

public class GetDataActivity extends AppCompatActivity implements OnItemSelectedListener{

    private Spinner userChoice;
    private String TAG = "firebasedebug";
    private TextView DBData;
    private String DatabaseDataReference = "data";
    private String dataPoint1 = "angle";
    private String dataPoint2 = "time";
    private String dataPoint3 = "uid";
    private DatabaseReference currRef;
    private DatabaseReference newRef;
    private DatabaseReference dataRef;
    private Query databaseRef;
    private ValueEventListener event;
    private String UID;

    private ArrayList<HashMap<String,String>> dataArray;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        UID = mUser.getUid();
        Log.d(TAG,UID);
        DBData = findViewById(R.id.liveData);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dataRef = database.getReference(DatabaseDataReference);
        Log.d(TAG,dataRef.toString());
        /*
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"snapshot:" + dataSnapshot.toString());
                ArrayList<String> data = (ArrayList<String>) dataSnapshot.getValue();
                dataLength = data.size();
                Log.d(TAG,"length: " + dataLength);
                initialiseSpinner();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        */

        /*
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
        */
        currRef = dataRef;
        currRef.orderByChild("uid").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Log.d(TAG,"snapshot:" + dataSnapshot.toString());
                Log.d(TAG,"snapshot2:" + dataSnapshot.getValue().toString());
                ArrayList<HashMap<String,String>> data = (ArrayList<HashMap<String,String>>) dataSnapshot.getValue();
                Log.d(TAG,"data: " + data.toString());
                dataArray = data;
                //keySet = data.keySet();
                //Log.d(TAG,"length: " + keySet.toString());
                initialiseSpinner();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        initialiseSpinner();
    }

    private void initialiseSpinner() {
        userChoice = null;
        userChoice = findViewById(R.id.spinner);
        userChoice.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        if(dataArray==null){
            categories.add("");
        }else{
            for(int i = 0;i<dataArray.size();i++){
                if(dataArray.get(i)!=null){
                    categories.add(i+"");
                }
            }
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        userChoice.setAdapter(dataAdapter);
        Log.d(TAG,"initialised spinner");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        Log.d(TAG,"starting onItemSelected");
        String item = "";
        item = parent.getItemAtPosition(position).toString();
        //newRef.removeEventListener(event);
        /*newRef = dataRef.child(item);
        event = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String,String> data = (HashMap<String,String>) dataSnapshot.getValue();
                String value = "";
                if(data.get(dataPoint3).equals(UID)){
                    value += data.get(dataPoint1) + "\n";
                    value += data.get(dataPoint2) + "\n";
                    value += data.get(dataPoint3) + "\n";
                }else{
                    value += "Incorrect UID";
                }
                DBData.setText(value);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };
        */
        if(item!=""){
            Log.d(TAG,"itemselected something");
            Log.d(TAG,"getting: " + item);
            Log.d(TAG,"out of: " + dataArray.toString());
            HashMap<String,String> value = dataArray.get(Integer.parseInt(item));
            Log.d(TAG,"this value is: " + value.toString());
            String textToPrint = "";
            textToPrint+=value.get(dataPoint1) + "\n";
            textToPrint+=value.get(dataPoint2) + "\n";
            textToPrint+=value.get(dataPoint3) + "\n";

            DBData.setText(textToPrint);
        }else{
            Log.d(TAG,"itemselected whatever");
        }
        //currRef.addValueEventListener(event);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
