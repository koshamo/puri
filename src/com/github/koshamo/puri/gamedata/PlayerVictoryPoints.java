package com.github.koshamo.puri.gamedata;

public class PlayerVictoryPoints {

	private final String name;
	private final String ki;
	private final int shippedVP;
	private final int buildingVP;
	private final int specialVP;
	
	public PlayerVictoryPoints(String name, String ki, int shippedVP, int buildingVP, int specialVP) {
		super();
		this.name = name;
		this.ki = ki;
		this.shippedVP = shippedVP;
		this.buildingVP = buildingVP;
		this.specialVP = specialVP;
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
}
