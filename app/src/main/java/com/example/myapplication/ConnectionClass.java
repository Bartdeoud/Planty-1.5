package com.example.myapplication;

import static com.example.myapplication.Home.ip;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String classs = "com.mysql.jdbc.Driver";

    String url = "jdbc:mysql://" + ip + "/Planty";
    String un = "user";
    String password = "Planty";

    public Connection CONN(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL = null;
        try{
            Class.forName(classs);
            conn = DriverManager.getConnection(url, un, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}