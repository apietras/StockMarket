package pl.starterkit.stocks;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import pl.starterkit.stocks.form.NewAccountRequest;
import pl.starterkit.stocks.form.PlaceOrderRequest;
import pl.starterkit.stocks.form.UploadStocksPricesRequest;
import pl.starterkit.stocks.form.UploadStocksPricesRequest.StockPrice;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.enums.OrderStatus;
import pl.starterkit.stocks.model.enums.OrderType;
import pl.starterkit.stocks.services.interfaces.TradingOfficeService;
import pl.starterkit.stocks.webto.AccountTo;
import pl.starterkit.stocks.webto.OrderTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TradingOffice.class)
@WebIntegrationTest
public class FunctionalTest {
	
	private static final String SAMPLE_STOCK_SYMBOL = "SAMPLESTOCK";

	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private TradingOfficeService tradingOfficeService;
	
	@Test
	public void applicationStarts() {
		//do nothing, if no exceptions are thrown, it means that application started successfully
	}
	
	/*
		a) wgraj dane dla akcji (dowolna z metod StocksDataService lub StocksServiceImpl.addStockValue() // StocksApiController.putData [dane jako string z plikiem CSV] lub StocksApiController.postData [dane w strukturze] )
		a) otwórz dzień (TradingOfficeServiceImpl.openNextDay() // TradingOfficeApiController.openNextDay())
		b) złóż zlecenie dla danego konta (OrdersServiceImpl.placeOrder() // OrdersApiController.placeOrder() )
		c) zamknij dzień (TradingOfficeServiceImpl.closeDay() // TradingOfficeApiController.closeDay())
		d) pobierz Order żeby zweryfikować status (OdersServiceImpl.getById() // OrdersApiController.getOrder)
	 */
	
	@Test
	public void endToEndTest() {
		//create account for manual trading
		NewAccountRequest request = new NewAccountRequest("ManualAccount", null);
		AccountTo manualAccount = restTemplate.postForObject(uri("/accounts/create"), request, AccountTo.class);
		
		//create account with CouchPotatoStrategy as a strategy
		NewAccountRequest automaticRequest = new NewAccountRequest("AutomaticAccount", "pl.starterkit.stocks.strategy.implementation.CouchPotatoStrategy");
		AccountTo automaticAccount = restTemplate.postForObject(uri("/accounts/create"), automaticRequest, AccountTo.class);
		
		//put stocks data
		UploadStocksPricesRequest uploadPricesRequest = new UploadStocksPricesRequest();
		uploadPricesRequest.setStockPrices(Collections.singletonList(new StockPrice(SAMPLE_STOCK_SYMBOL, BigDecimal.TEN, tradingOfficeService.getCurrentDate())));
		restTemplate.postForEntity(uri("/stocks/prices/"), uploadPricesRequest, String.class);
		
		//place order that is sure to execute with 'automated' account
		PlaceOrderRequest successRequest = new PlaceOrderRequest(automaticAccount.getId(), SAMPLE_STOCK_SYMBOL, Currency.PLN, 10, BigDecimal.valueOf(50), OrderType.BID);
		OrderTo boundToSuccessOrder = restTemplate.postForObject(uri("/orders/place"), successRequest, OrderTo.class);
		
		//place order that can't execute with 'manual' account
		PlaceOrderRequest failRequest = new PlaceOrderRequest(automaticAccount.getId(), SAMPLE_STOCK_SYMBOL, Currency.PLN, 10, BigDecimal.valueOf(50), OrderType.ASK);
		OrderTo boundToFailOrder = restTemplate.postForObject(uri("/orders/place"), failRequest, OrderTo.class);
		
		//close day
		restTemplate.getForObject(uri("/office/closeDay"), String.class);
		
		//verify order statuses
		OrderTo succeededOrder = restTemplate.getForObject(uri("/orders/"+boundToSuccessOrder.getId()), OrderTo.class);
		Assert.assertEquals(OrderStatus.EXECUTED, succeededOrder.getStatus());
		
		OrderTo failedOrder = restTemplate.getForObject(uri("/orders/"+boundToFailOrder.getId()), OrderTo.class);
		Assert.assertEquals(OrderStatus.CANCELLED, failedOrder.getStatus());
	}

	private String uri(String uri) {
		return "http://localhost:8080" + uri;
	}
	
}
