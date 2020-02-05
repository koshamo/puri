package com.github.koshamo.puri.setup;

public class BuildingsModel {

	private final BuildingTypeList type;
	private int left;
	
	public BuildingsModel(BuildingTypeList type, int available) {
		super();
		this.type = type;
		left = available;
	}

	public String getName() {
		return type.getName();
	}

	public String getDescription() {
		return type.getDescription();
	}

	public String getShortDescription() {
		return type.getShortDescription();
	}

	public String getCost() {
		return String.valueOf(type.getCost());
	}

	public String getVictoryPoints() {
		return String.valueOf(type.getVictoryPoints());
	}

	public String getLeft() {
		return String.valueOf(left);
	}
	
	public BuildingTypeList type() {
		return type;
	}

	public BuildingTypeList removeBuilding() {
		left--;
		return type;
	}
}
