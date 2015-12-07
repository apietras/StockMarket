package pl.starterkit.stocks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.persistence.StocksDAO;
import pl.starterkit.stocks.services.StocksDataService;
import pl.starterkit.stocks.services.interfaces.StocksService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TradingOfficeApplication.class)
@WebIntegrationTest
public class StockServicesTest {

	private static final String SAMPLE_STOCK_NAME = "TEST";

	@Autowired
	private StocksDataService stockDataService;

	@Autowired
	private StocksService stocksService;

	@Autowired
	private StocksDAO stocksDao;
	
	@Test
	@DirtiesContext
	public void stockDataServiceCreatesNotExistingStock() {
		Assert.assertNull(stocksDao.getById(SAMPLE_STOCK_NAME));
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.TEN, LocalDate.now());
		Assert.assertNotNull(stocksDao.getById(SAMPLE_STOCK_NAME));
	}
	
	@Test
	@DirtiesContext
	public void stockDataServicePersistsStockValues() {
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.TEN, LocalDate.now());
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.TEN, LocalDate.now().minusDays(1));
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.TEN, LocalDate.now().minusDays(2));
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.TEN, LocalDate.now().minusDays(3));
		Stock stock = stocksDao.getById(SAMPLE_STOCK_NAME);
		Assert.assertNotNull(stock.getValueForDay(LocalDate.now()));
		Assert.assertNotNull(stock.getValueForDay(LocalDate.now().minusDays(3)));
	}
	
	@Test
	@DirtiesContext
	public void stockDataServiceIgnoresInvalidInput() {
		int numberOfProcessedRows = stockDataService.addStockData("INTEL;20011024;105.3");
		Assert.assertEquals(0, numberOfProcessedRows);
	}
	
	@Test
	@DirtiesContext
	public void stockDataServiceprocessesValidInput() {
		int numberOfProcessedRows = stockDataService.addStockData("INTEL,20011024,105.3\nMICROSOFT,20011024,15.1");
		Stock stock = stocksDao.getById("MICROSOFT");
		Assert.assertNotNull(stock);
		Assert.assertEquals(BigDecimal.valueOf(151, 1), stock.getValueForDay(LocalDate.of(2001, 10, 24)).get().getPrice());
		Assert.assertEquals(2, numberOfProcessedRows);
	}
	
	@Test
	@DirtiesContext
	public void stockValueHoldsUntilOverwritten() {
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.TEN, LocalDate.of(2001, 10, 24));
		Stock stock = stocksDao.getById(SAMPLE_STOCK_NAME);
		Assert.assertNotNull(stock);
		Assert.assertEquals(BigDecimal.TEN, stock.getValueForDay(LocalDate.of(2001, 10, 24)).get().getPrice());
		Assert.assertEquals(BigDecimal.TEN, stock.getValueForDay(LocalDate.of(2005, 10, 24)).get().getPrice());
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.ONE, LocalDate.of(2004, 10, 24));
		Assert.assertEquals(BigDecimal.TEN, stock.getValueForDay(LocalDate.of(2001, 10, 24)).get().getPrice());
		Assert.assertEquals(BigDecimal.ONE, stock.getValueForDay(LocalDate.of(2005, 10, 24)).get().getPrice());
	}
	
	@Test
	public void stockValueForFutureIsNotAvailable() {
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.ONE, LocalDate.now().minusDays(10));
		stockDataService.addStockData(SAMPLE_STOCK_NAME, BigDecimal.TEN, LocalDate.now().plusDays(10));
		Optional<BigDecimal> stockValue = stocksService.getStockPrice(SAMPLE_STOCK_NAME, LocalDate.now().plusDays(10));
		Assert.assertFalse(stockValue.isPresent());
	}
	
}
