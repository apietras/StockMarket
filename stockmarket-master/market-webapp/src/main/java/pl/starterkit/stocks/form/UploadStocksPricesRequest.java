package pl.starterkit.stocks.form;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UploadStocksPricesRequest {
	
	private List<StockPrice> stockPrices;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class StockPrice {
		@NotBlank
		private String symbol;
		
		@DecimalMin("0.0")
		private BigDecimal value;
		
		@NotNull
		@JsonDeserialize(using = LocalDateDeserializer.class)
		@JsonSerialize(using = LocalDateSerializer.class)
		private LocalDate date;
	}
}
