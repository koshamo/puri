package com.github.koshamo.puri.ai;

import java.util.ArrayList;
import java.util.Arrays;
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

	private final int POINTS_GULDEN_ON_ROLE = 3;
	private final int POINTS_FOR_CORN = 2;
	private final int DIVIDER_FOR_SETTLER = 11;
	private final int DIVIDER_FOR_GOUVERNOR = 3;
	private final int NUM_OF_PRODUCTS_TO_REDUCE_PRODUCER = 4;
	private final int DIVIDER_FOR_PRODUCER = 3;
	
	public BeginnerAi(List<Player> players, 
			Board gameBoard, RoleBoard roleBoard) {
		super(players, gameBoard, roleBoard);
	}

	@Override
	public void chooseRole() {
		System.out.print(player.name() + " choose Role: ");
		
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
		int quarries = player.activeQuarries();
		List<BuildingTypeList> ownedBuildings = player.ownedBuildings();
		
		int maxPoints = 0;
		// TODO: think about weighting of buildings per phase
		for (BuildingsModel bm : gameBoard.availableBuildings()) {
			int cost = bm.type().getCost();
			int vp = bm.type().getVictoryPoints();
			int quarryGain = vp < quarries ? vp : quarries; 

			if (Integer.valueOf(bm.getLeft()).intValue() > 0
					&& !(cost - quarryGain > availableGulden)
					&& !ownedBuildings.contains(bm.type())) {
				if (bm.type().getVictoryPoints() > maxPoints)
					maxPoints = bm.type().getVictoryPoints();
			}
		}
		
		if (ownedBuildings.size() > 4 && maxPoints < 2)
			return 0;

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
		int[] plantations = calcColonistsOnPlantations();
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

	private int[] calcColonistsOnPlantations() {
		int[] plantations = new int[12]; // active, inactive,...
										 // corn = 8, quarry = 10
		
		for (PlantationField pf : player.ownedPlantations()) 
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
		if (player.hasActiveBuilding(BuildingTypeList.MANUFAKTUR))
			production += calcNumOfDifferentProducts(
					producableMaterials, producableProducts) - 1;
		int availableProducts = player.availableProducts(PlantationType.INDIGO)
				+ player.availableProducts(PlantationType.SUGAR)
				+ player.availableProducts(PlantationType.CORN)
				+ player.availableProducts(PlantationType.TOBACCO)
				+ player.availableProducts(PlantationType.COFFEE);
		
		if (availableProducts > NUM_OF_PRODUCTS_TO_REDUCE_PRODUCER)
			production /= DIVIDER_FOR_PRODUCER;
		
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
	
	private static int calcProduction(int[] materials, int[] products) {
		int production = 0;
		
		for (int i = 0; i < products.length; i++)
			production += Math.min(materials[i], products[i]);
		production += materials[4];
		
		return production;
	}

	private static int calcNumOfDifferentProducts(int[] materials, int[] products) {
		int production = 0;
		
		for (int i = 0; i < products.length; i++)
			if (materials[i] > 0 && products[i] > 0)
				production++;
		if (materials[4] > 0)
			production++;
		
		return production;
	}

	private int calcGainCaptain() {
		int maxAmount = calcMaxShippableProductsWithPrivilege();
		int extra = 0;
		
		if (player.hasActiveBuilding(BuildingTypeList.HAFEN))
			extra++;
		if (maxAmount > 0 && player.hasActiveBuilding(BuildingTypeList.WERFT))
			extra++;
		
		return maxAmount + extra;
	}
	
	private int calcMaxShippableProductsWithPrivilege() {
		int max = 0;
		int shippable = 0;
		
		shippable = calcShippableProductsPerType(PlantationType.INDIGO);
		if (shippable > max)
			max = shippable;
		shippable = calcShippableProductsPerType(PlantationType.SUGAR);
		if (shippable > max)
			max = shippable;
		shippable = calcShippableProductsPerType(PlantationType.CORN);
		if (shippable > max)
			max = shippable;
		shippable = calcShippableProductsPerType(PlantationType.TOBACCO);
		if (shippable > max)
			max = shippable;
		shippable = calcShippableProductsPerType(PlantationType.COFFEE);
		if (shippable > max)
			max = shippable;
		
		return max > 0 ? max++ : max;
	}

	private int calcShippableProductsPerType(PlantationType type) {
		int playerAmount = player.availableProducts(type);
		
		if (playerAmount > 0) {
			int amount = 0;
			if (gameBoard.hasShip(type)) 
				amount = gameBoard.freePlacesOnShipWith(type);
			else if (gameBoard.numShipsWithNone() > 0) 
				amount = gameBoard.freePlacesOnShipWith(PlantationType.NONE);
			if (player.hasActiveBuilding(BuildingTypeList.WERFT))
				return playerAmount;
			return Math.min(playerAmount, amount);
		}
		return 0;
	}

	private int calcGainTrader() {
		int maxGain = calcMaxTradingGain();
		
		return maxGain;
	}

	private int calcMaxTradingGain() {
		int maxGain = 0;
		int gain = 0;
		List<PlantationType> productsInMarket = gameBoard.listProductsInMarket();
		
		gain = calcTradingGainPerProduct(productsInMarket, PlantationType.INDIGO);
		if (gain > maxGain)
			maxGain = gain;
		gain = calcTradingGainPerProduct(productsInMarket, PlantationType.SUGAR);
		if (gain > maxGain)
			maxGain = gain;
		gain = calcTradingGainPerProduct(productsInMarket, PlantationType.CORN);
		if (gain > maxGain)
			maxGain = gain;
		gain = calcTradingGainPerProduct(productsInMarket, PlantationType.TOBACCO);
		if (gain > maxGain)
			maxGain = gain;
		gain = calcTradingGainPerProduct(productsInMarket, PlantationType.COFFEE);
		if (gain > maxGain)
			maxGain = gain;

		return maxGain;
	}
	
	private int calcTradingGainPerProduct(List<PlantationType> productsInMarket, PlantationType type) {
		int extra = 0;
		
		if (player.hasActiveBuilding(BuildingTypeList.KL_MARKT))
			extra += 1;
		if (player.hasActiveBuilding(BuildingTypeList.GR_MARKT))
			extra += 2;

		if (player.availableProducts(type) > 0
				&& (!productsInMarket.contains(type)
					|| player.hasActiveBuilding(BuildingTypeList.KONTOR)))
				return type.getPrice() + extra;
		return 0;
	}

	private int calcGainGoldfinder() {
		return POINTS_GULDEN_ON_ROLE;
	}

	@Override
	public Optional<BuildingTypeList> purchaseBuilding(boolean privilege) {
		int availableGulden = privilege ? player.availableGulden() + 1
				: player.availableGulden();
		
		List<BuildingTypeList> buyableBuildings = calcBuyableBuildings(availableGulden);
		
		if (buyableBuildings.size() == 0)
			return Optional.empty();
		
		BuildingTypeList toPurchase = calcBuildingToPurchase(buyableBuildings);
		
		if (toPurchase != BuildingTypeList.NONE
				&& player.ownedBuildings().size() > 4 
				&& toPurchase.getCost() < 3 
				&& (toPurchase.getType() != BuildingType.SMALL_PRODUCTION 
				|| toPurchase.getType() != BuildingType.PRODUCTION))
			toPurchase = BuildingTypeList.NONE;
			
		System.out.println(player.name() + " choose Building: " + toPurchase);
		return toPurchase == BuildingTypeList.NONE 
				? Optional.empty() 
				: Optional.of(toPurchase);
	}

	private BuildingTypeList calcBuildingToPurchase(List<BuildingTypeList> buyableBuildings) {
		int n = buyableBuildings.size() - 1;
		BuildingTypeList toPurchase = buyableBuildings.get(n);
		
		while (toPurchase.getType() == BuildingType.SMALL_PRODUCTION 
				|| toPurchase.getType() == BuildingType.PRODUCTION) {
			
			List<PlantationType> plantations = new LinkedList<>(); 
			for (PlantationField pf : player.ownedPlantations())
				plantations.add(pf.type());
			
			if (toPurchase == BuildingTypeList.KL_INDIGO 
					&& plantations.contains(PlantationType.INDIGO)) 
				break;
			if (toPurchase == BuildingTypeList.GR_INDIGO 
					&& plantations.indexOf(PlantationType.INDIGO) != plantations.lastIndexOf(PlantationType.INDIGO)) 
				break;
			if (toPurchase == BuildingTypeList.KL_ZUCKER 
					&& plantations.contains(PlantationType.SUGAR)) 
				break;
			if (toPurchase == BuildingTypeList.GR_ZUCKER
					&& plantations.indexOf(PlantationType.SUGAR) != plantations.lastIndexOf(PlantationType.SUGAR)) 
				break;
			if (toPurchase == BuildingTypeList.TABAK 
					&& plantations.contains(PlantationType.TOBACCO)) 
				break;
			if (toPurchase == BuildingTypeList.KAFFEE 
					&& plantations.contains(PlantationType.COFFEE)) 
				break;

			if (n > 0) {
				n--;
				toPurchase = buyableBuildings.get(n);
			} else {
				toPurchase = BuildingTypeList.NONE;
				break;
			}
		}
		return toPurchase;
	}

	private List<BuildingTypeList> calcBuyableBuildings(int availableGulden) {
		int quarries = player.activeQuarries();
		List<BuildingTypeList> ownedBuildings = player.ownedBuildings();
		
		List<BuildingTypeList> buyableBuildings = new ArrayList<>();
		for (BuildingsModel bm : gameBoard.availableBuildings()) {
			int cost = bm.type().getCost();
			int vp = bm.type().getVictoryPoints();
			int quarryGain = vp < quarries ? vp : quarries; 
			if (!ownedBuildings.contains(bm.type()) 
					&& !(cost - quarryGain > availableGulden)
					&& Integer.valueOf(bm.getLeft()).intValue() > 0)
				buyableBuildings.add(bm.type());
		}
		return buyableBuildings;
	}

	@Override
	public Optional<ButtonType> useHazienda() {
		System.out.println(player.name() + " use Hazienda "  + (player.numPlantations() < 11));
		if (player.numPlantations() < 11)
			return Optional.of(ButtonType.YES);
		return Optional.of(ButtonType.NO);
	}

	@Override
	public void choosePlantation(boolean canQuarry) {
		List<Pair<PlantationType, Integer>> mostNeeded = 
				calcMostNeededPlantation(canQuarry);
		
		PlantationType toDraw = calcPlantationToDraw(mostNeeded);

		System.out.println(player.name() + " choose Plantation " + toDraw);
		propagatePlantation(toDraw);
	}

	private PlantationType calcPlantationToDraw(List<Pair<PlantationType, Integer>> mostNeeded) {
		List<PlantationType> drawable = Arrays.asList(gameBoard.plantations().drawablePlantations());
		drawable.sort((p1,p2) -> {return p2.compareTo(p1);});
		
		PlantationType toDraw = PlantationType.NONE;
		
		for (Pair<PlantationType,Integer> p : mostNeeded) {
			if (drawable.contains(p.first())
					|| p.first() == PlantationType.QUARRY 
					&& gameBoard.plantations().drawableQuarries() > 0) {
				toDraw = p.first();
				break;
			}
		}
		if (toDraw == PlantationType.NONE) 
			for (PlantationType pt : drawable)
				if (pt != PlantationType.NONE) {
					toDraw = pt;
					break;
				}
		return toDraw;
	}

	private List<Pair<PlantationType, Integer>> calcMostNeededPlantation(boolean canQuarry) {
		int[] rawMaterials = calcOwnedPlantationsPerType();
		int[] products = calcProductionCapacityPerType();
		
		List<Pair<PlantationType,Integer>> mostNeeded = new ArrayList<>();
		if (products[0] > rawMaterials[0]) 
			mostNeeded.add(new Pair<>(PlantationType.INDIGO, Integer.valueOf(products[0] - rawMaterials[0])));
		if (products[1] > rawMaterials[1]) 
			mostNeeded.add(new Pair<>(PlantationType.SUGAR, Integer.valueOf(products[1] - rawMaterials[1])));
		if (products[0] > rawMaterials[2]) 
			mostNeeded.add(new Pair<>(PlantationType.TOBACCO, Integer.valueOf(products[2] - rawMaterials[2])));
		if (products[0] > rawMaterials[3]) 
			mostNeeded.add(new Pair<>(PlantationType.COFFEE, Integer.valueOf(products[3] - rawMaterials[3])));
		mostNeeded.add(new Pair<>(PlantationType.CORN, Integer.valueOf(4 - rawMaterials[4])));
		if (canQuarry)
			mostNeeded.add(new Pair<>(PlantationType.QUARRY, Integer.valueOf(4 - rawMaterials[5])));

		mostNeeded.sort((p1,p2) -> {return p2.second().compareTo(p1.second());});
		return mostNeeded;
	}

	@Override
	public void distributeColonists() {
		int freeCols = player.getColonistsFromPool();

		freeCols = distributeColonistsToProductionChain(freeCols);
		
		if (freeCols > 0) {
			freeCols = distributeColonistsToQuarries(freeCols);
		}
		
		if (freeCols > 0) {
			freeCols = distributeColonistsToCorn(freeCols);
		}
		
		if (freeCols > 0) {
			freeCols = distributeColonistsToBuildings(freeCols);
		}
		
		if (freeCols > 0) {
			freeCols = distributeColonistsToPlantations(freeCols);
		}
		
		System.out.println("AI: distribute Colonists");
		propagateColonistDistribution();
	}

	private int distributeColonistsToProductionChain(int freeColonists) {
		int freeCols = freeColonists;
		List<BuildingField> buildings = player.ownedBuildingsAsField();
		for (int i = buildings.size() - 1; i >= 0; i--) {
			BuildingField building = buildings.get(i); 
			
			freeCols = distributeColonistsForProduct(freeCols, building, BuildingTypeList.KAFFEE);
			freeCols = distributeColonistsForProduct(freeCols, building, BuildingTypeList.TABAK);
			freeCols = distributeColonistsForProduct(freeCols, building, BuildingTypeList.GR_ZUCKER);
			freeCols = distributeColonistsForProduct(freeCols, building, BuildingTypeList.KL_ZUCKER);
			freeCols = distributeColonistsForProduct(freeCols, building, BuildingTypeList.GR_INDIGO);
			freeCols = distributeColonistsForProduct(freeCols, building, BuildingTypeList.KL_INDIGO);
		}
		return freeCols;
	}

	private int distributeColonistsForProduct(int freeColonists, BuildingField building, BuildingTypeList buildingType) {
		// FIXME: SUGAR and INDIGO do not recognize small and large buildings
		
		int freeCols = freeColonists;
		PlantationType pType = mapBuildingToPlantation(buildingType);
		
		if (building.type() == buildingType) {
			int colMax = building.type().getPlaces();
			int colCur = building.colonists();
			int plantTotal = countPlantations(pType, true);
			int plantAct = countPlantations(pType, false);
			
			if (colCur == plantAct) {
				while (colMax > colCur && plantTotal > plantAct && freeCols > 1) {
					building.addColonist();
					player.addColonistToPlantation(pType);
					freeCols -= 2;
				}
			} else if (colCur > plantAct) {
				if (plantTotal > plantAct) { 
					player.addColonistToPlantation(pType);
					freeCols--;
				} else {
					building.removeColonist();
					freeCols++;
				}
			} else /* colCur < plantAct*/ {
				if (colMax > colCur) {
					building.addColonist();
					freeCols--;
				} else {
					player.removeColonistFromPlantation(pType);
					freeCols++;
				}
			}
		}
		return freeCols;
	}

	private int distributeColonistsToPlantations(int freeColonists) {
		int freeCols = freeColonists;
		for (PlantationField pf : player.ownedPlantations()) {
			if (pf.state() == State.INACTIVE
					&& freeCols > 0) {
				pf.activate();
				freeCols--;
			}
		}
		return freeCols;
	}

	private int distributeColonistsToBuildings(int freeColonists) {
		int freeCols = freeColonists;

		List<BuildingField> buildings = player.ownedBuildingsAsField();
		for (int i = buildings.size() - 1; i >= 0; i--) {
			BuildingField building = buildings.get(i); 
			while (building.emptyPlaces() > 0 && freeCols > 0) {
				building.addColonist();
				freeCols--;
			}
		}
		
		return freeCols;
	}

	private int distributeColonistsToQuarries(int freeColonists) {
		int freeCols = freeColonists;
		for (PlantationField pf : player.ownedPlantations()) {
			if (pf.type() == PlantationType.QUARRY 
					&& pf.state() == State.INACTIVE
					&& freeCols > 0) {
				pf.activate();
				freeCols--;
			}
		}
		return freeCols;
	}

	private int distributeColonistsToCorn(int freeColonists) {
		int freeCols = freeColonists;
		for (PlantationField pf : player.ownedPlantations()) {
			if (pf.type() == PlantationType.CORN 
					&& pf.state() == State.INACTIVE
					&& freeCols > 0) {
				pf.activate();
				freeCols--;
			}
		}
		return freeCols;
	}

	private static PlantationType mapBuildingToPlantation(BuildingTypeList buildingType) {
		PlantationType pType;
		switch (buildingType) {
		case KAFFEE: pType = PlantationType.COFFEE; break;
		case TABAK: pType = PlantationType.TOBACCO; break;
		case GR_ZUCKER: pType = PlantationType.SUGAR; break;
		case KL_ZUCKER: pType = PlantationType.SUGAR; break;
		case GR_INDIGO: pType = PlantationType.INDIGO; break;
		case KL_INDIGO: pType = PlantationType.INDIGO; break;
		default: pType = PlantationType.NONE;
		}
		return pType;
	}
	
	private int countPlantations(PlantationType type, boolean all) {
		int cnt = 0;
		
		for (PlantationField pf : player.ownedPlantations())
			if (pf.type() == type
					&& (all || pf.state() == State.ACTIVE))
				cnt++;
		return cnt;
	}

	@Override
	public Optional<List<PlantationType>> chooseProductionExtra(int[] possibleExtras) {
		List<PlantationType> extra = new LinkedList<>(); 
		PlantationType type = PlantationType.NONE;
		if (possibleExtras[4] > 0)
			type = PlantationType.COFFEE;
		else if (possibleExtras[3] > 0)
			type = PlantationType.TOBACCO;
		else if (possibleExtras[2] > 0)
			type = PlantationType.CORN;
		else if (possibleExtras[1] > 0)
			type = PlantationType.SUGAR;
		else if (possibleExtras[0] > 0)
			type = PlantationType.INDIGO;
		
		System.out.println(player.name() + " choose Production Extra: " + type);
		extra.add(type);
		return Optional.of(extra);
	}

	@Override
	public void shipProduct() {
		PlantationType type = calcMaxShippableProdcutType();
		
		int toShip; 
		if (type == PlantationType.NONE && player.hasActiveBuilding(BuildingTypeList.WERFT)) {
			toShip = shipProductsWithWerft();
		} else 
			toShip = shipProducts(type);

		player.reduceProduct(type, toShip);
		player.addVictoryPoints(toShip);
		gameBoard.reduceVictoryPoints(toShip);
		gameBoard.moveProductBackToPool(type, toShip);

		System.out.println(player.name() + " ship Products: " + toShip + " " + type);
		propagateShipping();
	}

	private int shipProductsWithWerft() {
		PlantationType type = calcMaxAvailableProductType();
		int playerAmount = player.availableProducts(type);
		
		player.useWerft();
		
		return playerAmount;
	}

	private int shipProducts(PlantationType type) {
		int playerAmount = player.availableProducts(type);

		int amount = 0;
		if (gameBoard.hasShip(type)) 
			amount = gameBoard.freePlacesOnShipWith(type);
		else if (gameBoard.numShipsWithNone() > 0) 
			amount = gameBoard.freePlacesOnShipWith(PlantationType.NONE);
		int toShip = Math.min(playerAmount, amount);

		gameBoard.autoShipProduct(type, toShip);
		
		return toShip;
	}
	
	private PlantationType calcMaxAvailableProductType() {
		PlantationType type = PlantationType.NONE;
		int max = 0;
		
		int cur = player.availableProducts(PlantationType.INDIGO); 
		if (cur > max) {
			max = cur;
			type = PlantationType.INDIGO;
		}
		cur = player.availableProducts(PlantationType.SUGAR); 
		if (cur > max) {
			max = cur;
			type = PlantationType.SUGAR;
		}
		cur = player.availableProducts(PlantationType.CORN); 
		if (cur > max) {
			max = cur;
			type = PlantationType.CORN;
		}
		cur = player.availableProducts(PlantationType.TOBACCO); 
		if (cur > max) {
			max = cur;
			type = PlantationType.TOBACCO;
		}
		cur = player.availableProducts(PlantationType.COFFEE); 
		if (cur > max) {
			max = cur;
			type = PlantationType.COFFEE;
		}
		return type;
	}
	
	private PlantationType calcMaxShippableProdcutType() {
		int max = 0;
		PlantationType type = PlantationType.NONE;
		
		int cur = calcShippableProductsPerType(PlantationType.INDIGO);
		if (cur > max) {
			max = cur;
			type = PlantationType.INDIGO;
		}
		cur = calcShippableProductsPerType(PlantationType.SUGAR);
		if (cur > max) {
			max = cur;
			type = PlantationType.SUGAR;
		}
		cur = calcShippableProductsPerType(PlantationType.CORN);
		if (cur > max) {
			max = cur;
			type = PlantationType.CORN;
		}
		cur = calcShippableProductsPerType(PlantationType.TOBACCO);
		if (cur > max) {
			max = cur;
			type = PlantationType.TOBACCO;
		}
		cur = calcShippableProductsPerType(PlantationType.COFFEE);
		if (cur > max) {
			max = cur;
			type = PlantationType.COFFEE;
		}
		
		return type;
	}

	@Override
	public void trade() {
		boolean kontor = player.hasActiveBuilding(BuildingTypeList.KONTOR);
		boolean market = player.hasActiveBuilding(BuildingTypeList.KL_MARKT) 
				|| player.hasActiveBuilding(BuildingTypeList.GR_MARKT);
		List<PlantationType> inMarket = gameBoard.listProductsInMarket();
		
		PlantationType type = PlantationType.NONE;
		if (player.availableProducts(PlantationType.COFFEE) > 0
			&& (kontor || !inMarket.contains(PlantationType.COFFEE))) {
			type = PlantationType.COFFEE;
		} else if (player.availableProducts(PlantationType.TOBACCO) > 0
				&& (kontor || !inMarket.contains(PlantationType.TOBACCO))) {
			type = PlantationType.TOBACCO;
		} else if (player.availableProducts(PlantationType.SUGAR) > 0
				&& (kontor || !inMarket.contains(PlantationType.SUGAR))) {
			type = PlantationType.SUGAR;
		} else if (player.availableProducts(PlantationType.INDIGO) > 0
				&& (kontor || !inMarket.contains(PlantationType.INDIGO))) {
			type = PlantationType.INDIGO;
		} else if (player.availableProducts(PlantationType.CORN) > 0
				&& market
				&& (kontor || !inMarket.contains(PlantationType.CORN))) {
				type = PlantationType.CORN;
		}

		if (type != PlantationType.NONE)
			sellProduct(type);

		System.out.println(player.name() + " trade " + type);
		propagateTrading();
	}

	private void sellProduct(PlantationType type) {
		int extra = 0;
		if (player.hasActiveBuilding(BuildingTypeList.KL_MARKT))
			extra += 1;
		if (player.hasActiveBuilding(BuildingTypeList.GR_MARKT))
			extra += 2;

		int amount = 0;
		switch (type) {
		case INDIGO: amount = 1; break;
		case SUGAR: amount = 2; break;
		case CORN: amount = 0; break;
		case TOBACCO: amount = 3; break;
		case COFFEE: amount = 4; break;
		default: amount = 0;
		}
		
		player.reduceProduct(type, 1);
		player.addGulden(amount + extra);
		gameBoard.addProductToMarket(type);
	}

	@Override
	public Optional<List<PlantationType>> chooseProductsToKeep() {
		List<PlantationType> toStore;
		int extraStorage = calcExtraStorage();
		
		if (extraStorage == 0)
			toStore = calcSingleStorage();
		else
			toStore = calcMultipleStorage(extraStorage);
		
		return Optional.of(toStore);
	}

	private List<PlantationType> calcSingleStorage() {
		List<PlantationType> toStore = new LinkedList<>();
		PlantationType mostValuable = calcMostValuableProductAvailable();
		
		toStore.add(mostValuable);
		return toStore;
	}

	private PlantationType calcMostValuableProductAvailable() {
		PlantationType mostValuable = PlantationType.NONE;
		
		if (player.availableProducts(PlantationType.COFFEE) > 0)
			mostValuable = PlantationType.COFFEE;
		else if (player.availableProducts(PlantationType.TOBACCO) > 0)
			mostValuable = PlantationType.TOBACCO;
		else if (player.availableProducts(PlantationType.SUGAR) > 0)
			mostValuable = PlantationType.SUGAR;
		else if (player.availableProducts(PlantationType.INDIGO) > 0)
			mostValuable = PlantationType.INDIGO;
		else if (player.availableProducts(PlantationType.CORN) > 0)
			mostValuable = PlantationType.CORN;
		
		return mostValuable;
	}

	private List<PlantationType> calcMultipleStorage(int extraStorage) {
		List<PlantationType> toStore = new LinkedList<>();

		List<Pair<PlantationType,Integer>> availableProducts = new ArrayList<>();
		availableProducts.add(new Pair<>(PlantationType.INDIGO, Integer.valueOf(player.availableProducts(PlantationType.INDIGO))));
		availableProducts.add(new Pair<>(PlantationType.SUGAR, Integer.valueOf(player.availableProducts(PlantationType.SUGAR))));
		availableProducts.add(new Pair<>(PlantationType.CORN, Integer.valueOf(player.availableProducts(PlantationType.CORN))));
		availableProducts.add(new Pair<>(PlantationType.TOBACCO, Integer.valueOf(player.availableProducts(PlantationType.TOBACCO))));
		availableProducts.add(new Pair<>(PlantationType.COFFEE, Integer.valueOf(player.availableProducts(PlantationType.COFFEE))));

		availableProducts.sort((p1,p2) -> {
			int amountCheck = p2.second().compareTo(p1.second());
			if (amountCheck != 0)
				return amountCheck;
			return p2.first().compareTo(p1.first());
			});
				
		for (int i = extraStorage; i >= 0; i--)
			toStore.add(availableProducts.get(i).first());
		
		return toStore;
	}

	private int calcExtraStorage() {
		int extraStorage = 0;
		if (player.hasActiveBuilding(BuildingTypeList.KL_LAGER))
			extraStorage += 1;
		if (player.hasActiveBuilding(BuildingTypeList.GR_LAGER))
			extraStorage += 2;
		return extraStorage;
	}

}
