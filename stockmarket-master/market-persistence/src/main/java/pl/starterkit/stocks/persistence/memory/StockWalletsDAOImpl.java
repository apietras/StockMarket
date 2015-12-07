package pl.starterkit.stocks.persistence.memory;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.StockWallet;
import pl.starterkit.stocks.persistence.StockWalletsDAO;

@Repository
public class StockWalletsDAOImpl extends AbstractDAOImpl<String, StockWallet> implements StockWalletsDAO {

}
