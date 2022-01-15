package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class LogIn<inPasswordET> extends AppCompatActivity {

    Button btLogin;
    TextView btSignin;
    public String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btSignin = findViewById(R.id.signInButon);
        btLogin = findViewById(R.id.Loginbtn);

        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Singinpage = new Intent(LogIn.this, SignUp.class);
                startActivity(Singinpage);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckLoginData()) {
                    Intent mainActivityintent = new Intent(LogIn.this, MainActivity.class);
                    startActivity(mainActivityintent);
                }
                else{
                    EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
                    inPasswordET.setError("Password incorrect");
                }
            }
        });
    }

    public boolean CheckLoginData() {
        Connection connect;
        String ConnectionResult = "";
        boolean autenticated = false;
        EditText inMailET = (EditText) findViewById(R.id.inputEmailAddress);
        EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
        String outKey = "";

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

                    while (rs.next()) {
                        //puts query output in string
                        key = rs.getString(1).toString();
                    }
                } else {
                    ConnectionResult = "Check Connection";
                }
            } catch (Exception ex) {
                Log.e("Error ", ex.getMessage());
            }

            //checks if passwords are the same
            if (Validated()) {
                autenticated = true;
            }
        }
        return autenticated;
    }

    public boolean Validated(){
        EditText email2 = findViewById(R.id.inputEmailAddress);
        EditText password2 = findViewById(R.id.inputPassword);
        String email = email2.getText().toString();

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(email);

        String decrypted = encryptor.decrypt(key);
        if(decrypted.equals(password2.getText().toString())){
            return true;
        }
        return false;
    }

    //checks if all boxes al filled
    public boolean CheckIfFilled(String text1, String text2){
        boolean FilledIn = true;
        if(TextUtils.isEmpty(text1)){
            EditText inMailET = (EditText) findViewById(R.id.inputEmailAddress);
            inMailET.setError("Fill in email");
            FilledIn = false;
        }
        if(TextUtils.isEmpty(text2)){
            EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
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