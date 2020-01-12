package com.github.koshamo.puri.ui.controls.board;

import java.util.ArrayList;
import java.util.List;

import com.github.koshamo.puri.setup.PlantationType;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class Market extends Control {

	private final MarketSkin skin;
	private List<PlantationType> products;
	
	public Market() {
		skin = new MarketSkin(this);
		products = new ArrayList<>(4);
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(products.toArray(new PlantationType[0]));
	}
}
