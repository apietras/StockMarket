package pl.starterkit.stocks.model;

import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.interfaces.Identifable;

/**
 * A class representing wallet of given currency
 */
@Data
@AllArgsConstructor
@Setter(AccessLevel.NONE)
public class Wallet implements Identifable<String> {
	
	private String id;
	
	private Currency currency;
	
	private BigDecimal amount;
	
	public void setAmount(BigDecimal amount) {
		if (amount.signum() == -1) {
			throw new IllegalArgumentException("Wallet cannot hold a negative amount of given currency");
		}
		this.amount = amount;
	}
}
