package com.example.myapplication;


import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper
{
    Connection con;
    String uname, pass, ip, port, database;

    public Connection Connectionclass()
    {
        ip = "192.168.2.19";
        database = "master";

        uname = "sa";
        pass = "planty4life";
        port = "1433";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        con = null;
        String ConnectionURL = null;

        try
        {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + uname + ";password=" + pass + ";";
            con = DriverManager.getConnection(ConnectionURL);
        }
        catch (Exception ex)
        {
            Log.e("Error ", ex.getMessage());
        }

        return con;
    }
}