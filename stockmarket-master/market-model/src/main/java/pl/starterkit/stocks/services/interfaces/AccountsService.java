package pl.starterkit.stocks.services.interfaces;

import java.time.LocalDate;
import java.util.Optional;

import pl.starterkit.stocks.model.Account;
import pl.starterkit.stocks.model.Session;
import pl.starterkit.stocks.strategy.AccountStrategy;

public interface AccountsService {

	Session findOrCreateCurrentSession(Account account, LocalDate date);

	Account createAccount(String name, Optional<AccountStrategy> optionalStrategy);

}
