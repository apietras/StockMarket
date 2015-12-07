package pl.starterkit.stocks.controller;

import java.util.Optional;

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

import lombok.extern.slf4j.Slf4j;
import pl.starterkit.stocks.form.NewAccountRequest;
import pl.starterkit.stocks.model.Account;
import pl.starterkit.stocks.services.interfaces.AccountsService;
import pl.starterkit.stocks.strategy.AccountStrategy;
import pl.starterkit.stocks.webto.AccountTo;

@Slf4j
@Api(basePath="/accounts", value="Accounts", description="API to create new accounts")
@RestController
@RequestMapping("/accounts")
public class AccountsApiController {

	@Autowired
	private AccountsService accountService;
	
	@ApiOperation(value="Creates new account", httpMethod="POST")
	@RequestMapping(value="/create", method = RequestMethod.POST)
	public ResponseEntity<AccountTo> createAccount(@Valid @RequestBody NewAccountRequest newAccount, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<AccountTo>(HttpStatus.BAD_REQUEST);
		}
		Optional<AccountStrategy> optionalStrategy = Optional.empty();
		if (newAccount.getStrategyClass() != null && !newAccount.getStrategyClass().isEmpty()) {
			try {
				Object newInstance = Class.forName(newAccount.getStrategyClass()).newInstance();
				optionalStrategy = Optional.of((AccountStrategy)newInstance);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				log.error("Invalid class name {} - not a strategy", newAccount.getStrategyClass());
			}
		}
		Account account = accountService.createAccount(newAccount.getName(), optionalStrategy);
		AccountTo accountTo = new AccountTo(account);
		return new ResponseEntity<AccountTo>(accountTo, HttpStatus.OK);
	}
	
	
}
