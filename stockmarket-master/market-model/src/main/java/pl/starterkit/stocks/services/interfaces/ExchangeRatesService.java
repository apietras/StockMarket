package pl.starterkit.stocks.services.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;

import pl.starterkit.stocks.model.ExchangeRate;
import pl.starterkit.stocks.model.enums.Currency;

/**
 * This service manages exchange rates, generates new ones when needed and
 * allows accounts to buy/sell currencies.
 */
public interface ExchangeRatesService {

	BigDecimal getExchangeRate(Currency selling, Currency buying, LocalDate date);

	BigDecimal getExchangeRate(Currency selling, Currency buying);

	BigDecimal buyCurrency(Currency selling, Currency buying, BigDecimal buyingAmount);

	BigDecimal sellCurrency(Currency selling, Currency buying, BigDecimal sellingAmount);

	ExchangeRate getExchangeRate(Currency currency, LocalDate date);

	ExchangeRate getExchangeRate(Currency currency);

}
