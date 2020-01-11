package com.github.koshamo.puri.ui.controls.player;

import com.github.koshamo.puri.setup.BuildingType;
import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/*private*/ class BuildingFieldSkin extends SkinBase<BuildingField> {

	private final static int HEIGHT = 85;
	private final static int WIDTH = 80;
	
	protected BuildingFieldSkin(BuildingField control) {
		super(control);
		drawComponent(BuildingType.NONE, 0, 0);
	}

	public void drawComponent(BuildingType type, int places, int colonists) {
		this.getChildren().clear();
		
		Pane pane = new Pane();
		
		addField(type, pane);
		addTitle(type, pane);
		addPlaces(places, colonists, pane);

		this.getChildren().add(pane);
	}

	private static void addTitle(BuildingType type, Pane pane) {
		Color txtColor = selectTextColor(type);

		Text title = new Text(type.getName());
		title.setFill(txtColor);
		title.relocate(2, 2);

		pane.getChildren().addAll(title);
	}

	private static void addField(BuildingType type, Pane pane) {
		Color bColor = selectBuildingColor(type);

		int width = type.getSize() * WIDTH;
		Rectangle border = new Rectangle(width + 2, HEIGHT + 2, Color.BLACK);
		Rectangle rect = new Rectangle(width, HEIGHT, bColor);
		rect.relocate(1, 1);

		Tooltip tooltip = new Tooltip(type.getDescription());
		Tooltip.install(rect, tooltip);

		pane.getChildren().addAll(border, rect);
	}

	private static void addPlaces(int places, int colonists, Pane pane) {
		for (int p = 0, c = 0; p < places; p++, c++) {
			Color color = c < colonists 
					? PrColors.ACTIVE_BGD.getColor() 
					: PrColors.INACTIVE.getColor();
			Circle circle = new Circle(10, color);
			circle.relocate(5 + p * 25, 60);
			pane.getChildren().add(circle);
		}
	}

	private static Color selectTextColor(BuildingType type) {
		Color color;
		switch (type) {
		case KL_INDIGO:	/*fall through*/
		case GR_INDIGO: color = PrColors.INDIGO_TXT.getColor(); break;
		case KL_ZUCKER:	/*fall through*/
		case GR_ZUCKER: color = PrColors.SUGAR_TXT.getColor(); break;
		case TABAK: color = PrColors.TOBACCO_TXT.getColor(); break;
		case KAFFEE: color = PrColors.COFFEE_TXT.getColor(); break;
		default: color = Color.BLACK;
		}
		return color;
	}

	private static Color selectBuildingColor(BuildingType type) {
		Color color;
		switch (type) {
		case KL_INDIGO:	/*fall through*/
		case GR_INDIGO: color = PrColors.INDIGO.getColor(); break;
		case KL_ZUCKER:	/*fall through*/
		case GR_ZUCKER: color = PrColors.SUGAR.getColor(); break;
		case TABAK: color = PrColors.TOBACCO.getColor(); break;
		case KAFFEE: color = PrColors.COFFEE.getColor(); break;
		case NONE: color = PrColors.DEFAULT_BGD.getColor(); break;
		default: color = PrColors.BUILDING.getColor();
		}
		return color;
	}

}
