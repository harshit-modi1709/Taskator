package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DatabaseConnection {
    private static final String URL="jdbc:mariadb://localhost:3306/time_management_system";
    private static final String USER="timemanager";
    private static final String PASSWORD="";

    private DatabaseConnection(){}

    public static Connection getConnection() throws SQLException {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        }catch(ClassNotFoundException e){
            throw new SQLException("MariaDB JDBC Driver not found.",e);
        }
        return DriverManager.getConnection(URL,USER,PASSWORD);
    }
}
