package com.github.koshamo.puri.gamedata;

public class PlayerVictoryPoints {

	private final String name;
	private final String ki;
	private final int shippedVP;
	private final int buildingVP;
	private final int specialVP;
	private final int products;
	
	public PlayerVictoryPoints(String name, String ki, int shippedVP, int buildingVP, int specialVP, int products) {
		super();
		this.name = name;
		this.ki = ki;
		this.shippedVP = shippedVP;
		this.buildingVP = buildingVP;
		this.specialVP = specialVP;
		this.products = products;
	}

	public String getName() {
		return name;
	}

	public String getKi() {
		return ki;
	}

	public String getTotalVP() {
		return String.valueOf(shippedVP + buildingVP + specialVP);
	}
	
	public String getShippedVP() {
		return String.valueOf(shippedVP);
	}

	public String getBuildingVP() {
		return String.valueOf(buildingVP);
	}

	public String getSpecialVP() {
		return String.valueOf(specialVP);
	}
	
	public int getProducts() {
		return products;
	}
}
