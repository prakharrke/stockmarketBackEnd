package orderbooks;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import order.Order;

public interface OrderBook extends Map<String, PriorityQueue<Order>>{
	Map<String, PriorityQueue<Order>>sellOrderBook = new HashMap<String, PriorityQueue<Order>>();
	Map<String, PriorityQueue<Order>>buyOrderBook = new HashMap<String, PriorityQueue<Order>>();

	void put(Order order);



}
