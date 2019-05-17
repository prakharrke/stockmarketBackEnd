package orderbooks;

import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import order.Order;
import order.OrderComparator;

public class SellOrderBook implements OrderBook {

	
	public void clear() {

		sellOrderBook.clear();
	}

	
	public boolean containsKey(Object key) {

		return sellOrderBook.containsKey(key);
	}

	
	public boolean containsValue(Object value) {

		return sellOrderBook.containsValue(value);
	}

	
	public Set<Entry<String, PriorityQueue<Order>>> entrySet() {

		return sellOrderBook.entrySet();
	}

	
	public PriorityQueue<Order> get(Object key) {

		return sellOrderBook.get(key);
	}


	public boolean isEmpty() {

		return sellOrderBook.isEmpty();
	}

	
	public Set<String> keySet() {

		return sellOrderBook.keySet();
	}

	
	public PriorityQueue<Order> put(String key, PriorityQueue<Order> value) {
		// TODO Auto-generated method stub
		return sellOrderBook.put(key, value);
	}
	// * Overloaded put method
	public void put(Order order) {
		String key = order.getOrderKey();
		if(sellOrderBook.containsKey(key)) {

			sellOrderBook.get(key).add(order);
		}else {
			PriorityQueue<Order> orderBookPriorityQueue = new PriorityQueue<Order>(new OrderComparator());
			orderBookPriorityQueue.add(order);
			sellOrderBook.put(key, orderBookPriorityQueue);
		}


	}

	
	public void putAll(Map<? extends String, ? extends PriorityQueue<Order>> m) {
		// Unimplemented

	}

	
	public PriorityQueue<Order> remove(Object key) {

		return sellOrderBook.remove(key);
	}

	
	public int size() {

		return sellOrderBook.size();
	}

	
	public Collection<PriorityQueue<Order>> values() {

		return sellOrderBook.values();
	}
}
