package pl.starterkit.stocks.strategy.implementation;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.model.Wallet;
import pl.starterkit.stocks.services.interfaces.ExchangeRatesService;
import pl.starterkit.stocks.services.interfaces.StocksValuesReadingService;
import pl.starterkit.stocks.strategy.AccountStrategy;

@Slf4j
public class CouchPotatoStrategy implements AccountStrategy {

	@Override
	public void init(StocksValuesReadingService stocksService, ExchangeRatesService exchangeRatesService) {
		log.info("Couch potato strategy initialized");
	}

	@Override
	public List<Order> placeOrders(LocalDate date, Set<Wallet> wallets, Set<StockWallet> stockWallets) {
		log.info("Couch potato strategy executing for day {}", date);
		return Collections.emptyList();
	}

}
