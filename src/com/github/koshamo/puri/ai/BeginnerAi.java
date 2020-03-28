package com.github.koshamo.puri.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.setup.BuildingType;
import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.BuildingsModel;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.State;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.BuildingField;
import com.github.koshamo.puri.ui.controls.player.PlantationField;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;
import com.github.koshamo.puri.ui.controls.role.RoleCard;

import javafx.scene.control.ButtonType;

public class BeginnerAi extends AbstractAi {

	private final int POINTS_GULDEN_ON_ROLE = 5;
	private final int POINTS_FOR_CORN = 5;
	private final int DIVIDER_FOR_SETTLER = 10;
	private final int DIVIDER_FOR_GOUVERNOR = 3;
	
	public BeginnerAi(List<Player> players, 
			Board gameBoard, RoleBoard roleBoard) {
		super(players, gameBoard, roleBoard);
	}

	@Override
	public void chooseRole() {
		System.out.print("AI " + player.name() + ": choose Role: ");
		
		List<Pair<RoleCard,Integer>> ratedRoles = new LinkedList<>();
		for (RoleCard rc : roleBoard.roleCards()) 
			if (!rc.isUsed()) {
				int points = calcRoleGain(rc);
				ratedRoles.add(new Pair<>(rc, Integer.valueOf(points)));
			}
		
		ratedRoles.sort((o1,o2) -> {return o2.second().compareTo(o1.second());});
		System.out.println(ratedRoles.get(0).first().type() + " " + ratedRoles.get(0).second() + " Points");
		for (Pair<RoleCard,Integer> p : ratedRoles)
			System.out.println("  > " + p.first().type() + " " + p.second());
		propagateRole(ratedRoles.get(0).first());
	}

	private int calcRoleGain(RoleCard rc) {
		int points = rc.gulden() * POINTS_GULDEN_ON_ROLE;
		
		switch (rc.type()) {
		case BAUMEISTER: return points + calcGainBuilder();
		case SIEDLER: return points + calcGainSettler();
		case BUERGERMEISTER: return points + calcGainGouvernor();
		case AUFSEHER: return points + calcGainProducer();
		case KAPITAEN: return points + calcGainCaptain();
		case HAENDLER: return points + calcGainTrader();
		case GOLDSUCHER: return points + calcGainGoldfinder();
		default: return 0;
		}
	}

	private int calcGainBuilder() {
		int availableGulden = player.availableGulden() + 1;
		List<BuildingTypeList> ownedBuildings = player.ownedBuildings();
		
		int maxPoints = 0;
		// TODO: think about weighting of buildings per phase
		for (BuildingsModel bm : gameBoard.availableBuildings()) {
			if (Integer.valueOf(bm.getLeft()).intValue() > 0
					&& !(bm.type().getCost() > availableGulden)
					&& !ownedBuildings.contains(bm.type())) {
				if (bm.type().getVictoryPoints() > maxPoints)
					maxPoints = bm.type().getVictoryPoints();
			}
		}

		return maxPoints;
	}

	private int calcGainSettler() {
		int points = 0;
		
		// the more free plantations the more relevant is settler
		points += calcFreePlantationGain();

		int[] rawMaterials = calcOwnedPlantationsPerType();
		points += calcQuarryGain(rawMaterials[5]);
		
		int[] products = calcProductionCapacityPerType();
		points += calcPointsPerDrawablePlantation(rawMaterials, products);
		
		return points / DIVIDER_FOR_SETTLER;
	}

	private int calcFreePlantationGain() {
		return 12 - player.numPlantations();
	}
	
	private int[] calcOwnedPlantationsPerType() {
		int[] rawMaterials = new int[6]; // corn = 4, quarry = 5

		for (PlantationField pf : player.ownedPlantations())
			switch (pf.type()) {
			case QUARRY: rawMaterials[5]++; break;
			case INDIGO: rawMaterials[0]++; break;
			case SUGAR: rawMaterials[1]++; break;
			case CORN: rawMaterials[4]++; break;
			case TOBACCO: rawMaterials[2]++; break;
			case COFFEE: rawMaterials[3]++; break;
			default: break;
			}
		return rawMaterials;
	}
	
