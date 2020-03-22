package com.github.koshamo.puri.ai;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;
import com.github.koshamo.puri.ui.controls.role.RoleCard;

import javafx.scene.control.ButtonType;

public class BeginnerAi extends AbstractAi {

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
		
		ratedRoles.sort((o1,o2) -> {return o1.second().compareTo(o2.second());});
		System.out.println(ratedRoles.get(0).first().type());
		propagateRole(ratedRoles.get(0).first());
	}

	private int calcRoleGain(RoleCard rc) {
		// TODO Auto-generated method stub
		return 0;
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
