package com.example.myapplication;

import static com.example.myapplication.Home.plantNames;
import static com.example.myapplication.addplant2.loadBitmap;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.hdodenhof.circleimageview.CircleImageView;

public class WateringCycle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering_cycle);
        loadPlants();
        loadPlantValues();
    }

    private void loadPlants(){
        CircleImageView circleImageViewP1 = findViewById(R.id.profile_imageP1);
        CircleImageView circleImageViewP2 = findViewById(R.id.profile_imageP2);
        CircleImageView circleImageViewP3 = findViewById(R.id.profile_imageP3);
        CircleImageView circleImageViewP4 = findViewById(R.id.profile_imageP4);
        CircleImageView circleImageViewP5 = findViewById(R.id.profile_imageP5);
        CircleImageView circleImageViewP6 = findViewById(R.id.profile_imageP6);
        TextView textViewP21 = findViewById(R.id.textViewP21);
        TextView textViewP22 = findViewById(R.id.textViewP22);
        TextView textViewP23 = findViewById(R.id.textViewP23);
        TextView textViewP24 = findViewById(R.id.textViewP24);
        TextView textViewP25 = findViewById(R.id.textViewP25);
        TextView textViewP26 = findViewById(R.id.textViewP26);
        circleImageViewP1.setImageBitmap(loadBitmap(getFilePath(plantNames[0]).getPath()));
        textViewP21.setText(plantNames[0]);
        if (plantNames[1] == null) return;
        circleImageViewP2.setImageBitmap(loadBitmap(getFilePath(plantNames[1]).getPath()));
        textViewP22.setText(plantNames[1]);
        if (plantNames[2] == null) return;
        circleImageViewP3.setImageBitmap(loadBitmap(getFilePath(plantNames[2]).getPath()));
        textViewP23.setText(plantNames[2]);
        if (plantNames[3] == null) return;
        circleImageViewP4.setImageBitmap(loadBitmap(getFilePath(plantNames[3]).getPath()));
        textViewP24.setText(plantNames[3]);
        if (plantNames[4] == null) return;
        circleImageViewP5.setImageBitmap(loadBitmap(getFilePath(plantNames[4]).getPath()));
        textViewP25.setText(plantNames[4]);
        if (plantNames[5] == null) return;
        circleImageViewP6.setImageBitmap(loadBitmap(getFilePath(plantNames[5]).getPath()));
        textViewP26.setText(plantNames[5]);
    }

    private File getFilePath(String fileName){
        //gets directory for foto's
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(fotoDirectory, fileName + ".png");
        return file;
    }

    @SuppressLint("SetTextI18n")
    public void loadPlantValues(){
        ConnectionClass connectionClass;
        String result = "";
        TextView textView;

        connectionClass = new ConnectionClass();
        try {
            Connection con = connectionClass.CONN();
            if (con == null) {

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

    public void reloadPage(View view) {
        loadPlants();
        loadPlantValues();
    }
}