	private int[] calcProductionCapacityPerType() {
		int[] products = new int[4];
		
		for (BuildingTypeList btl : player.ownedBuildings())
			switch (btl) {
			case KL_INDIGO: products[0] += 1; break;
			case GR_INDIGO: products[0] += 3; break;
			case KL_ZUCKER: products[1] += 1; break;
			case GR_ZUCKER: products[1] += 3; break;
			case TABAK: products[2] += 3; break;
			case KAFFEE: products[3] += 2; break;
			default: break;
			}
		return products;
	}
	
	private int calcQuarryGain(int quarries) {
		if (gameBoard.plantations().drawableQuarries() > 0)
			return 4 - quarries;
		return 0;
	}
	
	private int calcPointsPerDrawablePlantation(int[] rawMats, int[] prods) {
		int points = 0;
		
		for (PlantationType pt : gameBoard.plantations().drawablePlantations()) {
			if (pt == PlantationType.INDIGO)
				points += prods[0] - rawMats[0];
			if (pt == PlantationType.SUGAR)
				points += prods[1] - rawMats[1];
			if (pt == PlantationType.CORN)
				points += POINTS_FOR_CORN;
			if (pt == PlantationType.TOBACCO)
				points += prods[2] - rawMats[2];
			if (pt == PlantationType.COFFEE)
				points += prods[3] - rawMats[3];
		}

		return points;
	}

	private int calcGainGouvernor() {
		int colonistsToGet = calcDrawableColonists();
		int[] plantations = calcPlantationsColonists();
		int[] productions = calcProductionBuildingsColonists();
		int inactiveBuildings = calcEmptyBuildings();
		
		int freeProduction = 0;
		for (int i = 0; i < productions.length; i += 2) {
			if (plantations[i] != productions[i])
				if (plantations[i] > productions[i])
					freeProduction += productions[i+1];
				else
					freeProduction += plantations[i+1];
				
			if (plantations[i+1] > 0 && productions[i+1] > 0)
				freeProduction += Math.abs(plantations[i] - productions[i]);
		}
		freeProduction += plantations[9];
		freeProduction += plantations[11];
		
		return (freeProduction + inactiveBuildings - colonistsToGet) / DIVIDER_FOR_GOUVERNOR;
	}
	
	private int calcDrawableColonists() {
		int colonistsOnBoard = gameBoard.colonists();
		int colonistsToGet = colonistsOnBoard % players.size() > 0 
				? colonistsOnBoard / 3 + 2 : colonistsOnBoard / 3 + 1;
		return colonistsToGet;
	}

	private int[] calcPlantationsColonists() {
		int[] plantations = new int[12]; // active, inactive,...
										 // corn = 8, quarry = 10
		
		for (PlantationField pf : player.ownedPlantations())
			if (pf.state() == State.INACTIVE) 
				switch (pf.type()) {
				case INDIGO: 
					addPointPerState(plantations, pf.state(), 0);
					break;
				case SUGAR: 
					addPointPerState(plantations, pf.state(), 2);
					break;
				case TOBACCO: 
					addPointPerState(plantations, pf.state(), 4);
					break;
				case COFFEE: 
					addPointPerState(plantations, pf.state(), 6);
					break;
				case CORN: 
					addPointPerState(plantations, pf.state(), 8);
					break;
				case QUARRY: 
					addPointPerState(plantations, pf.state(), 10);
					break;
				default: break;
				}
		
		return plantations;
	}
	
	private static void addPointPerState(int[] field, State state, int index) {
		if (state == State.ACTIVE) 
			field[index]++;
		else 
			field[index + 1]++;
	}
	
