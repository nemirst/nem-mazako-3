package com.example.lauriswebapp1.server;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdentityType;


import com.example.lauriswebapp1.client.Game.Player;
//import com.example.lauriswebapp1.client.Game.Player;
import com.example.lauriswebapp1.client.Stats;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class StorageStatistics {
	@Persistent
	@PrimaryKey
	private String key = "StatisticsKey";
	@Persistent
	private int pcWins;
	@Persistent
	private int userWins;
	@Persistent
	private int draws;

	StorageStatistics(Stats s) {
		this.pcWins = s.getWins(Player.COMPUTER);
		this.userWins = s.getWins(Player.USER);
		this.draws = s.getWins(Player.NONE);
	}
	
	public int getPcWins() {
		return pcWins;
	}

	public void setPcWins(int pcWins) {
		this.pcWins = pcWins;
	}

	public int getUserWins() {
		return userWins;
	}

	public void setUserWins(int userWins) {
		this.userWins = userWins;
	}

	public int getDraws() {
		return draws;
	}

	public void setDraws(int draws) {
		this.draws = draws;
	}
}
