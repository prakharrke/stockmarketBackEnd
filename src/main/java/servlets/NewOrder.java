package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import databasecommunication.DBCommunication;
import databasecommunication.DBConnection;
import net.sf.json.JSONObject;
import order.Order;
import stockmarket.StockMarket;

/**
 * Servlet implementation class NewOrder
 */
public class NewOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NewOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderDetailsString = request.getParameter("orderDetails");
		JSONObject orderDetails = new Gson().fromJson(orderDetailsString, JSONObject.class);
		long orderTimeStamp = orderDetails.getLong("orderTimeStamp");
		String orderKey = orderDetails.getString("orderKey");
		String orderAction = orderDetails.getString("orderAction");
		double orderPrice = orderDetails.getDouble("orderPrice");
		int orderQuantity = orderDetails.getInt("orderQuantity");
		
		String userName = orderDetails.getString("userName");
		long orderID = orderTimeStamp;
		Order order = new Order().createOrder(orderAction);
		order.setOrderID(orderID)
			 .setOrderKey(orderKey)
			 .setOrderPrice(orderPrice)
			 .setOrderQuantity(orderQuantity)
			 .setUserID(userName)
			 .setorderTimeStamp(orderTimeStamp);
		try {
			Connection connection = DBConnection.getConnection();
			DBCommunication dbCommunication = new DBCommunication(connection);
			dbCommunication.addOrderToDB(order);
			PrintWriter out = response.getWriter();
			out.println("Order Placed");
			StockMarket.addToWaitingQueue(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
			
	}

}
