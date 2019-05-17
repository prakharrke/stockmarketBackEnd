package orderbooks;

import java.util.Collection;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import order.Order;
import order.OrderComparator;

public class BuyOrderBook implements OrderBook{

	public void clear() {
		
		buyOrderBook.clear();
	}

	public boolean containsKey(Object key) {
		
		return buyOrderBook.containsKey(key);
	}

	public boolean containsValue(Object value) {
		
		return buyOrderBook.containsValue(value);
	}

	
	public Set<Entry<String, PriorityQueue<Order>>> entrySet() {
		
		return buyOrderBook.entrySet();
	}

	
	public PriorityQueue<Order> get(Object key) {
		
		return buyOrderBook.get(key);
	}

	
	public boolean isEmpty() {
		
		return buyOrderBook.isEmpty();
	}

	
	public Set<String> keySet() {
		
		return buyOrderBook.keySet();
	}

	
	public PriorityQueue<Order> put(String key, PriorityQueue<Order> value) {
		
		return buyOrderBook.put(key, value);
	}
	
	// * Overloaded put() method
	public void put(Order order) {
		String key = order.getOrderKey();
		if(buyOrderBook.containsKey(key)) {
			
			buyOrderBook.get(key).add(order);
		}else {
			PriorityQueue<Order> orderBookPriorityQueue = new PriorityQueue<Order>(new OrderComparator());
			orderBookPriorityQueue.add(order);
			buyOrderBook.put(key, orderBookPriorityQueue);
		}
		
		
	}


	public void putAll(Map<? extends String, ? extends PriorityQueue<Order>> m) {
		// Unimplemented
		
	}

	
	public PriorityQueue<Order> remove(Object key) {
		
		return buyOrderBook.remove(key);
	}

	
	public int size() {
		
		return buyOrderBook.size();
	}

	
	public Collection<PriorityQueue<Order>> values() {
		
		return buyOrderBook.values();
	}


	
	
}
