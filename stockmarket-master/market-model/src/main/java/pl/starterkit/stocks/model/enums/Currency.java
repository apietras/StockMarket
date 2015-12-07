package pl.starterkit.stocks.model.enums;

import java.math.BigDecimal;
import java.util.Random;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * This class represents currency, with given interval of currency rate.
 * @author aniapietras
 *
 */
public enum Currency {
	
	PLN(true, BigDecimal.ONE, BigDecimal.ONE), 
	EUR(false, BigDecimal.valueOf(39, 1), BigDecimal.valueOf(41, 1)), 
	USD(false, BigDecimal.valueOf(35, 1), BigDecimal.valueOf(39, 1));
	
	private static final int MAXIMUM_RANDOM_RATE_PRECISION = 2;

	private boolean isBase;
	private BigDecimal minExchangeRate;
	private BigDecimal maxExchangeRate;
	
	public BigDecimal getRandomExchangeRate() {
		BigDecimal difference = maxExchangeRate.subtract(minExchangeRate);
		BigDecimal randomDifference = difference.multiply(new BigDecimal(new Random().nextDouble()));
		randomDifference = randomDifference.setScale(MAXIMUM_RANDOM_RATE_PRECISION);
		return minExchangeRate.add(randomDifference);
	}
}
