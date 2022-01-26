package com.example.myapplication;

import android.util.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class outsideVariables {
    public static String ip = "10.0.0.2", gebruikerCode = "1001";
    public static String[] plantNames = new String[6];

    public static String commitQuery(String query){
        Connection connect;
        String result = "";
        try {
            ConnectionHelper connectionHelper = new ConnectionHelper();
            connect = connectionHelper.Connectionclass();
            if (connect != null) {
                //query statement
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);
                while (rs.next()) {
                    //puts query output in string
                    result = rs.getString(1);
                }
            }
        } catch (Exception ex) {
            Log.e("Error ", ex.getMessage());
        }
        return result;
    }
}
