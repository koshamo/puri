package com.github.koshamo.puri.ai;

import java.util.List;

import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;

public class AiFactory {

	public enum AiList {
		BEGINNER ("Anfänger");
		
		private final String name;
		
		private AiList(String name) {
			this.name = name;
		}
	
		@Override
		public String toString() {
			return name;
		}
	}
	
	public static String[] AiNames() {
		AiList[] ais = AiList.values();
		String[] names = new String[ais.length];
		
		for (int i = 0; i < ais.length; i++)
			names[i] = ais[i].toString();
		
		return names;
	}
	
	public static AbstractAi createAi(String name, List<Player> players, 
			Board gameBoard, RoleBoard roleBoard) {
		switch (name) {
		case "Anfänger": return new BeginnerAi(players, gameBoard, roleBoard);
		default: return null;
		}
	}
}
