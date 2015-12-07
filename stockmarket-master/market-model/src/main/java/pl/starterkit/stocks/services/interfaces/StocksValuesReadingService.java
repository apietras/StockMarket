package pl.starterkit.stocks.services.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Provides (historical) information about stock prices not allowing to manipulate data.
 *
 * Used only for strategies
 * 
 */
public interface StocksValuesReadingService {

	Optional<BigDecimal> getStockPrice(String symbol, LocalDate date);

}
