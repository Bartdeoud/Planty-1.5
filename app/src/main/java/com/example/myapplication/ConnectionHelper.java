package com.example.myapplication;


import static com.example.myapplication.outsideVariables.ip;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper
{
    Connection con;
    String uname, pass, port, database;

    public Connection Connectionclass()
    {
            database = "master";
            //acces data
            uname = "sa";
            pass = "planty4life";
            port = "1433";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            con = null;
            String ConnectionURL;

            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ":" + port + ";" + "databasename=" + database + ";user=" + uname + ";password=" + pass + ";";
                con = DriverManager.getConnection(ConnectionURL);
            } catch (Exception ex) {
                Log.e("Error ", ex.getMessage());
            }
        return con;
    }
}