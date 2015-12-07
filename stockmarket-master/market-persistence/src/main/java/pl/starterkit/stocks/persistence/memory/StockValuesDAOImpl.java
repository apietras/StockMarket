package pl.starterkit.stocks.persistence.memory;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.StockValue;
import pl.starterkit.stocks.persistence.StockValuesDAO;

@Repository
public class StockValuesDAOImpl extends AbstractDAOImpl<String, StockValue> implements StockValuesDAO {
	
}
