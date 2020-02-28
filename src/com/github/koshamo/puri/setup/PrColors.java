package com.github.koshamo.puri.setup;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public enum PrColors {

	INDIGO (Color.DODGERBLUE),
	INDIGO_TXT (Color.BLACK),
	SUGAR (Color.WHITE),
	SUGAR_TXT (Color.BLACK),
	CORN (Color.YELLOW),
	CORN_TXT (Color.BLACK),
	TOBACCO (Color.BURLYWOOD),
	TOBACCO_TXT (Color.BLACK),
	COFFEE (Color.MAROON),
	COFFEE_TXT (Color.WHITE),
	QUARRY (Color.SILVER),
	QUARRY_TXT (Color.BLACK),
	BUILDING (Color.KHAKI),
	COLONIST (Color.HOTPINK),
	COLONIST_TXT (Color.BLACK),
	GOUVERNOR_BGD (Color.DARKVIOLET),
	GOUVERNOR_TXT (Color.WHITE),
	ACTIVE_BGD (Color.RED),
	ACTIVE_TXT (Color.WHITE),
	INACTIVE (Color.GHOSTWHITE),
	DEFAULT_BGD (Color.ANTIQUEWHITE),
	ROLE (Color.BURLYWOOD),
	GULDEN (Color.GOLD),
	PLAYER_1 (Color.SPRINGGREEN),
	PLAYER_2 (Color.SLATEBLUE),
	PLAYER_3 (Color.TOMATO),
	PLAYER_4 (Color.GOLD),
	PLAYER_5 (Color.SLATEGREY);
	
	private Color color;
	private WritableImage icon;
	private WritableImage brightIcon;
	
	private PrColors (Color color) {
		this.color = color;
		icon = drawDnDImage(color.darker());
		brightIcon = drawDnDImage(color.brighter());
	}
	
	public Color getColor() {
		return color;
	}
	
	public static PrColors getPlayerColor(int i) {
		if (i == 1)
			return PLAYER_1;
		else if (i ==2)
			return PLAYER_2;
		else if (i == 3)
			return PLAYER_3;
		else if (i == 4)
			return PLAYER_4;
		else
			return PLAYER_5;
	}
	
	public WritableImage icon() {
		return icon;
	}
	
	public WritableImage brightIcon() {
		return brightIcon;
	}
	
	public static PrColors getByPlantationType(PlantationType type) {
		switch (type) {
		case INDIGO: return INDIGO;
		case SUGAR: return SUGAR;
		case CORN: return CORN;
		case TOBACCO: return TOBACCO;
		case COFFEE: return COFFEE;
		default: return DEFAULT_BGD;
		}
	}
	
	private static WritableImage drawDnDImage(Color color) {
		StackPane pane = new StackPane();
		Scene scene = new Scene(pane, 20, 20);
		WritableImage wim = new WritableImage(20, 20);

		Canvas canvas = new Canvas(20, 20);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.strokeOval(0, 0, 20, 20);
		gc.setFill(color);
		gc.fillOval(1, 1, 18, 18);
		
		pane.getChildren().add(canvas);
		scene.snapshot(wim);

		return wim;
	}
}
