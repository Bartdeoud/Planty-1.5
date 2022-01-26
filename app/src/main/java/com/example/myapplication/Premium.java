package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Premium extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);
    }
    public void buttonGotoHome4(View view) {
        Intent intent = new Intent(Premium.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void buttonGotoWateringcycle4(View view) {
        Intent intent = new Intent(Premium.this, WateringCycle.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void gotoAddplant4(View view) {
        Intent intent = new Intent(Premium.this, addplant2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void gotoPremium4(View view) {
    }

    public void PremiumService(View view) {
        Toast.makeText(Premium.this, "Unable to reach service", Toast.LENGTH_SHORT).show();
    }
}