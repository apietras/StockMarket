package pl.starterkit.stocks;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.starterkit.stocks.model.Account;
import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.enums.OrderStatus;
import pl.starterkit.stocks.model.enums.OrderType;
import pl.starterkit.stocks.services.StocksDataService;
import pl.starterkit.stocks.services.interfaces.AccountsService;
import pl.starterkit.stocks.services.interfaces.OrdersService;
import pl.starterkit.stocks.services.interfaces.StocksService;
import pl.starterkit.stocks.services.interfaces.TradingOfficeService;
import pl.starterkit.stocks.strategy.AccountStrategy;
import pl.starterkit.stocks.strategy.implementation.CouchPotatoStrategy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TradingOfficeApplication.class)
@WebIntegrationTest
public class FunctionalServicesTest {
	
	private static final String SAMPLE_STOCK_SYMBOL = "SAMPLESTOCK";

	@Autowired
	private AccountsService accountsService;
	
	@Autowired
	private StocksDataService stocksDataService;
	
	@Autowired
	private StocksService stocksService;
	
	@Autowired
	private TradingOfficeService tradingOfficeService;
	
	@Autowired
	private OrdersService ordersService;
	
	@Test
	public void applicationStarts() {
		//do nothing, if no exceptions are thrown, it means that application started successfully
	}
	
	@Test
	public void endToEndTest() {
		//create account for manual trading
		Account manualAccount = accountsService.createAccount("ManualAccount", Optional.empty());
		
		//create account with CouchPotatoStrategy as a strategy
		AccountStrategy strategy = new CouchPotatoStrategy();
		Account automaticAccount = accountsService.createAccount("AutomaticAccount", Optional.of(strategy));
		
		//put stocks data
		stocksDataService.addStockData(SAMPLE_STOCK_SYMBOL, BigDecimal.TEN, tradingOfficeService.getCurrentDate());
		
		//place order that is sure to execute with 'automated' account
		Stock stock = stocksService.getStockBySymbol(SAMPLE_STOCK_SYMBOL);
		Order boundToSuccessOrder = Order.create(stock, Currency.PLN, BigDecimal.valueOf(50), 10, OrderType.BID);
		boundToSuccessOrder = ordersService.createOrder(automaticAccount.getId(), boundToSuccessOrder);
		
		//place order that can't execute with 'manual' account
		Order boundToFailOrder = Order.create(stock, Currency.PLN, BigDecimal.valueOf(50), 10, OrderType.ASK);
		boundToFailOrder = ordersService.createOrder(manualAccount.getId(), boundToFailOrder);
		
		//close day
		tradingOfficeService.closeDay();
		
		//verify order statuses
		Order succeededOrder = ordersService.getById(boundToSuccessOrder.getId());
		Assert.assertEquals(OrderStatus.EXECUTED, succeededOrder.getStatus());
		
		Order failedOrder = ordersService.getById(boundToFailOrder.getId());
		Assert.assertEquals(OrderStatus.CANCELLED, failedOrder.getStatus());
	}
}
