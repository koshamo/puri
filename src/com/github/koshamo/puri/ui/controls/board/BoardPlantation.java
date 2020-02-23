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
	private List<PlantationType> usedPlantations;
	private int quarries;
	private PlantationType[] toDraw;
	
	private boolean privilege; 
	private boolean active;
	
	public BoardPlantation(Board board, int numPlayers) {
		this.board = board;
		this.numPlayers = numPlayers;
		quarries = PlantationType.QUARRY.getMax();
		skin = new BoardPlantationSkin(this, numPlayers);
		initPlantations();
		updatePlantations();
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
		removePlantation(type);
		update();
		
		board.selectPlantation(type);
	}
	
	public void update() {
		skin.drawComponent(plantations.size(), quarries, toDraw, active, privilege);
	}
	
	public void refreshPlantations() {
		for (PlantationType type : plantations) 
			if (!type.equals(PlantationType.NONE))
				usedPlantations.add(type);
		updatePlantations();
		update();
	}
	
	public PlantationType drawPlantation() {
		if (plantations.size() == 0)
			refreshDrawingPile();
		if (plantations.size() > 0)
			return plantations.remove(0);
		return PlantationType.NONE;
	}
	
	private void updatePlantations() {
		toDraw = new PlantationType[numPlayers + 1];
		
		for (int i = 0; i < toDraw.length; i++) {
			refreshDrawingPile();
			toDraw[i] = plantations.remove(0);
		}
	}

	private void refreshDrawingPile() {
		if (plantations.size() == 0) {
			if (usedPlantations.size() == 0)
				return;
			plantations.addAll(usedPlantations);
			usedPlantations.clear();
			ListUtils.generateRandomList(plantations);
		}
	}

	private void removePlantation(PlantationType type) {
		if (type.equals(PlantationType.QUARRY))
			quarries--;
		else {
			for (int i = 0; i < toDraw.length; i++) {
				if (toDraw[i].equals(type)) {
					toDraw[i] = PlantationType.NONE;
					break;	// no more plantations to remove!
				}
			}
		}
	}

	private void initPlantations() {
		plantations = new LinkedList<>();
		usedPlantations = new LinkedList<>();
		
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
