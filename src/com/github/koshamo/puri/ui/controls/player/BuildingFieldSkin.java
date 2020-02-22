package com.github.koshamo.puri.ui.controls.player;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.PrColorUtils;
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
		drawComponent(BuildingTypeList.NONE, 0, 0);
	}

	public void drawComponent(BuildingTypeList type, int places, int colonists) {
		this.getChildren().clear();
		
		Pane pane = new Pane();
		
		addField(type, pane);
		addTitle(type, pane);
		addPlaces(places, colonists, pane);
		addVP(type, pane);

		this.getChildren().add(pane);
	}

	private static void addTitle(BuildingTypeList type, Pane pane) {
		Color txtColor = PrColorUtils.selectTextColor(type);

		String text = type.getName();
		Text title;
		if (text != null)
			title = new Text(text.replace('-', '\n'));
		else 
			title = new Text();
		title.setFill(txtColor);
		title.relocate(2, 2);

		pane.getChildren().addAll(title);
	}

	private static void addField(BuildingTypeList type, Pane pane) {
		Color bColor = PrColorUtils.selectBuildingColor(type);

		int width = type.getSize() * WIDTH;
		Rectangle border = new Rectangle(width + 2, HEIGHT + 2, Color.BLACK);
		Rectangle rect = new Rectangle(width, HEIGHT, bColor);
		rect.relocate(1, 1);

		if (type != BuildingTypeList.NONE) {
			Tooltip tooltip = new Tooltip(type.getDescription());
			Tooltip.install(rect, tooltip);
		}

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

	private static void addVP(BuildingTypeList type, Pane pane) {
		if (type != BuildingTypeList.NONE) {
			Circle circle = new Circle(10, PrColors.DEFAULT_BGD.getColor());
			circle.relocate(WIDTH - 20, 40);

			Text vp = new Text(String.valueOf(type.getVictoryPoints()));
			vp.relocate(WIDTH - 15, 42);

			pane.getChildren().addAll(circle, vp);
		}
	}

}
