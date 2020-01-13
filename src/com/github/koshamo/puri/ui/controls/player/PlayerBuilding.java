package com.github.koshamo.puri.ui.controls.player;

import java.util.ArrayList;
import java.util.List;

import com.github.koshamo.puri.setup.BuildingType;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/*private*/ class PlayerBuilding extends Region {

	private List<BuildingField> buildings;
	
	private HBox rowOne;
	private HBox rowTwo;

	public PlayerBuilding() {
		buildings = new ArrayList<>(12);
		initGui();
		initBuildings();
		update();
	}

	public void addBuilding(BuildingType type) {
		int index = buildings.size() - type.getSize();
		
		if (!buildings.get(index).type().equals(BuildingType.NONE))
			return;
		
		buildings.get(index).addBuilding(type);
		if (type.getSize() == 2)
			buildings.remove(index + 1);
		sort();
	}
	
	private void initGui() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(2, 0, 0, 2));
		rowOne = new HBox(3);
		rowTwo = new HBox(3);
		vbox.getChildren().addAll(rowOne, rowTwo);
		this.getChildren().add(vbox);
	}

	private void initBuildings() {
		for (int i = 0; i < 12; i++)
			buildings.add(new BuildingField());
	}
	
	private void update() {
		rowOne.getChildren().clear();
		rowTwo.getChildren().clear();

		int listIndex = 0;
		int placesInRow = 0;
		int endReduced = 0;
		
		for (; placesInRow < 5; listIndex++) {
			BuildingField building = buildings.get(listIndex);
			rowOne.getChildren().add(building);
			placesInRow += building.size();
		}
		
		BuildingField building = buildings.get(listIndex);
		if (placesInRow < 6) {
			if (building.size() < 2) {
				rowOne.getChildren().add(building);
				listIndex++;
			} 
			else if (buildings.get(buildings.size()-1).type().equals(BuildingType.NONE) ) {
				rowOne.getChildren().add(buildings.get(buildings.size()-1));
				endReduced++;
			}
		}
			
		for (; listIndex < buildings.size() - endReduced; listIndex++) {
			rowTwo.getChildren().add(buildings.get(listIndex));
		}
	}

	private void sort() {
		buildings.sort((p1, p2) -> {
			return p1.type().compareTo(p2.type());
		});
		update();
	}}
