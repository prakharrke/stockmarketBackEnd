package order;

import constants.StockExchangeConstants;

public class ExecutedOrder extends Order{
	
	public String toString() {
		
		StringBuilder executedOrderString = new StringBuilder();
		executedOrderString.append(StockExchangeConstants.HASH)
					  .append(this.getSellOrder().getOrderID())
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getOrderQuantity())
					  .append(StockExchangeConstants.SPACE)
					  .append(this.getSellOrder().getOrderPrice())
					  .append(StockExchangeConstants.SPACE)
					  .append(StockExchangeConstants.HASH)
					  .append(this.getBuyOrder().getOrderID());
		return  executedOrderString.toString();
	}

}
