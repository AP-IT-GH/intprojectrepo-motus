package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InjuryActivity extends AppCompatActivity {
private ArrayList<SpinnerItem> _itemList;
private ArrayList<SpinnerItem> injuryList;
private SpinnerAdapter _adapter;
private SpinnerAdapter injuryAdapter;
private FirebaseDatabase database = FirebaseDatabase.getInstance();
private TextView txtDescription;
private Button btnInjuries;
    private FirebaseAuth mAuth;

    int Teller1 = 0;
    int Teller2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_injury);
        mAuth = FirebaseAuth.getInstance();

        initList();
        injuryList = new ArrayList<>();
        createList("");

        txtDescription = findViewById(R.id.text_view_description);
        btnInjuries = findViewById(R.id.injuryButton);
        final Spinner spinner = findViewById(R.id.categorySpinner);
        final Spinner injurySpinner = findViewById(R.id.injurySpinner);
        _adapter = new SpinnerAdapter(this, _itemList);
        spinner.setAdapter(_adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem clickedItem = (SpinnerItem) parent.getItemAtPosition(position);
                String clickedItemName = clickedItem.getItem();
                Toast.makeText(InjuryActivity.this, clickedItemName, Toast.LENGTH_SHORT).show();
                createList(clickedItemName);
                injuryAdapter = new SpinnerAdapter(InjuryActivity.this, injuryList);
                injurySpinner.setAdapter(injuryAdapter);
                injurySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SpinnerItem clickedItem = (SpinnerItem) parent.getItemAtPosition(position);
                        final String clickedItemName = clickedItem.getItem();
                        createDescription(clickedItemName);
                        initButton(clickedItemName);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnInjuries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = injurySpinner.getSelectedItemPosition();
                String name = injuryList.get(position).getItem();
                if(btnInjuries.getText().equals("Remove injury")){
                    removeInjury(name);
                    Teller2 =0;
                }else if(btnInjuries.getText().equals("Add injury")){
                    addInjury(name);
                    Teller1 = 0;
                    Toast.makeText(InjuryActivity.this, name, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initList(){
        _itemList = new ArrayList<>();
        _itemList.add(0,new SpinnerItem("Choose category"));
        _itemList.add(new SpinnerItem("Biceps"));
        _itemList.add(new SpinnerItem("Triceps"));
        _itemList.add(new SpinnerItem("Forearm"));
    }

    private void createList(final String selectedItem){
        injuryList.clear();
        injuryList.add(0,new SpinnerItem("Choose injury"));
            DatabaseReference myRef = database.getReference("injuries");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (int i=1;i<10;i++) {
                        String category = dataSnapshot.child(Integer.toString(i)).child("category").getValue().toString();
                        if (category.toLowerCase().equals(selectedItem.toLowerCase())) {
                            String name = dataSnapshot.child(Integer.toString(i)).child("name").getValue().toString();
                            injuryList.add(new SpinnerItem(name));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });
    }

    private void createDescription(final String selectedItem){
        DatabaseReference myRef = database.getReference("injuries");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i=1;i<10;i++) {
                    String name = dataSnapshot.child(Integer.toString(i)).child("name").getValue().toString();
                    if (name.toLowerCase().equals(selectedItem.toLowerCase())) {
                        String description = dataSnapshot.child(Integer.toString(i)).child("description").getValue().toString();
                        txtDescription.setText(description);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void initButton(final String selectedItem){
        try {
        DatabaseReference myRef = database.getReference("injuries");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i=1;i<10;i++) {
                    String name = dataSnapshot.child(Integer.toString(i)).child("name").getValue().toString();
                    if(name.toLowerCase().equals(selectedItem.toLowerCase())) {
                        if(dataSnapshot.child(Integer.toString(i)).hasChild("users")) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot.child(Integer.toString(i)).child("users").getChildren()) {
                                if (!dataSnapshot2.child(mAuth.getCurrentUser().getUid()).exists()) {
                                    btnInjuries.setText("Remove injury");
                                } else {
                                    btnInjuries.setText("Add injury");
                                }
                            }
                        }else {
                            btnInjuries.setText("Add injury");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        }catch (Exception exception){
            btnInjuries.setText("Add injury");
        }
    }

    private void addInjury(final String selectedItem){
        final DatabaseReference myRef = database.getReference("injuries");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i=1;i<10;i++) {
                    String name = dataSnapshot.child(Integer.toString(i)).child("name").getValue().toString();
                    if (name.toLowerCase().equals(selectedItem.toLowerCase())) {
                        if (!dataSnapshot.child(Integer.toString(i)).child("users").child(mAuth.getCurrentUser().getUid()).exists()) {
                            if (Teller1 <1)
                            {
                                myRef.child(Integer.toString(i)).child("users").child(mAuth.getCurrentUser().getUid()).child("name").setValue(mAuth.getCurrentUser().getDisplayName());
                            }
                            Teller1 = Teller1 +1;
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

    private void removeInjury(final String selectedItem){
        final DatabaseReference myRef = database.getReference("injuries");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i=1;i<10;i++) {
                    String name = dataSnapshot.child(Integer.toString(i)).child("name").getValue().toString();
                    if (name.toLowerCase().equals(selectedItem.toLowerCase())) {
                        if (dataSnapshot.child(Integer.toString(i)).child("users").child(mAuth.getCurrentUser().getUid()).exists()) {
                            if (Teller2 <1)
                            {
                                myRef.child(Integer.toString(i)).child("users").child(mAuth.getCurrentUser().getUid()).removeValue();
                            }
                            Teller2 = Teller2 +1;
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
}
