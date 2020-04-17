package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateDummyData extends AppCompatActivity {

    ArrayList<String> lijst;
    private EditText userText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dummy_data);
        lijst = new ArrayList<>();
        /*lijst.add("Placeholder data. ");
        lijst.add("Only in the event ");
        lijst.add("the user didn't put ");
        lijst.add("in any data.");
        */
        userText = findViewById(R.id.userData);
    }

    public void startSecondActivity(View view){
        Intent intent = new Intent(this, ShowActivity.class);
        if(userText.getText().toString()!=""){
            lijst.add(userText.getText().toString());
            userText.setText("");
        }
        intent.putStringArrayListExtra("dataList",lijst);
        startActivity(intent);
    }

    public void addData(View view) {
        if(userText.getText().toString()!=""){
            lijst.add(userText.getText().toString());
            userText.setText("");
            Toast.makeText(getApplicationContext(),"Added data",Toast.LENGTH_SHORT).show();
        }
    }
}
