package com.example.myapplication;

import static com.example.myapplication.LogIn.gebruikerCode;
import static com.example.myapplication.SignUp.commitQuery;
import static com.example.myapplication.addplant2.loadBitmap;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Home extends AppCompatActivity {

    public static String ip = "192.168.2.12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadPlants();
        TextView textName = findViewById(R.id.textViewName);
        String query = "select Voornaam from Gebruiker Where Gebruikercode = '" + gebruikerCode + "'";
        textName.setText(commitQuery(query).trim() + "'s Oasis");
    }

    public static String[] plantNames = new String[6];



    private void loadPlants(){
        ImageView imageView1 = findViewById(R.id.imageViewP1);
        ImageView imageView2 = findViewById(R.id.imageViewP2);
        ImageView imageView3 = findViewById(R.id.imageViewP3);
        ImageView imageView4 = findViewById(R.id.imageViewP4);
        ImageView imageView5 = findViewById(R.id.imageViewP5);
        ImageView imageView6 = findViewById(R.id.imageViewP6);
        TextView text1 = findViewById(R.id.textViewP1);
        TextView text2 = findViewById(R.id.textViewP2);
        TextView text3 = findViewById(R.id.textViewP3);
        TextView text4 = findViewById(R.id.textViewP4);
        TextView text5 = findViewById(R.id.textViewP5);
        TextView text6 = findViewById(R.id.textViewP6);
        loadPlantnames();
        imageView1.setImageBitmap(loadBitmap(getFilePath(plantNames[0]).getPath()));
        text1.setText(plantNames[0]);
        if (plantNames[1] == null) return;
        imageView2.setImageBitmap(loadBitmap(getFilePath(plantNames[1]).getPath()));
        text2.setText(plantNames[1]);
        if (plantNames[2] == null) return;
        imageView3.setImageBitmap(loadBitmap(getFilePath(plantNames[2]).getPath()));
        text3.setText(plantNames[2]);
        if (plantNames[3] == null) return;
        imageView4.setImageBitmap(loadBitmap(getFilePath(plantNames[3]).getPath()));
        text4.setText(plantNames[3]);
        if (plantNames[4] == null) return;
        imageView5.setImageBitmap(loadBitmap(getFilePath(plantNames[4]).getPath()));
        text5.setText(plantNames[4]);
        if (plantNames[5] == null) return;
        imageView6.setImageBitmap(loadBitmap(getFilePath(plantNames[5]).getPath()));
        text6.setText(plantNames[5]);
    }

    //For pushing query to sql and receiving data
    private void loadPlantnames(){
        Connection connect;
        String result = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                Statement st = connect.createStatement();
                String query = "select Plantnaam from Plant where Gebruikercode = '" + gebruikerCode + "' order by PrivatePlantnummer";
                ResultSet rs = st.executeQuery(query);
                int i = 0;
                while (rs.next()) {
                    //puts query output in string
                    if (rs.getString(1).isEmpty()) return;
                    plantNames[i] = rs.getString(1);
                    i++;
                }
            }
        } catch (Exception ex) {
            Log.e("Error 001", ex.getMessage());
        }
    }

    private File getFilePath(String fileName){
        //gets directory for foto's
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File file = new File(fotoDirectory, fileName + ".png");
        return file;
    }

    public void buttonGotoHome(View view) {
    }

    public void buttonGotoWateringcycle(View view) {
        Intent Singinpage = new Intent(Home.this, WateringCycle.class);
        startActivity(Singinpage);
    }

    public void gotoAddplant(View view) {
        Intent Singinpage = new Intent(Home.this, addplant2.class);
        startActivity(Singinpage);
    }

    public void gotoPremium(View view) {
        Intent Singinpage = new Intent(Home.this, Premium.class);
        startActivity(Singinpage);
    }
}