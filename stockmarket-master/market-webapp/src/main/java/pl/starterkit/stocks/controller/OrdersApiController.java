package pl.starterkit.stocks.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import pl.starterkit.stocks.form.PlaceOrderRequest;
import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.model.Stock;
import pl.starterkit.stocks.services.interfaces.OrdersService;
import pl.starterkit.stocks.services.interfaces.StocksService;
import pl.starterkit.stocks.webto.OrderTo;

@Api(basePath="/orders", value="Orders", description="API to place orders and retrieve their status")
@RequestMapping("/orders")
@RestController
public class OrdersApiController {

	@Autowired
	private OrdersService ordersService;
	
	@Autowired
	private StocksService stocksService;
	
	@ApiOperation(value="Places new order", httpMethod="POST")
	@RequestMapping(value="/place", method = RequestMethod.POST)
	public ResponseEntity<OrderTo> placeOrder(@Valid @RequestBody PlaceOrderRequest request, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		Stock stock = stocksService.getStockBySymbol(request.getStockSymbol());
		Order order = Order.create(stock, request.getCurrency(), request.getPrice(), request.getAmount(), request.getType());
		order = ordersService.createOrder(request.getAccountId(), order);
		return new ResponseEntity<>(new OrderTo(order), HttpStatus.OK);
	}
	
	@ApiOperation(value="Retrieves order by id", httpMethod="GET")
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<OrderTo> get(@PathVariable String id) {
		Order order = ordersService.getById(id);
		return new ResponseEntity<>(new OrderTo(order), HttpStatus.OK);
	}
	
	
}
