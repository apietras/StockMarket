package pl.starterkit.stocks.persistence;

import java.util.List;

import pl.starterkit.stocks.model.Account;

public interface AccountsDAO extends AbstractDAO<String, Account> {

	List<Account> findAllAutomated();

}
