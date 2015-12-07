package pl.starterkit.stocks.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.interfaces.Identifable;

/**
 * Represents currency exchange rate relative to base currency in which stocks are valued.
 * Assumes fixed spread.
 */
@Data
@AllArgsConstructor
@Setter(AccessLevel.NONE)
public class ExchangeRate implements Identifable<String> {

	private static final BigDecimal BID_SELL_SPREAD = BigDecimal.valueOf(2L, 1); //BigDecimal for 0.2
	
	private String id;
	private Currency currency;
	private LocalDate date;
	private BigDecimal exchangeRate;
	
	public BigDecimal getSellRate() {
		return (currency.isBase()) ? exchangeRate : exchangeRate.multiply(BigDecimal.ONE.add(BID_SELL_SPREAD));
	}
	
	public BigDecimal getBuyRate() {
		return (currency.isBase()) ? exchangeRate : exchangeRate.multiply(BigDecimal.ONE.subtract(BID_SELL_SPREAD));
	}
}
