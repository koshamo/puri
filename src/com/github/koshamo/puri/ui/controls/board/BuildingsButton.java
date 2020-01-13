package com.github.koshamo.puri.ui.controls.board;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class BuildingsButton extends Control {

	private final BuildingsButtonSkin skin;
	
	public BuildingsButton() {
		skin = new BuildingsButtonSkin(this);
		update();
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent();
	}
	
}
