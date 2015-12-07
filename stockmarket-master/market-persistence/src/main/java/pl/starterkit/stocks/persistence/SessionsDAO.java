package pl.starterkit.stocks.persistence;

import java.time.LocalDate;
import java.util.List;

import pl.starterkit.stocks.model.Session;

public interface SessionsDAO extends AbstractDAO<String, Session> {

	List<Session> findByDate(LocalDate date);

}
