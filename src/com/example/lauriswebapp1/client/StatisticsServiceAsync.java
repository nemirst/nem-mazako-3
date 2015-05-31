package com.example.lauriswebapp1.client;

import com.example.lauriswebapp1.client.Game.Player;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatisticsServiceAsync {
	public void getStatistics(AsyncCallback<Stats> callback);
	public void updateStatistics(Player winner, AsyncCallback<Void> callback);
}
