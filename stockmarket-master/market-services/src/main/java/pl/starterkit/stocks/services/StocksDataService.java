package pl.starterkit.stocks.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.model.StockValue;
import pl.starterkit.stocks.services.interfaces.StocksService;

@Slf4j
@Service
public class StocksDataService {

	Pattern pattern = Pattern.compile("([A-Z]+),([0-9]{4})([0-9]{2})([0-9]{2}),([0-9]+\\.[0-9]+)");
	
	@Autowired
	private StocksService stocksService;
	
	public void addStockData(String symbol, BigDecimal value, LocalDate date) {
		Stock stock = stocksService.getStockBySymbol(symbol);
		if (stock == null) {
			stock = stocksService.createStock(symbol);
		}
		stocksService.addStockValue(stock , new StockValue(UUID.randomUUID().toString(), value, date));
	}
	
	public int addStockData(String csvData) {
		int processedLines = 0;
		String[] lines = csvData.split("\n");
		for (String line : lines) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				LocalDate date = LocalDate.of(
						Integer.parseInt(matcher.group(2)), 
						Integer.parseInt(matcher.group(3)), 
						Integer.parseInt(matcher.group(4)));
				BigDecimal value = new BigDecimal(matcher.group(5));
				addStockData(matcher.group(1), value, date);
				processedLines++;
			} else {
				log.warn("Invalid data line format");
			}
		}
		return processedLines;
	}
	
}
