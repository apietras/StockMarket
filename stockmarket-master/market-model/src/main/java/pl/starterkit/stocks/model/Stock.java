package pl.starterkit.stocks.model;

import java.time.LocalDate;
import java.util.Optional;
import java.util.TreeSet;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import pl.starterkit.stocks.model.interfaces.Identifable;

/**
 * Represents particular stock.
 *
 */
@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class Stock implements Identifable<String> {

	private final String symbol;
	
	@Getter(AccessLevel.NONE)
	private final TreeSet<StockValue> stockValues = new TreeSet<>(StockValue.byDateComparator);
	
	/**
	 * Return a stock value that was valid for given day, or empty if one does not exist
	 * @param date Day for which the lookup is performed
	 * @return Stock value valid for given day or empty
	 */
	public Optional<StockValue> getValueForDay(LocalDate date) {
		//iterating from the end of set == from latest date
		for (StockValue value : stockValues.descendingSet()) {
			if (!value.getDate().isAfter(date)) {
				return Optional.of(value);
			}
		}
		return Optional.empty();
	}
	
	public void addStockValue(StockValue stockValue) {
		stockValues.add(stockValue);
	}

	@Override
	public String getId() {
		return getSymbol();
	}
}
