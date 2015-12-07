package pl.starterkit.stocks.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.starterkit.stocks.model.Account;
import pl.starterkit.stocks.model.ExchangeRate;
import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.Session;
import pl.starterkit.stocks.model.StockValue;
import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.model.Wallet;
import pl.starterkit.stocks.model.enums.OrderType;
import pl.starterkit.stocks.persistence.AccountsDAO;
import pl.starterkit.stocks.persistence.OrdersDAO;
import pl.starterkit.stocks.persistence.StockWalletsDAO;
import pl.starterkit.stocks.persistence.WalletsDAO;
import pl.starterkit.stocks.services.interfaces.AccountsService;
import pl.starterkit.stocks.services.interfaces.ExchangeRatesService;
import pl.starterkit.stocks.services.interfaces.OrdersService;
import pl.starterkit.stocks.services.interfaces.TradingOfficeService;

@Slf4j
@Service
public class OrdersServiceImpl implements OrdersService {

	@Autowired
	private AccountsService accountsService;
	
	@Autowired
	private AccountsDAO accountsDao;
	
	@Autowired
	private OrdersDAO ordersDao;
	
	@Autowired
	private WalletsDAO walletsDao;
	
	@Autowired
	private StockWalletsDAO stockWalletsDao;
	
	@Autowired
	private ExchangeRatesService exchangeRateService;
	
	@Autowired
	private TradingOfficeService tradingOffice;
	
	@Override
	public Order createOrder(String accountId, Order order) {
		Account account = accountsDao.getById(accountId);
		Session accountSession = accountsService.findOrCreateCurrentSession(account, tradingOffice.getCurrentDate());
		order = ordersDao.create(order);
		accountSession.getOrders().add(order);
		return order;
	}
	
	@Override
	public Order getById(String orderId) {
		return ordersDao.getById(orderId);
	}
	
	@Override
	public void processOrder(Order order, Wallet currencyWallet, StockWallet stockWallet) {
		LocalDate date = tradingOffice.getCurrentDate();
		Optional<StockValue> stockPriceOptional = order.getStock().getValueForDay(date);
		if (stockPriceOptional.isPresent()) {
			ExchangeRate exchangeRateObject = exchangeRateService.getExchangeRate(order.getCurrency(), date);
			if (order.getType().equals(OrderType.ASK)) {
				//we are buying the foreign currency after selling stocks, 
				//thus the trading office converts currency as if it's selling it
				if (stockWallet.getAmount() >= order.getTransactionAmount()) {
					BigDecimal exchangeRate = exchangeRateObject.getSellRate();
					BigDecimal transactionPriceInBase = order.getTransactionPrice().multiply(exchangeRate);
					if (transactionPriceInBase.compareTo(stockPriceOptional.get().getPrice())!=1) { //transactionPrice is at most stock price
						BigDecimal tradeValueInCurrency = order.getTransactionPrice().multiply(BigDecimal.valueOf(order.getTransactionAmount()));
						BigDecimal tradeValueInBase = tradeValueInCurrency.multiply(exchangeRate);
						BigDecimal commissionInCurrency = tradingOffice.calculateCommission(tradeValueInBase).divide(exchangeRate);
						BigDecimal transactionIncome = tradeValueInCurrency.subtract(commissionInCurrency);
						updateWallets(currencyWallet, stockWallet, transactionIncome, -order.getTransactionAmount());
						order.execute();
						log.info("Executed transaction {}: sold {} stocks (symbol {}) for {} base each, receiving {} {} ({} base - comission) in total",
								order.getId(), order.getTransactionAmount(), order.getStock().getSymbol(),
								transactionPriceInBase, transactionIncome, order.getCurrency(), tradeValueInBase);
						return;
					} else {
						log.warn("Cancelling order {}, as order price ({} {} = {} base) is higher than market price ({})", 
								order.getId(), order.getTransactionPrice(), order.getCurrency(),
								transactionPriceInBase, stockPriceOptional.get().getPrice());
					}
				} else {
					log.warn("Cancelling order {}, as stock wallet does not contain enough stocks (has {}, order is for {})", order.getId(), stockWallet.getAmount(), order.getTransactionAmount());
				}
			} else { //BID - we're buying
				//we are selling the foreign currency to buy stocks, 
				//thus the trading office converts currency as if it's buying it
				BigDecimal exchangeRate = exchangeRateObject.getBuyRate();
				BigDecimal transactionPriceInBase = order.getTransactionPrice().multiply(exchangeRate);
				if (transactionPriceInBase.compareTo(stockPriceOptional.get().getPrice())!=-1) { //transactionPrice is at least stock price
					BigDecimal tradeValueInCurrency = order.getTransactionPrice().multiply(BigDecimal.valueOf(order.getTransactionAmount()));
					BigDecimal tradeValueInBase = tradeValueInCurrency.multiply(exchangeRate);
					BigDecimal commissionInCurrency = tradingOffice.calculateCommission(tradeValueInBase).divide(exchangeRate);
					BigDecimal transactionCost = tradeValueInCurrency.add(commissionInCurrency);
					if (currencyWallet.getAmount().compareTo(transactionCost)!=-1) { //our wallet contains at least given amount of given currency
						updateWallets(currencyWallet, stockWallet, transactionCost.negate(), order.getTransactionAmount());
						order.execute();
						log.info("Executed transaction {}: bought {} stocks (symbol {}) for {} base each, paying {} {} ({} base + comission) in total",
								order.getId(), order.getTransactionAmount(), order.getStock().getSymbol(),
								transactionPriceInBase, transactionCost, order.getCurrency(), tradeValueInBase);
						return;
					} else {
						log.warn("Cancelling order {}, as wallet does not contain enough currency to execute order (has: {}, needed: {})", 
								order.getId(), currencyWallet.getAmount(), transactionCost);
					}
				} else {
					log.warn("Cancelling order {}, as order price ({} {} = {} base) is lower than market price ({})", 
							order.getId(), order.getTransactionPrice(), order.getCurrency(),
							transactionPriceInBase, stockPriceOptional.get().getPrice());
				}
			}
		} else {
			log.warn("Cancelling order {}, as the stock does not have value for {}", order.getId(), date);
		}
		order.cancel();
	}

	private void updateWallets(Wallet currencyWallet, StockWallet stockWallet,
			BigDecimal currencyAmountChange, Integer stocksAmountChange) {
		stockWallet.setAmount(stockWallet.getAmount()+stocksAmountChange);
		currencyWallet.setAmount(currencyWallet.getAmount().add(currencyAmountChange));
		walletsDao.update(currencyWallet);
		stockWalletsDao.update(stockWallet);
	}
	
}
