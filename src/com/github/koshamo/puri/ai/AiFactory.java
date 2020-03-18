package com.github.koshamo.puri.ai;

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
	
	public static AbstractAi AiByName(String name) {
		switch (name) {
		case "Anfänger": return /*new Beginner AI*/ null;
		default: return null;
		}
	}
}
