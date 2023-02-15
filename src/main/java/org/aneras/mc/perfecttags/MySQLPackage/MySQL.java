package org.aneras.mc.perfecttags.MySQLPackage;


import org.aneras.mc.perfecttags.configs.Names;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private  String host = Names.get().getString("host");
    private String port = Names.get().getString("port");
    private  String database = Names.get().getString("database");
    private  String username = Names.get().getString("username");
    private  String password = Names.get().getString("password");

    private Connection connection;

    public boolean isConnected(){
        return (connection == null ? false : true);
    }

    public void Connect() throws ClassNotFoundException, SQLException{
        if(!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);
        }
    }
    public void Disconnect(){
        if(isConnected()){
            try {
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

    }
    public Connection getConnection() {
        return connection;
    }
}
