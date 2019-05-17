/*
 * This class maintains hashmaps for buy orders and sell orders. it uses the name of the stock as the key of hashmaps
 */

package stockmarket;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import databasecommunication.DBCommunication;
import databasecommunication.DBConnection;
import order.BuyOrder;
import order.ExecutedOrder;
import order.Order;
import order.SellOrder;
import orderbooks.BuyOrderBook;
import orderbooks.OrderBook;
import orderbooks.SellOrderBook;

public class StockMarket {

	static OrderBook buyOrderBook = new BuyOrderBook();
	static OrderBook sellOrderBook = new SellOrderBook();
	static Queue<Order>executedOrders = new LinkedList<Order>();
	static volatile  Queue<Order> waitingOrders = new LinkedList<Order>();
	public static Queue<Order> addOrder(Order order) throws ClassNotFoundException, SQLException {

		String orderKey = order.getOrderKey();


		if(order instanceof BuyOrder) {
			// * Checking if the match of the current buy order is present in the sell order book

			if(sellOrderBook.containsKey(orderKey)) {

				PriorityQueue<Order> sellOrderPriorityQueue = sellOrderBook.get(orderKey);
				Iterator<Order>sellIterator = sellOrderPriorityQueue.iterator();

				while(sellIterator.hasNext()) {
					Order sellOrder = (Order)sellIterator.next();
					if(sellOrder.getUserID().equals(order.getUserID()))
						continue;
					// * Check if current sell and buy order objects satisfies BUY_Price > SELL_PRICE
					if(order.getOrderPrice() > sellOrder.getOrderPrice()) {

						// * Check the quantity and make adjustments to current orders
						if(order.getOrderQuantity() > sellOrder.getOrderQuantity()) {

							order.setOrderQuantity(order.getOrderQuantity() - sellOrder.getOrderQuantity());
							Date executedOrderDate = new Date();
							// * Creating ExecutedOrder Object 
							Order executedOrder = new ExecutedOrder();
							executedOrder.setOrderID(executedOrderDate.getTime())
							.setOrderKey(order.getOrderKey())
							.setOrderPrice(sellOrder.getOrderPrice())
							.setOrderQuantity(sellOrder.getOrderQuantity())
							.setorderTimeStamp(executedOrderDate.getTime())
							.setBuyOrder(order)
							.setSellOrder(sellOrder);
							// * Adding executedOrder Object to executedOrder Queue
							Connection connection = DBConnection.getConnection();
							DBCommunication dbCommunication = new DBCommunication(connection);
							dbCommunication.addExecutedOrdersToDB(order, sellOrder, sellOrder.getOrderQuantity());
							executedOrders.add(executedOrder);
							
							// * ADDING THE EXECUTED ORDERS TO EXECUTED ORDERS TABLE IN DB
							
							/*
							 * Removing sell order from sellOrderBook 
							 * Using Iterator.remove() instead of PriorityQueue.remove() to avoid ConcurrentModificationException
							 */
							sellIterator.remove();
							continue;
						}
						// * Case if buy order quantity is less than sell order quantity 
						else if(order.getOrderQuantity() < sellOrder.getOrderQuantity()) {

							sellOrder.setOrderQuantity(sellOrder.getOrderQuantity() - order.getOrderQuantity());
							Date executedOrderDate = new Date();
							// * Creating Executed Order Object 
							Order executedOrder = new ExecutedOrder();
							executedOrder.setOrderID(executedOrderDate.getTime())
							.setOrderKey(order.getOrderKey())
							.setOrderPrice(sellOrder.getOrderPrice())
							.setOrderQuantity(order.getOrderQuantity())
							.setorderTimeStamp(executedOrderDate.getTime())
							.setBuyOrder(order)
							.setSellOrder(sellOrder);
							// * Adding executedOrder Object to executedOrder Queue

							executedOrders.add(executedOrder);
							
							// * ADDING EXECUTED ORDERS TO DB
							Connection connection = DBConnection.getConnection();
							DBCommunication dbCommunication = new DBCommunication(connection);
							dbCommunication.addExecutedOrdersToDB(order, sellOrder, order.getOrderQuantity());
							// * Setting buy order quantity to zero
							order.setOrderQuantity(0);
							break;
						}
						// * Case when buy order quantity and sell order quantity are equal

						else {

							Date executedOrderDate = new Date();
							// * Creating Executed Order Object 
							Order executedOrder = new ExecutedOrder();
							executedOrder.setOrderID(executedOrderDate.getTime())
							.setOrderKey(order.getOrderKey())
							.setOrderPrice(sellOrder.getOrderPrice())
							.setOrderQuantity(order.getOrderQuantity())
							.setorderTimeStamp(executedOrderDate.getTime())
							.setBuyOrder(order)
							.setSellOrder(sellOrder);
							// * Adding executedOrder Object to executedOrder Queue
							executedOrders.add(executedOrder);
							
							Connection connection = DBConnection.getConnection();
							DBCommunication dbCommunication = new DBCommunication(connection);
							dbCommunication.addExecutedOrdersToDB(order, sellOrder, sellOrder.getOrderQuantity());

							/*
							 * Removing sell order from sellOrderBook 
							 * Using Iterator.remove() instead of PriorityQueue.remove() to avoid ConcurrentModificationException
							 */
							sellIterator.remove();

							// * Setting buy order quantity to zero
							order.setOrderQuantity(0);
							break;
						}


					}else {

						continue;
					}

				}

				// * Checking if Buy Order quantity is greater than zero. If yes, add it to buyOrderBook

				if(order.getOrderQuantity() > 0)
					buyOrderBook.put(order);

			}else {

				buyOrderBook.put(order);
			}
		}
		if(order instanceof SellOrder) {

			// * Checking if the match of the order is present in the buy order book

			if(buyOrderBook.containsKey(orderKey)) {

				PriorityQueue<Order> buyOrderPriorityQueue = buyOrderBook.get(orderKey);
				Iterator<Order>buyIterator = buyOrderPriorityQueue.iterator();

				while(buyIterator.hasNext()) {
					Order buyOrder = (Order)buyIterator.next();
					if(buyOrder.getUserID().equals(order.getUserID()))
						continue;
					// * Check if current sell and buy order objects satisfies BUY_Price > SELL_PRICE

					if(buyOrder.getOrderPrice() > order.getOrderPrice()) {

						// * Check the quantity and make adjustments to current orders
						if(order.getOrderQuantity() > buyOrder.getOrderQuantity()) {

							order.setOrderQuantity(order.getOrderQuantity() - buyOrder.getOrderQuantity());
							Date executedOrderDate = new Date();
							// * Creating Executed Order Object 
							Order executedOrder = new ExecutedOrder();
							executedOrder.setOrderID(executedOrderDate.getTime())
							.setOrderKey(order.getOrderKey())
							.setOrderPrice(order.getOrderPrice())
							.setOrderQuantity(buyOrder.getOrderQuantity())
							.setorderTimeStamp(executedOrderDate.getTime())
							.setBuyOrder(buyOrder)
							.setSellOrder(order);

							// * Adding executedOrder Object to executedOrder Queue

							executedOrders.add(executedOrder);
							
							Connection connection = DBConnection.getConnection();
							DBCommunication dbCommunication = new DBCommunication(connection);
							dbCommunication.addExecutedOrdersToDB(buyOrder, order, buyOrder.getOrderQuantity());

							/*
							 * Removing buy order from buyOrderBook 
							 * Using Iterator.remove() instead of PriorityQueue.remove() to avoid ConcurrentModificationException
							 */
							buyIterator.remove();
							continue;
						}
						// * Case if sell order quantity is less than buy order quantity 
						else if(order.getOrderQuantity() < buyOrder.getOrderQuantity()) {

							order.setOrderQuantity(buyOrder.getOrderQuantity() - order.getOrderQuantity());
							Date executedOrderDate = new Date();
							// * Creating ExecutedOrder Object 
							ExecutedOrder executedOrder = new ExecutedOrder();
							executedOrder.setOrderID(executedOrderDate.getTime())
							.setOrderKey(order.getOrderKey())
							.setOrderPrice(order.getOrderPrice())
							.setOrderQuantity(order.getOrderQuantity())
							.setorderTimeStamp(executedOrderDate.getTime())
							.setBuyOrder(buyOrder)
							.setSellOrder(order);

							// * Adding executedOrder Object to executedOrder Queue

							executedOrders.add(executedOrder);
							
							Connection connection = DBConnection.getConnection();
							DBCommunication dbCommunication = new DBCommunication(connection);
							dbCommunication.addExecutedOrdersToDB(buyOrder, order, order.getOrderQuantity());
							// * Setting sell order quantity to zero
							order.setOrderQuantity(0);
							break;
						}
						// * Case when buy order quantity and sell order quantity are equal

						else {

							Date executedOrderDate = new Date();
							// * Creating Executed Order Object 
							Order executedOrder = new ExecutedOrder();
							executedOrder.setOrderID(executedOrderDate.getTime())
							.setOrderKey(order.getOrderKey())
							.setOrderPrice(order.getOrderPrice())
							.setOrderQuantity(order.getOrderQuantity())
							.setorderTimeStamp(executedOrderDate.getTime())
							.setBuyOrder(buyOrder)
							.setSellOrder(order);
							// * Adding executedOrder Object to executedOrder Queue
							executedOrders.add(executedOrder);
							
							Connection connection = DBConnection.getConnection();
							DBCommunication dbCommunication = new DBCommunication(connection);
							dbCommunication.addExecutedOrdersToDB(buyOrder, order, order.getOrderQuantity());

							/*
							 * Removing sell order from sellOrderBook 
							 * Using Iterator.remove() instead of PriorityQueue.remove() to avoid ConcurrentModificationException
							 */
							buyIterator.remove();

							// * Setting buy order quantity to zero
							order.setOrderQuantity(0);
							break;
						}


					}else {

						continue;
					}

				}

				// * Checking if Sell Order quantity is greater than zero. If yes, add it to buyOrderBook

				if(order.getOrderQuantity() > 0)
					sellOrderBook.put(order);

			}else {

				sellOrderBook.put(order);
			}


		}

		return executedOrders;
	}
	
	public static Queue<Order> addToWaitingQueue(Order order) {
		waitingOrders.add(order);
		return waitingOrders;
	}
	public static Queue<Order> getWaitingOrderQueue() {
		
		return waitingOrders;
	}
}
