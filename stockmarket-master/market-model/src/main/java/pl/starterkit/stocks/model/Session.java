package pl.starterkit.stocks.model;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.enums.OrderStatus;
import pl.starterkit.stocks.model.interfaces.Identifable;

@Data
@AllArgsConstructor
@Setter(AccessLevel.NONE)
public class Session implements Identifable<String> {
	
	public static Comparator<? super Session> byDateComparator = new Comparator<Session>() {
		@Override
		public int compare(Session o1, Session o2) {
			return o1.getDate().compareTo(o2.getDate());
		}
	};
	
	private String id;
	private Boolean closed;
	private LocalDate date;
	private Set<Wallet> wallets;
	
	/**
	 * All stocks owned by account owner after this session.
	 * 
	 * This field might be null before the session is closed.
	 */
	private Set<StockWallet> stockWallets;
	
	/**
	 * A list of orders placed during this session.
	 */
	private List<Order> orders;
	
	public List<Order> getOrdersInStatus(OrderStatus status) {
		return orders.stream().filter(order -> order.getStatus().equals(status)).collect(Collectors.toList());
	}
	
	public Wallet getWalletInCurrency(Currency currency) {
		Optional<Wallet> walletOptional = wallets.stream().filter(wallet -> wallet.getCurrency().equals(currency)).findFirst();
		if (walletOptional.isPresent()) {
			return walletOptional.get();
		}
		throw new IllegalStateException("Session does not contain wallet for given currency");
	}

	public Optional<StockWallet> getStockWalletForStock(Stock stock) {
		return stockWallets.stream().filter(wallet -> wallet.getStock().equals(stock)).findFirst();
	}
}
