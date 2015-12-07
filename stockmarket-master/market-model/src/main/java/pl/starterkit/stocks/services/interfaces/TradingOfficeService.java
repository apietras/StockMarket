package pl.starterkit.stocks.services.interfaces;

import java.math.BigDecimal;
import java.time.LocalDate;

import pl.starterkit.stocks.services.model.TradingOfficeStatus;


public interface TradingOfficeService {

	BigDecimal calculateCommission(BigDecimal transactionValue);

	LocalDate getCurrentDate();

	LocalDate getLastClosedDate();

	TradingOfficeStatus getStatus();

	TradingOfficeStatus openNextDay();

	void closeDay();

}
