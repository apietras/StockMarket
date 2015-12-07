package pl.starterkit.stocks.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import pl.starterkit.stocks.form.UploadStocksPricesRequest;
import pl.starterkit.stocks.form.UploadStocksPricesRequest.StockPrice;
import pl.starterkit.stocks.services.StocksDataService;

@Api(basePath="/stocks/prices", value="Stock prices", description="API to upload stocks prices")
@RequestMapping("/stocks/prices")
@RestController
public class StocksDataApiController {

	@Autowired
	StocksDataService stocksDataService;
	
	@ApiOperation(value="Adds stock prices", httpMethod="POST")
	@RequestMapping(value="/", method = RequestMethod.POST)
	public ResponseEntity<String> uploadObject(@Valid @RequestBody UploadStocksPricesRequest request, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		for (StockPrice price : request.getStockPrices()) {
			stocksDataService.addStockData(price.getSymbol(), price.getValue(), price.getDate());
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@ApiOperation(value="Adds raw CSV stock prices data", httpMethod="PUT")
	@RequestMapping(value="/csv", method = RequestMethod.PUT)
	public ResponseEntity<String> uploadRaw(@RequestBody String request) {
		if (request==null || request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		stocksDataService.addStockData(request);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
}
