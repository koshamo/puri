package com.github.koshamo.puri.ai;

import java.util.List;

import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;

public abstract class AbstractAi {

	protected final List<Player> players;
	protected final Board gameBoard;
	protected final RoleBoard roleBoard;
	
	public AbstractAi(List<Player> players, Board gameBoard, RoleBoard roleBoard) {
		this.players = players;
		this.gameBoard = gameBoard;
		this.roleBoard = roleBoard;
	}
	/*
	 * call
	 * controller.chooseRole(card.type(), card.removeGuldenAndDeactivateCard());
	 */
	public abstract void chooseRole();

}
