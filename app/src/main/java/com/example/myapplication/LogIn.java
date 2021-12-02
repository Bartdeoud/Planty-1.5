package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);


        final Button button = findViewById(R.id.btnLogIn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //in - inside / out -outside
                EditText inMailET = (EditText) findViewById(R.id.inputEmailAddress);
                String Inmail = inMailET.getText().toString();

                //checks with database
                String outPassword = "1";

                EditText inPasswordET = (EditText) findViewById(R.id.inputPassword);
                String InPassword = inPasswordET.getText().toString();

                if (inPasswordET.equals(outPassword)){
                    Intent myIntent = new Intent(LogIn.this, MainActivity.class);
                    startActivity(myIntent);
                }
            }
        });
    }
}