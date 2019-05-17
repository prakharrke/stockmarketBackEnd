package order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import constants.StockExchangeConstants;

public class BuyOrder extends Order {

	public String toString() {
		Date orderTime = new Date(this.getOrderTimeStamp());
		DateFormat dateFormat = new SimpleDateFormat("hh:mm");
		String orderTimeString = dateFormat.format(orderTime);
		StringBuilder buyOrderString = new StringBuilder();
		buyOrderString.append(StockExchangeConstants.HASH)
					  .append(this.getOrderID())
					  .append(StockExchangeConstants.SPACE)
					  .append(orderTimeString)
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getOrderKey())
					  .append(StockExchangeConstants.SPACE)
					  .append(StockExchangeConstants.buyAction)
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getOrderQuantity())
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getOrderPrice());
		return  buyOrderString.toString();
	}
}
