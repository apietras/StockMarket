Solution description
====================

Trading office is the whole application, where the main control over administrative tasks can be managed by TradingOfficeService (internally) and TradingOfficeController (via REST API).

REST API can be explored and used with Swagger UI - accessible under /swagger path

How does it work
----------------
1. First, data has to be uploaded to the system using TradingOfficeService
  2. A beginning day is considered one, for which all the stocks had a value
  2. If there is no value assigned to stock for given day, it is assumed that price from previous day is still valid
1. To begin trading, an account has to be created
  2. If account is created with strategy it is considered automatic account, but it's still possible to place orders manually for this account. On the other hand, if no strategy is assigned, the account is 'manual' and it requires placing an order for any transaction to happen
  2. Accounts may be created in any time during application life, but the 'starting' day is the current one - it's not possible to create 'historical' account
  2. Strategy might place any number of orders, or may not place any orders at all
  2. It is possible to place orders before stock prices update for given day is uploaded.
1. Manual orders might be placed any time, and they are assigned to a session for given day (one that id open within TradingOffice)
  2. An exception is a situation in which trading day has been closed, and new one not opened - in such case, placing orders is not possible
  2. Any strategy assigned to account is executed at the end of day, when it is being closed. Strategy returns a list of orders it wants to place
  2. Orders are executed after the day has been closed and all strategies have executed
  
Assumptions and other implementation details
---------------------
* Application requires Java 8 to build and run
* All date-related fields are handled as LocalDate - the assumption is that the 'working unit of time' is always one day and time zone is irrelevant (and local to Trading Office)
* All money-related fields are handled as BigDecimal to control the precision during operations
* Exchange rates are remembered, once randomly selected for given date; internal service allows setting exchange rates, if not set already, though this method is not externalized via REST
* All accounts start with empty stock wallet - they do not own any shares initially
* Currencies are hard-coded with exchange rate bounds in relation to base currency. 
  * There must be exactly one base currency and all the transactions are executed in this currency. 
  * When order is placed in different currency, price is converted to base currency before executing transaction.
  * System allows handling unlimited number of currencies, provided that they are added as next Enum value
  * Exchange rate spread (buy/sell prices difference) is hardcoded and fixed for both transaction types and all currencies (see constant BID_SELL_SPREAD in ExchangeRate class)
  * Currency conversion are executed immediately (if trading day is open) and not logged in system
  * Currency conversion can only be done using base currency - converting between two non-base currencies results in two transactions (buy and sell) to convert through base currency
  * All code related to order execution is based on currency exchange - if order currency is base one, 
* StockPrices are visible only after the day is 'opened'
  * Strategies are executed, when the price of stock for next day is not yet visible
  * Orders are executed after the prices become visible
* Orders are executed in atomic way - either the whole order is executed or it is cancelled
  * Orders that are not executed are cancelled, and do not live in next session
  * Both the amount and price of order are randomized according to rules set in task before order is evaluated
  * Order is not executed, if the amount of money in given currency is not enough to cover the cost; other currencies are not converted automatically to cover the bill
  * Order of executing orders are first-come-first-served
* Storage is implemented in-memory only, based on Java collections, and because of it no separate persistence domain was created. However, all services-persistence layer interactions are done over interfaces, adding JPA persistence with separate model is possible with conversion in DAO layer
* SpringBoot is used in Web layer start application in embed mode and Spring IO (platform) is used as dependency management source for Maven (to make sure versions used are compatible)
* Apart from ExchangeRate and StockValue, all other objects are mutable
  * Some objects might become immutable by a state change, those include Session and Order
  * Some objects might become practically immutable by a state change of their owner, those include StockWallet and Wallet
  * Some objects are always mutable, those include Account and Stock
* IDs where applicable are assigned in Service layer using UUID.randomUUID()
* Lombok was used to generate boilerplate code to speed up development
* Balancing the wallet (about 50% of money in foreign currency) is not implemented, but can be by implementing DayCloseObserver interface
* Creating new strategy object is implemented using reflection based on class name, as described here: http://stackoverflow.com/a/9886331/1563204

Main elements
=============

Key services
------------
There are several services (with respective REST controllers for external access, when applicable):
* *TradingOfficeService* - a main 'administrative' controller 'advancing' the simulation one day, also initiates end-of-day processing in other components
* *StocksDataService* - allows uploading new stock prices data
* *AccountsService* - service to manage accounts - create new ones for manual and strategy-based trading, executes strategies
* *ExchangeRateService* - manages exchange rates, generating new ones when needed and allows accounts to buy/sell currencies
* *StocksService* - provides (historical) information about stock prices (a price becomes 'visible' when TradingOfficeService considers respective date as 'closed')
* *OrderService* - manages orders and order executions, as initiated by SessionService and provides interface to place manual orders
* *SessionService*  (internal only) - executes strategies and passes orders for processing upon day completion, initiated by SessionService


Key objects
---------------------
Fields and relationships between those objects are depicted on class diagram in stockmarket_model.xml file (can be opened with draw.io )
* *Account* - a trading account, either for manual trading or strategy-based (but not both); owns a list of Wallet, which represents initial amount of currencies the account has. Current values for stocks and currencies are accessed through history only.
* *Wallet* - object representing amount of currency account can have. It is used in two ways - owned by Account to denote initial amount of money Account owns, and owned by Session to denote the current amount of money this account has for given session (note: this value might change during open session as currencies are exchanged and during orders execution)
* *StockWallet* - object representing amount of stocks account has for given session (note: it's value might change during open sessions as orders are executed)
* *Stock* - a symbol that can be traded (note: even though StockWallet has reference to this object, there is no return reference - Stock does not know about any StockWallets)
* *StockValue* - a value of given symbol for given date (note: this object is immutable)
* *ExchangeRate* - an exchange rate between base currency and given currency in certain point in time (note: this object is immutable)
* *Session* - single account activity for given day - this object for given day and account holds all the orders that were placed as well as current (or final, if the session was closed) Wallets and StockWallets. It is used mainly to manage day changes within application, but might be used as account history as well
* *Order* - order placed within the system by given account at given day (linked with both by means of Session), contains all the information required to execute an order, as well as it's execution result

Key interfaces and classes
---------------------
* *pl.starterkit.stocks.services.DayCloseObserver* - interface that each service that should be notified when trading day closes has to implement; TradingOfficeService iterates over all beans implementing this interface notifying them of the change. This mechanism is used to both execute orders and strategies.
* *pl.starterkit.stocks.model.interfaces.Identifable<K>* - interface that declares getId method - used in in-memory persistence to easily generalize DAOs
* *pl.starterkit.stocks.TradingOfficeApplication* - test application for purpose of tests in services package (does not start REST controllers)


Modules
---------------------
* *market-model* (no dependencies) - contains internal model objects and interfaces
* *market-persistence* (depends on market-model) - contains persistence-related code, including DAOs
* *market-services* (depends on market-model, market-persistence) - contains internal services that use DAOs
* *market-webapp* (depends on market-model, market-persistence, market-services) - contains REST controllers, additional DTOs and web configuration layer.


