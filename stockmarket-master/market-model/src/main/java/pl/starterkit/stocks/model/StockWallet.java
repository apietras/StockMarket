package pl.starterkit.stocks.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.starterkit.stocks.model.interfaces.Identifable;

/**
 * A class representing wallet of given stock
 *
 */
@Data
@AllArgsConstructor
@Setter(AccessLevel.NONE)
public class StockWallet implements Identifable<String> {

	private String id;

	/** Stock owned by simulation */
	private Stock stock;

	/** Amount of stocks owned */
	private Integer amount;

	public void setAmount(Integer amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Wallet cannot hold a negative amount of given currency");
		}
		this.amount = amount;
	}
}
