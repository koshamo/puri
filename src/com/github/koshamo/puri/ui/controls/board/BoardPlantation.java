package com.github.koshamo.puri.ui.controls.board;

import java.util.LinkedList;
import java.util.List;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.utils.ListUtils;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class BoardPlantation extends Control {

	private final BoardPlantationSkin skin;
	
	private final Board board;
	private final int numPlayers;
	private List<PlantationType> plantations;
	private final int quarries;
	
	private boolean privilege; 
	private boolean active;
	
	public BoardPlantation(Board board, int numPlayers) {
		this.board = board;
		this.numPlayers = numPlayers;
		quarries = PlantationType.QUARRY.getMax();
		skin = new BoardPlantationSkin(this, numPlayers);
		initPlantations();
		update();
	}
	
	public void activate(boolean privilege) {
		this.privilege = quarries > 0 ? privilege : false;
		active = true;
		update();
	}

	public void selectPlantation(PlantationType type) {
		privilege = false;
		active = false;
		update();
		
		board.selectPlantation(type);
	}
	
	public void update() {
		PlantationType[] toDraw = new PlantationType[numPlayers + 1];
		for (int i = 0; i < toDraw.length; i++) {
			toDraw[i] = plantations.remove(0);
		}
		skin.drawComponent(plantations.size(), quarries, toDraw, active, privilege);
	}

	private void initPlantations() {
		plantations = new LinkedList<>();
		int usedCorn = numPlayers / 2;
		int usedIndigo = numPlayers - usedCorn;
		
		for (int indigo = 0; indigo < PlantationType.INDIGO.getMax() - usedIndigo; indigo++)
			plantations.add(PlantationType.INDIGO);
		for (int sugar = 0; sugar < PlantationType.SUGAR.getMax(); sugar++)
			plantations.add(PlantationType.SUGAR);
		for (int corn = 0; corn < PlantationType.CORN.getMax() - usedCorn; corn++)
			plantations.add(PlantationType.CORN);
		for (int tobacco = 0; tobacco < PlantationType.TOBACCO.getMax(); tobacco++)
			plantations.add(PlantationType.TOBACCO);
		for (int coffee = 0; coffee < PlantationType.COFFEE.getMax(); coffee++)
			plantations.add(PlantationType.COFFEE);
		
		plantations = ListUtils.generateRandomList(plantations);
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
}
