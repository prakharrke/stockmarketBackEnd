package inputparser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import constants.StockExchangeConstants;
import order.Order;

/*
 * Class to convert input taken from the user to Order
 */

public class InputParser {

	public Order generateOrder(String orderDetails) throws ParseException {

		/*
		 *  order input format - <order-id> <order-key> <time> <action> <quantity> <price>
		 */
		int orderID;
		String orderKey;
		Date orderDate = new Date();
		String action;
		int quantity;
		double price;
		Order order;
		// * Splitting the input string using regex of white spaces. It can perfectly split regardless of the number of white spaces present

		String[] inputParameters = orderDetails.split("[\\s+]");

		if(inputParameters.length != 6)
			throw new IllegalArgumentException("Bad Input. Please provide input in the specified format.\nOrder input format - <order-id> <order-key> <time> <action> <quantity> <price>");
		String orderIDString = inputParameters[0].trim();
		if(orderIDString.startsWith(StockExchangeConstants.HASH)) {
			orderIDString = orderIDString.replace(StockExchangeConstants.HASH,StockExchangeConstants.emptyString);
			orderID = Integer.parseInt(orderIDString);
		}else {
			throw new IllegalArgumentException("Bad Input for order ID");
		}

		String timeString = inputParameters[1].trim();

		DateFormat dateFormat = new SimpleDateFormat(StockExchangeConstants.orderTimeFormat);
		orderDate = dateFormat.parse(timeString);
					
		orderKey = inputParameters[2].trim();

		String actionString = inputParameters[3].trim();
		if(actionString.equalsIgnoreCase(StockExchangeConstants.buyAction) || actionString.equalsIgnoreCase(StockExchangeConstants.sellAction)) {
			action = actionString;
		}else {
			throw new IllegalArgumentException("Bad Input for order action");
		}

		String quantityString = inputParameters[4].trim();
		quantity = Integer.parseInt(quantityString);

		String priceString = inputParameters[5].trim();
		price = Double.parseDouble(priceString);

		// * Implementing Factor Method Design Pattern - Dependency Inversion Principal
		order = new Order().createOrder(action);	
		order.setOrderID(orderID)
		.setOrderKey(orderKey)
		.setorderTimeStamp(orderDate.getTime())
		.setOrderQuantity(quantity)
		.setOrderPrice(price);
		return order;

	}
}
