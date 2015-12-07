package pl.starterkit.stocks.services.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class TradingOfficeStatus {
	
	private LocalDate currentDate;
	
	@Setter
	public Boolean dayClosed;

	public void incrementDay() {
		currentDate = currentDate.plusDays(1);
	}

}