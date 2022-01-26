package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize / assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationview);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.mainActivity);


        //set click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> false);
    }
}