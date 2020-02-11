package com.github.koshamo.puri;

import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.RoleType;
import com.github.koshamo.puri.setup.StartupConstants;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.board.BuildingsDialog;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;

public class GameController {

	private final StartupConstants gameConstants;
	private final List<Player> players;
	private final Board gameBoard;
	private final RoleBoard roleBoard;
	private final int NUM_PLAYERS;
	
	private int playerRoleIndex;
	private int activePlayerIndex;
	private int activePlayerCount;
	
	private RoleType activeRole;
	
	private boolean gameEnd = false;
	
	int turnCount = 0;
	
	public GameController(StartupConstants gameConstants, List<Player> players, Board gameBoard, RoleBoard roleBoard) {
		this.gameConstants = gameConstants;
		this.players = players;
		this.gameBoard = gameBoard;
		gameBoard.connectController(this);
		this.roleBoard = roleBoard;
		roleBoard.connectController(this);
		NUM_PLAYERS = players.size();
		for (Player p : players)
			p.connectController(this);
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
		
		activeRole = type;
		handleRoleAction(true);
	}
	
	public void nextPlayerActive() {
		players.get(activePlayerIndex).deactivatePlayer();
		activePlayerCount++;
		if (activePlayerCount < NUM_PLAYERS) {
			activePlayerIndex++;
			if (activePlayerIndex == NUM_PLAYERS)
				activePlayerIndex = 0;
			players.get(activePlayerIndex).activatePlayer();
			handleRoleAction(false);
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
		turnCount++;

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
	
	private void handleRoleAction(boolean privilege) {
		switch (activeRole) {
		case BAUMEISTER: handleBuilder(privilege); break;
		case SIEDLER: handleSettler(privilege); break;
		case BUERGERMEISTER: handleGouvernor(privilege); break;
		case AUFSEHER: handleProducer(privilege); break;
		case KAPITAEN: handleCaptain(privilege); break;
		case HAENDLER: handleTrader(privilege); break;
		case GOLDSUCHER: handleGoldfinder(); break;
		default: throw new IllegalArgumentException("Role does not exist");
		}
	}

	private void handleBuilder(boolean privilege) {
		Player currentPlayer = players.get(activePlayerIndex);
		BuildingsDialog dialog = new BuildingsDialog(
				gameConstants, 
				privilege, 
				currentPlayer.availableGulden(), 
				currentPlayer.ownedBuildings());
		Optional<BuildingTypeList> building = dialog.showAndWait();
		
		if (building.isPresent()) {
			BuildingTypeList type = building.get();
			int cost = privilege ? type.getCost() - 1 : type.getCost();
			currentPlayer.purchaseBuilding(type, cost);
			if (currentPlayer.isBuildingSpaceFull())
				gameEnd = true;
		}
		
		nextPlayerActive();
}

	private void handleSettler(boolean privilege) {
		gameBoard.activateSettler(privilege);
	}
	
	public void selectPlantation(PlantationType type ) {
		Player currentPlayer = players.get(activePlayerIndex);
		currentPlayer.addPlantation(type);
		if (activePlayerCount == NUM_PLAYERS - 1)
			gameBoard.refreshPlantations();
		nextPlayerActive();
	}

	private void handleGouvernor(boolean privilege) {
		Player currentPlayer = players.get(activePlayerIndex);
		if (privilege) {
			distributeColonistsToPlayers();
		}
		currentPlayer.distributeColonists();
	}
	
	public void gouvernorDone() {
		if (activePlayerIndex == NUM_PLAYERS - 1) {
			int emptyPlaces = 0;
			for (Player p : players)
				emptyPlaces += p.calcEmptyPlaces();
			int left = gameBoard.refreshColonists(emptyPlaces);
			if (left == 0)
				gameEnd = true;
		}
		nextPlayerActive();
	}

	private void distributeColonistsToPlayers() {
		int colonists = gameBoard.dischargeColonists();

		int each = colonists / NUM_PLAYERS;
		int remaining = colonists % NUM_PLAYERS;
		
		for (int playerCnt = 0, playerIndex = activePlayerIndex; 
				playerCnt < NUM_PLAYERS; 
				playerCnt++, remaining--, playerIndex++) {
			
			if (playerIndex == NUM_PLAYERS)
				playerIndex = 0;
			int curPlayerCols = remaining > 0 ? each + 1 : each;
			if (playerIndex == activePlayerIndex)
				curPlayerCols++;
			players.get(playerIndex).addColonists(curPlayerCols);
		}
	}

	private void handleProducer(boolean privilege) {
		int[] production = calcActualProduction();
		produce(production);
		
		nextPlayerActive();
	}
	
	private int[] calcActualProduction() {
		int[] production = players.get(activePlayerIndex).calcProduction();
		int[] realProduction = new int[5];
		
		realProduction[0] = Math.min(production[0], gameBoard.availableProduct(PlantationType.INDIGO));
		realProduction[1] = Math.min(production[1], gameBoard.availableProduct(PlantationType.SUGAR));
		realProduction[2] = Math.min(production[2], gameBoard.availableProduct(PlantationType.TOBACCO));
		realProduction[3] = Math.min(production[3], gameBoard.availableProduct(PlantationType.COFFEE));
		realProduction[4] = Math.min(production[4], gameBoard.availableProduct(PlantationType.CORN));

		return realProduction;
	}
	
	private void produce(int[] production) {
		Player currentPlayer = players.get(activePlayerIndex);
		
		currentPlayer.addProduction(PlantationType.INDIGO, production[0]);
		gameBoard.removeProduction(PlantationType.INDIGO, production[0]);
		
		currentPlayer.addProduction(PlantationType.SUGAR, production[1]);
		gameBoard.removeProduction(PlantationType.SUGAR, production[1]);
		
		currentPlayer.addProduction(PlantationType.TOBACCO, production[2]);
		gameBoard.removeProduction(PlantationType.TOBACCO, production[2]);
		
		currentPlayer.addProduction(PlantationType.COFFEE, production[3]);
		gameBoard.removeProduction(PlantationType.COFFEE, production[3]);
		
		currentPlayer.addProduction(PlantationType.CORN, production[4]);
		gameBoard.removeProduction(PlantationType.CORN, production[4]);
	}

	private void handleCaptain(boolean privilege) {
		// TODO Auto-generated method stub
		
		nextPlayerActive();
	}

	private void handleTrader(boolean privilege) {
		// TODO Auto-generated method stub
		
		nextPlayerActive();
	}

	private void handleGoldfinder() {
		players.get(activePlayerIndex).addGulden(1);
		
		nextPlayerChooseRole();
	}
	
}
