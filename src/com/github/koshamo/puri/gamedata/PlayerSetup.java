package com.github.koshamo.puri.gamedata;

import com.github.koshamo.puri.setup.GameSet;

public class PlayerSetup {

	public final String name;
	public final boolean ai;
	public final String aiType;
	public final GameSet gameSet;
	
	public PlayerSetup(String name, boolean ai, String aiType, GameSet gameSet) {
		this.name = name;
		this.ai = ai;
		this.aiType = aiType;
		this.gameSet = gameSet;
	}
}
