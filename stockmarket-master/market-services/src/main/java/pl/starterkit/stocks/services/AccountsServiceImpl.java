package pl.starterkit.stocks.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.starterkit.stocks.interfaces.DayCloseObserver;
import pl.starterkit.stocks.model.Account;
import pl.starterkit.stocks.model.Session;
import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.model.Wallet;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.persistence.AccountsDAO;
import pl.starterkit.stocks.persistence.WalletsDAO;
import pl.starterkit.stocks.services.interfaces.AccountsService;
import pl.starterkit.stocks.services.interfaces.ExchangeRatesService;
import pl.starterkit.stocks.services.interfaces.SessionsService;
import pl.starterkit.stocks.services.interfaces.StocksService;
import pl.starterkit.stocks.services.interfaces.StocksValuesReadingService;
import pl.starterkit.stocks.strategy.AccountStrategy;

@Service
public class AccountsServiceImpl implements AccountsService, DayCloseObserver {

	@Autowired
	private AccountsDAO accountsDao;
	
	@Autowired
	private WalletsDAO walletsDao;
	
	@Autowired
	private StocksService stocksService;
	
	@Autowired
	private StocksValuesReadingService stocksValueReadingService;
	
	@Autowired
	private ExchangeRatesService exchangeRatesService;

	@Autowired
	private SessionsService sessionsService;

	@Override
	public void notifyBeforeDayCloses(LocalDate date) {
		List<Account> accounts = accountsDao.findAllAutomated();
		for (Account account : accounts) {
			Session currentSession = findOrCreateCurrentSession(account, date);
			//we're passing copies to prevent any manipulation on actual, real objects
			Set<Wallet> wallets = currentSession.getWallets().stream()
					.map(wallet -> new Wallet(wallet.getId(), wallet.getCurrency(), wallet.getAmount()))
					.collect(Collectors.toSet());
			Set<StockWallet> stockWallets = currentSession.getStockWallets().stream()
					.map(wallet -> new StockWallet(wallet.getId(), wallet.getStock(), wallet.getAmount()))
					.collect(Collectors.toSet());
			account.getStrategy().get().placeOrders(date, wallets, stockWallets);
		}
	}

	@Override
	public Account createAccount(String name, Optional<AccountStrategy> optionalStrategy) {
		Account account = new Account(UUID.randomUUID().toString(), name, optionalStrategy, createInitialWallets(), new TreeSet<Session>(Session.byDateComparator));
		if (optionalStrategy.isPresent()) {
			optionalStrategy.get().init(stocksValueReadingService, exchangeRatesService);
		}
		return accountsDao.create(account);
	}
	
	@Override
	public Session findOrCreateCurrentSession(Account account, LocalDate date) {
		Optional<Session> session = account.getSessionForDate(date);
		if (!session.isPresent()) {
			Optional<Session> lastSession = account.getLatestSessionBeforeDate(date);
			return sessionsService.createNewSession(date, lastSession, account.getInitialWallets());
		}
		return session.get();
	}

	private Set<Wallet> createInitialWallets() {
		Set<Wallet> wallets = new HashSet<>();
		wallets.add(walletsDao.create(new Wallet(UUID.randomUUID().toString(), Currency.PLN, BigDecimal.valueOf(5000))));
		wallets.add(walletsDao.create(new Wallet(UUID.randomUUID().toString(), Currency.EUR, BigDecimal.valueOf(1250))));
		return wallets;
	}
	
	
}
