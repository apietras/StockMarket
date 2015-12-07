package pl.starterkit.stocks.persistence.memory;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.Order;
import pl.starterkit.stocks.persistence.OrdersDAO;

@Repository
public class OrdersDAOImpl extends AbstractDAOImpl<String, Order> implements OrdersDAO {

}
