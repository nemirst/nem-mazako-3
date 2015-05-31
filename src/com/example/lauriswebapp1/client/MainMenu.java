package com.example.lauriswebapp1.client;

import com.example.lauriswebapp1.client.Game.Player;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class MainMenu extends Composite {
	private static MainMenuUiBinder uiBinder = GWT
			.create(MainMenuUiBinder.class);

	interface MainMenuUiBinder extends UiBinder<Widget, MainMenu> {
	}
	
	private void RefreshStatistics(Stats s) {
		winsPC.setText(String.valueOf(s.getWins(Player.COMPUTER)));
		winsUser.setText(String.valueOf(s.getWins(Player.USER)));
		draws.setText(String.valueOf(s.getWins(Player.NONE)));
	}

	private void GetStatistics() {
		// Set up the callback object.
		AsyncCallback<Stats> callback = new AsyncCallback<Stats>() {
			public void onFailure(Throwable caught) {
				Window.alert("Server communication failure: "
						+ caught.getMessage());
			}

			public void onSuccess(Stats result) {
				RefreshStatistics(result);
			}
		};

		// Make the call to the statistics service.
		statisticsSvc.getStatistics(callback);
	}
	
	public void SendStatistics(Player p) {
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert("Server communication failure: "
						+ caught.getMessage());
			}
			public void onSuccess(Void result) {
				GetStatistics();				
			}
		};

		// Make the call to the statistics service.
		statisticsSvc.updateStatistics(p, callback);
	}

	private StatisticsServiceAsync statisticsSvc = GWT
			.create(StatisticsService.class);

	public MainMenu(boolean skipStatistics) {
		initWidget(uiBinder.createAndBindUi(this));
		if(!skipStatistics) {
			GetStatistics();
		}
	}
	
	public MainMenu() {
		this(false);
	}
	
	@UiField
	RadioButton choiceComputer;
	
	@UiField InlineLabel winsPC;
	@UiField InlineLabel winsUser;
	@UiField InlineLabel draws;

	@UiHandler("buttonNoteikumi")
	void handleClickNoteikumi(ClickEvent e) {
		new Noteikumi().show();
	}
	
	@UiHandler("buttonSakt")
	void handleClickSakt(ClickEvent e) {
		RootLayoutPanel.get().clear();
		RootLayoutPanel.get().add(new GameScreen(this.choiceComputer.getValue()));
	}
}
