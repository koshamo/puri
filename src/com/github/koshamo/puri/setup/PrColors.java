package com.github.koshamo.puri.setup;
import javafx.scene.paint.Color;

public enum PrColors {

	INDIGO (Color.DODGERBLUE),
	INDIGO_TXT (Color.BLACK),
	SUGAR (Color.WHITE),
	SUGAR_TXT (Color.BLACK),
	CORN (Color.YELLOW),
	CORN_TXT (Color.BLACK),
	TOBACCO (Color.BURLYWOOD),
	TOBACCO_TXT (Color.BLACK),
	COFFEE (Color.MAROON),
	COFFEE_TXT (Color.WHITE),
	QUARRY (Color.SILVER),
	QUARRY_TXT (Color.BLACK),
	BUILDING (Color.KHAKI),
	EMPTY_FIELD (Color.LAVENDER),
	COLONIST (Color.HOTPINK),
	COLONIST_TXT (Color.BLACK),
	GOUVERNOR_BGD (Color.DARKVIOLET),
	GOUVERNOR_TXT (Color.WHITE),
	ACTIVE_BGD (Color.RED),
	ACTIVE_TXT (Color.WHITE),
	PLAYER_1 (Color.SPRINGGREEN),
	PLAYER_2 (Color.SLATEBLUE),
	PLAYER_3 (Color.TOMATO),
	PLAYER_4 (Color.GOLD),
	PLAYER_5 (Color.SLATEGREY);
	
	private Color color;
	
	private PrColors (Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
