package com.github.koshamo.puri.setup;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class StartupConstants {
	
	public final int VICTORY_POINTS;
	public final int COLONISTS;
	public final int MIN_COLONISTS;
	public final int MIN_PLANTATION;
	public final int SMALL_SHIP_PLACES;
	public final int MEDIUM_SHIP_PLACES;
	public final int LARGE_SHIP_PLACES;
	public final int NUM_PLAYERS;
	public final PlantationType[] INITIAL_PLANTATIONS;
	public final ObservableList<BuildingsModel> availableBuildings;
	
	
	public StartupConstants(int players) {
		NUM_PLAYERS = players;
		MIN_COLONISTS = players;
		MIN_PLANTATION = players + 1;
		
		SMALL_SHIP_PLACES = players + 1;
		MEDIUM_SHIP_PLACES = players + 2;
		LARGE_SHIP_PLACES = players + 3;
		
		if (players == 3) {
			VICTORY_POINTS = 75;
			COLONISTS = 55;
			INITIAL_PLANTATIONS = new PlantationType[] 
					{PlantationType.INDIGO, PlantationType.INDIGO, 
							PlantationType.CORN};
		}
		else if (players == 4) {
			VICTORY_POINTS = 100;
			COLONISTS = 75;
			INITIAL_PLANTATIONS = new PlantationType[] 
					{PlantationType.INDIGO, PlantationType.INDIGO, 
							PlantationType.CORN, PlantationType.CORN};
		}
		else if (players == 5) {
			VICTORY_POINTS = 126;
			COLONISTS = 95;
			INITIAL_PLANTATIONS = new PlantationType[] 
					{PlantationType.INDIGO, PlantationType.INDIGO, 
							PlantationType.INDIGO, PlantationType.CORN,
							PlantationType.CORN};
		}
		else {
			VICTORY_POINTS = 0;
			COLONISTS = 0;
			INITIAL_PLANTATIONS = null;
		}
		
		availableBuildings = FXCollections.observableArrayList();
		fillBuildingsList();
	}


	private void fillBuildingsList() {
		BuildingTypeList[] buildingsList = BuildingTypeList.values();
		
		for (BuildingTypeList type : buildingsList) {
			int avail = getNumOfObjects(type);

			if (!type.equals(BuildingTypeList.NONE))
				availableBuildings.add(new BuildingsModel(type, avail));
		}
		sortBuildings();
	}


	private void sortBuildings() {
		availableBuildings.sort((o1, o2) -> {
			int vpComp = o1.getVictoryPoints().compareTo(o2.getVictoryPoints());
			if (vpComp != 0)
				return vpComp;
			return o1.getCost().compareTo(o2.getCost());
		});
	}


	private int getNumOfObjects(BuildingTypeList type) {
		int avail;

		switch(type.getType()) {
		case SMALL_PRODUCTION: avail = NUM_PLAYERS > 3 ? 4 : 3; break;
		case PRODUCTION: avail = 3; break;
		case BUILDING: avail = 2; break;
		case LARGE_BUILDING: avail = 1; break;
		default: avail = 0;
		}
		
		return avail;
	}
}
