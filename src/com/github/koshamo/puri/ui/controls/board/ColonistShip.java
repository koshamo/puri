package com.github.koshamo.puri.ui.controls.board;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class ColonistShip extends Control {

	private final ColonistShipSkin skin;
	private final int minColonists;
	
	private int colonists;
	
	public ColonistShip(int minColonists) {
		skin = new ColonistShipSkin(this);
		this.minColonists = minColonists;
		
		colonists = minColonists;
		update();
	}
	
	public int getColonists() {
		return colonists;
	}
	
	public void setColonists(int colonists) {
		if (colonists < minColonists)
			this.colonists = minColonists;
		else
			this.colonists = colonists;
	}
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(colonists);
	}
}
