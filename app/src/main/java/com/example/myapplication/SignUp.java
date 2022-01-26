package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignUp extends AppCompatActivity {

    public EditText firstName, lastName, address, company, telephone, email, password, passwordConfirm;
    public String Bedrijfscode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Button button = findViewById(R.id.btnMakeAccount);
        button.setOnClickListener(v -> {
            Connection connect;
            firstName =  findViewById(R.id.textProfile1);
            lastName =  findViewById(R.id.textProfile2);
            address =  findViewById(R.id.textProfile3);
            company =  findViewById(R.id.textProfile4);
            telephone =  findViewById(R.id.textProfile5);
            email =  findViewById(R.id.textProfile6);
            password =  findViewById(R.id.textProfile7);
            passwordConfirm =  findViewById(R.id.input_ConfPassword);

            if(SignInValidator()){
                int Gebruikersnummer;
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
                        Toast.makeText(SignUp.this, "Could not connect with database", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.e("Error ", ex.getMessage());
                    Toast.makeText(SignUp.this, "Error " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(SignUp.this, LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
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
        return encryptor.encrypt(password);
    }

    //Main validator
    public boolean SignInValidator(){
        if(!FilledIn()){
            return false;
        }
        if (EmailAlredyExist()){
            return false;
        }
        bedrijfExist();

        return true;
    }

    //checks if filled in bedijf exist & put Bedrijfcode in Bedrijfcode
    public void bedrijfExist(){
        String query1 = "SELECT COUNT(Naam) FROM Bedrijf WHERE Naam = '" + company.getText().toString() + "';";
        String companyCount = commitQuery(query1);
        if(Integer.parseInt(companyCount) > 0){
            String query2 = "SELECT Bedrijfcode FROM Bedrijf WHERE Naam = '" + company.getText().toString() + "';";
            Bedrijfscode = commitQuery(query2);
            return;
        }
        Bedrijfscode = "1";
    }

    //Checks if Emal exist in database
    public boolean EmailAlredyExist(){
        EditText inMailET = findViewById(R.id.textProfile6);
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
    public static String commitQuery(String query){
        Connection connect;
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
                    result = rs.getString(1);
                }
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