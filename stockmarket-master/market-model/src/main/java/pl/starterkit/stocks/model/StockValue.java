package pl.starterkit.stocks.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.starterkit.stocks.model.interfaces.Identifable;
/**
 * Represents value of given stock.
 */
@Data
@Setter(AccessLevel.NONE)
@AllArgsConstructor
public class StockValue implements Identifable<String> {
	
	private String id;
	
	private BigDecimal price;
	
	private LocalDate date;
	
	/**
	 * Comparator to compare two StockValue objects by date only.
	 */
	public static Comparator<StockValue> byDateComparator = new Comparator<StockValue>() {
		@Override
		public int compare(StockValue o1, StockValue o2) {
			return o1.getDate().compareTo(o2.getDate());
		}
	} ;
	
}
