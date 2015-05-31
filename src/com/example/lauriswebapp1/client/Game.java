package com.example.lauriswebapp1.client;

import java.util.ArrayList;

import com.google.gwt.user.client.Random;

public class Game {
	private String rootNumber;
	private String curNumber;
	private ArrayList<ArrayList<String>> winningPaths;
	private Player startingPlayer;
	private Player curPlayer;
	private int gameResult;
	private TreeGenerator gen;
	private boolean computerWillWin;
	private Player gameWinner;
	private boolean gameCompleted = false;
	
	private String newNumber() {
		String number = "";
		while(number.length() != 6) {
			number += Random.nextInt(6) + 1;
		}
		return number;
	}
	
	public Game(String rootNumber, boolean computerStarts) {
		if(rootNumber.length() == 0) {
			rootNumber = newNumber();
		}
		curNumber = this.rootNumber = rootNumber;
		if(computerStarts) {
			startingPlayer = curPlayer = Player.COMPUTER;
		}
		else {
			startingPlayer = curPlayer = Player.USER;
		}
	}
	
	public enum Player { NONE, USER, COMPUTER };
	
	public boolean nextMove(String userChoice) {
		GameTree tree = gen.getTree();
		TreeNode curNode = tree.getNodes().get(curNumber);
		int curResult = curNode.getMinMaxValue();
		
		if(curPlayer == Player.COMPUTER) {
			if(!computerWillWin) {
				// Check if computer has regained better game position (loosing->draw, loosing->winning, draw->winning).
				boolean resultChanged = false;
				if(this.startingPlayer == Player.COMPUTER && curResult < gameResult)
				{
					gameResult = curResult;
					resultChanged = true;
					if(curResult == -1) {
						this.computerWillWin = true;
					}
				}
				else if(this.startingPlayer == Player.USER && curResult > gameResult)
				{
					gameResult = curResult;
					resultChanged = true;
					if(curResult == 1) {
						this.computerWillWin = true;
					}
				}
				if(resultChanged) {
					// Switch to winner or at least draw tactics - 
					// regenerate draw or winning paths from current node.
					winningPaths = gen.getWinningPaths(curNumber);
				}
			}

			if(computerWillWin || gameResult == 0) {
				// Winning or draw position tactics.
				for(ArrayList<String> path : winningPaths) {
					int idx = path.get(0).length()-curNumber.length();
					if(path.get(idx).equals(curNumber))
					{
						curNumber = path.get(idx + 1);
						break;
					}
				}
			}
			else {
				// Loosing position tactics, reduce chances that user continues a winning path.
				// Choose next node with less winning paths going through it.
				int leastWinningCount = -1;
				String nextNode = "";
				for(String child : curNode.getChildren()) {
					int winningCount = 0;
					for(ArrayList<String> path : winningPaths) {
						int idx = path.get(0).length()-curNumber.length();
						if(path.get(idx + 1).equals(child)) {
							winningCount++;
						}
					}
					if(leastWinningCount == -1 || winningCount < leastWinningCount) {
						nextNode = child;
						leastWinningCount = winningCount;
						if(leastWinningCount == 0) {
							break;
						}
					}
				}
				curNumber = nextNode;
			}
			curPlayer = Player.USER;
		}
		else {
			if(tree.getNodes().get(curNumber).getChildren().contains(userChoice))
			{
				curNumber = userChoice;
				curPlayer = Player.COMPUTER;
			}
		}
		
		if(curNumber.length() <= TreeGenerator.MIN_NUM_LENGTH) {
			curNode = tree.getNodes().get(curNumber);
			curResult = curNode.getMinMaxValue();
			if(curResult == 0) {
				gameWinner = Player.NONE;
			}
			else if((startingPlayer == Player.COMPUTER && curResult == -1) ||
					(startingPlayer == Player.USER && curResult == 1)) {
				gameWinner = Player.COMPUTER;
			}
			else {
				gameWinner = Player.USER;
			}
			this.gameCompleted = true;
			return false;
		}
		
		return true;
	}
	
	public Player getGameWinner() {
		return this.gameWinner;
	}
	
	public String getCurNumber() {
		return this.curNumber;
	}
	
	public boolean isComplete() {
		return this.gameCompleted;
	}
	
	public Player getCurPlayer() {
		return this.curPlayer;
	}
	
	public void start() {
		this.gameCompleted = false;
		gen = new TreeGenerator(rootNumber);
		gen.generate();
		gen.fillMinMaxValues();
		GameTree tree = gen.getTree();
		gameResult = tree.getNodes().get(rootNumber).getMinMaxValue();
		winningPaths = gen.getWinningPaths(rootNumber);
		this.computerWillWin = (this.startingPlayer == Player.COMPUTER && gameResult == -1) ||  // Minimizer starts
				(this.startingPlayer == Player.USER && gameResult == 1);
	}
}
