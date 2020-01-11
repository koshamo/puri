package com.github.koshamo.puri.setup;

/*
 * number of product pieces equals number of plantations
 */

// TODO: rename to something like PlantationAndWareType

public enum PlantationType {

	QUARRY (8), INDIGO (12), SUGAR (11), CORN (10), TOBACCO (9), COFFEE (8), NONE (0);
	
	private int max;
	
	private PlantationType(int max) {
		this.max = max;
	}
	
	public int getMax() {
		return max;
	}
}
