package com.github.koshamo.puri.ui.controls.player;

import java.util.LinkedList;
import java.util.List;

import com.github.koshamo.puri.GameController;
import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.setup.RoleType;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Player extends Region {

	private GameController controller;
	private final PlayerStat stats;
	private final PlayerPlantation plantations;
	private final PlayerBuilding buildings;
	private final Color color;
	
	public Player(String name, PrColors color) {
		this.color = color.getColor();
		stats = new PlayerStat(this, name, color);
		plantations = new PlayerPlantation(this);
		buildings = new PlayerBuilding(this);
		
		drawComponent();
	}
	
	private void drawComponent() {
		HBox hbox = new HBox(5);
		hbox.setPadding(new Insets(5));
		hbox.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM, new Insets(3))));
		
		hbox.getChildren().addAll(stats, plantations, buildings);
		this.getChildren().add(hbox);
	}
	
	public void connectController(GameController controller) {
		this.controller = controller;
	}
	
	public void activateGouvernor() {
		stats.activateGouvernor();
		activateRole();
	}
	
	public void deactivateGouvernor() {
		stats.deactivateGouvernor();
	}
	
	public void activateRole() {
		stats.activateRole();
		activatePlayer();
	}
	
	public void chooseRole(RoleType type) {
		stats.chooseRole(type);
	}
	
	public void deactivateRole() {
		stats.deactivateRole();
	}
	
	public void activatePlayer() {
		stats.activatePlayer();
	}
	
	public void deactivatePlayer() {
		stats.deactivatePlayer();
	}

	public void addGulden(int gulden) {
		stats.addGulden(gulden);
	}
	
	public void addPlantation(PlantationType type) {
		plantations.addPlantation(type);
	}
	
	public int availableGulden() {
		return stats.currentGulden();
	}
	
	public List<BuildingTypeList> ownedBuildings() {
		return buildings.ownedBuildings();
	}
	
	public void purchaseBuilding(BuildingTypeList type, int amount) {
		buildings.addBuilding(type);
		stats.subGulden(amount);
	}
	
	public boolean isBuildingSpaceFull() {
		return buildings.isBuildingSpaceFull();
	}
	
	public void addColonists(int num) {
		stats.addColonists(num);
	}

	public int calcEmptyPlaces() {
		return buildings.calcEmptyPlaces();
	}
	
	public int[] calcProduction() {
		int[] materials = plantations.calcProducedMaterials();
		int[] producable = buildings.calcProducableProducts();
		int[] products = new int[5];
		
		for (int i = 0; i < 4; i++)
			products[i] = Math.min(materials[i], producable[i]);
		products[4] = materials[4];
		
		return products;
	}
	
	public void addProduction(PlantationType type, int amount) {
		stats.addProduct(type, amount);
	}
	
	public void reduceProduct(PlantationType type, int amount) {
		stats.subProduct(type, amount);
	}

	public void distributeColonists() {
		if (stats.colonists() < 
				plantations.calcEmptyPlaces() + buildings.calcEmptyPlaces()) {
			stats.activateColonistsDnD();
			plantations.activateColonistsDnD();
			buildings.activateColonistsDnD();
		}
		else {
			int plCols = plantations.distributeColonists();
			int buCols = buildings.distributeColonists();
			stats.subColonists(plCols + buCols);
			distributionDone();
		}
	}
	
	public void distributionDone() {
		stats.deactivateColonistsDnD();
		plantations.deactivateColonistsDnD();
		buildings.deactivateColonistsDnD();
		
		controller.gouvernorDone();
	}
	
	public void shipProducts() {
		stats.activateProductDnD();
	}
	
	public void shippingDone() {
		stats.deactivateProductDnD();
		controller.shippingDone();
	}
	
	public List<PlantationType> listShippableProducts() {
		List<PlantationType> products = new LinkedList<>();
		
		if (stats.hasProduct(PlantationType.INDIGO))
			products.add(PlantationType.INDIGO);
		if (stats.hasProduct(PlantationType.SUGAR))
			products.add(PlantationType.SUGAR);
		if (stats.hasProduct(PlantationType.CORN))
			products.add(PlantationType.CORN);
		if (stats.hasProduct(PlantationType.TOBACCO))
			products.add(PlantationType.TOBACCO);
		if (stats.hasProduct(PlantationType.COFFEE))
			products.add(PlantationType.COFFEE);
		
		return products;
	}
	
	public int availableProducts(PlantationType type) {
		return stats.availableProducts(type);
	}
	
	public void addVictoryPoints(int amount) {
		stats.addVictoryPoints(amount);
	}
	
	public void checkProductStorage() {
		stats.checkProductStorage();
	}

	public void activateTrader(boolean privilege) {
		stats.purchaseProduct(privilege);
	}
	
	public void tradingDone(int amount) {
		controller.handleTraderDone(amount);
	}
}
