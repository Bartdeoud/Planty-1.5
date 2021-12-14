package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LogIn<inPasswordET> extends AppCompatActivity {

    Button btLogin;
    TextView btSignin;

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
            }
        });
    }

    public boolean CheckLoginData() {
        Connection connect;
        String ConnectionResult = "";
        boolean autenticated = false;
        EditText inMailET = (EditText) findViewById(R.id.inputEmailAddress);
        EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
        String outPassword = "";

        if (CheckIfFilled(inMailET.getText().toString(), inPasswordET.getText().toString())) {

            //in - inside / out -outside

            String Inmail = inMailET.getText().toString();

            //connection with database
            try {
                ConnectionHelper connectionHelper = new ConnectionHelper();
                connect = connectionHelper.Connectionclass();
                if (connect != null) {
                    //query statement
                    String query = "SELECT Wachtwoord FROM Gebruiker WHERE Email = '" + Inmail + "'";
                    Statement st = connect.createStatement();
                    ResultSet rs = st.executeQuery(query);

                    while (rs.next()) {
                        //puts query output in string
                        outPassword = rs.getString(1).toString();
                    }
                } else {
                    ConnectionResult = "Check Connection";
                }
            } catch (Exception ex) {
                Log.e("Error ", ex.getMessage());
            }

            //checks if passwords are the same
            String InPassword = inPasswordET.getText().toString();
            if ((InPassword.toString()).equals(outPassword)) {
                autenticated = true;
            }
        }
        return autenticated;
    }

    //checks if all boxes al filled
    public boolean CheckIfFilled(String text1, String text2){
        boolean FilledIn = true;
        if(TextUtils.isEmpty(text1)){
            EditText inMailET = (EditText) findViewById(R.id.inputEmailAddress);
            inMailET.setError("Empty");
            FilledIn = false;
        }
        if(TextUtils.isEmpty(text2)){
            EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
            inPasswordET.setError("Empty");
            FilledIn = false;
        }
        return FilledIn;
    }

    public void buttonlogin(View view) {
    }

    public void ButtonSignin(View view) {
    }
}