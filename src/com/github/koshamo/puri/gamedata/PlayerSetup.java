package com.github.koshamo.puri.gamedata;

import com.github.koshamo.puri.ai.AbstractAi;
import com.github.koshamo.puri.ai.AiFactory;
import com.github.koshamo.puri.setup.GameSet;

public class PlayerSetup {

	public final String name;
	public final boolean ai;
	public final AbstractAi aiType;
	public final GameSet gameSet;
	
	public PlayerSetup(String name, boolean ai, String aiType, GameSet gameSet) {
		this.name = name;
		this.ai = ai;
		this.aiType = AiFactory.AiByName(aiType);
		this.gameSet = gameSet;
	}
}
