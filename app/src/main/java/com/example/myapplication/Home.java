package com.example.myapplication;

import static com.example.myapplication.outsideVariables.commitQuery;
import static com.example.myapplication.addplant2.loadBitmap;
import static com.example.myapplication.outsideVariables.gebruikerCode;
import static com.example.myapplication.outsideVariables.plantNames;
import static com.example.myapplication.userData.loadBitmapP;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class Home extends AppCompatActivity {

    private String plantToRemove = "";
    private int dialogInterface = 0, timesHomeLoaded = 0;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private ArrayList<TextView> textViews = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loadPlants();
        TextView textName = findViewById(R.id.textViewName);
        String query = "select Voornaam from Gebruiker Where Gebruikercode = '" + gebruikerCode + "'";
        textName.setText(commitQuery(query).trim() + "'s Oasis");
        ImageView view = findViewById(R.id.imageProfile);
        Bitmap bitmap = loadBitmapP(getFilePathP("P" + gebruikerCode + "ProfilePicture").getPath());
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadPlants();
        TextView textName = findViewById(R.id.textViewName);
        String query = "select Voornaam from Gebruiker Where Gebruikercode = '" + gebruikerCode + "'";
        textName.setText(commitQuery(query).trim() + "'s Oasis");
        ImageView view = findViewById(R.id.imageProfile);
        Bitmap bitmap = loadBitmapP(getFilePathP("P" + gebruikerCode + "ProfilePicture").getPath());
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
        }
    }

    private void loadPlants() {
        imageViews = new ArrayList<>(Arrays.asList(findViewById(R.id.imageViewP1), findViewById(R.id.imageViewP2), findViewById(R.id.imageViewP3), findViewById(R.id.imageViewP4), findViewById(R.id.imageViewP5), findViewById(R.id.imageViewP6)));
        textViews = new ArrayList<>(Arrays.asList(findViewById(R.id.textViewP1), findViewById(R.id.textViewP2), findViewById(R.id.textViewP3), findViewById(R.id.textViewP4), findViewById(R.id.textViewP5), findViewById(R.id.textViewP6)));

        for (TextView textView: textViews){
            textView.setText("");
        }

        loadPlantnames();
        if (!(plantNames[0] == null)) {
            boolean allLoaded = true;
            for (int i = 0; i <= 5; i++) {
                if (getFilePath(plantNames[i]).exists()) {
                    imageViews.get(i).setImageBitmap(loadBitmap(getFilePath(plantNames[i]).getPath()));
                } else if (plantNames[i] != null){
                    allLoaded = false;
                }
                if (plantNames[i] == null) {
                    imageViews.get(i).setImageBitmap(null);
                }
                textViews.get(i).setText(plantNames[i]);
            }
            if (!allLoaded) {
                Toast.makeText(Home.this, "Could not load all images", Toast.LENGTH_SHORT).show();
            }
        } else if (!(timesHomeLoaded >= 1)) {
            //makes sure it is only asked one time
            timesHomeLoaded++;
            //creates pop up message yes/no
            dialogInterface = 1;
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setMessage("You do not have any plants stored.\nDo you want to add a plant?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            for (ImageView imageView : imageViews) {
                imageView.setImageBitmap(null);
            }
        }
    }

    //For pushing query to sql and receiving data
    private void loadPlantnames() {
        Arrays.fill(plantNames, null);
        Connection connect;
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
            } else {
                Toast.makeText(Home.this, "Database server is unreachable", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e("Error: ", ex.getMessage());
            Toast.makeText(Home.this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
        if (dialogInterface == 0) {
            if (which == DialogInterface.BUTTON_POSITIVE) {//Yes button clicked
                String query = "delete from Plant where Gebruikercode = '" + gebruikerCode + "' and Plantnaam = '" + plantToRemove + "'";
                commitQuery(query);
                File file = getFilePath(gebruikerCode + plantToRemove);
                file.delete();
                loadPlants();
            }
        } else {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                Intent intent = new Intent(Home.this, addplant2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }
    };

    private File getFilePathP(String fileName){
        //gets directory for foto's
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(fotoDirectory, fileName + ".png");
    }

    public void removePlant(View view) {
        //creates pop up message yes/no
        dialogInterface = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        builder.setMessage("Delete plant?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        //saves the name of plant to remove
        plantToRemove = textViews.get(imageViews.indexOf(view)).getText().toString();
    }

    private File getFilePath(String fileName) {
        //gets directory for foto's
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(fotoDirectory, gebruikerCode + fileName + ".png");
    }

    public void buttonGotoHome(View view) {
    }

    public void buttonGotoWateringcycle(View view) {
        Intent intent = new Intent(Home.this, WateringCycle.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("Load", "true");
        startActivity(intent);
    }

    public void gotoAddplant(View view) {
        Intent intent = new Intent(Home.this, addplant2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void gotoPremium(View view) {
        Intent intent = new Intent(Home.this, Premium.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void userData(View view) {
        Intent intent = new Intent(Home.this, userData.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}