package com.github.koshamo.puri.ui.controls;

import com.github.koshamo.puri.setup.PlantationType;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class PlantationField extends Control {

	public enum State {ACTIVE, INACTIVE}
	
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

	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(type, state);
	}
	
}
