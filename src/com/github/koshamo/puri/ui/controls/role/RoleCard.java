package com.github.koshamo.puri.ui.controls.role;

import com.github.koshamo.puri.setup.RoleType;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class RoleCard extends Control {

	private final RoleCardSkin skin;
	private final RoleType type;
	private int gulden = 0;
	private boolean used = false;

	// TODO: use used
	
	public RoleCard(RoleType type) {
		skin = new RoleCardSkin(this, type);
		this.type = type;
		update();
	}
	
	public void addGulden() {
		gulden++;
		update();
	}
	
	public void clear() {
		gulden = 0;
		used = false;
		update();
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(used, gulden);
	}
}
