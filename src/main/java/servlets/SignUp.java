package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import databasecommunication.DBCommunication;
import databasecommunication.DBConnection;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SignUp
 */
public class SignUp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUp() {
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
		// TODO Auto-generated method stub
		String userDetailsString = request.getParameter("userDetails");
		JSONObject userDetails = new Gson().fromJson(userDetailsString, JSONObject.class);
		String username = userDetails.getString("username");
		String password = userDetails.getString("password");

		try {
			Connection connection = DBConnection.getConnection();
			DBCommunication dbCommunication = new DBCommunication(connection);
			if(dbCommunication.isUserRegistered(username)) {
				response.setStatus(401);
				PrintWriter out = response.getWriter();
				out.println("User already Registered.");
				return;
				
			}else {
				dbCommunication.registerUser(username, password);
				PrintWriter out = response.getWriter();
				out.println("Registered");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

}
