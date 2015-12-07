package pl.starterkit.stocks.webto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.enums.OrderStatus;
import pl.starterkit.stocks.model.enums.OrderType;

@Data
@NoArgsConstructor
public class OrderTo {

	public OrderTo(Order order) {
		this.id = order.getId();
		this.stock = order.getStock().getSymbol();
		this.currency = order.getCurrency();
		this.offerAmount = order.getOfferAmount();
		this.offerPrice = order.getOfferPrice();
		this.transactionAmount = order.getTransactionAmount();
		this.transactionPrice = order.getTransactionPrice();
		this.status = order.getStatus();
		this.type = order.getType();
	}
	
	private String id;
	private String stock;
	private Currency currency;
	private BigDecimal offerPrice;
	private Integer offerAmount;
	private BigDecimal transactionPrice;
	private Integer transactionAmount;
	private OrderStatus status;
	private OrderType type;
	
}
