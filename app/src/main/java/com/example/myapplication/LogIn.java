package com.example.myapplication;

import static com.example.myapplication.outsideVariables.commitQuery;
import static com.example.myapplication.outsideVariables.gebruikerCode;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class LogIn extends AppCompatActivity {

    Button btLogin;
    TextView btSignin;
    public String key = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btSignin = findViewById(R.id.signInButon);
        btLogin = findViewById(R.id.Loginbtn);

        btSignin.setOnClickListener(v -> {
            Intent intent = new Intent(LogIn.this, SignUp.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        btLogin.setOnClickListener(v -> {
            if (CheckLoginData()) {

                Intent intent = new Intent(LogIn.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    public boolean CheckLoginData() {
        Connection connect;
        boolean autenticated = false;
        EditText inMailET = findViewById(R.id.inputEmailAddress);
        EditText inPasswordET = findViewById(R.id.inputPassword);

        if (CheckIfFilled(inMailET.getText().toString(), inPasswordET.getText().toString())) {
            //connection with database
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                    connect = connectionHelper.Connectionclass();
                if (connect != null) {
                    //query statement
                    String query = "SELECT Encription_key FROM Gebruiker WHERE Email = '" + inMailET.getText().toString() + "'";
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    key = "";
                    while (rs.next()) {
                        //puts query output in string
                        key = rs.getString(1);
                    }
                }
                else{
                    Toast.makeText(LogIn.this, "Database server is unreachable", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {
                Log.e("Error ", ex.getMessage());
                Toast.makeText(LogIn.this, "Error connection:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            //checks if passwords are the same
            if (Validated()) {
                autenticated = true;
                String query = "select Gebruikercode From Gebruiker Where Email = '" + inMailET.getText().toString() + "'";
                String GebruikercodeTP = commitQuery(query);
                gebruikerCode = GebruikercodeTP.substring(0, GebruikercodeTP.length() - 2);

            }
            if(autenticated) {
                inMailET.setText("");
                inPasswordET.setText("");
            }
        }
        return autenticated;
    }

    public boolean Validated(){
        try {
            EditText email2 = findViewById(R.id.inputEmailAddress);
            EditText password2 = findViewById(R.id.inputPassword);
            String email = email2.getText().toString();
            String password = password2.getText().toString();

            if(key.equals("")){
                email2.setError("Email does not exist");
                return false;
            }
            else {
                StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
                encryptor.setPassword(password);

                String decrypted = encryptor.decrypt(key);
                if (decrypted.equals(password2.getText().toString())) {
                    return true;
                }
                else{
                    password2.setText("");
                    password2.setError("Password incorrect");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            EditText password2 = findViewById(R.id.inputPassword);
            password2.setText("");
            password2.setError("Password incorrect");
        }
        return false;
    }

    //checks if all boxes al filled
    public boolean CheckIfFilled(String text1, String text2){
        boolean FilledIn = true;
        if(TextUtils.isEmpty(text1)){
            EditText inMailET = findViewById(R.id.inputEmailAddress);
            inMailET.setError("Fill in email");
            FilledIn = false;
        }
        if(TextUtils.isEmpty(text2)){
            EditText inPasswordET = findViewById(R.id.inputPassword);
            inPasswordET.setError("Fill in password");
            FilledIn = false;
        }
        return FilledIn;
    }

    public void buttonlogin(View view) {
    }

    public void ButtonSignin(View view) {
    }
}