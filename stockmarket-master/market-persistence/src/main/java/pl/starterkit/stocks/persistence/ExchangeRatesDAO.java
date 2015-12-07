package pl.starterkit.stocks.persistence;

import java.time.LocalDate;

import pl.starterkit.stocks.model.ExchangeRate;
import pl.starterkit.stocks.model.enums.Currency;

public interface ExchangeRatesDAO extends AbstractDAO<String, ExchangeRate> {

	ExchangeRate findByCurrencyAndDate(Currency currency, LocalDate date);
	
}
