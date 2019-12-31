import javafx.scene.paint.Color;

public enum PrColors {

	INDIGO (Color.DODGERBLUE),
	ZUCKER (Color.WHITE),
	MAIS (Color.YELLOW),
	TABAK (Color.BURLYWOOD),
	KAFFEE (Color.MAROON),
	QUARRY (Color.SILVER),
	GEBAUEDE (Color.KHAKI),
	LEERFELD (Color.LAVENDER),
	KOLONIST (Color.HOTPINK),
	PLAYER_1 (Color.SPRINGGREEN),
	PLAYER_2 (Color.SLATEBLUE),
	PLAYER_3 (Color.TOMATO),
	PLAYER_4 (Color.GOLD),
	PLAYER_5 (Color.SLATEGREY);
	
	private Color color;
	
	private PrColors (Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
}
