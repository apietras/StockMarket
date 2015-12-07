package pl.starterkit.stocks.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.starterkit.stocks.model.enums.Currency;
import pl.starterkit.stocks.model.enums.OrderType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderRequest {
	@NotBlank
	private String accountId;
	
	@NotBlank
	private String stockSymbol;
	
	@NotNull
	private Currency currency;
	
	@NotNull
	@Min(1)
	private Integer amount;
	
	@NotNull
	@DecimalMin("0.0")
	private BigDecimal price;
	
	@NotNull
	private OrderType type;
}
