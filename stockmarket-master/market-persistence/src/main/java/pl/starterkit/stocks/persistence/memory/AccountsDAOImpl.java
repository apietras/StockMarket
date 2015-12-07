package pl.starterkit.stocks.persistence.memory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.Account;
import pl.starterkit.stocks.persistence.AccountsDAO;

@Repository
public class AccountsDAOImpl extends AbstractDAOImpl<String, Account> implements AccountsDAO {

	public List<Account> findAllAutomated() {
		return objectsMap.values().stream().filter(account->account.isAutomated()).collect(Collectors.toList());
	}

}
