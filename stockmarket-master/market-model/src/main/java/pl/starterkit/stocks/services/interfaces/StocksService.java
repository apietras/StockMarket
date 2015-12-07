package pl.starterkit.stocks.services.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.model.StockValue;

/**
 * Provides (historical) information about stock prices.
 *
 * 
 */
public interface StocksService {

	Stock addStockValue(Stock stock, StockValue stockValue);

	Optional<BigDecimal> getStockPrice(String symbol, LocalDate date);

	List<Stock> getAllStocks();

	Stock getStockBySymbol(String symbol);

	Stock createStock(String symbol);

}
