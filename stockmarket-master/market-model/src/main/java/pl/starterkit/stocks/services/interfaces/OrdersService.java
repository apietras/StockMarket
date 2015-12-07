package pl.starterkit.stocks.services.interfaces;

import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.model.Wallet;

public interface OrdersService {

	void processOrder(Order order, Wallet currencyWallet, StockWallet stockWallet);

	Order createOrder(String accountId, Order order);

	Order getById(String orderId);

}
