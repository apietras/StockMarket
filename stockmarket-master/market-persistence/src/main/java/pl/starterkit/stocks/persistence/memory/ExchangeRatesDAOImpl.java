package pl.starterkit.stocks.persistence.memory;

import java.time.LocalDate;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.ExchangeRate;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.persistence.ExchangeRatesDAO;

@Repository
public class ExchangeRatesDAOImpl extends AbstractDAOImpl<String, ExchangeRate> implements ExchangeRatesDAO {

	@Override
	public ExchangeRate findByCurrencyAndDate(Currency currency, LocalDate date) {
		return objectsMap.values().stream().filter(rate->rate.getDate().equals(date))
				.filter(rate->rate.getCurrency().equals(currency)).findAny().get();
	}

}
