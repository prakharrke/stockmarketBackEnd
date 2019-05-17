package databasecommunication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	static String myDbUrl = "jdbc:mysql://db4free.net:3306/stockmarketdb";
	static String myDbDriver = "com.mysql.jdbc.Driver";
	static String myDbUser = "stockmarket24";
	static String myDbPwd = "asdf1234";
    static Connection connection = null;
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
    	if(connection != null && connection.isValid(10))
    		return connection;
    	Class.forName(myDbDriver);
    	connection = DriverManager.getConnection(myDbUrl, myDbUser, myDbPwd);
    	return connection;
    	
    }
    
    
    
    
}
