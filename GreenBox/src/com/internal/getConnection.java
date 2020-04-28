package com.internal;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ravi
 */
public class getConnection {
private String driver = "org.postgresql.Driver";
private String connectionUrl = "jdbc:postgresql://localhost:5959/";
private String database = "Greenbox";
private String userid = "root";
private String password = "26_v9Fe},,6\\Z3E?";
Connection connection = null;
    public Connection getConnection() {
        try {
        Class.forName(driver);
        connection = DriverManager.getConnection(connectionUrl+database, userid, password);
        return connection;
        } 
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
   
}
