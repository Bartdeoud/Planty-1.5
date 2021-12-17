package com.example.myapplication;


import static java.net.InetAddress.getLocalHost;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.net.DatagramSocket;
import java.net.InetAddress;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper
{
    Connection con;
    String uname, pass, ip, port, database;

    public Connection Connectionclass()
    {
            //local ip adress from cmd
            ip = "192.168.2.11";
            database = "master";
            //acces data
            uname = "sa";
            pass = "planty4life";
            port = "1433";

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            con = null;
            String ConnectionURL = null;

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