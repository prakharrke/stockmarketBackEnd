package driver;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;

/*
 * Driver Class - Takes input as mentioned in the assignment, one order per line. Evaluates the order after every Enter (CTRL)
 */

import inputparser.InputParser;
import order.Order;
import stockmarket.StockMarket;

public class StockMarketDriver extends Thread {

	
	public void run() {
		
		while(true) {
			
			Queue<Order> waitinOrderQueue= StockMarket.getWaitingOrderQueue();
	
			if(waitinOrderQueue.size() > 0) {
			System.out.println(waitinOrderQueue.size());
			Order order = waitinOrderQueue.poll();
			System.out.println(order);
			try {
				StockMarket.addOrder(order);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
		}


	}
}
