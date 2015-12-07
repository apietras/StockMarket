package pl.starterkit.stocks.interfaces;

import java.time.LocalDate;

public interface DayCloseObserver {
	public default void notifyBeforeDayCloses(LocalDate localDate) {};
	public default void notifyAfterDayCloses(LocalDate localDate) {};
	public default void notifyDayClosingCompleted(LocalDate localDate) {};
}
