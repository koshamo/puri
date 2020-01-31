package com.github.koshamo.puri.setup;

public class BuildingsModel {

	private final String name;
	private final String description;
	private final String shortDescription;
	private final int cost;
	private final int victoryPoints;
	private final int left;
	
	public BuildingsModel(String name, String description, String shortDescription, int cost, int victoryPoints,
			int left) {
		super();
		this.name = name;
		this.description = description;
		this.shortDescription = shortDescription;
		this.cost = cost;
		this.victoryPoints = victoryPoints;
		this.left = left;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public Integer getCost() {
		return Integer.valueOf(cost);
	}

	public Integer getVictoryPoints() {
		return Integer.valueOf(victoryPoints);
	}

	public Integer getLeft() {
		return Integer.valueOf(left);
	}

}
