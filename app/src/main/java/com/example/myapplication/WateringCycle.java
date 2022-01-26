package com.example.myapplication;

import static com.example.myapplication.Home.gebruikerCode;
import static com.example.myapplication.Home.plantNames;
import static com.example.myapplication.addplant2.loadBitmap;
import static com.example.myapplication.addplant2.plantName;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class WateringCycle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering_cycle);
        loadPlants();
    }

    private void loadPlants(){
        loadPlantValues();
        ArrayList<CircleImageView> circleImageViews = new ArrayList<>(Arrays.asList(findViewById(R.id.profile_imageP1),findViewById(R.id.profile_imageP2),findViewById(R.id.profile_imageP3),findViewById(R.id.profile_imageP4),findViewById(R.id.profile_imageP5),findViewById(R.id.profile_imageP6)));
        ArrayList<TextView> textViews = new ArrayList<>(Arrays.asList(findViewById(R.id.textViewP21),findViewById(R.id.textViewP22),findViewById(R.id.textViewP23),findViewById(R.id.textViewP24),findViewById(R.id.textViewP25),findViewById(R.id.textViewP26)));
        ArrayList<TextView> textViews1 = new ArrayList<>(Arrays.asList(findViewById(R.id.textViewPW1),findViewById(R.id.textViewPW2),findViewById(R.id.textViewPW3),findViewById(R.id.textViewPW4),findViewById(R.id.textViewPW5),findViewById(R.id.textViewPW6)));

        boolean allLoaded = true;
        for(int i = 0; i <= 5; i++){
            if(getFilePath(plantNames[i]).exists()) {
                circleImageViews.get(i).setImageBitmap(loadBitmap(getFilePath(plantNames[i]).getPath()));
            }
            else {
                allLoaded = false;
            }
            if(plantNames[i] == null){
                circleImageViews.get(i).setImageBitmap(null);
                textViews1.get(i).setText("");
            }
            textViews.get(i).setText(plantNames[i]);
        }
        if(!allLoaded){
            Toast.makeText(WateringCycle.this, "Could not load all images", Toast.LENGTH_SHORT).show();
        }
    }

    private File getFilePath(String fileName){
        //gets directory for foto's
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(fotoDirectory, gebruikerCode + fileName + ".png");
        return file;
    }

    @SuppressLint("SetTextI18n")
    public void loadPlantValues(){
        ConnectionClass connectionClass;
        String result = "";

        connectionClass = new ConnectionClass();
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {
                Toast.makeText(WateringCycle.this, "Database server is unreachable", Toast.LENGTH_SHORT).show();
            } else {
                String query = "SELECT Water_Level FROM `planten_water`";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    //puts query output in string
                    result = rs.getString(1);
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        TextView textViewPW1 = findViewById(R.id.textViewPW1);
        if(Integer.parseInt(result) > 200){
            textViewPW1.setText("Doesn't need water");
        } else {
            textViewPW1.setText("Needs water");
        }
    }

    public void buttonGotoHome2(View view) {
        Intent intent = new Intent(WateringCycle.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void buttonGotoWateringcycle2(View view) {
    }

    public void gotoAddplant2(View view) {
        Intent intent = new Intent(WateringCycle.this, addplant2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void gotoPremium2(View view) {
        Intent intent = new Intent(WateringCycle.this, Premium.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void reloadPage(View view) {
        loadPlantValues();
        Toast.makeText(WateringCycle.this, "Realoding data", Toast.LENGTH_SHORT).show();
    }
}