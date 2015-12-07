package pl.starterkit.stocks.strategy;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.model.Wallet;
import pl.starterkit.stocks.services.interfaces.ExchangeRatesService;
import pl.starterkit.stocks.services.interfaces.StocksValuesReadingService;

public interface AccountStrategy {
	
	public void init(StocksValuesReadingService stocksService, ExchangeRatesService exchangeRatesService);
	
	public List<Order> placeOrders(LocalDate date, Set<Wallet> wallets, Set<StockWallet> stockWallets);
	
}
