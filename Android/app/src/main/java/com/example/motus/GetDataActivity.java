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
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.widget.AdapterView.OnItemSelectedListener;

import org.w3c.dom.Text;

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

    private LineGraphSeries<DataPoint> series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        UID = mUser.getUid();
        DBData = findViewById(R.id.liveData);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dataRef = database.getReference(DatabaseDataReference);
        currRef = dataRef;
        currRef.orderByChild("uid").equalTo(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                ArrayList<HashMap<String,String>> data = (ArrayList<HashMap<String,String>>) dataSnapshot.getValue();
                dataArray = data;
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
        if(dataArray!=null){
            if(dataArray.size()!=0){
                drawGraph();
            }
        }
    }

    private void drawGraph(){

        //Dit is een voorbeeld van hoe een grafiek werkt. Dit kan in eender welke activity verwerkt
        //worden.

        //twee punten, x en y aanmaken
        double x,y;
        TextView t = findViewById(R.id.liveData);
        t.setVisibility(View.INVISIBLE);
        //de graphview uit de layout halen
        GraphView graph = (GraphView)findViewById(R.id.graph);
        graph.setVisibility(View.VISIBLE);

        //een serie punten voor op de graphview
        series = new LineGraphSeries<>();

        //een vooraf bepaald aantal datapunten
        int numDataPoints = 100;

        //we gaan voor elk punt dat we willen een nieuw datapunt aanmaken en toevoegen aan de serie
        for(int i = 0;i<dataArray.size();i++){
            if(dataArray.get(i)!=null){
                y = Double.parseDouble(dataArray.get(i).get(dataPoint1));
                x = Double.parseDouble(dataArray.get(i).get(dataPoint2));
                //nieuw datapunt aanmaken en toevoegen aan de serie datapunten
                series.appendData(new DataPoint(x,y),true,100);
            }

        }

        //we voegen de serie toe aan de grafiek
        graph.addSeries(series);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = "";
        item = parent.getItemAtPosition(position).toString();
        if(item!=""){
            HashMap<String,String> value = dataArray.get(Integer.parseInt(item));
            String textToPrint = "";
            textToPrint+=value.get(dataPoint1) + "\n";
            textToPrint+=value.get(dataPoint2) + "\n";
            textToPrint+=value.get(dataPoint3) + "\n";

            DBData.setText(textToPrint);
        }
        //currRef.addValueEventListener(event);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
