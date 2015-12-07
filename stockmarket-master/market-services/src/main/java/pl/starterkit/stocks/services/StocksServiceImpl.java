package pl.starterkit.stocks.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.model.StockValue;
import pl.starterkit.stocks.persistence.StockValuesDAO;
import pl.starterkit.stocks.persistence.StocksDAO;
import pl.starterkit.stocks.services.interfaces.StocksService;
import pl.starterkit.stocks.services.interfaces.StocksValuesReadingService;
import pl.starterkit.stocks.services.interfaces.TradingOfficeService;

@Service
public class StocksServiceImpl implements StocksService, StocksValuesReadingService {

	@Autowired
	private StocksDAO stocksDao;

	@Autowired
	private StockValuesDAO stockValuesDao;
	
	@Autowired
	private TradingOfficeService tradingOffice;
	
	@Override
	public Stock getStockBySymbol(String symbol) {
		return stocksDao.getById(symbol);
	}
	
	@Override
	public List<Stock> getAllStocks() {
		return stocksDao.getAll();
	}
	
	@Override
	public Optional<BigDecimal> getStockPrice(String symbol, LocalDate date) {
		if (!date.isAfter(tradingOffice.getCurrentDate())) {
			Stock stock = getStockBySymbol(symbol);
			Optional<StockValue> valueForDay = stock.getValueForDay(date);
			if (valueForDay.isPresent()) {
				return Optional.of(valueForDay.get().getPrice());
			}
		}
		return Optional.empty();
	}
	
	@Override
	public Stock addStockValue(Stock stock, StockValue stockValue) {
		stockValue = stockValuesDao.create(stockValue);
		stock.addStockValue(stockValue);
		return stocksDao.update(stock);
	}

	@Override
	public Stock createStock(String symbol) {
		return stocksDao.create(new Stock(symbol));
	}
	
}
