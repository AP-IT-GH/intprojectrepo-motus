package com.example.motus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class edit_profile_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);


        final Button ConfirmChangeButton = findViewById(R.id.button_confirm_changed_userdata);
        final Button CancelChangeButton = findViewById(R.id.button_cancel_changed_userdata);

        ConfirmChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmChanges(v);
            }
        });
        CancelChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelChanges(v);
            }
        });

    }
    public void ConfirmChanges (View view){
        //insert code where checks if things changed and send data to database.

        Intent replyIntent = new Intent(this, edit_profile_screen.class);
        finish();
    }
    public void CancelChanges(View view){
        finish();
    }
}
