package pl.starterkit.stocks.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.starterkit.stocks.interfaces.DayCloseObserver;
import pl.starterkit.stocks.services.interfaces.TradingOfficeService;
import pl.starterkit.stocks.services.model.TradingOfficeStatus;

@Service
public class TradingOfficeServiceImpl implements TradingOfficeService {

	private static final int MINIMAL_COMISSION = 5;

	private static final BigDecimal TRADING_OFFICE_COMMISTION = BigDecimal.valueOf(5L, 3); //equivalent of 0.005

	private TradingOfficeStatus status = new TradingOfficeStatus(LocalDate.now(), false);
	
	@Autowired
	private List<DayCloseObserver> dayCloseObservers;

	@Override
	public void closeDay() {
		dayCloseObservers.stream().forEach(observer -> observer.notifyBeforeDayCloses(getCurrentDate()));
		status.setDayClosed(true);
		dayCloseObservers.stream().forEach(observer -> observer.notifyAfterDayCloses(getCurrentDate()));
		dayCloseObservers.stream().forEach(observer -> observer.notifyDayClosingCompleted(getCurrentDate()));
	}
	
	@Override
	public TradingOfficeStatus openNextDay() {
		if (!status.getDayClosed()) {
			closeDay();
		}
		status.incrementDay();
		status.setDayClosed(false);
		return status;
	}

	@Override
	public TradingOfficeStatus getStatus() {
		return status;
	}

	@Override
	public LocalDate getLastClosedDate() {
		if (status.getDayClosed()) {
			return getCurrentDate();
		} else {
			return getCurrentDate().minusDays(1L);
		}
	}

	@Override
	public LocalDate getCurrentDate() {
		return status.getCurrentDate();
	}
	
	@Override
	public BigDecimal calculateCommission(BigDecimal transactionValue) {
		return transactionValue.multiply(TRADING_OFFICE_COMMISTION).max(BigDecimal.valueOf(MINIMAL_COMISSION));
	}
	
}
