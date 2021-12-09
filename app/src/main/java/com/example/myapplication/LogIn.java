package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class LogIn extends AppCompatActivity {

    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
            btLogin = findViewById(R.id.Loginbtn);

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

        //in - inside / out -outside
        //EditText inMailET = (EditText) findViewById(R.id.inputEmailAddress);
        //String Inmail = inMailET.getText().toString();

        //checks with database
        String outPassword = "1";

        EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
        String InPassword = inPasswordET.getText().toString();

        if ((InPassword.toString()).equals(outPassword)){
            autenticated = true;
        }
        return autenticated;
    }

    public void buttonlogin(View view) {
    }
}