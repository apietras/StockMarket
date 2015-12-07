package pl.starterkit.stocks.form;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewAccountRequest {
	
	@NotNull
	@NotBlank
	private String name;
	
	private String strategyClass;
	
}
