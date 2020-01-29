package com.github.koshamo.puri.ui.controls.player;

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

	private final PlayerStat stats;
	private final PlayerPlantation plantations;
	private final PlayerBuilding buildings;
	private final Color color;
	
	public Player(String name, PrColors color) {
		this.color = color.getColor();
		stats = new PlayerStat(name, color);
		plantations = new PlayerPlantation();
		buildings = new PlayerBuilding();
		
		drawComponent();
	}
	
	private void drawComponent() {
		HBox hbox = new HBox(5);
		hbox.setPadding(new Insets(5));
		hbox.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM, new Insets(3))));
		
		hbox.getChildren().addAll(stats, plantations, buildings);
		this.getChildren().add(hbox);
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

	public void addPlantation(PlantationType type) {
		plantations.addPlantation(type);
	}
}
