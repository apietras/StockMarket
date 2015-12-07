package pl.starterkit.stocks.persistence.memory;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.persistence.StocksDAO;

@Repository
public class StocksDAOImpl extends AbstractDAOImpl<String, Stock> implements StocksDAO {

}
