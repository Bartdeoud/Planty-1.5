package com.example.myapplication;

import static com.example.myapplication.outsideVariables.commitQuery;
import static com.example.myapplication.outsideVariables.gebruikerCode;

import android.Manifest;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class addplant2 extends AppCompatActivity {

    public static Bitmap imagePlant;
    public static String plantName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addplant2);

        //Request camera permissions
        if (ContextCompat.checkSelfPermission(addplant2.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(addplant2.this, new String[]{Manifest.permission.CAMERA}, 100);
        }
        if(ContextCompat.checkSelfPermission(addplant2.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(addplant2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if(ContextCompat.checkSelfPermission(addplant2.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(addplant2.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EditText editText = findViewById(R.id.etGivePlantName);
        editText.setText("");
    }

    public boolean CheckIFFilled(){
        EditText name = findViewById(R.id.etGivePlantName);
        if(TextUtils.isEmpty(name.getText())){
           name.setError("Empty");
           return false;
       }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //Get capture image
            imagePlant = (Bitmap) data.getExtras().get("data");
            saveBitmap(imagePlant);
            Intent mainActivityintent = new Intent(addplant2.this, ChooseLocation.class);
            startActivity(mainActivityintent);
        }
    }

    public static Bitmap loadBitmap(String filename) {
        try {
            //checks if file exist
            File f = new File(filename);
            if (!f.exists()) {
                return null;
            }
            //gets bitmap from foto
            return BitmapFactory.decodeFile(filename);
        } catch (Exception e) {
            return null;
        }
    }

    public void saveBitmap(Bitmap pictureBitmap) {
        try {
            //create file with path
        File file = getFilePath(gebruikerCode + plantName);
        //generates new stream to file
            OutputStream fOut = new FileOutputStream(file);
        //saves bitmap to file
        pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        fOut.flush(); // does not do much. just to be safe
        fOut.close(); // close the stream to file
        //safes photo to media file
        MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFilePath(String fileName){
        //gets directory for foto's
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(fotoDirectory, fileName + ".png");
    }

    public void makePicture(View view) {
        if (ContextCompat.checkSelfPermission(addplant2.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(addplant2.this, "The application does not have the right to use the camera", Toast.LENGTH_SHORT).show();
        } else {
            if (CheckIFFilled()) {
                //saves plant name
                TextView text = findViewById(R.id.etGivePlantName);
                plantName = text.getText().toString();
                //checks if plant count is higher than 6
                String query = "select PrivatePlantnummer from Plant Where Gebruikercode = '" + gebruikerCode + "' order by PrivatePlantnummer";
                int Plants = 1;
                try {
                    Plants = Integer.parseInt(commitQuery(query));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

                if (!(Plants >= 6)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                } else {
                    text.setError("You have reached the limit of plants that you can store");
                }
            }
        }
    }

    public void buttonGotoHome3(View view) {
        Intent intent = new Intent(addplant2.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void buttonGotoWateringcycle3(View view) {
        Intent intent = new Intent(addplant2.this, WateringCycle.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void gotoAddplant3(View view) {
    }

    public void gotoPremium3(View view) {
        Intent intent = new Intent(addplant2.this, Premium.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}