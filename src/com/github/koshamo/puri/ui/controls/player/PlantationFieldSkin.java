package com.github.koshamo.puri.ui.controls.player;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColorUtils;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.setup.State;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/*private*/ class PlantationFieldSkin extends SkinBase<PlantationField> {

	protected PlantationFieldSkin(PlantationField control, PlantationType type, State state) {
		super(control);
		drawComponent(type, state);
	}

	public void drawComponent(PlantationType type, State state) {
		this.getChildren().clear();
		
		Color bgColor = PrColorUtils.selectFieldColor(type);
		Color colColor = selectColonistColor(state);
		Pane pane = new Pane();
		
		Rectangle border = new Rectangle(57, 57, Color.BLACK);
		Rectangle rect = new Rectangle(55, 55, bgColor);
		rect.relocate(1, 1);
		pane.getChildren().addAll(border, rect);
		
		if (type != PlantationType.NONE) {
			Circle cir = new Circle(10, colColor);
			pane.getChildren().add(cir);
			cir.relocate(5, 30);
		}
		this.getChildren().add(pane);
	}
	
	private static Color selectColonistColor(State state) {
		if (state == State.ACTIVE)
			return PrColors.ACTIVE_BGD.getColor();
		return PrColors.INACTIVE.getColor();
	}

}
