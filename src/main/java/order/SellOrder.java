package order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import constants.StockExchangeConstants;

public class SellOrder extends Order {
	

	public String toString() {
		Date orderTime = new Date(this.getOrderTimeStamp());
		DateFormat dateFormat = new SimpleDateFormat("hh:mm");
		String orderTimeString = dateFormat.format(orderTime);
		StringBuilder sellOrderString = new StringBuilder();
		sellOrderString.append(StockExchangeConstants.HASH)
					  .append(this.getOrderID())
					  .append(StockExchangeConstants.SPACE)
					  .append(orderTimeString)
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getOrderKey())
					  .append(StockExchangeConstants.SPACE)
					  .append(StockExchangeConstants.sellAction)
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getOrderQuantity())
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getOrderPrice());
		return  sellOrderString.toString();
	}
}
