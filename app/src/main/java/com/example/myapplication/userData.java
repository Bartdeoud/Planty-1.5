package com.example.myapplication;

import static com.example.myapplication.SignUp.commitQuery;
import static com.example.myapplication.outsideVariables.gebruikerCode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class userData extends AppCompatActivity {

    private String[] string = new String[6];
    private ArrayList<TextView> textViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        loadDataFromDB();
        loadData();
    }

    private void loadData(){
        textViews = new ArrayList<>(Arrays.asList(findViewById(R.id.textProfile1),findViewById(R.id.textProfile2),findViewById(R.id.textProfile3),findViewById(R.id.textProfile4),findViewById(R.id.textProfile5),findViewById(R.id.textProfile6)));
        int i = 0;
        for(String string: string){
            textViews.get(i).setText(string);
            i++;
        }
    }

    private void loadDataFromDB(){
        Connection connect;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                Statement st = connect.createStatement();
                String query = "select voornaam, Achternaam, Adres, Bedrijf, Telefoonnummer, Email from Gebruiker where Gebruikercode = '" + gebruikerCode + "'";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //puts query output in string
                    for(int i = 0; i <= 5; i++) {
                        string[i] = rs.getString(i + 1).trim();
                    }
                }
                query = "select Naam from Bedrijf Where Bedrijfcode = '" + string[3].substring(0, string[3].length() - 2) + "'";
                string[3] = commitQuery(query);
            } else {
                Toast.makeText(userData.this, "Database server is unreachable", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e("Error: ", ex.getMessage());
            Toast.makeText(userData.this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void changeProfile(View view) {
        textViews = new ArrayList<>(Arrays.asList(findViewById(R.id.textProfile1),findViewById(R.id.textProfile2),findViewById(R.id.textProfile3),findViewById(R.id.textProfile4),findViewById(R.id.textProfile5),findViewById(R.id.textProfile6)));
        int i = 0;
        for(TextView textView: textViews){
            string[i] = textView.getText().toString().trim();
            i++;
        }
        Connection connect;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                Connection con = connectionHelper.Connectionclass();
                @SuppressLint("DefaultLocale") String query2 = String.format("update Gebruiker set voornaam = '%s', Achternaam = '%s', Adres = '%s', Bedrijf = '%s', Telefoonnummer = '%s', Email = '%s' where Gebruikercode = '%s'",
                        string[0],
                        string[1],
                        string[2],
                        getBedrijf(string[3]),
                        string[4],
                        string[5],
                        gebruikerCode);
                System.out.println(query2);
                PreparedStatement prepsInsertProduct = con.prepareStatement(query2);
                prepsInsertProduct.execute();
                Intent intent = new Intent(userData.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(userData.this, "Could not connect to database", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(userData.this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getBedrijf(String bedrijf){
        String query1 = "SELECT COUNT(Naam) FROM Bedrijf WHERE Naam = '" + bedrijf + "';";
        String companyCount = commitQuery(query1);
        if(Integer.parseInt(companyCount) > 0){
            String query2 = "SELECT Bedrijfcode FROM Bedrijf WHERE Naam = '" + bedrijf + "';";
            return commitQuery(query2);
        }
        return "1";
    }
}