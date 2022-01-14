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
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class SignUp extends AppCompatActivity {

    public EditText firstName, lastName, address, company, telephone, email, password, passwordConfirm;
    public String Bedrijfscode = "";

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
                    String query1 = "SELECT Gebruikercode FROM Gebruiker ORDER BY Gebruikercode";
                    String Gebruikerscode = commitQuery(query1);
                    Gebruikersnummer = Integer.parseInt(Gebruikerscode.substring(0, Gebruikerscode.length() - 2)) + 1;
                //not using commitQuery() becous INSERT
                    try {
                        ConnectionHelper connectionHelper = new ConnectionHelper();
                        connect = connectionHelper.Connectionclass();
                        if (connect != null) {
                            //query statement
                            Connection con = connectionHelper.Connectionclass();
                            String query2 = "INSERT INTO Gebruiker (Gebruikercode, Voornaam, Achternaam, Telefoonnummer, Email, Bedrijf, Adres, Encription_key) VALUES ('" + Gebruikersnummer + "', '" + firstName.getText() + "', '" + lastName.getText() + "', '" + telephone.getText() + "', '" + email.getText() + "', '" + Bedrijfscode + "', '" + address.getText() + "', '" + GetEnqKey() +"')";
                            System.out.println(query2);
                            PreparedStatement prepsInsertProduct = con.prepareStatement(query2);
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

    public String GetEnqKey(){
        EditText email2 = email;
        EditText password2 = password;
        String email = email2.getText().toString();
        String password = password2.getText().toString();

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(email);
        String encrypted = encryptor.encrypt(password);
        return encrypted;
    }

    //Main validator
    public boolean SignInValidator(){
        if(!FilledIn()){
            return false;
        }
        //if (EmailAlredyExist()){
        //    return false;
        //}
        if(!bedrijfExist()){
            return false;
        }
        return true;
    }

    //checks if filled in bedijf exist & put Bedrijfcode in Bedrijfcode
    public boolean bedrijfExist(){
        String query1 = "SELECT COUNT(Naam) FROM Bedrijf WHERE Naam = '" + company.getText().toString() + "';";
        String companyCount = commitQuery(query1);
        if(Integer.parseInt(companyCount) > 0){
            String query2 = "SELECT Bedrijfcode FROM Bedrijf WHERE Naam = '" + company.getText().toString() + "';";
            Bedrijfscode = commitQuery(query2);
            return true;
        }
        company.setError("Could not find company");
        return false;
    }

    //Checks if Emal exist in database
    public boolean EmailAlredyExist(){
        EditText inMailET = (EditText) findViewById(R.id.input_Email);
        String Inmail = inMailET.getText().toString();
        String query = "SELECT COUNT(Email) FROM Gebruiker WHERE Email = '" + Inmail + "';";
        String CountEmails = commitQuery(query);
        if(Integer.parseInt(CountEmails) > 0){
            inMailET.setError("Email alredy exist");
            return true;
        }
        return false;
    }

    //For pushing query to sql and receiving data
    public String commitQuery(String query){
        Connection connect;
        String ConnectionResult = "";
        String result = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //puts query output in string
                    result = rs.getString(1).toString();
                }
            } else {
                ConnectionResult = "Check Connection";
            }
        } catch (Exception ex) {
            Log.e("Error 001", ex.getMessage());
        }
        return result;
    }

    //checks if every box is filled in
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

        //checks if everything is a go
        if(same && filled){
            authenticated = true;
        }
        return authenticated;
    }
}