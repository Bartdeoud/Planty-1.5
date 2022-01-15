package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class addplant2 extends AppCompatActivity {

    ImageView imageView;

    Button btOpen;

    public static Bitmap imagePlant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplant2);

        imageView = findViewById(R.id.image_view);
        btOpen = findViewById(R.id.button_picture);

        //Request camera permissions
        if (ContextCompat.checkSelfPermission(addplant2.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(addplant2.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);
        }
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckIFFilled()) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                }
            }
        });
    }

    public boolean CheckIFFilled(){
        EditText name = (EditText) findViewById(R.id.etGivePlantName);
        if(TextUtils.isEmpty(name.getText())){
           name.setError("Empty");
           return false;
       }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //Get capture image
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
            imagePlant = captureImage;
            //Set capture image to ImageView
            imageView.setImageBitmap(captureImage);
        }
    }

    public void makePicture(View view) {
    }
}