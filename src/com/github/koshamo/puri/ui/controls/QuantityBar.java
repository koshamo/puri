package com.github.koshamo.puri.ui.controls;

import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

public class QuantityBar extends Control {

	private final int max;
	private final QuantityBarSkin skin;
	private int quantity;
	
	public QuantityBar (int max) {
		this(max, Color.AZURE);
	}
	
	public QuantityBar (int max, Color fillColor) {
		this.max = max;
		skin = new QuantityBarSkin(this, max, fillColor);
	}
	
	public void changeQuantity (int quantity) {
		if (quantity > max || quantity < 0)
			throw new IllegalArgumentException("Quantity out of bounds");
		
		this.quantity = quantity;
		update();
	}
	
	public void add (int num) {
		if (quantity + num > max)
			throw new IllegalArgumentException("Quantity to high");
		quantity += num;
		update();
	}
	
	public void sub (int num) {
		if (quantity - num < 0)
			throw new IllegalArgumentException("Quantity to low");
		quantity -= num;
		update();
	}
	
	public int quantity() {
		return quantity;
	}
	
	public void setTextColor(Color color) {
		skin.setInsettingTextColor(color);
	}
	
	@Override
	protected Skin<?> createDefaultSkin() {
		return skin;
	}
	
	public void update() {
		skin.drawComponent(quantity);
	}
	
	
}
