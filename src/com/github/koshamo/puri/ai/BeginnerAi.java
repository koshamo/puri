package com.github.koshamo.puri.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.BuildingsModel;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;
import com.github.koshamo.puri.ui.controls.role.RoleCard;

import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;

public class BeginnerAi extends AbstractAi {

	private final int POINTS_GULDEN_ON_ROLE = 5;
	
	public BeginnerAi(List<Player> players, 
			Board gameBoard, RoleBoard roleBoard) {
		super(players, gameBoard, roleBoard);
	}

	@Override
	public void chooseRole() {
		System.out.print("AI: choose Role: ");
		
		List<Pair<RoleCard,Integer>> ratedRoles = new LinkedList<>();
		for (RoleCard rc : roleBoard.roleCards()) 
			if (!rc.isUsed()) {
				int points = calcRoleGain(rc);
				ratedRoles.add(new Pair<>(rc, Integer.valueOf(points)));
			}
		
		ratedRoles.sort((o1,o2) -> {return o2.second().compareTo(o1.second());});
		System.out.println(ratedRoles.get(0).first().type() + " " + ratedRoles.get(0).second() + " Points");
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
		ObservableList<BuildingsModel> availableBuildings = gameBoard.availableBuildings();
		
		int maxPoints = 0;
		// TODO: think about weighting of buildings per phase
		for (BuildingsModel bm : availableBuildings) {
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
		// TODO Auto-generated method stub
		return 0;
	}

	private int calcGainGouvernor() {
		// TODO Auto-generated method stub
		return 0;
	}

	private int calcGainProducer() {
		// TODO Auto-generated method stub
		return 0;
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
