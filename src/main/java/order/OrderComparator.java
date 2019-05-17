package order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {

	public int compare(Order order1, Order order2) {
		if(order1 instanceof SellOrder && order2 instanceof SellOrder) {
			// * Price Quantity comparison 
			if(order1.getOrderPrice() > order2.getOrderPrice())
				return 1;
			if(order1.getOrderPrice() < order2.getOrderPrice())
				return -1;
			else {
				// * If prices are equal, time priority is given (sooner the order, higher the priority)
				if(order1.getOrderTimeStamp() > order2.getOrderTimeStamp())
					return 1;
				if(order1.getOrderTimeStamp() < order2.getOrderTimeStamp())
					return -1;
				else
					return 0;

			}
		}
		else {
			// * Price Quantity comparison 
			if(order1.getOrderPrice() < order2.getOrderPrice())
				return 1;
			if(order1.getOrderPrice() > order2.getOrderPrice())
				return -1;
			else {
				// * If prices are equal, time priority is given (sooner the order, higher the priority)
				if(order1.getOrderTimeStamp() > order2.getOrderTimeStamp())
					return 1;
				if(order1.getOrderTimeStamp() < order2.getOrderTimeStamp())
					return -1;
				else
					return 0;

			}

		}
	}

}
