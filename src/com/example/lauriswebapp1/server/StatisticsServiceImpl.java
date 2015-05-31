package com.example.lauriswebapp1.server;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.example.lauriswebapp1.client.Game.Player;
import com.example.lauriswebapp1.client.StatisticsService;
import com.example.lauriswebapp1.client.Stats;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class StatisticsServiceImpl extends RemoteServiceServlet implements
		StatisticsService {
	private static final long serialVersionUID = -3606850405340593697L;
	public static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private StorageStatistics getStatistics(PersistenceManager pm) {
		StorageStatistics s;
		try {
			s = pm.getObjectById(StorageStatistics.class, "StatisticsKey");
		} catch (javax.jdo.JDOObjectNotFoundException e) {
			s = new StorageStatistics(new Stats(0, 0, 0));
			pm.makePersistent(s);
		}
		return s;
	}

	public Stats getStatistics() {
		PersistenceManager pm = PMF.getPersistenceManager();
		Stats s;
		try {
			StorageStatistics statistics = getStatistics(pm);
			s = new Stats(statistics.getPcWins(), statistics.getUserWins(),
					statistics.getDraws());
		} finally {
			pm.close();
		}
		return s;
	}

	public void updateStatistics(Player result) {
		PersistenceManager pm = PMF.getPersistenceManager();
		try {
			StorageStatistics s = getStatistics(pm);
			if(result == Player.COMPUTER) {
				s.setPcWins(s.getPcWins() + 1);
			}
			else if(result == Player.USER) {
				s.setUserWins(s.getUserWins() + 1);
			}
			else if(result == Player.NONE) {
				s.setDraws(s.getDraws() + 1);
			}
		} finally {
			pm.close();
		}
	}
}
