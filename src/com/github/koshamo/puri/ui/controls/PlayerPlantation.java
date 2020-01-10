package com.github.koshamo.puri.ui.controls;

import java.util.ArrayList;
import java.util.List;

import com.github.koshamo.puri.setup.PlantationType;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class PlayerPlantation extends Region {

	private List<PlantationField> plantations;
	
	private HBox rowOne;
	private HBox rowTwo;
	private HBox rowThree;
	
	public PlayerPlantation() {
		plantations = new ArrayList<>(12);
		initGui();
		initPlantations();
		update();
	}

	public void addPlantation(PlantationType type) {
		if (!plantations.get(11).type().equals(PlantationType.NONE))
			return;
		plantations.get(11).addPlantation(type);
		sort();
	}
	
	private void initGui() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(2, 0, 0, 0));
		rowOne = new HBox(3);
		rowTwo = new HBox(3);
		rowThree = new HBox(3);
		vbox.getChildren().addAll(rowOne, rowTwo, rowThree);
		this.getChildren().add(vbox);
	}

	private void initPlantations() {
		for (int i = 0; i < 12; i++)
			plantations.add(new PlantationField());
	}
	
	private void update() {
		rowOne.getChildren().clear();
		rowTwo.getChildren().clear();
		rowThree.getChildren().clear();
		
		for (int i = 0; i < 4; i++) {
			rowOne.getChildren().add(plantations.get(i));
			rowTwo.getChildren().add(plantations.get(i+4));
			rowThree.getChildren().add(plantations.get(i+8));
		}
	}

	private void sort() {
		plantations.sort((p1, p2) -> {
			int comp = p1.type().compareTo(p2.type());
			if (comp == 0)
				comp = p1.state().compareTo(p2.state());
			return comp;
		});
		update();
	}
	
}
