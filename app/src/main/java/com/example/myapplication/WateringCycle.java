package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WateringCycle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering_cycle);
    }
    public void buttonGotoHome2(View view) {
        Intent Singinpage = new Intent(WateringCycle.this, Home.class);
        startActivity(Singinpage);
    }

    public void buttonGotoWateringcycle2(View view) {
    }

    public void gotoAddplant2(View view) {
        Intent Singinpage = new Intent(WateringCycle.this, addplant2.class);
        startActivity(Singinpage);
    }

    public void gotoPremium2(View view) {
        Intent Singinpage = new Intent(WateringCycle.this, Premium.class);
        startActivity(Singinpage);
    }
}