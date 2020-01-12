package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GoodsShipSkin extends SkinBase<GoodsShip> {

	private final int places;
	private final int upperRowCnt;
	private final int lowerRowCnt;
	
	protected GoodsShipSkin(GoodsShip control, int places) {
		super(control);
		this.places = places;
		if (places > 4) {
			upperRowCnt = places / 2;
			lowerRowCnt = places / 2 + places % 2;
		} else {
			upperRowCnt = 0;
			lowerRowCnt = places;
		}
		drawComponent(PlantationType.NONE, 0);
	}
	
	public void drawComponent(PlantationType product, int amount) {
		this.getChildren().clear();
		
		Pane pane = new Pane();
		
		drawShip(pane);
		drawText(pane, amount);
		drawGoods(pane, product, amount);
		
		this.getChildren().add(pane);
	}

	private static void drawShip(Pane pane) {
		Path path = new Path();

		MoveTo start = new MoveTo(1, 50);
		LineTo line1 = new LineTo(21, 55);
		LineTo line2 = new LineTo(121, 55);
		LineTo line3 = new LineTo(141, 50);
		LineTo line4 = new LineTo(121, 85);
		LineTo line5 = new LineTo(21, 85);
		LineTo line6 = new LineTo(1, 50);

		path.getElements().add(start);
		path.getElements().addAll(line1, line2, line3, line4, line5, line6);
		path.setFill(Color.DARKGOLDENROD);
		pane.getChildren().add(path);
	}
	
	private void drawText(Pane pane, int amount) {
		Text text = new Text(amount + " / " + places);
		text.setX(60);
		text.setY(75);
		pane.getChildren().add(text);
	}
	
	private void drawGoods(Pane pane, PlantationType product, int amount) {
		Color productColor = selectColor(product);
		Color curColor;
		double offset = (40 - upperRowCnt*20.0/2) * (lowerRowCnt-upperRowCnt);
		double distance = 80 / (lowerRowCnt-1);
		int load = 0;
		
		for (int i = 0; i < lowerRowCnt; i++, load++) {
			if (load < amount)
				curColor = productColor;
			else 
				curColor = PrColors.DEFAULT_BGD.getColor();
			Rectangle border = new Rectangle(22, 22, Color.BLACK);
			border.relocate(21+i*distance, 30);
			Rectangle rect = new Rectangle(20, 20, curColor);
			rect.relocate(22+i*distance, 31);
			pane.getChildren().addAll(border, rect);
		}
		for (int i = 0; i < upperRowCnt; i++, load++) {
			if (load < amount)
				curColor = productColor;
			else 
				curColor = PrColors.DEFAULT_BGD.getColor();
			Rectangle border = new Rectangle(22, 22, Color.BLACK);
			border.relocate(21+offset+i*distance, 3);
			Rectangle rect = new Rectangle(20, 20, curColor);
			rect.relocate(22+offset+i*distance, 4);
			pane.getChildren().addAll(border, rect);
		}
	}

	private Color selectColor(PlantationType product) {
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

}
