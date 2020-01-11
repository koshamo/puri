package com.github.koshamo.puri.ui.controls.player;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.State;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class PlantationField extends Control {

	private final PlantationFieldSkin skin;
	private PlantationType type = PlantationType.NONE;
	private State state = State.INACTIVE;
	
	public PlantationField() {
		skin = new PlantationFieldSkin(this, type, state);
	}
	
	public void addPlantation(PlantationType type) {
		this.type = type;
		state = State.INACTIVE;
		update();
	}
	
	public void activate() {
		state = State.ACTIVE;
		update();
	}

	public void deactivate() {
		state = State.INACTIVE;
		update();
	}
	
	public PlantationType type() {
		return type;
	}
	
	public State state() {
		return state;
	}

	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(type, state);
	}
	
}
