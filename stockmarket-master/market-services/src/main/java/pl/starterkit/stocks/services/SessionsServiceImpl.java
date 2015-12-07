package pl.starterkit.stocks.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import pl.starterkit.stocks.interfaces.DayCloseObserver;
import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.Session;
import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.model.Wallet;
import pl.starterkit.stocks.persistence.SessionsDAO;
import pl.starterkit.stocks.persistence.StockWalletsDAO;
import pl.starterkit.stocks.persistence.WalletsDAO;
import pl.starterkit.stocks.services.interfaces.OrdersService;
import pl.starterkit.stocks.services.interfaces.SessionsService;

@Slf4j
@Service
public class SessionsServiceImpl implements SessionsService, DayCloseObserver {

	@Autowired
	private SessionsDAO sessionsDao;
	
	@Autowired
	private WalletsDAO walletsDao;
	
	@Autowired
	private StockWalletsDAO stockWalletsDao;
	
	@Autowired
	private OrdersService ordersService;
	
	@Override
	public Session createNewSession(LocalDate date, Optional<Session> closedSession, Set<Wallet> initialWallets) {
		Session newSession = null;
		if (closedSession.isPresent()) {
			Session previousSession = closedSession.get();
			Set<Wallet> wallets = copyWalletsSet(previousSession.getWallets());
			Set<StockWallet> stockWallets = copyStockWalletsSet(previousSession.getStockWallets());
			newSession = new Session(UUID.randomUUID().toString(), false, date, wallets, stockWallets, new ArrayList<>());
		} else {
			Set<Wallet> wallets = copyWalletsSet(initialWallets);
			newSession = new Session(UUID.randomUUID().toString(), false, date, wallets, new HashSet<>(), new ArrayList<>());
		}
		return sessionsDao.create(newSession);
	}

	@Override
	public void notifyAfterDayCloses(LocalDate date) {
		List<Session> sessions = sessionsDao.findByDate(date);
		for (Session session : sessions) {
			List<Order> orders = session.getOrders();
			for (Order order : orders) {
				//TOO: if order not cancelled
				Wallet currencyWallet = session.getWalletInCurrency(order.getCurrency());
				StockWallet stockWallet = getOrCreateStockWalletOfSession(session, order.getStock());
				log.info("Order {} sent for processing", order.getId());
				ordersService.processOrder(order, currencyWallet, stockWallet);
			}
		}
	}

	private StockWallet getOrCreateStockWalletOfSession(Session session, Stock stock) {
		Optional<StockWallet> stockWalletOptional = session.getStockWalletForStock(stock);
		if (stockWalletOptional.isPresent()) {
			return stockWalletOptional.get();
		} else {
			StockWallet stockWallet = new StockWallet(UUID.randomUUID().toString(), stock, 0);
			stockWallet = stockWalletsDao.create(stockWallet);
			return stockWallet;
		}
	}
	
	private Set<StockWallet> copyStockWalletsSet(Set<StockWallet> stockWallets) {
		return new HashSet<>(stockWallets.stream()
				.map(wallet -> new StockWallet(UUID.randomUUID().toString(), wallet.getStock(), wallet.getAmount()))
				.map(newWallet -> stockWalletsDao.create(newWallet)).collect(Collectors.toSet()));
	}

	private Set<Wallet> copyWalletsSet(Set<Wallet> wallets) {
		return new HashSet<>(wallets.stream()
				.map(wallet -> new Wallet(UUID.randomUUID().toString(), wallet.getCurrency(), wallet.getAmount()))
				.map(newWallet -> walletsDao.create(newWallet)).collect(Collectors.toSet()));
	}


}
