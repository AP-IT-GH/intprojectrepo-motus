package com.example.motus;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.android.material.navigation.NavigationView;

public class NavigationMenu extends AppCompatActivity {
    protected DrawerLayout drawer;
    NavigationView nv;
    Switch mAssistantSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_menu);

        SharedPreferences switchPreference = getSharedPreferences("switch", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        nv = findViewById(R.id.nav_view);
        nv.getMenu().findItem(R.id.connect).setActionView(new Switch(this));
        mAssistantSwitch = ((Switch) nv.getMenu().findItem(R.id.connect).getActionView());
        mAssistantSwitch.setChecked(switchPreference.getBoolean("switchState",true));
        mAssistantSwitch.setTextColor(getResources().getColor(R.color.startYellow));
        mAssistantSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences switchPreference = getSharedPreferences("switch", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = switchPreference.edit();
                editor.putBoolean("switchState",isChecked);
                editor.apply();
                editor.commit();
            }
        });
        nv.getMenu().findItem(R.id.profile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowInfo();
                return false;
            }
        });;
        nv.getMenu().findItem(R.id.results).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowResults();
                return false;
            }
        });;
        nv.getMenu().findItem(R.id.home).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowHome();
                return false;
            }
        });;
        nv.getMenu().findItem(R.id.injuries).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowInjuries();
                return false;
            }
        });;
        nv.getMenu().findItem(R.id.measure).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShowMeasure();
                return false;
            }
        });;
        nv.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Logout();
                return false;
            }
        });;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void ShowInfo(){
        Intent intent = new Intent(this, ProfileScreenActivity.class);
        startActivity(intent);
    }
    public void ShowResults(){
        Intent intent = new Intent(this, GetDataActivity.class);
        startActivity(intent);
    }
    public void ShowHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
    public void ShowMeasure(){
        Intent intent = new Intent(this, MeasuringActivity.class);
        startActivity(intent);
    }
    public void ShowInjuries(){
        Intent intent = new Intent(this, InjuryActivity.class);
        startActivity(intent);
    }
    public void Logout(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
