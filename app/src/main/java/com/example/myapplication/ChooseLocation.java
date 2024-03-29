package com.example.myapplication;

import static com.example.myapplication.outsideVariables.commitQuery;
import static com.example.myapplication.addplant2.plantName;
import static com.example.myapplication.outsideVariables.gebruikerCode;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class ChooseLocation extends AppCompatActivity {

    public static String plantLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        final ImageButton button = findViewById(R.id.ibLivingRoom);
        button.setOnClickListener(v -> {
            plantLocation = "LivingRoom";
            goToNextScreen();
        });
        final ImageButton button2 = findViewById(R.id.ibPatio);
        button2.setOnClickListener(v -> {
            plantLocation = "Patio";
            goToNextScreen();
        });
        final ImageButton button3 = findViewById(R.id.ibOffice);
        button3.setOnClickListener(v -> {
            plantLocation = "Office";
            goToNextScreen();
        });
        final ImageButton button4 = findViewById(R.id.ibBedroom);
        button4.setOnClickListener(v -> {
            plantLocation = "Bedroom";
            goToNextScreen();
        });
        final ImageButton button5 = findViewById(R.id.ibKitchen);
        button5.setOnClickListener(v -> {
            plantLocation = "Kitchen";
            goToNextScreen();
        });
        final ImageButton button6 = findViewById(R.id.ibBalcony);
        button6.setOnClickListener(v -> {
            plantLocation = "Balcony";
            goToNextScreen();
        });
    }

    private void goToNextScreen(){
        Date d = new Date();
        int date = d.getYear() + d.getMonth() + d.getDay();
        String query = "select Plantnummer from Plant order by Plantnummer";
        String plantnummerst = commitQuery(query);
        int plantnummer = Integer.parseInt(plantnummerst.substring(0, plantnummerst.length() - 2)) + 1;
        String query2 = "select PrivatePlantnummer from Plant Where Gebruikercode = '" + gebruikerCode + "' order by PrivatePlantnummer";
        int PrivatePlantnummer = 1;
        try {
            PrivatePlantnummer = Integer.parseInt(commitQuery(query2)) + 1;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        String query3 = "insert into Plant ([Gebruikercode], [Plantnummer], [Plantnaam], [Toevoeg_Datum], [Microbit], [Locatie], [PrivatePlantnummer]) values ('" + gebruikerCode + "', '" + plantnummer + "', '" + plantName + "', '" + date + "', '1', '" + plantLocation + "', '" + PrivatePlantnummer +"')";
        commitQuery(query3);
        Intent mainActivityintent = new Intent(ChooseLocation.this, Home.class);
        startActivity(mainActivityintent);
        finish();
    }
}