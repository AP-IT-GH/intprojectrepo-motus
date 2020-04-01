package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newRef = database.getReference("message");
        //myRef.setValue("Hello, World! - Bart");
        newRef.setValue("Hello Database!");
    }

    public void launchShowDataActivity(View view) {
        Intent intent = new Intent(this,CreateDummyData.class);
        startActivity(intent);
    }

    public void launchGetDataActivity(View view) {
        Intent intent = new Intent(this,GetDataActivity.class);
        startActivity(intent);
    }
}
