package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class SignUp extends AppCompatActivity {

    public static EditText firstName;
    public static EditText lastName;
    public static EditText address;
    public static EditText company;
    public static EditText telephone;
    public static EditText email;
    public static EditText password;
    public static EditText passwordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Button button = findViewById(R.id.btnMakeAccount);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Connection connect;
                String ConnectionResult = "";
                firstName = (EditText) findViewById(R.id.input_FirstName);
                lastName = (EditText) findViewById(R.id.input_LastName);
                address = (EditText) findViewById(R.id.input_Address);
                company = (EditText) findViewById(R.id.input_Company);
                telephone = (EditText) findViewById(R.id.input_Telephone);
                email = (EditText) findViewById(R.id.input_Email);
                password = (EditText) findViewById(R.id.input_Password);
                passwordConfirm = (EditText) findViewById(R.id.input_ConfPassword);


                if(SignInValidator()){
                    int Gebruikersnummer = 0;

                    try {
                        ConnectionHelper connectionHelper = new ConnectionHelper();
                        connect = connectionHelper.Connectionclass();
                        if (connect != null) {
                            //query statement
                            String query = "SELECT Gebruikercode FROM Gebruiker ORDER BY Gebruikercode";
                            Statement st = connect.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()){
                                Gebruikersnummer = Integer.parseInt(rs.getString(1).substring(0, rs.getString(1).length() - 2)) + 1;
                            }
                        } else {
                            ConnectionResult = "Check Connection";
                        }
                    } catch (Exception ex) {
                        Log.e("Error ", ex.getMessage());
                    }

                    try {
                        ConnectionHelper connectionHelper = new ConnectionHelper();
                        connect = connectionHelper.Connectionclass();
                        if (connect != null) {
                            //query statement
                            Connection con = connectionHelper.Connectionclass();
                            String query = "INSERT INTO Gebruiker (Gebruikercode, Voornaam, Achternaam, Telefoonnummer, Email, Wachtwoord, Bedrijf, Adres) VALUES ('" + Gebruikersnummer + "', '" + firstName.getText() + "', '" + lastName.getText() + "', '" + telephone.getText() + "', '" + email.getText() + "', '" + password.getText() + "', '" + "1" + "', '" + address.getText() + "')";
                            System.out.println(query);
                            PreparedStatement prepsInsertProduct = con.prepareStatement(query);
                            prepsInsertProduct.execute();
                        } else {
                            ConnectionResult = "Check Connection";
                        }
                    } catch (Exception ex) {
                        Log.e("Error ", ex.getMessage());
                    }
                    Intent loginpage = new Intent(SignUp.this, LogIn.class);
                    startActivity(loginpage);
                }
            }
        });
    }

    public boolean SignInValidator(){
        if(!FilledIn()){
            return false;
        }
        if (EmailAlredyExist()){
            return false;
        }
        //if(!bedrijfExist()){
        //    return false;
        //}
        return true;
    }

    public String Bedrijfscode = "";

    public boolean bedrijfExist(){
        Connection connect;
        String ConnectionResult = "";
        EditText inBedrijfET = (EditText) findViewById(R.id.input_Company);
        String inBedrijf = inBedrijfET.getText().toString();
        String CountEmails = "";
        //counts the amout of same emails in database
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                String query = "SELECT COUNT(Naam) FROM Gebruiker WHERE Naam = '" + inBedrijf + "';";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //puts query output in string
                    CountEmails = rs.getString(1).toString();
                }
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error ", ex.getMessage());
        }
        if(Integer.parseInt(CountEmails) == 0){
            inBedrijfET.setError("Company does not exist");
            return false;
        } else {
            String companyNumber = "";
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.Connectionclass();
                if (connect != null) {
                    //query statement
                    String query = "SELECT Bedrijfcode FROM Bedrijf WHERE Naam = '" + inBedrijf + "';";
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        //puts query output in string
                        Bedrijfscode = rs.getString(1).toString();
                        return true;
                    }
                } else {
                    ConnectionResult = "Check Connection";
                }
            } catch (Exception ex) {
                Log.e("Error ", ex.getMessage());
            }
        }
        return false;
    }

    public boolean EmailAlredyExist(){
        Connection connect;
        String ConnectionResult = "";
        EditText inMailET = (EditText) findViewById(R.id.input_Email);
        String Inmail = inMailET.getText().toString();
        String CountEmails = "";
        //counts the amout of same emails in database
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                String query = "SELECT COUNT(Email) FROM Gebruiker WHERE Email = '" + Inmail + "';";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //puts query output in string
                    CountEmails = rs.getString(1).toString();
                }
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error ", ex.getMessage());
        }
        if(Integer.parseInt(CountEmails) > 0){
            inMailET.setError("Email alredy exist");
            return true;
        }
        return false;
    }

    public boolean FilledIn(){
        boolean authenticated = false;
        boolean filled = true;
        boolean same = false;
        //check if empty
            if(TextUtils.isEmpty(firstName.getText())){
                firstName.setError("Empty");
                filled = false;
            }
            if(TextUtils.isEmpty(lastName.getText())){
                lastName.setError("Empty");
                filled = false;
            }
            if(TextUtils.isEmpty(email.getText())){
                email.setError("Empty");
                filled = false;
            }
            if(TextUtils.isEmpty(password.getText())){
                password.setError("Empty");
                filled = false;
            }
            if(TextUtils.isEmpty(passwordConfirm.getText())){
                passwordConfirm.setError("Empty");
                filled = false;
            } else {
                //check if password are the same
                if(password.getText().toString().equals(passwordConfirm.getText().toString())){
                    same = true;
                } else {
                    passwordConfirm.setError("Password not the same");
                }
            }

        //checks if evryting is a go
        if(same && filled){
            authenticated = true;
        }
        return authenticated;
    }
}