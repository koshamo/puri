package com.github.koshamo.puri.setup;

/*
 * number of product pieces equals number of plantations
 */

// TODO: rename to something like PlantationAndWareType

public enum PlantationType {

	QUARRY (8, 0), 
	INDIGO (12, 1), 
	SUGAR (11, 2), 
	CORN (10, 0), 
	TOBACCO (9, 3), 
	COFFEE (8, 4), 
	NONE (0, 0);
	
	private int max;
	private int price;
	
	private PlantationType(int max, int price) {
		this.max = max;
		this.price = price;
	}
	
	public int getMax() {
		return max;
	}
	
	public int getPrice() {
		return price;
	}
	
	public static PlantationType getByString(String type) {
		switch (type) {
		case "QUARRY" : return QUARRY;
		case "INDIGO" : return INDIGO;
		case "SUGAR" : return SUGAR;
		case "CORN" : return CORN;
		case "TOBACCO" : return TOBACCO;
		case "COFFEE" : return COFFEE;
		default: return NONE;
		}
	}
}
