package databasecommunication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

import constants.StockExchangeConstants;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import order.BuyOrder;
import order.Order;

public class DBCommunication {

	Connection connection;
	
	public DBCommunication(Connection connection) {
		this.connection = connection;
	}
	
	public boolean isUserRegistered(String userID) throws SQLException {
		
		ResultSet rs = null;
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT USER_NAME FROM USERS WHERE USER_NAME='" + userID + "'");
			
			while(rs.next())
				return true;
			return false;
		}catch(Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}finally{
			if(stmt!=null)
				stmt.close();
		}
	}
	
	public void registerUser(String userID, String password) throws SQLException {
		
		ResultSet rs = null;
		PreparedStatement stmt = null;
		try{
			
			String query = "INSERT INTO USERS (USER_NAME, USER_PASSWORD) VALUES(?,?)";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, userID);
			stmt.setString(2, password);
			stmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}finally{
			if(stmt!=null)
				stmt.close();
		}
	}
	
	public boolean loginUser(String userID, String password) throws SQLException {
		
		ResultSet rs = null;
		Statement stmt = null;
		try{
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT USER_NAME, USER_PASSWORD FROM USERS WHERE USER_NAME='" + userID + "'");
			
				while(rs.next()) {
					String dbPassword = rs.getString("USER_PASSWORD");
					if(password.equals(dbPassword))
						return true;
					else
						return false;
				}
				
				return false;
		
		}catch(Exception e) {
			e.printStackTrace();
			throw new SQLException();
		}finally {
			if(stmt!=null)
				stmt.close();
		}
		
	}
	public void addOrderToDB(Order order) throws SQLException {
		String orderID = String.valueOf(order.getOrderID());
		String userID = order.getUserID();
		String orderKey = order.getOrderKey();
		String orderAction = (order instanceof BuyOrder) ? StockExchangeConstants.buyAction : StockExchangeConstants.sellAction;
		String orderPrice = String.valueOf(order.getOrderPrice());
		String orderTimeStamp = String.valueOf(order.getOrderTimeStamp());
		String orderQuantity = String.valueOf(order.getOrderQuantity());
		
		ResultSet rs = null;
		String query = "INSERT INTO ORDERS VALUES(?,?,?,?,?,?,?)";
		PreparedStatement stmt = null;
		
		try {
			stmt =  connection.prepareStatement(query);
			stmt.setString(1, orderID);
			stmt.setString(2, orderAction);
			stmt.setString(3, orderPrice);
			stmt.setString(4, orderKey);
			stmt.setString(5, orderQuantity);
			stmt.setString(6, userID);
			stmt.setString(7, orderTimeStamp);
			stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(stmt!=null)
				stmt.close();
		}
		
	}
	
	public String getPlacedOrders(String userName) throws SQLException {
		
		ResultSet rs = null;
		Statement stmt = null;
		JSONArray orders = new JSONArray();
		String query = "SELECT * FROM ORDERS WHERE USER_ID = '" + userName + "' ORDER BY TIME_STAMP DESC";
		try {
			stmt = connection.createStatement();
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				JSONObject order = new JSONObject();
				order.put("orderID", rs.getString("ORDER_ID"));
				order.put("orderType", rs.getString("ORDER_TYPE"));
				order.put("orderPrice", rs.getString("ORDER_PRICE"));
				order.put("orderKey", rs.getString("ORDER_KEY"));
				order.put("orderQuantity", rs.getString("QUANTITY"));
				long time  = Long.valueOf(rs.getString("TIME_STAMP"));
				Date orderDate = new Date(time);
				
				DateFormat df = new SimpleDateFormat("hh:mm:ss");
				order.put("orderTimeStamp", df.format(orderDate));
				orders.add(order);
			}
			
		return new Gson().toJson(orders);
		}catch(Exception e) {
				throw new SQLException();
		}finally {
			if(stmt!=null)
				stmt.close();
		}
	}
	
	public String getExecutedOrders(String userName) throws SQLException {
		
		ResultSet rs = null;
		Statement stmt = null;
		JSONArray executedOrders = new JSONArray();
		try {
			
			stmt = connection.createStatement();
			String query = "SELECT * FROM EXECUTED_ORDERS WHERE USER_ID='" + userName + "'";
			rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				JSONObject executedOrder = new JSONObject();
				executedOrder.put("sellOrderID", rs.getString("SELLORDER_ID"));
				executedOrder.put("buyOrderID", rs.getString("BUYORDER_ID"));
				executedOrder.put("quantity", rs.getString("QUANTITY"));
				executedOrder.put("orderKey", rs.getString("ORDER_KEY"));
//				executedOrders.add(executedOrder);
				
				String buyOrderID = rs.getString("BUYORDER_ID");
				String sellOrderID = rs.getString("SELLORDER_ID");
				ResultSet rs1 = null;
				Statement stmt1 = null;
				
				try {
					stmt1 = connection.createStatement();
					String query1 = "SELECT * FROM ORDERS WHERE ORDER_ID IN ('" + buyOrderID + "','" + sellOrderID + "')";
					rs1 = stmt1.executeQuery(query1);
					while(rs1.next()) {
						if(rs1.getString("ORDER_TYPE").equals("buy")) {
							StringBuilder details = new StringBuilder();
							details.append("Original Quantity - ")
								.append(rs1.getString("QUANTITY") + "\n")
								.append("Action - ")
								.append(rs1.getString("ORDER_TYPE") + "\n")
								.append("User - ")
								.append(rs1.getString("USER_ID"));
							executedOrder.put("buyOrderDetails", details.toString());
						}else {
							StringBuilder details = new StringBuilder();
							details.append("Original Quantity - ")
							.append(rs1.getString("QUANTITY") + "\n")
							.append("Action - ")
							.append(rs1.getString("ORDER_TYPE") + "\n")
							.append("User - ")
							.append(rs1.getString("USER_ID"));
						executedOrder.put("sellOrderDetails", details.toString());
							
						}
					}
				
				executedOrders.add(executedOrder);
				}catch(Exception e) {
				
				}finally {
					if(stmt1!=null)
						stmt1.close();
				}
			}
			
			
			
		}catch(Exception e) {
			
		}finally {
			
			if(stmt!=null)
				stmt.close();
		}
		
		return new Gson().toJson(executedOrders);
	}
	
	public void addExecutedOrdersToDB(Order buyOrder, Order sellOrder, int quantity) throws SQLException {
		connection.setAutoCommit(false);
		PreparedStatement stmt = null;
		String buyOrderID = String.valueOf(buyOrder.getOrderID());
		String sellOrderID = String.valueOf(sellOrder.getOrderID());
		// * ADD BUY ORDER TO DB
		try {
		String buyQuery = "INSERT INTO EXECUTED_ORDERS VALUES(?, ?, ?, ?, ?)";
		stmt = connection.prepareStatement(buyQuery);
		stmt.setString(1, sellOrderID);
		stmt.setString(2, buyOrderID);
		stmt.setString(3, buyOrder.getUserID());
		stmt.setString(4, String.valueOf(quantity));
		stmt.setString(5, buyOrder.getOrderKey());
		stmt.executeUpdate();
		}catch(Exception e) {
			
			throw new SQLException();
		}finally {
			if(stmt!=null)
				stmt.close();
		}
		try {
			// * ADDING SELL ORDER TO DB
			String sellQuery = "INSERT INTO EXECUTED_ORDERS VALUES(?, ?, ?, ?, ?)";
			stmt = connection.prepareStatement(sellQuery);
			stmt.setString(1, sellOrderID);
			stmt.setString(2, buyOrderID);
			stmt.setString(3, sellOrder.getUserID());
			stmt.setString(4, String.valueOf(quantity));
			stmt.setString(5, buyOrder.getOrderKey());
			stmt.executeUpdate();
			}catch(Exception e) {
				throw new SQLException();
				
			}finally {
				if(stmt!=null)
					stmt.close();
			}
		
		
		connection.commit();
		connection.setAutoCommit(true);
		
		
				
	}
}