	private int[] calcProductionBuildingsColonists() {
		int[] productionPlaces = new int[8]; // active, inactive,..
		
		for (BuildingField bf: player.ownedBuildingsAsField())
			switch (bf.type()) {
			case KL_INDIGO: 
				addPointPerState(productionPlaces, bf.state(), 0);
				break;
			case GR_INDIGO: 
				addPointsPerFreePlaces(productionPlaces, bf, 0);
				break;
			case KL_ZUCKER: 
				addPointPerState(productionPlaces, bf.state(), 2);
				break;
			case GR_ZUCKER: 
				addPointsPerFreePlaces(productionPlaces, bf, 2);
				break;
			case TABAK: 
				addPointsPerFreePlaces(productionPlaces, bf, 4);
				break;
			case KAFFEE: 
				addPointsPerFreePlaces(productionPlaces, bf, 6);
				break;
			default: break;
			}
		
		return productionPlaces;
	}
	
	private static void addPointsPerFreePlaces(int[] places, BuildingField field, int index) {
		places[index] += field.type().getPlaces() - field.emptyPlaces();
		places[index + 1] += field.emptyPlaces();
	}
	
	private int calcEmptyBuildings() {
		int emptyBuilding = 0;

		for (BuildingField bf: player.ownedBuildingsAsField())
			if (bf.state() == State.INACTIVE
				&& bf.type().getType() == BuildingType.BUILDING
				|| bf.type().getType() == BuildingType.LARGE_BUILDING)
				emptyBuilding++;

		return emptyBuilding;
	}
	
	private int calcGainProducer() {
		int[] producableMaterials = calcProducableMaterials();
		int[] producableProducts = calcProducableProducts();
		
		int production = calcProduction(producableMaterials, producableProducts);
		return production;
	}

	private int[] calcProducableMaterials() {
		int[] materials = new int[5];
		
		for (PlantationField pf: player.ownedPlantations())
			if (pf.state() == State.ACTIVE)
				switch (pf.type()) {
				case INDIGO: materials[0]++; break;
				case SUGAR: materials[1]++; break;
				case CORN: materials[4]++; break;
				case TOBACCO: materials[2]++; break;
				case COFFEE: materials[3]++; break;
				default: break;
				}

		return materials;
	}

	private int[] calcProducableProducts() {
		int[] activeProductionPlaces = new int[4];
		
		for (BuildingField bf: player.ownedBuildingsAsField())
			if (bf.state() == State.ACTIVE)
				switch (bf.type()) {
				case KL_INDIGO: activeProductionPlaces[0]++; break;
				case GR_INDIGO: activeProductionPlaces[0] += bf.colonists();
					break;
				case KL_ZUCKER: activeProductionPlaces[1]++; break;
				case GR_ZUCKER: activeProductionPlaces[1] += bf.colonists();
					break;
				case TABAK: activeProductionPlaces[2] += bf.colonists();
					break;
				case KAFFEE: activeProductionPlaces[3] += bf.colonists();
					break;
				default: break;
				}
		
		return activeProductionPlaces;
	}
	
	private int calcProduction(int[] materials, int[] products) {
		int production = 0;
		
		for (int i = 0; i < products.length; i++)
			production += Math.min(materials[i], products[i]);
		production += materials[4];
		
		return production;
	}

	private int calcGainCaptain() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int calcGainTrader() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int calcGainGoldfinder() {
		return POINTS_GULDEN_ON_ROLE;
	}

	@Override
	public Optional<BuildingTypeList> purchaseBuilding() {
		System.out.println("AI: choose Building");
		return Optional.empty();
	}

	@Override
	public Optional<ButtonType> useHazienda() {
		System.out.println("AI: use Hazienda");
		return Optional.empty();
	}

	@Override
	public void choosePlantation() {
		System.out.println("AI: choose Plantation");
		propagatePlantation(PlantationType.NONE);
	}

	@Override
	public void distributeColonists() {
		System.out.println("AI: distribute Colonists");
		propagateColonistDistribution();
	}

	@Override
	public Optional<List<PlantationType>> chooseProductionExtra(int[] possibleExtras) {
		System.out.println("AI: choose Production Extra");
		return Optional.empty();
	}

	@Override
	public void shipProduct() {
		System.out.println("AI: ship Products");
		propagateShipping();
	}

	@Override
	public void trade() {
		System.out.println("AI: trade");
		propagateTrading();
	}

}
