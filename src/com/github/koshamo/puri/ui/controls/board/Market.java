package com.github.koshamo.puri.ui.controls.board;

import java.util.ArrayList;
import java.util.List;

import com.github.koshamo.puri.setup.PlantationType;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/*private*/ class Market extends Control {

	private final MarketSkin skin;
	private List<PlantationType> products;
	
	public Market() {
		skin = new MarketSkin(this);
		products = new ArrayList<>(4);
		clearProducts();
	}

	public boolean hasEmptyPlace() {
		if (products.get(0) == PlantationType.NONE)
			return true;
		return false;
	}
	
	public boolean hasProduct(PlantationType type) {
		return products.contains(type);
	}
	
	public void addProduct(PlantationType type) {
		products.remove(0);
		products.add(type);
		update();
	}
	
	public List<PlantationType> getProducts() {
		List<PlantationType> availableProducts = new ArrayList<>();
		for (PlantationType type : products)
			if (type != PlantationType.NONE)
				availableProducts.add(type);
		
		return availableProducts;
	}
	
	public void clearProducts() {
		products.clear();
		for (int i = 0; i < 4; i++)
			products.add(PlantationType.NONE);
		update();
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(products.toArray(new PlantationType[0]));
	}
}
