package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColorUtils;

import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MarketSkin extends SkinBase<Market> {

	protected MarketSkin(Market control) {
		super(control);
		drawComponent(new PlantationType[] {
				PlantationType.NONE, PlantationType.NONE, 
				PlantationType.NONE, PlantationType.NONE
				});
		installTooltip(control);
	}

	private static void installTooltip(Market control) {
		Tooltip tooltip = new Tooltip(
				"Verkaufserlös:\tMais    = 0\nIndigo = 1\tZucker = 2\nTabak = 3\tKaffee  = 4");
		Tooltip.install(control, tooltip);
	}

	public void drawComponent(PlantationType[] products) {
		this.getChildren().clear();
		
		Pane pane = new Pane();
		drawFrame(pane);
		drawProducts(pane, products);
		
		this.getChildren().add(pane);
	}

	private static void drawFrame(Pane pane) {
		Rectangle border = new Rectangle(95, 95, Color.BLACK);
		Rectangle rect = new Rectangle(93, 93, Color.DARKMAGENTA);
		rect.relocate(1, 1);
		
		pane.getChildren().addAll(border, rect);
	}

	private static void drawProducts(Pane pane, PlantationType[] products) {
		for (int i = 0; i < products.length; i++) {
			Rectangle border = new Rectangle(42, 42, Color.BLACK);
			Rectangle rect = new Rectangle(40, 40, PrColorUtils.selectGoodsColor(products[i]));
			int posX = i / 2;
			int posY = i % 2;
			border.relocate(4 + posX * 45, 4 + posY * 45);
			rect.relocate(5 + posX * 45, 5 + posY * 45);
			pane.getChildren().addAll(border, rect);
		}
	}

}
