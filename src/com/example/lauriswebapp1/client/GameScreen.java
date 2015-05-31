package com.example.lauriswebapp1.client;

import com.example.lauriswebapp1.client.Game.Player;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class GameScreen extends Composite {
	private Game game;
	private static GameScreenUiBinder uiBinder = GWT
			.create(GameScreenUiBinder.class);

	interface GameScreenUiBinder extends UiBinder<Widget, GameScreen> {
	}

	@UiField
	HTMLPanel previous;

	@UiField
	HTMLPanel current;

	@UiField
	Button buttonPC;

	@UiHandler("buttonPC")
	void handleClickPC(ClickEvent e) {
		makeNextMove("");
	}
	
	@UiHandler("buttonLabi")
	void handleClickLabi(ClickEvent e) {
		RootLayoutPanel.get().clear();
		MainMenu mainMenu = new MainMenu(true);
		RootLayoutPanel.get().add(mainMenu);
		mainMenu.SendStatistics(game.getGameWinner());
		
	}

	private int GetDigitIndex(Object sender) {
		for (int i = 0; i < 6; i++) {
			if (sender == c[i]) {
				return i;
			}
		}
		return -1;
	}

	private void HandleClick(ClickEvent event) {
		if (game.isComplete() || game.getCurPlayer() != Player.USER) {
			return;
		}
		int index = GetDigitIndex(event.getSource());
		String number = game.getCurNumber();
		if (index < 0 || index >= number.length()) {
			return;
		}
		ResetStyles();
		if (index == number.length() - 1 && number.length() % 2 == 1) {
			makeNextMove(number.substring(0, number.length() - 1));
		} else {
			index = index / 2; // 0, 1, 2
			makeNextMove(number.substring(0, index * 2)
					+ // part before
					TreeGenerator.joinDigits(number, index * 2, index * 2 + 1)
					+ number.substring((index + 1) * 2)); // part after
		}
	}
	
	private void ResetStyles() {
		for(int i = 0; i < 6; i++) {
			c[i].removeStyleName(MyResources.INSTANCE.my().drop());
			c[i].removeStyleName(MyResources.INSTANCE.my().addUp());
		}
	}

	private void HandleMouseOver(MouseOverEvent event) {
		if (game.isComplete() || game.getCurPlayer() != Player.USER) {
			return;
		}
		int index = GetDigitIndex(event.getSource());
		String number = game.getCurNumber();
		if (index < 0 || index >= number.length()) {
			return;
		}
		if (index == number.length() - 1 && number.length() % 2 == 1) {
			c[index].addStyleName(MyResources.INSTANCE.my().drop());
		} else {
			index = index / 2; // 0, 1, 2
			c[index*2].addStyleName(MyResources.INSTANCE.my().addUp());
			c[index*2 + 1].addStyleName(MyResources.INSTANCE.my().addUp());
		}
	}

	private InlineLabel[] p = new InlineLabel[6];
	private InlineLabel[] c = new InlineLabel[6];

	private void CreateLabels() {
		for (int i = 0; i < 6; i++) {
			p[i] = new InlineLabel();
			c[i] = new InlineLabel();
			c[i].addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					HandleClick(event);
				}
			});
			c[i].addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					HandleMouseOver(event);
				}
			});
			c[i].addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					ResetStyles();
				}
			});
			previous.add(p[i]);
			current.add(c[i]);
		}
	}

	public GameScreen(boolean computerStarts) {
		initWidget(uiBinder.createAndBindUi(this));
		CreateLabels();
		initGame(computerStarts, "");
	}

	private void initGame(boolean computerStarts, String skaitlis) {
		game = new Game(skaitlis, computerStarts);
		game.start();
		skaitlis = game.getCurNumber();
		for (int i = 0; i < 6; i++) {
			c[i].setText(String.valueOf(skaitlis.charAt(i)));
		}
		if (game.getCurPlayer() != Player.COMPUTER) {
			buttonPC.setEnabled(false);
		}
	}

	private void RefreshNumbers() {
		String curNumber = game.getCurNumber();
		for (int i = 0; i < 6; i++) {
			p[i].setText(c[i].getText());
			if (curNumber.length() > i) {
				c[i].setText(String.valueOf(curNumber.charAt(i)));
			} else {
				c[i].setText("");
			}
		}
	}
	
	@UiField Label resultText;
	@UiField Button buttonLabi;
	private void ShowEnd() {
		Player winner = game.getGameWinner();
		String gameOverText;
		if (winner == Player.COMPUTER) {
			gameOverText = "Diemžēl dators uzvarēja :(";
		} else if (winner == Player.USER) {
			gameOverText = "Apsveicu! Tu uzvarēji!";
		} else {
			gameOverText = "Spēle beidzās ar neizšķirtu";
		}
		resultText.setText(gameOverText);
		buttonPC.removeFromParent();
		buttonLabi.setVisible(true);
	}

	// Performs chosen move. userChoice is empty for PC moves,
	// for user moves set to next number.
	private void makeNextMove(String userChoice) {
		if (!game.isComplete()) {
			game.nextMove(userChoice);
			RefreshNumbers();
			buttonPC.setEnabled(game.getCurPlayer() == Player.COMPUTER);
		}

		if (game.isComplete()) {
			ShowEnd();
		}
	}
}


