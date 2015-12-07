package pl.starterkit.stocks.webto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.starterkit.stocks.model.enums.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTo {
	private String id;
	private Currency currency;
	private BigDecimal amount;
}
