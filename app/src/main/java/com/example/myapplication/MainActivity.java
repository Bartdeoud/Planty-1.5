package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
//extra
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //extra
    Connection connect;
    String ConnectionResult = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize / assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomnavigationview);

        //Set home selected
        bottomNavigationView.setSelectedItemId(R.id.mainActivity);

        //set click listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                return false;
            }
        });
    }

    public void GetTechFromSQL(View view) {
        TextView tx1 = (TextView) findViewById(R.id.textView2);
        TextView tx2 = (TextView) findViewById(R.id.textView9);

        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                String query = "SELECT * FROM Gebruiker";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    tx1.setText(rs.getString(1));
                    tx2.setText(rs.getString(2));
                }
            } else {
                ConnectionResult = "Check Connection";
                tx1.setText("failed");
            }
        } catch (Exception ex) {
            Log.e("Error ", ex.getMessage());
        }

    }
}