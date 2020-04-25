package com.example.motus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileScreenActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseAuth mAuth;


    TextView Email;
    TextView Name;
    ImageView ProfilePicture;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference newRef = database.getReference("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        mAuth = FirebaseAuth.getInstance();
        final Button changeUserDataButton = findViewById(R.id.button_change_userdata);
        Email = findViewById(R.id.profile_email_text);
        Name = findViewById(R.id.profile_username_text);
        ProfilePicture = findViewById(R.id.profile_picture);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            Email.setText(user.getEmail());
            Name.setText(user.getDisplayName());
            ProfilePicture.setImageURI(user.getPhotoUrl());
        }



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
