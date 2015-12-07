package pl.starterkit.stocks.model;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import pl.starterkit.stocks.model.interfaces.Identifable;
import pl.starterkit.stocks.strategy.AccountStrategy;

/**
 * A single account used for trading.
 */
@Data
@AllArgsConstructor
@Setter(AccessLevel.NONE)
public class Account implements Identifable<String> {
	private String id;
	private String name;
	private Optional<AccountStrategy> strategy;
	private Set<Wallet> initialWallets;
	
	/**
	 * Note: this field is ignore and not returned in serialized data.
	 * All session retrievals should be handled by separate service.
	 */
	@JsonIgnore
	private TreeSet<Session> sessions;
	
	public Optional<Session> getSessionForDate(LocalDate date) {
		Optional<Session> optionalSession = sessions.stream().filter(session -> session.getDate().isEqual(date)).findFirst();
		return optionalSession;
	}

	public Optional<Session> getLatestSessionBeforeDate(LocalDate date) {
		Optional<Session> optionalSession = sessions.stream().filter(session -> session.getDate().isBefore(date))
				.sorted(Session.byDateComparator.reversed()).findFirst();
		return optionalSession;
	}
	
	public boolean isAutomated() {
		return strategy.isPresent();
	}
	
}
