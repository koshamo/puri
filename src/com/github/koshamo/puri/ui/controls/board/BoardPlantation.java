package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.PlantationType;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class BoardPlantation extends Control {

	private final BoardPlantationSkin skin;
	
	public BoardPlantation(int numPlayers) {
		skin = new BoardPlantationSkin(this, numPlayers);
		update();
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		// TODO: use class internal fields to store data and use them here
		skin.drawComponent(50,3, new PlantationType[] {PlantationType.CORN, PlantationType.NONE, PlantationType.TOBACCO, PlantationType.INDIGO});
	}
}
