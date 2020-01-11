package com.github.koshamo.puri.ui.controls;

import com.github.koshamo.puri.setup.BuildingType;
import com.github.koshamo.puri.setup.State;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class BuildingField extends Control {

	private final BuildingFieldSkin skin;
	
	private BuildingType type = BuildingType.NONE;
	private int places;
	private int colonists;
	
	public BuildingField() {
		skin = new BuildingFieldSkin(this);
	}
	
	public State state() {
		return colonists > 0 ? State.ACTIVE : State.INACTIVE;
	}
	
	public BuildingType type() {
		return type;
	}
	
	public int size() {
		return type.getSize();
	}
	
	public void addBuilding(BuildingType type) {
		this.type = type;
		places = type.getPlaces();
		update();
	}
	
	public void addColonist() {
		if (colonists >= places)
			throw new IllegalArgumentException("all places full");
		colonists++;
		update();
	}
	
	public void removeColonist() {
		if (colonists <= 0)
			throw new IllegalArgumentException("no colonists here");
		colonists--;
		update();
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(type, places, colonists);
	}
}
