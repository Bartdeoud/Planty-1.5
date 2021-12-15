package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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


                if(checkList()){
                    int Gebruikersnummer = 0;
                    Intent loginpage = new Intent(SignUp.this, LogIn.class);
                    startActivity(loginpage);

                    try {
                        ConnectionHelper connectionHelper = new ConnectionHelper();
                        connect = connectionHelper.Connectionclass();
                        if (connect != null) {
                            //query statement
                            String query = "SELECT Gebruikercode FROM Gebruiker";
                            Statement st = connect.createStatement();
                            ResultSet rs = st.executeQuery(query);
                            while (rs.next()){
                                Gebruikersnummer = Integer.parseInt(rs.getString(1) + 1);
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
                            String query = "INSERT INTO Gebruiker (Gebruikercode ,Voornaam ,Achternaam ,Telefoonnummer ,Email ,Wachtwoord ,Bedrijf ,Adres) VALUES (" + Gebruikersnummer + ", " + firstName + ", " + lastName + ", " + telephone + ", " + email + ", " + password + ", " + address + ")";
                            Statement st = connect.createStatement();
                            ResultSet rs = st.executeQuery(query);
                        } else {
                            ConnectionResult = "Check Connection";
                        }
                    } catch (Exception ex) {
                        Log.e("Error ", ex.getMessage());
                    }
                }

            }
        });
    }
    public static boolean checkList(){
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
            }

        //check if password are the same
        if(password.equals(passwordConfirm) && !password.toString().isEmpty()){
            same = true;
        }
        //checks if evryting is a go
        if(same && filled){
            authenticated = true;
        }
        return authenticated;
    }
}