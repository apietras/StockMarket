package pl.starterkit.stocks.persistence.memory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import pl.starterkit.stocks.model.Session;
import pl.starterkit.stocks.persistence.SessionsDAO;

@Repository
public class SessionsDAOImpl extends AbstractDAOImpl<String, Session> implements SessionsDAO {

	@Override
	public List<Session> findByDate(LocalDate date) {
		return objectsMap.values().stream().filter(session -> session.getDate().equals(date)).collect(Collectors.toList());
	}

}
