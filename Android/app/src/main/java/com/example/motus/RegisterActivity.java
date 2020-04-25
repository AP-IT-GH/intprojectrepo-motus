package com.example.motus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    Data data;
    EditText email;
    EditText password;
    Button register;
    EditText name;
    String emailInput;
    String passwordInput;
    FirebaseAuth firebaseAuth;
    String nameInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        data = new Data();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        name = findViewById(R.id.name);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUPMail();
            }
        });
    }

    private void registerUPMail(){
        emailInput = email.getText().toString();
        passwordInput = password.getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();
        nameInput = name.getText().toString();
        if (emailInput.length() == 0 || passwordInput.length()==0 || nameInput.length() == 0){
            Toast.makeText(RegisterActivity.this, "invalid email or password", Toast.LENGTH_SHORT).show();

        }else{
            firebaseAuth.createUserWithEmailAndPassword(emailInput,passwordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(RegisterActivity.this,"User registered", Toast.LENGTH_SHORT).show();
                                    email.setText("");
                                    password.setText("");
                                    ShowLogin();
                                }else{
                                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public void ShowLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("NAME_OF_USER", nameInput);
        startActivity(intent);
    }
}
