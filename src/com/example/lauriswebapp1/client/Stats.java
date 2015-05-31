package com.example.lauriswebapp1.client;

import java.io.Serializable;

import com.example.lauriswebapp1.client.Game.Player;

public class Stats implements Serializable {
	private static final long serialVersionUID = 6706652097694418918L;
	private int pcWins;
	private int userWins;
	private int draws;
	public Stats() {
		this(0, 0, 0);
	}
	public Stats(int pcWins, int userWins, int draws) {
		this.pcWins = pcWins;
		this.userWins = userWins;
		this.draws = draws;
	}
	public int getWins(Player p) {
		if (p == Player.COMPUTER) {
			return this.pcWins;
		} else if (p == Player.USER) {
			return this.userWins;
		}
		return this.draws;
	}

	public void setWins(Player p, int count) {
		if (p == Player.COMPUTER) {
			this.pcWins = count;
		} else if (p == Player.USER) {
			this.userWins = count;
		} else {
			this.draws = count;
		}
	}
}
