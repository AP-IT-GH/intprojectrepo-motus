package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.motus.R;

import java.util.ArrayList;

public class ShowActivity extends AppCompatActivity {

    ArrayList<String> lijst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Intent intent = getIntent();
        lijst = intent.getStringArrayListExtra("dataList");
        TextView box = findViewById(R.id.dataBox);
        String data = "";
        for(int i = 0;i<lijst.size();i++){
            data += lijst.get(i);
            data += "\n";
        }
        if(data==""){
            data = "no data found.";
        }
        box.setText(data);
    }
}
