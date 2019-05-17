package servlets;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;

import databasecommunication.DBCommunication;
import databasecommunication.DBConnection;
import driver.StockMarketDriver;

public class ContextInitializer implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
		
		
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		
		try {
			DBConnection.getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StockMarketDriver stockMarketDriver = new StockMarketDriver();
		stockMarketDriver.setDaemon(true);
		stockMarketDriver.start();
	}

}
