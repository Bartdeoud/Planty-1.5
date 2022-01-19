package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Premium extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
    }
    public void buttonGotoHome4(View view) {
        Intent Singinpage = new Intent(Premium.this, Home.class);
        startActivity(Singinpage);
    }

    public void buttonGotoWateringcycle4(View view) {
        Intent Singinpage = new Intent(Premium.this, WateringCycle.class);
        startActivity(Singinpage);
    }

    public void gotoAddplant4(View view) {
        Intent Singinpage = new Intent(Premium.this, addplant2.class);
        startActivity(Singinpage);
    }

    public void gotoPremium4(View view) {
    }
}