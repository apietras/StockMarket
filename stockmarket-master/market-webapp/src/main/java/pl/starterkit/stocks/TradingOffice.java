package pl.starterkit.stocks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.mangofactory.swagger.plugin.EnableSwagger;

@EnableSwagger
@EnableAutoConfiguration
@ComponentScan(basePackages="pl.starterkit.stocks")
public class TradingOffice {
	
	public static void main(String[] args) {
		SpringApplication.run(TradingOffice.class, args);
	}
}
