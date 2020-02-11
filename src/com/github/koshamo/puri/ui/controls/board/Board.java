package com.github.koshamo.puri.ui.controls.board;

import java.util.LinkedList;

import com.github.koshamo.puri.GameController;
import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.setup.StartupConstants;
import com.github.koshamo.puri.ui.controls.QuantityBar;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
		int amount = 0;
		
		switch (type) {
		case INDIGO: amount = availIndigo.quantity(); break;
		case SUGAR: amount = availSugar.quantity(); break;
		case CORN: amount = availCorn.quantity(); break;
		case TOBACCO: amount = availTobacco.quantity(); break;
		case COFFEE: amount = availCoffee.quantity(); break;
		default:
		}
		
		return amount;
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
