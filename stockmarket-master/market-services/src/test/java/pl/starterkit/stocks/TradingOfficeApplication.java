package pl.starterkit.stocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackages="pl.starterkit.stocks")
public class TradingOfficeApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(TradingOfficeApplication.class, args);
	}
}
