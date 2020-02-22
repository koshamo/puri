package com.github.koshamo.puri;

import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.RoleType;
import com.github.koshamo.puri.setup.StartupConstants;
import com.github.koshamo.puri.ui.controls.ProductDialog;
import com.github.koshamo.puri.ui.controls.ProductDialog.State;
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
	
	private int captainIndex;
	private boolean captainPrivilege;
	
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
		case KAPITAEN: handleCaptain(); break;
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
		if (activePlayerCount == NUM_PLAYERS - 1) {
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
		
		if (privilege) {
			int[] possibleExtras = calcPrivilegeProduct(production);
			int availableExtras = calcAvailableExtraProducts(possibleExtras);
			
			if (availableExtras == 1) {
				PlantationType type = availableExtraProduct(possibleExtras);
				players.get(activePlayerIndex).addProduction(type, 1);
				gameBoard.removeProduction(type, 1);
			}
			else if (availableExtras > 1) {
				ProductDialog dialog = new ProductDialog(possibleExtras, State.PRODUCTION);
				Optional<PlantationType> product = dialog.showAndWait();
				if (product.isPresent()) {
					players.get(activePlayerIndex).addProduction(product.get(), 1);
					gameBoard.removeProduction(product.get(), 1);
				}
			}
		}
		
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

	/*
	 * return value back in regular order!
	 * Product order: indigo, sugar, corn, tobacco, coffee
	 */
	private int[] calcPrivilegeProduct(int[] production) {
		int[] extraProct = new int[5];
		
		extraProct[0] = production[0] > 0 
				&& gameBoard.availableProduct(PlantationType.INDIGO) > 0
				? production[0] : 0; 
		extraProct[1] = production[1] > 0 
				&& gameBoard.availableProduct(PlantationType.SUGAR) > 0
				? production[1] : 0; 
		extraProct[2] = production[4] > 0 
				&& gameBoard.availableProduct(PlantationType.CORN) > 0
				? production[4] : 0; 
		extraProct[3] = production[2] > 0 
				&& gameBoard.availableProduct(PlantationType.TOBACCO) > 0
				? production[2] : 0; 
		extraProct[4] = production[3] > 0 
				&& gameBoard.availableProduct(PlantationType.COFFEE) > 0
				? production[3] : 0; 
				
		return extraProct;
	}

	private static int calcAvailableExtraProducts(int[] production) {
		int cnt = 0;
		for (int p : production)
			if (p > 0)
				cnt++;
		return cnt;
	}

	private static PlantationType availableExtraProduct(int[] extraProduct) {
		if (extraProduct[0] > 0)
			return PlantationType.INDIGO;
		else if (extraProduct[1] > 0)
			return PlantationType.SUGAR;
		else if (extraProduct[2] > 0)
			return PlantationType.CORN;
		else if (extraProduct[3] > 0)
			return PlantationType.TOBACCO;
		else
			return PlantationType.COFFEE;
 	}

	private void handleCaptain() {
		captainIndex = activePlayerIndex;
		captainPrivilege = false;
		loopCaptain(0);
	}
	
	private void handleCaptainDone() {
		if (captainPrivilege) {
			players.get(activePlayerIndex).addVictoryPoints(1);
			gameBoard.reduceVictoryPoints(1);
		}
		gameBoard.clearShips();
		checkPlayerStorage();
		if (gameBoard.leftVictoryPoints() < 1)
			gameEnd = true;
		nextPlayerChooseRole();
	}

	private void loopCaptain(int noSellCount) {
		if (captainIndex == NUM_PLAYERS) 
			captainIndex = 0;

		if (noSellCount == 3)
			handleCaptainDone();
		else
			shipProducts(noSellCount);
	}

	private void shipProducts(int noSellCount) {
		int shippableProducts = canShipProducts();
		
		if (shippableProducts == 0) {
			captainIndex++;
			loopCaptain(noSellCount + 1);
		}
		if (shippableProducts == 1) 
			autoShipProducts();
		if (shippableProducts > 1) 
			playerShipProducts();
	}

	private int canShipProducts() {
		List<PlantationType> availProducts = players.get(captainIndex).listShippableProducts();
		
		if (availProducts.size() == 0)
			return 0;
		
		int shippable = 0;
		for (PlantationType type : availProducts) {
			if (gameBoard.hasShip(type)) {
				if (gameBoard.freePlacesOnShipWith(type) > 0)
					shippable++;
			}
			else if (gameBoard.hasShip(PlantationType.NONE))
				shippable += gameBoard.numShipsWithNone();
		}

		return shippable;
	}

	private void autoShipProducts() {
		Player player = players.get(captainIndex);
		List<PlantationType> availProducts = player.listShippableProducts();
		
		if (captainIndex == activePlayerIndex)
			captainPrivilege = true;

		for (PlantationType type : availProducts) {
			if (gameBoard.hasShip(type)) {
				int places = gameBoard.freePlacesOnShipWith(type);
				if (places > 0) {
					int shippable = Math.min(player.availableProducts(type), places);
					player.reduceProduct(type, shippable);
					player.addVictoryPoints(shippable);
					gameBoard.autoShipProduct(type, shippable);
				}
			} else if (gameBoard.numShipsWithNone() == 1) {
				int places = gameBoard.freePlacesOnShipWith(PlantationType.NONE);
				int shippable = Math.min(player.availableProducts(type), places);
				player.reduceProduct(type, shippable);
				player.addVictoryPoints(shippable);
				gameBoard.autoShipProduct(type, shippable);
			}

		}
		captainIndex++;
		loopCaptain(0);
	}

	private void playerShipProducts() {
		if (captainIndex == activePlayerIndex)
			captainPrivilege = true;
		
		for (int i = 0; i < NUM_PLAYERS; i++) {
			if (i == captainIndex)
				players.get(i).activatePlayer();
			else
				players.get(i).deactivatePlayer();
		}

		gameBoard.activateCaptainDnD();
		players.get(captainIndex).shipProducts();
	}
	
	public void shippingDone() {
		gameBoard.deactivateCaptainDnD();
		captainIndex++;
		loopCaptain(0);
	}

	private void checkPlayerStorage() {
		captainIndex = activePlayerIndex;
		for (int i = 0; i < NUM_PLAYERS; i++) {
			Player player = players.get(captainIndex); 
			player.checkProductStorage();
			player.deactivatePlayer();
			captainIndex++;
			if (captainIndex == NUM_PLAYERS)
				captainIndex = 0;
		}
	}

	private void handleTrader(boolean privilege) {
		Player currentPlayer = players.get(activePlayerIndex);
		
		List<PlantationType> products = currentPlayer.listShippableProducts();
		if (products.size() == 0) 
			nextPlayerActive();
		else {
			List<PlantationType> market = gameBoard.listProductsInMarket();
			boolean sellable = false;
			for (PlantationType type : products) {
				if (!market.contains(type)) {
					sellable = true;
				}
			}
			if (sellable) {
				gameBoard.activateTraderDnD(privilege);
				currentPlayer.activateTrader();
			}
			else
				nextPlayerActive();
		}
	}
	
	public void handleTraderDone() {
		gameBoard.deactivateTraderDnD();
		if (gameBoard.checkForMarketClearance())
			handleTraderTurnDone();
		else
			nextPlayerActive();
	}
	
	public void handleTraderTurnDone() {
		gameBoard.deactivateTraderDnD();
		for (int i = 0; i < NUM_PLAYERS; i++)
			players.get(i).deactivateTrader();
		nextPlayerChooseRole();
	}

	private void handleGoldfinder() {
		players.get(activePlayerIndex).addGulden(1);
		
		nextPlayerChooseRole();
	}

	public void dropProduct(PlantationType toReduce, int quantity) {
		gameBoard.moveProductBackToPool(toReduce, quantity);
	}
	
}
