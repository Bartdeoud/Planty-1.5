package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.TextView;

public class LogIn extends AppCompatActivity {

    Button btLogin;
    TextView btSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btLogin = findViewById(R.id.Loginbtn);
        btSignin = findViewById(R.id.signInButon);

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

                if (CheckLoginData()){
                    Intent mainActivityintent = new Intent(LogIn.this, MainActivity.class);
                    startActivity(mainActivityintent);
                }
            }
        });
    }


    public boolean CheckLoginData(){
        boolean autenticated = false;
        EditText inMailET = (EditText) findViewById(R.id.inputEmailAddress);
        EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
        if(TextUtils.isEmpty(inMailET.getText())){
            inMailET.setError("Empty");
        }
        if(TextUtils.isEmpty(inPasswordET.getText())){
            inPasswordET.setError("Empty");
        }
        //in - inside / out -outside

        String Inmail = inMailET.getText().toString();

        //checks with database
        String outPassword = "1";


        String InPassword = inPasswordET.getText().toString();

        if ((InPassword.toString()).equals(outPassword)){
            autenticated = true;
        }
        return autenticated;
    }

    public void buttonlogin(View view) {
    }

    public void ButtonSignin(View view) {
    }
}