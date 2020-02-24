package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColorUtils;
import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.Node;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/*private*/ class BoardPlantationSkin extends SkinBase<BoardPlantation> {

	private final BoardPlantation control;
	private final int numFields;
	
	private boolean active = false;
	private boolean privilege = false;
	
	public BoardPlantationSkin(BoardPlantation control, int numPlayers) {
		super(control);
		this.control = control;
		this.numFields = numPlayers + 3;
		PlantationType[] emptyPlantations = initPlantations(numPlayers);
		drawComponent(50, 8, emptyPlantations, active, privilege);
	}
	
	private static PlantationType[] initPlantations(int numPlayers) {
		PlantationType[] plantations = new PlantationType[numPlayers+1];
		for (int i = 0; i < numPlayers+1; i++)
			plantations[i] = PlantationType.NONE;
		return plantations;
	}

	public void drawComponent(int cards, int quarries, PlantationType[] plantations, boolean active, boolean privilege) {
		this.active = active;
		this.privilege = privilege;
		
		this.getChildren().clear();
		Pane pane = new Pane();
		
		drawFrame(pane);
		drawPlantations(pane, cards, quarries, plantations);
		
		this.getChildren().add(pane);
	}

	private void drawFrame(Pane pane) {
		int basicSize = numFields * 46;
		Rectangle border = new Rectangle(basicSize+6, 52, Color.BLACK);
		Rectangle rect = new Rectangle(basicSize+4, 50, Color.GRAY);
		rect.relocate(1, 1);
		pane.getChildren().addAll(border, rect);
	}

	private void drawPlantations(Pane pane, int cards, int quarries, PlantationType[] plantations) {
		for (int i = 0; i < numFields; i++) {
			Rectangle border = new Rectangle(42, 42, Color.BLACK);
			Node rect;
			if (i == 0)
				rect = drawCardStack(cards);
			else if (i == 1)
				rect = drawQuarry(quarries);
			else
				rect = drawPlantation(plantations[i-2]);
			border.relocate(5+i*46, 5);
			rect.relocate(6+i*46, 6);
			pane.getChildren().addAll(border, rect);
		}
	}

	private static Node drawCardStack(int cards) {
		StackPane stack = new StackPane();
		Rectangle rect = new Rectangle(40, 40, Color.AQUAMARINE);
		Text text = new Text(String.valueOf(cards));
		stack.getChildren().addAll(rect, text);
		
		Tooltip tooltip = new Tooltip("verfügbare Nachziehkarten");
		Tooltip.install(stack, tooltip);
		return stack;
	}

	private Node drawQuarry(int quarries) {
		StackPane stack = new StackPane();
		Rectangle rect = new Rectangle(40, 40, PrColors.QUARRY.getColor());
		Text text = new Text(String.valueOf(quarries));
		stack.getChildren().addAll(rect, text);
		
		if (active && privilege) {
			stack.setOnMouseClicked(ev -> {
				control.selectPlantation(PlantationType.QUARRY);
			});
		} else
			stack.setOnMouseClicked(null);
		
		Tooltip tooltip = new Tooltip("verfügbare Steinbrüche");
		Tooltip.install(stack, tooltip);
		return stack;
	}

	private Node drawPlantation(PlantationType plantation) {
		if (plantation.equals(PlantationType.NONE)) {
			return drawEmptyField();
		} 
		
		Rectangle rect = new Rectangle(40, 40, PrColorUtils.selectFieldColor(plantation));
		if (active) {
			rect.setOnMouseClicked(ev -> {control.selectPlantation(plantation);});
		} else
			rect.setOnMouseClicked(null);
		

		return rect;
	}

	private static Node drawEmptyField() {
		StackPane stack = new StackPane();
		
		Rectangle rect = new Rectangle(40, 40, PrColors.DEFAULT_BGD.getColor());
		
		Line line1 = new Line(3,3,37,37);
		line1.setStroke(Color.RED);
		line1.setStrokeWidth(3);
		Line line2 = new Line(3,37,37,3);
		line2.setStroke(Color.RED);
		line2.setStrokeWidth(3);
		
		stack.getChildren().addAll(rect, line1, line2);
		return stack;
	}

}
