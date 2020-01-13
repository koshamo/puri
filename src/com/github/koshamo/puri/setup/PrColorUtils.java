package com.github.koshamo.puri.setup;

import javafx.scene.paint.Color;

public class PrColorUtils {

	public static Color selectGoodsColor(PlantationType product) {
		Color color;
		switch (product) {
		case INDIGO: color = PrColors.INDIGO.getColor(); break;
		case SUGAR: color = PrColors.SUGAR.getColor(); break;
		case CORN: color = PrColors.CORN.getColor(); break;
		case TOBACCO: color = PrColors.TOBACCO.getColor(); break;
		case COFFEE: color = PrColors.COFFEE.getColor(); break;
		default: color = PrColors.DEFAULT_BGD.getColor();
		}
		return color;
	}

	public static Color selectFieldColor(PlantationType type) {
		Color color;
		
		switch (type) {
		case QUARRY: color = PrColors.QUARRY.getColor(); break;
		case CORN: color = PrColors.CORN.getColor(); break;
		case INDIGO: color = PrColors.INDIGO.getColor(); break;
		case SUGAR: color = PrColors.SUGAR.getColor(); break;
		case TOBACCO: color = PrColors.TOBACCO.getColor(); break;
		case COFFEE: color = PrColors.COFFEE.getColor(); break;
		default: color = PrColors.DEFAULT_BGD.getColor();
		}
		
		return color;
	}

	public static Color selectTextColor(BuildingType type) {
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

	public static Color selectBuildingColor(BuildingType type) {
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
