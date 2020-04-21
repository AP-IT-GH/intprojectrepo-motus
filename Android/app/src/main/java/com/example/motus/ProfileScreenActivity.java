package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfileScreenActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        final Button changeUserDataButton = findViewById(R.id.button_change_userdata);

        changeUserDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchEditProfileScreen(v);
            }
        });


    }

    public void LaunchEditProfileScreen (View view){


        Intent intent = new Intent(this, edit_profile_screen.class);
        startActivity(intent);
    }
}
