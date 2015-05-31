package com.example.lauriswebapp1.client;
import com.example.lauriswebapp1.client.Game.Player;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("statistics")
public interface StatisticsService extends RemoteService {
      public Stats getStatistics();
      public void updateStatistics(Player winner);
}