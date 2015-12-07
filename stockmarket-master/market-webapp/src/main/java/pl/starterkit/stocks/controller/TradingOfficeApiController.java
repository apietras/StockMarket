package pl.starterkit.stocks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import pl.starterkit.stocks.services.interfaces.TradingOfficeService;

@Api(basePath="/office", value="TradingOffice", description="API to manage trading days")
@RequestMapping("/office")
@RestController
public class TradingOfficeApiController {

	@Autowired
	private TradingOfficeService tradingOfficeService;

	@ApiOperation(value="Closes current day", httpMethod="GET")
	@RequestMapping(value="/closeDay", method = RequestMethod.GET)
	public ResponseEntity<String> closeDay() {
		tradingOfficeService.closeDay();
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
