package com.github.koshamo.puri.ui.controls.player;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.State;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class BuildingField extends Control {

	private final BuildingFieldSkin skin;
	
	private BuildingTypeList type = BuildingTypeList.NONE;
	private int places;
	private int colonists;
	
	public BuildingField() {
		skin = new BuildingFieldSkin(this);
	}
	
	public State state() {
		return colonists > 0 ? State.ACTIVE : State.INACTIVE;
	}
	
	public BuildingTypeList type() {
		return type;
	}
	
	public int size() {
		return type.getSize();
	}
	
	public void addBuilding(BuildingTypeList type) {
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
	
	public int emptyPlaces() {
		return places - colonists;
	}
	
	public int productionPlaces() {
		return colonists;
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(type, places, colonists);
	}
}
