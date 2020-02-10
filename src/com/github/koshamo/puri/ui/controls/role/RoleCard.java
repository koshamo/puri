package com.github.koshamo.puri.ui.controls.role;

import com.github.koshamo.puri.setup.RoleType;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class RoleCard extends Control {

	private final RoleCardSkin skin;
	private final RoleType type;
	private int gulden = 0;
	private boolean used = false;

	public RoleCard(RoleType type) {
		skin = new RoleCardSkin(this, type);
		this.type = type;
		update();
	}
	
	public boolean isUsed() {
		return used;
	}
	
	public RoleType type() {
		return type;
	}
	
	public int removeGulden() {
		int gulden = this.gulden;
		this.gulden = 0;
		used = true;
		update();
		return gulden;
	}
	
	public void prepareNextTurn() {
		if (!used)
			gulden++;
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
