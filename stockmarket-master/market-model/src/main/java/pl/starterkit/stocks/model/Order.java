package pl.starterkit.stocks.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.enums.OrderStatus;
import pl.starterkit.stocks.model.enums.OrderType;
import pl.starterkit.stocks.model.interfaces.Identifable;

/**
 * Represents particular stock order.
 */

@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor(access=AccessLevel.PROTECTED)
public class Order implements Identifable<String> {
	private String id;
	private Stock stock;
	private Currency currency;
	private BigDecimal offerPrice;
	private Integer offerAmount;
	private BigDecimal transactionPrice;
	private Integer transactionAmount;

	/** Order status - whether the transaction has been executed or not */
	private OrderStatus status;

	/** Type of order - buy (BID) or sell (ASK) */
	private OrderType type;

	public Order(String id, Stock stock, Currency currency, BigDecimal offerPrice, Integer offerAmount,
			OrderStatus status, OrderType type) {
		super();
		this.id = id;
		this.stock = stock;
		this.currency = currency;
		this.offerPrice = offerPrice;
		this.offerAmount = offerAmount;
		this.status = status;
		this.type = type;
		// TODO: calculate according to rules
		this.transactionPrice = offerPrice.multiply(getPriceMultiplier());
		this.transactionAmount = (int) Math.round(offerAmount * getAmountMultiplier());
	}

	private Double getAmountMultiplier() {
		double randomMultiplier = 0.8 + Math.random() * 0.2;
		return randomMultiplier;
	}

	private BigDecimal getPriceMultiplier() {
		double randomMultiplier = Math.random() * 0.02;
		BigDecimal multiplierBigDecimal = BigDecimal.valueOf(randomMultiplier).setScale(2, BigDecimal.ROUND_HALF_UP);
		return (this.type == OrderType.BID) ? BigDecimal.ONE.add(multiplierBigDecimal)
				: BigDecimal.ONE.subtract(multiplierBigDecimal);
	}

	public void cancel() {
		if (status != OrderStatus.PLACED) {
			throw new IllegalStateException("Cannot cancel order that is not in pending state");
		}
		status = OrderStatus.CANCELLED;
	}

	public void execute() {
		if (status != OrderStatus.PLACED) {
			throw new IllegalStateException("Cannot execute order that is not in pending state");
		}
		status = OrderStatus.EXECUTED;
	}
	
	public static Order create(Stock stock, Currency currency, BigDecimal offerPrice, Integer offerAmount, OrderType type) {
		return new Order(UUID.randomUUID().toString(), stock, currency, offerPrice, offerAmount, OrderStatus.PLACED, type);
	}
}
