package com.github.koshamo.puri.gamedata;

import com.github.koshamo.puri.setup.GameSet;

public class PlayerSetup {

	public final String name;
	public final boolean ki;
	public final String kiType;
	public final GameSet gameSet;
	
	public PlayerSetup(String name, boolean ki, String kiType, GameSet gameSet) {
		this.name = name;
		this.ki = ki;
		this.kiType = kiType;
		this.gameSet = gameSet;
	}
}
