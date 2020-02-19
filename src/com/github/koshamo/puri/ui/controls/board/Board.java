package com.github.koshamo.puri.ui.controls.board;

import java.util.LinkedList;
import java.util.List;

import com.github.koshamo.puri.GameController;
import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.setup.StartupConstants;
import com.github.koshamo.puri.ui.controls.QuantityBar;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class Board extends Region {

	private final StartupConstants gameConstants;
	private GameController controller;

	private QuantityBar availVictoryPoints;
	private QuantityBar availColonists;
	private QuantityBar availIndigo;
	private QuantityBar availSugar;
	private QuantityBar availCorn;
	private QuantityBar availTobacco;
	private QuantityBar availCoffee;
	
	private ColonistShip colonistShip;
	private GoodsShip smallGoodsShip;
	private GoodsShip mediumGoodsShip;
	private GoodsShip largeGoodsShip;
	private Market market;
	private BoardPlantation plantations;

	public Board(int numPlayers) {
		gameConstants = new StartupConstants(numPlayers);
		init();
	}
	
	public void connectController(GameController controller) {
		this.controller = controller;
	}
	
	public void activateSettler(boolean privilege) {
		plantations.activate(privilege);
	}
	
	public void selectPlantation(PlantationType type) {
		controller.selectPlantation(type);
	}
	
	public void refreshPlantations() {
		plantations.refreshPlantations();
	}
	
	public int dischargeColonists() {
		if (availColonists.quantity() > 0)
			availColonists.sub(1);
		return colonistShip.discharge();
	}
	
	public int refreshColonists(int emptyPlaces) {
		int colonists = colonistShip.charge(emptyPlaces);
		if (colonists < availColonists.quantity())
			availColonists.sub(colonists);
		else 
			availColonists.changeQuantity(0);
		return availColonists.quantity();
	}
	
	public int availableProduct(PlantationType type) {
		QuantityBar bar = selectProductComponent(type);
		int amount = bar.quantity();
		
		return amount;
	}
	
	public void removeProduction(PlantationType type, int amount) {
		QuantityBar bar = selectProductComponent(type);
		bar.sub(amount);
	}
	
	public void activateCaptainDnD() {
		updateCaptainDropping();
	}

	public void deactivateCaptainDnD() {
		cancelCaptainDropping();
	}
	
	public boolean hasShip(PlantationType type) {
		if (smallGoodsShip.type() == type)
			return true;
		if (mediumGoodsShip.type() == type)
			return true;
		if (largeGoodsShip.type() == type)
			return true;
		return false;
	}
	
	public int numShipsWithNone() {
		int num = 0;
		if (smallGoodsShip.type() == PlantationType.NONE)
			num++;
		if (mediumGoodsShip.type() == PlantationType.NONE)
			num++;
		if (largeGoodsShip.type() == PlantationType.NONE)
			num++;
		return num;
	}
	
	public int freePlacesOnShipWith(PlantationType type) {
		if (smallGoodsShip.type() == type)
			return smallGoodsShip.storageLeft();
		if (mediumGoodsShip.type() == type)
			return mediumGoodsShip.storageLeft();
		if (largeGoodsShip.type() == type)
			return largeGoodsShip.storageLeft();
		return 0;
	}
	
	public void autoShipProduct(PlantationType type, int amount) {
		if (smallGoodsShip.type() == type)
			smallGoodsShip.addGoods(type, amount);
		if (mediumGoodsShip.type() == type)
			mediumGoodsShip.addGoods(type, amount);
		if (largeGoodsShip.type() == type)
			largeGoodsShip.addGoods(type, amount);
	}

	public void reduceVictoryPoints(int amount) {
		if (availVictoryPoints.quantity() > amount)
			availVictoryPoints.sub(amount);
		else
			availVictoryPoints.changeQuantity(0);
	}
	
	public int leftVictoryPoints() {
		return availVictoryPoints.quantity();
	}

	public void clearShips() {
		clearShip(smallGoodsShip);
		clearShip(mediumGoodsShip);
		clearShip(largeGoodsShip);
	}

	public void activateTraderDnD(boolean privilege) {
		if (!market.hasEmptyPlace()) {
			clearMarket();
			controller.handleTraderTurnDone();
		}
		updateTraderDropping(privilege);
	}

	public void deactivateTraderDnD() {
		market.setOnDragOver(null);
		market.setOnDragEntered(null);
		market.setOnDragExited(null);
		market.setOnDragDropped(null);
	}

	private void clearShip(GoodsShip ship) {
		if (ship.storageLeft() == 0) {
			PlantationType type = ship.type();
			int amount = ship.size();

			QuantityBar bar = selectProductComponent(type);
			bar.add(amount);
			
			ship.clear();
		}
	}

	private void clearMarket() {
		List<PlantationType> products = market.getProducts();
		for (PlantationType type : products) {
			QuantityBar bar = selectProductComponent(type);
			bar.add(1);
		}
		market.clearProducts();
	}

	private void updateCaptainDropping() {
		updateSmallShipDropping();
		updateMediumShipDropping();
		updateLargeShipDropping();
	}

	private void updateSmallShipDropping() {
		smallGoodsShip.setOnDragOver(ev -> {
		    if (ev.getGestureSource() != smallGoodsShip 
		    		&& ev.getDragboard().hasString()
		    		&& smallGoodsShip.storageLeft() > 0) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	if (smallGoodsShip.type() == PlantationType.NONE
		    			|| smallGoodsShip.type().toString().equals(product[0]))
		    		ev.acceptTransferModes(TransferMode.MOVE);
		    }
		    ev.consume();
		});
		smallGoodsShip.setOnDragEntered(ev -> {
			// TODO: show drop possible
			ev.consume();
		});
		smallGoodsShip.setOnDragExited(ev -> {
			// TODO: end show drop possible
			ev.consume();
		});
		smallGoodsShip.setOnDragDropped(ev -> {
		    Dragboard db = ev.getDragboard();
		    boolean success = false;
		    if (db.hasString()) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	int playerAmount = Integer.valueOf(product[1]).intValue();
		    	int shipAmount = smallGoodsShip.storageLeft();
		    	int shipped = Math.min(playerAmount, shipAmount);
		    	if (smallGoodsShip.type() == PlantationType.NONE) {
		    		PlantationType type = PlantationType.getByString(product[0]);
		    		smallGoodsShip.addGoods(type, shipped);
		    	} else
		    		smallGoodsShip.addGoods(shipped);
		    	success = true;
				ClipboardContent cc = new ClipboardContent();
				cc.putString(String.valueOf(product[0] + " " + shipped));
				db.setContent(cc);
		    }
		    ev.setDropCompleted(success);
		    ev.consume();
		});
	}

	private void updateMediumShipDropping() {
		mediumGoodsShip.setOnDragOver(ev -> {
		    if (ev.getGestureSource() != mediumGoodsShip 
		    		&& ev.getDragboard().hasString()
		    		&& mediumGoodsShip.storageLeft() > 0) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	if (mediumGoodsShip.type() == PlantationType.NONE
		    			|| mediumGoodsShip.type().toString().equals(product[0]))
		    		ev.acceptTransferModes(TransferMode.MOVE);
		    }
		    ev.consume();
		});
		mediumGoodsShip.setOnDragEntered(ev -> {
			// TODO: show drop possible
			ev.consume();
		});
		mediumGoodsShip.setOnDragExited(ev -> {
			// TODO: end show drop possible
			ev.consume();
		});
		mediumGoodsShip.setOnDragDropped(ev -> {
		    Dragboard db = ev.getDragboard();
		    boolean success = false;
		    if (db.hasString()) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	int playerAmount = Integer.valueOf(product[1]).intValue();
		    	int shipAmount = mediumGoodsShip.storageLeft();
		    	int shipped = Math.min(playerAmount, shipAmount);
		    	if (mediumGoodsShip.type() == PlantationType.NONE) {
		    		PlantationType type = PlantationType.getByString(product[0]);
		    		mediumGoodsShip.addGoods(type, shipped);
		    	} else
		    		mediumGoodsShip.addGoods(shipped);
		    	success = true;
				ClipboardContent cc = new ClipboardContent();
				cc.putString(String.valueOf(product[0] + " " + shipped));
				db.setContent(cc);
		    }
		    ev.setDropCompleted(success);
		    ev.consume();
		});
	}

	private void updateLargeShipDropping() {
		largeGoodsShip.setOnDragOver(ev -> {
		    if (ev.getGestureSource() != largeGoodsShip 
		    		&& ev.getDragboard().hasString()
		    		&& largeGoodsShip.storageLeft() > 0) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	if (largeGoodsShip.type() == PlantationType.NONE
		    			|| largeGoodsShip.type().toString().equals(product[0]))
		    		ev.acceptTransferModes(TransferMode.MOVE);
		    }
		    ev.consume();
		});
		largeGoodsShip.setOnDragEntered(ev -> {
			// TODO: show drop possible
			ev.consume();
		});
		largeGoodsShip.setOnDragExited(ev -> {
			// TODO: end show drop possible
			ev.consume();
		});
		largeGoodsShip.setOnDragDropped(ev -> {
		    Dragboard db = ev.getDragboard();
		    boolean success = false;
		    if (db.hasString()) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	int playerAmount = Integer.valueOf(product[1]).intValue();
		    	int shipAmount = largeGoodsShip.storageLeft();
		    	int shipped = Math.min(playerAmount, shipAmount);
		    	if (largeGoodsShip.type() == PlantationType.NONE) {
		    		PlantationType type = PlantationType.getByString(product[0]);
		    		largeGoodsShip.addGoods(type, shipped);
		    	} else
		    		largeGoodsShip.addGoods(shipped);
		    	success = true;
				ClipboardContent cc = new ClipboardContent();
				cc.putString(String.valueOf(product[0] + " " + shipped));
				db.setContent(cc);
		    }
		    ev.setDropCompleted(success);
		    ev.consume();
		});
	}

	private void cancelCaptainDropping() {
		cancelSmallShipDropping();
		cancelMediumShipDropping();
		cancelLargeShipDropping();
	}

	private void cancelSmallShipDropping() {
		smallGoodsShip.setOnDragOver(null);
		smallGoodsShip.setOnDragEntered(null);
		smallGoodsShip.setOnDragExited(null);
		smallGoodsShip.setOnDragDropped(null);
	}

	private void cancelMediumShipDropping() {
		mediumGoodsShip.setOnDragOver(null);
		mediumGoodsShip.setOnDragEntered(null);
		mediumGoodsShip.setOnDragExited(null);
		mediumGoodsShip.setOnDragDropped(null);
	}

	private void cancelLargeShipDropping() {
		largeGoodsShip.setOnDragOver(null);
		largeGoodsShip.setOnDragEntered(null);
		largeGoodsShip.setOnDragExited(null);
		largeGoodsShip.setOnDragDropped(null);
	}

	private void updateTraderDropping(boolean privilege) {
		market.setOnDragOver(ev -> {
		    if (ev.getGestureSource() != market 
		    		&& ev.getDragboard().hasString() 
		    		&& market.hasEmptyPlace()) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	PlantationType type = PlantationType.getByString(product[0]);
		    	if (!market.hasProduct(type))
		    		ev.acceptTransferModes(TransferMode.MOVE);
		    }
		    ev.consume();
		});
		market.setOnDragEntered(ev -> {
			// TODO: show drop possible
			ev.consume();
		});
		market.setOnDragExited(ev -> {
			// TODO: end show drop possible
			ev.consume();
		});
		market.setOnDragDropped(ev -> {
		    Dragboard db = ev.getDragboard();
		    boolean success = false;
		    if (db.hasString()) {
		    	String[] product = ev.getDragboard().getString().split(" ");
		    	PlantationType type = PlantationType.getByString(product[0]);
		    	market.addProduct(type);
		    	int price = privilege ? type.getPrice() + 1 : type.getPrice();
		    	success = true;
				ClipboardContent cc = new ClipboardContent();
				cc.putString(String.valueOf(product[0] + " " + price));
				db.setContent(cc);
		    }
		    ev.setDropCompleted(success);
		    ev.consume();
		});
	}

	private QuantityBar selectProductComponent(PlantationType type ) {
		QuantityBar bar;
		
		switch (type) {
		case INDIGO: bar = availIndigo; break;
		case SUGAR: bar = availSugar; break;
		case CORN: bar = availCorn; break;
		case TOBACCO: bar = availTobacco; break;
		case COFFEE: bar = availCoffee; break;
		default: bar = null;
		}

		return bar;
	}

	private void init() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(5));
		vbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM, new Insets(3))));
	
		vbox.getChildren().addAll(
				initPlantations(),
				initColonistsAndVictoryPoints(),
				initGoodsShipsAndProducts());
		
		this.getChildren().add(vbox);
	}

	private Node initColonistsAndVictoryPoints() {
		HBox hbox = new HBox(20);
		
		hbox.getChildren().addAll(initVpColBars(), initColShip());
		return hbox;
	}

	private Node initVpColBars() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setHgap(15.0);
		grid.setVgap(5.0);

		availVictoryPoints = new QuantityBar(gameConstants.VICTORY_POINTS, Color.GOLD);
		availVictoryPoints.changeQuantity(gameConstants.VICTORY_POINTS);
		availVictoryPoints.setPrefWidth(105);
		availColonists = new QuantityBar(gameConstants.COLONISTS, PrColors.COLONIST.getColor());
		availColonists.changeQuantity(gameConstants.COLONISTS - gameConstants.NUM_PLAYERS);
		
		grid.add(new Label("verbleibende\nSiegpunkte"), 0, 0);
		grid.add(new Label("verbleibende\nKolonisten"), 0, 1);
		grid.add(availVictoryPoints, 1, 0);
		grid.add(availColonists, 1, 1);
		
		return grid;
	}

	private Node initColShip() {
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(5));
		
		Label lblCol = new Label("Kolonisten");
		colonistShip = new ColonistShip(gameConstants.MIN_COLONISTS);

		vbox.getChildren().addAll(lblCol, colonistShip);
		return vbox;
	}

	private Node initGoodsShipsAndProducts() {
		HBox hbox = new HBox(20);

		hbox.getChildren().addAll(initGoodsShips(), initProductsAndMarket());
		return hbox;
	}

	private Node initGoodsShips() {
		VBox vbox = new VBox(5);
		vbox.setPadding(new Insets(5));
		
		Label lblGoods = new Label("Lieferungen");
		smallGoodsShip = new GoodsShip(gameConstants.SMALL_SHIP_PLACES);
		mediumGoodsShip = new GoodsShip(gameConstants.MEDIUM_SHIP_PLACES);
		largeGoodsShip = new GoodsShip(gameConstants.LARGE_SHIP_PLACES);
		
		vbox.getChildren().addAll(
				lblGoods, smallGoodsShip, mediumGoodsShip, largeGoodsShip);
		
		return vbox;
	}

	private Node initProductsAndMarket() {
		VBox vbox = new VBox(3);
		
		vbox.getChildren().addAll(initProducts(), initMarketAndButton());
		return vbox;
	}

	private Node initMarketAndButton() {
		HBox hbox = new HBox(20);
		hbox.getChildren().addAll(initMarket(), initBuildings());
		return hbox;
	}

	private Node initMarket() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(5));
	
		Label lblMarekt = new Label("Handelshaus");
		market = new Market();
		
		vbox.getChildren().addAll(lblMarekt, market);
		return vbox;
	}

	private Node initProducts() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setHgap(15.0);
		grid.setVgap(5.0);

		grid.add(new Label("verfügbare Waren"), 0, 0, 2, 1);
		
		grid.add(new Label("Indigo:"), 0, 1);
		grid.add(new Label("Zucker:"), 0, 2);
		grid.add(new Label("Mais:"), 0, 3);
		grid.add(new Label("Tabak:"), 0, 4);
		grid.add(new Label("Kaffee:"), 0, 5);
		
		availIndigo = new QuantityBar(12, PrColors.INDIGO.getColor());
		availIndigo.setTextColor(PrColors.INDIGO_TXT.getColor());
		availIndigo.changeQuantity(PlantationType.INDIGO.getMax());
		availIndigo.setPrefWidth(105);
		grid.add(availIndigo, 1, 1);
		availSugar = new QuantityBar(12, PrColors.SUGAR.getColor());
		availSugar.setTextColor(PrColors.SUGAR_TXT.getColor());
		availSugar.changeQuantity(PlantationType.SUGAR.getMax());
		grid.add(availSugar, 1, 2);
		availCorn = new QuantityBar(12, PrColors.CORN.getColor());
		availCorn.setTextColor(PrColors.CORN_TXT.getColor());
		availCorn.changeQuantity(PlantationType.CORN.getMax());
		grid.add(availCorn, 1, 3);
		availTobacco = new QuantityBar(12, PrColors.TOBACCO.getColor());
		availTobacco.setTextColor(PrColors.TOBACCO_TXT.getColor());
		availTobacco.changeQuantity(PlantationType.TOBACCO.getMax());
		grid.add(availTobacco, 1, 4);
		availCoffee = new QuantityBar(12, PrColors.COFFEE.getColor());
		availCoffee.setTextColor(PrColors.COFFEE_TXT.getColor());
		availCoffee.changeQuantity(PlantationType.COFFEE.getMax());
		grid.add(availCoffee, 1, 5);
		return grid;
	}
	
	private Node initBuildings() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(5));

		Label lblBuildings = new Label("Gebäude");
		BuildingsButton btnBuilding = new BuildingsButton();
		btnBuilding.setOnMouseClicked(ev -> {
			BuildingsDialog dialog = new BuildingsDialog(gameConstants, false, 10, new LinkedList<BuildingTypeList>());
			dialog.showAndWait();
			});
		vbox.getChildren().addAll(lblBuildings, btnBuilding);
		
		return vbox;
	}

	private Node initPlantations() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(5));

		Label lblPlantations = new Label("Plantagen");
		plantations = new BoardPlantation(this, gameConstants.NUM_PLAYERS);
		vbox.getChildren().addAll(lblPlantations, plantations);
		return vbox;
	}


}
