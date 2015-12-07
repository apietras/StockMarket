package pl.starterkit.stocks.services.interfaces;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.Session;
import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.model.Wallet;

/**
 * This internal service executes strategies and passes orders for
 * processing upon day completion, initiated by SessionService.
 * 
 * @author aniapietras
 *
 */
public interface SessionsService {

	Session createNewSession(LocalDate date, Optional<Session> closedSession, Set<Wallet> initialWallets);

}
