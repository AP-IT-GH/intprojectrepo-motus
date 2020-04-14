package com.example.motus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity  extends NavigationMenu {
    private SignInButtonImpl signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private String TAG="LoginActivity";
    private FirebaseAuth mAuth;
    private Button btnSignOut;
    private Button btnSendData;
    private int RC_SIGN_IN = 1;
    int Teller = 0;
    Data data;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference newRef = database.getReference("users");
    FirebaseDatabase databaseMessage = FirebaseDatabase.getInstance();
    DatabaseReference newRefMessage = databaseMessage.getReference("data");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signInButton = findViewById(R.id.sign_in_bt);
        mAuth = FirebaseAuth.getInstance();
        btnSignOut = findViewById(R.id.sign_out_bt);
        btnSendData = findViewById(R.id.send_data_id);
        data = new Data();

        GoogleSignInOptions signInOptions =  new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,signInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btnSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getUid() != null)
                {
                    sendMessage();
                    Toast.makeText(LoginActivity.this, "Send data button SUCCESS", Toast.LENGTH_SHORT).show();
                    Teller = 0;
                }
            }
        });
        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleSignInClient.signOut();
                Toast.makeText(LoginActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                btnSignOut.setVisibility(View.INVISIBLE);
            }
        });
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_login, null, false);
        drawer.addView(contentView, 0);
    }
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Toast.makeText(LoginActivity.this, "Signed In, Great Success", Toast.LENGTH_SHORT).show();
            firebaseGoogleAuth(account);
        }
        catch (ApiException e){
            Toast.makeText(LoginActivity.this, "Sign In Failure", Toast.LENGTH_SHORT).show();
            firebaseGoogleAuth(null);
        }
    }
    private  void firebaseGoogleAuth(GoogleSignInAccount accountGoogle){
        AuthCredential authCredential = GoogleAuthProvider.getCredential(accountGoogle.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI();
                    loginUser(user);
                    String currentUser = user.getUid();
                    data.setUid(currentUser);
                }else{
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    updateUI();

                }
            }
        });
    }

    private void sendMessage(){
        data.setAngle("45");
        data.setTime("0.1");
        newRefMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int itemsData;
                itemsData =(int)dataSnapshot.getChildrenCount();
                String temp = String.valueOf(itemsData);
                Toast.makeText(LoginActivity.this, temp, Toast.LENGTH_SHORT).show();
                if (Teller<1){
                    newRefMessage.child(temp).setValue(data);
                }
                Teller = Teller + 1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                String temp = String.valueOf(databaseError);
                Toast.makeText(LoginActivity.this, temp, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private  void loginUser(FirebaseUser userGoogle)
    {
        if (userGoogle != null){
            String personName = userGoogle.getDisplayName();
            String personEmail = userGoogle.getEmail();
            Uri personPhoto = userGoogle.getPhotoUrl();
            String currentUser = userGoogle.getUid();
            newRef.child(currentUser)
                    .child("name").setValue(personName);
            newRef.child(currentUser)
                    .child("uid").setValue(currentUser);
            newRef.child(currentUser)
                    .child("mail").setValue(personEmail);
            Toast.makeText(LoginActivity.this, personName + personEmail, Toast.LENGTH_SHORT).show();

        }


    }
    private void updateUI(){
        btnSignOut.setVisibility(View.VISIBLE);
    }
}