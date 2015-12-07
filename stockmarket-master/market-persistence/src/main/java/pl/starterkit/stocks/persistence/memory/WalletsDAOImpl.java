package pl.starterkit.stocks.persistence.memory;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.Wallet;
import pl.starterkit.stocks.persistence.WalletsDAO;

@Repository
public class WalletsDAOImpl extends AbstractDAOImpl<String, Wallet> implements WalletsDAO {

}
