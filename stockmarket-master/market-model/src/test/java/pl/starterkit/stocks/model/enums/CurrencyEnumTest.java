package pl.starterkit.stocks.model.enums;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

public class CurrencyEnumTest {

	private static final long LEFT_HIGHER_THAN_RIGHT = 1L;

	@Test
	public void testExactlyOneBaseCurrencyExists() {
		long baseCurrenciesCount = Arrays.asList(Currency.values()).stream()
				.filter(currency -> currency.isBase()).count();
		Assert.assertEquals(1L, baseCurrenciesCount);
	}

	@Test
	public void testMinPriceNoHigherThanMaxPrice() {
		for (Currency currency : Currency.values()) {
			Assert.assertNotEquals(LEFT_HIGHER_THAN_RIGHT, currency.getMinExchangeRate().compareTo(currency.getMaxExchangeRate()));
		}
	}
	
}
