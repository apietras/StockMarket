package pl.starterkit.stocks.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.starterkit.stocks.model.ExchangeRate;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.persistence.ExchangeRatesDAO;
import pl.starterkit.stocks.services.interfaces.ExchangeRatesService;
import pl.starterkit.stocks.services.interfaces.TradingOfficeService;

@Service
public class ExchangeRateServiceImpl implements ExchangeRatesService {

	@Autowired
	private ExchangeRatesDAO exchangeRatesDao;
	
	@Autowired
	private TradingOfficeService tradingOfficeService;
	
	@Override
	public ExchangeRate getExchangeRate(Currency currency) {
		return getExchangeRate(currency, tradingOfficeService.getCurrentDate());
	}
	
	@Override
	public ExchangeRate getExchangeRate(Currency currency, LocalDate date) {
		if (date.isAfter(tradingOfficeService.getCurrentDate())) {
			throw new IllegalArgumentException("Exchange rates for future are not available");
		}
		if (currency.isBase()) {
			return new ExchangeRate("baseExchangeRate", currency, date, BigDecimal.ONE);
		}
		ExchangeRate rate = exchangeRatesDao.findByCurrencyAndDate(currency, date);
		if (rate == null) {
			rate = createExchangeRate(currency, date);
		}
		return rate;
	}

	protected ExchangeRate createExchangeRate(Currency currency, LocalDate date) {
		ExchangeRate exchangeRate = new ExchangeRate(UUID.randomUUID().toString(), currency, date, currency.getRandomExchangeRate());
		return exchangeRatesDao.create(exchangeRate);
	}
	
	@Override
	public BigDecimal sellCurrency(Currency selling, Currency buying, BigDecimal sellingAmount) {
		return getExchangeRate(selling, buying).multiply(sellingAmount);
	}
	
	@Override
	public BigDecimal buyCurrency(Currency selling, Currency buying, BigDecimal buyingAmount) {
		return buyingAmount.divide(getExchangeRate(selling, buying));
	}

	@Override
	public BigDecimal getExchangeRate(Currency selling, Currency buying) {
		return getExchangeRate(selling, buying, tradingOfficeService.getCurrentDate());
	}

	@Override
	public BigDecimal getExchangeRate(Currency selling, Currency buying, LocalDate date) {
		if (date.isAfter(tradingOfficeService.getCurrentDate())) {
			throw new IllegalArgumentException("Exchange rates for future are not available");
		}
		if (selling.equals(buying)) {
			return BigDecimal.ONE;
		} else if (selling.isBase()) {
			return getExchangeRate(buying, date).getSellRate();
		} else if (buying.isBase()) {
			return getExchangeRate(selling, date).getBuyRate();
		} else {
			return getExchangeRate(buying).getSellRate().multiply(getExchangeRate(selling).getBuyRate());
		}
	}

}
