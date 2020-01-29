package com.github.koshamo.puri;

import java.util.List;

import com.github.koshamo.puri.setup.RoleType;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;

public class GameController {

	private final List<Player> players;
	private final Board gameBoard;
	private final RoleBoard roleBoard;
	private final int NUM_PLAYERS;
	
	private int playerRoleIndex;
	private int activePlayerIndex;
	private int activePlayerCount;
	
	private boolean gameEnd = false;
	
	int tempTurnCount = 0;
	
	public GameController(List<Player> players, Board gameBoard, RoleBoard roleBoard) {
		this.players = players;
		this.gameBoard = gameBoard;
		gameBoard.connectController(this);
		this.roleBoard = roleBoard;
		roleBoard.connectController(this);
		NUM_PLAYERS = players.size();
	}
	
	public void start() {
		activePlayerIndex = 0;
		activePlayerCount = 0;
		playerRoleIndex = 0;
		players.get(playerRoleIndex).activateGouvernor();
		roleBoard.activate();
	}
	
	public void chooseRole(RoleType type, int gulden) {
		Player activePlayer = players.get(playerRoleIndex); 
		activePlayer.addGulden(gulden);
		activePlayer.chooseRole(type);
		// TODO: role action
		nextPlayerActive();
	}
	
	public void nextPlayerActive() {
		players.get(activePlayerIndex).deactivatePlayer();
		activePlayerCount++;
		if (activePlayerCount < NUM_PLAYERS) {
			activePlayerIndex++;
			if (activePlayerIndex == NUM_PLAYERS)
				activePlayerIndex = 0;
			players.get(activePlayerIndex).activatePlayer();
		} 
		else
			nextPlayerChooseRole();
	}

	private void nextPlayerChooseRole() {
		players.get(playerRoleIndex).deactivateRole();
		playerRoleIndex++;
		activePlayerCount = 0;
		activePlayerIndex = playerRoleIndex;
		if (playerRoleIndex < NUM_PLAYERS) {
			players.get(playerRoleIndex).activateRole();
			roleBoard.activate();
		}
		else
			nextTurn();
	}

	private void nextTurn() {
		tempTurnCount++;
		if (tempTurnCount == 3)
			gameEnd = true;

		if (gameEnd) {
			// TODO: game is ended
		}
		else {
			Player oldGov = players.remove(0);
			oldGov.deactivateGouvernor();
			players.add(oldGov);
			playerRoleIndex = 0;
			activePlayerIndex = playerRoleIndex;
			activePlayerCount = 0;
			roleBoard.prepareNextTurn();
			players.get(0).activateGouvernor();
			roleBoard.activate();
		}
	}
	
}
