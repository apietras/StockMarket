package pl.starterkit.stocks.webto;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.starterkit.stocks.model.Account;

@Data
@NoArgsConstructor
public class AccountTo {
	public AccountTo(Account account) {
		this.id = account.getId();
		this.name = account.getName();
		this.strategyClass = account.getStrategy().isPresent() ? account.getStrategy().get().getClass().getName() : "";
		this.initialWallets = account.getInitialWallets().stream().map(wallet -> new WalletTo(wallet.getId(), wallet.getCurrency(), wallet.getAmount())).collect(Collectors.toSet());
		this.automated = account.isAutomated();
	}
	private String id;
	private String name;
	private String strategyClass;
	private Set<WalletTo> initialWallets;
	private Boolean automated;
}
