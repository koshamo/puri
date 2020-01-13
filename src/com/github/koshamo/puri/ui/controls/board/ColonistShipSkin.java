package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;

/*private*/ class ColonistShipSkin extends SkinBase<ColonistShip> {

	protected ColonistShipSkin(ColonistShip control) {
		super(control);
		drawComponent(0);
	}

	public void drawComponent(int colonists) {
		this.getChildren().clear();
		
		Pane pane = new Pane();
		
		drawShip(pane);
		drawText(pane, colonists);
		drawColonists(pane, colonists);
		
		this.getChildren().add(pane);
	}
	
	private static void drawShip(Pane pane) {
		Path path = new Path();

		MoveTo start = new MoveTo(1, 25);
		LineTo line1 = new LineTo(21, 30);
		LineTo line2 = new LineTo(121, 30);
		LineTo line3 = new LineTo(141, 25);
		LineTo line4 = new LineTo(121, 60);
		LineTo line5 = new LineTo(21, 60);
		LineTo line6 = new LineTo(1, 25);

		path.getElements().add(start);
		path.getElements().addAll(line1, line2, line3, line4, line5, line6);
		path.setFill(Color.DARKGOLDENROD);
		pane.getChildren().add(path);
	}
	
	private static void drawText(Pane pane, int colonists) {
		Text text = new Text(String.valueOf(colonists));
		text.setX(65);
		text.setY(50);
		pane.getChildren().add(text);
	}
	
	private static void drawColonists(Pane pane, int colonists) {
		double distance = 100 / (colonists-1);
		for (int i = 0; i < colonists; i++) {
			Circle border = new Circle(21+i*distance, 19, 11, Color.BLACK);
			Circle circle = new Circle(21+i*distance, 19, 10, PrColors.COLONIST.getColor());
			pane.getChildren().addAll(border, circle);
		}
	}
}
