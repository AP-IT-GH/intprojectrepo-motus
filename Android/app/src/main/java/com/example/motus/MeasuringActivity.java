package com.example.motus;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class MeasuringActivity extends NavigationMenu {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    boolean connected = true;
    int teller = 0;
    //55072829-bc9e-4c53-938a-74a6d4c78776 uuid voor eps32
//00001101-0000-1000-8000-00805F9B34FB

    static final UUID mUUID = UUID.fromString("55072829-bc9e-4c53-938a-74a6d4c78776");
    private ArrayList<Character> recievedata;
    private char b;
    private  String string;
    private String[] splitData;
    private ArrayList<String> Angle;
    private ArrayList<String > Time;
    private ArrayList<String> Movement;
    Data data = new Data();
    LoginActivity SendData = new LoginActivity();

    BluetoothSocket btSocket = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        System.out.println("devices:" + btAdapter.getBondedDevices());

        mAuth = FirebaseAuth.getInstance();

/// print alle verbonden toestellen
//  FC:F5:C4:54:E0:1E address
        //98:D3:31:FD:17:D6
        BluetoothDevice hc05 = btAdapter.getRemoteDevice("FC:F5:C4:54:E0:1E");
        System.out.println(hc05.getName());

        // starten van de socket
        int counter = 0;
        do {
            try {
                btSocket = hc05.createRfcommSocketToServiceRecord(mUUID);
                System.out.println("sokcet start " + btSocket);
                btSocket.connect();
                System.out.println("connection status: " + btSocket.isConnected());
            } catch (IOException e) {
                e.printStackTrace();
            }
            counter++;
        } while (!btSocket.isConnected() && counter < 3);

        if (btSocket.isConnected() == false){
            BuildDialog();
        }

        Meassure();

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_measuring, null, false);
        drawer.addView(contentView, 0);


        final Button cancelMeasurement = findViewById(R.id.stop_measuring_button);
        cancelMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_TAG, "Pressed cancel button");
                EndMeasuring(v);
            }
        });

        CheckInjuries();
        teller =0;

    }

    public void EndMeasuring(View view) {
        //Log.d(LOG_TAG, "Stopping Measurements.");
        //finish();
        ShowResults();
    }

    public void BuildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MeasuringActivity.this);

        builder.setCancelable(false);
        builder.setTitle("No connection found!");
        builder.setMessage("Please check your connection with the device and try again.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void BuildInjuryAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MeasuringActivity.this);

        builder.setCancelable(false);
        builder.setTitle("Injuries Added!");
        builder.setMessage("You have added injuries, please be careful during your exercise.\nContact the injuries tab for more information.");

        builder.setNegativeButton("Injuries", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShowInjuries();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void CheckInjuries() {
        final DatabaseReference myRef = database.getReference("injuries");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (int i=0;i<9;i++) {
                    //String name = dataSnapshot.child(Integer.toString(i)).child("name").getValue().toString();
                    if (dataSnapshot.child(Integer.toString(i)).hasChild("users")) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.child(Integer.toString(i)).child("users").getChildren()) {
                            try {
                                String user = dataSnapshot.child(Integer.toString(i)).child("users").child(mAuth.getCurrentUser().getUid()).getKey();
                                if (dataSnapshot2.getKey().equals(mAuth.getCurrentUser().getUid())) {
                                    if (teller < 1) {
                                        BuildInjuryAlert();
                                    }
                                    teller = teller + 1;
                                }
                            }catch (Exception e){

                            }
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

    public void ShowInjuries() {
        Intent intent = new Intent(this, InjuryActivity.class);
        startActivity(intent);
    }

    public void ShowResults() {
        Intent intent = new Intent(this, GetDataActivity.class);
        startActivity(intent);
    }

    public void Meassure(){
        //schrijven naar toestel
        try {
            OutputStream outputStream = btSocket.getOutputStream();
            outputStream.write(48);
            System.out.println("bericht verzonden");

        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream inputStream = null;

        try {
            inputStream = btSocket.getInputStream();

            recievedata = new ArrayList<>();
            do {

                b = (char) inputStream.read();
                recievedata.add(b);
                // System.out.println(recievedata);


                if (b == ';') {

                    string = new String((String.valueOf(recievedata)).replace(";", ""));
                    string = string.replace(",", "");
                    //importeren van klassen en dan naar databank sturen
                    recievedata.clear();
                    System.out.println(string);

                    splitData = string.split(" ");
                    System.out.println(string);

                    Movement.add(splitData[1]);
                    Angle.add(splitData[2]);
                    Time.add(splitData[3]);

                }

                //System.out.println();
                if (b == '&')
                    break;

                //breken van de loop !!!
            } while (b > 0);

            btSocket.close();
            System.out.println("loop gebroken");

            //send data to database
            sendData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData() {

        for (int i = 0; i < Angle.size(); i++) {
            data.setAngle(Angle.get(i));
            data.setTime(Time.get(i));
            data.setMovement(Movement.get(i));
            SendData.sendMessage();
        }
    }
}
