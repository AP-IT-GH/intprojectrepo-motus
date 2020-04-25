package com.example.motus;

import androidx.appcompat.app.ActionBar;
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

public class edit_profile_screen extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextView Email;
    TextView Name;
    ImageView ProfilePicture;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference newRef = database.getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_screen);

        Email = findViewById(R.id.editable_email);
        Name = findViewById(R.id.editable_username);
        ProfilePicture = findViewById(R.id.editable_profile_picture);

        FirebaseUser user = mAuth.getCurrentUser();
        Email.setHint(user.getEmail());
        Name.setHint(user.getDisplayName());
        ProfilePicture.setImageURI(user.getPhotoUrl());

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
