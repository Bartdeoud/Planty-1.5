package com.example.myapplication;

import static com.example.myapplication.outsideVariables.commitQuery;
import static com.example.myapplication.outsideVariables.gebruikerCode;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import kotlinx.coroutines.AwaitKt;

public class userData extends AppCompatActivity {

    private String[] string = new String[6];
    private ArrayList<TextView> textViews = new ArrayList<>();
    private String enckey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        loadDataFromDB();
        loadData();
        cameraPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataFromDB();
        loadData();
        cameraPermission();
    }

    private void cameraPermission(){
        if (ContextCompat.checkSelfPermission(userData.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(userData.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        }
    }

    private void loadData() {
        textViews = new ArrayList<>(Arrays.asList(findViewById(R.id.textProfile1), findViewById(R.id.textProfile2), findViewById(R.id.textProfile3), findViewById(R.id.textProfile4), findViewById(R.id.textProfile5), findViewById(R.id.textProfile6)));
        int i = 0;
        for (String string : string) {
            textViews.get(i).setText(string);
            i++;
        }
        ImageView view = findViewById(R.id.ProfilePictureProfile);
        Bitmap bitmap = loadBitmapP(getFilePathP("P" + gebruikerCode + "ProfilePicture").getPath());
        if (bitmap != null) {
            view.setImageBitmap(bitmap);
        }
    }

    private void loadDataFromDB() {
        Connection connect;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                Statement st = connect.createStatement();
                String query = "select voornaam, Achternaam, Adres, Bedrijf, Telefoonnummer, Email from Gebruiker where Gebruikercode = '" + gebruikerCode + "'";
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //puts query output in string
                    for (int i = 0; i <= 5; i++) {
                        string[i] = rs.getString(i + 1).trim();
                    }
                }
                query = "select Naam from Bedrijf Where Bedrijfcode = '" + string[3].substring(0, string[3].length() - 2) + "'";
                string[3] = commitQuery(query);
            } else {
                Toast.makeText(userData.this, "Database server is unreachable", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Log.e("Error: ", ex.getMessage());
            Toast.makeText(userData.this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    public void Validator(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = findViewById(R.id.textProfile7);
        if(textView.getText().toString().equals("")) {
            builder.setTitle("Fill in password to confirm");
        } else {
            builder.setTitle("Fill in old password to confirm");
        }

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String password = input.getText().toString();
            if (passwordCorrect(password)){
                changeProfile();
            } else {
                Toast.makeText(userData.this, "Password is incorect", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private boolean passwordCorrect(String password){
        String query = "select Encription_key from Gebruiker where Gebruikercode = '" + gebruikerCode + "'";
        enckey = commitQuery(query);
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        try {
            String decrypted = encryptor.decrypt(enckey);
            if (decrypted.equals(password)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void changeProfile() {
        textViews = new ArrayList<>(Arrays.asList(findViewById(R.id.textProfile1), findViewById(R.id.textProfile2), findViewById(R.id.textProfile3), findViewById(R.id.textProfile4), findViewById(R.id.textProfile5), findViewById(R.id.textProfile6)));
        int i = 0;
        for (TextView textView : textViews) {
            string[i] = textView.getText().toString().trim();
            i++;
        }
        Connection connect;
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                Connection con = connectionHelper.Connectionclass();
                @SuppressLint("DefaultLocale") String query2 = String.format("update Gebruiker set voornaam = '%s', Achternaam = '%s', Adres = '%s', Bedrijf = '%s', Telefoonnummer = '%s', Email = '%s', Encription_key = '%s' where Gebruikercode = '%s'",
                        string[0],
                        string[1],
                        string[2],
                        getBedrijf(string[3]),
                        string[4],
                        string[5],
                        getEncKey(),
                        gebruikerCode);
                System.out.println(query2);
                PreparedStatement prepsInsertProduct = con.prepareStatement(query2);
                prepsInsertProduct.execute();
                Intent intent = new Intent(userData.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(userData.this, "Could not connect to database", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(userData.this, "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getBedrijf(String bedrijf) {
        String query1 = "SELECT COUNT(Naam) FROM Bedrijf WHERE Naam = '" + bedrijf + "';";
        String companyCount = commitQuery(query1);
        if (Integer.parseInt(companyCount) > 0) {
            String query2 = "SELECT Bedrijfcode FROM Bedrijf WHERE Naam = '" + bedrijf + "';";
            return commitQuery(query2);
        }
        Toast.makeText(userData.this, "WARNING company does not exist in database\nIt will not be stored", Toast.LENGTH_LONG).show();
        return "1";
    }

    public void changeProfilePicture(View view) {
        String tja = "tja";
        if (ContextCompat.checkSelfPermission(userData.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(userData.this, "The application does not have the right to use the camera", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(userData.this,
                    new String[]{Manifest.permission.CAMERA},
                    100);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 100);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            //Get capture image
            Bitmap imagePlant = (Bitmap) data.getExtras().get("data");
            saveBitmap(imagePlant);
        }
    }

    public void saveBitmap(Bitmap pictureBitmap) {
        try {
            //create file with path
            File file = getFilePathP("P" + gebruikerCode + "ProfilePicture");
            //generates new stream to file
            OutputStream fOut = new FileOutputStream(file);
            //saves bitmap to file
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush(); // does not do much. just to be safe
            fOut.close(); // close the stream to file
            //safes photo to media file
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFilePathP(String fileName) {
        //gets directory for foto's
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File fotoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(fotoDirectory, fileName + ".png");
    }

    public static Bitmap loadBitmapP(String filename) {
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

    public void buttonGotoHome(View view) {
        Intent intent = new Intent(userData.this, Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    private String getEncKey() {
        EditText passwordET = findViewById(R.id.textProfile7);
        String password = passwordET.getText().toString();
        if(!password.equals("")) {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(password);
            return encryptor.encrypt(password);
        }
        return enckey;
    }
}