package com.github.koshamo.puri.ui.controls;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class QuantityBarSkin extends SkinBase<QuantityBar> {

	private final int max;
	private final Color fillColor;
	private final Color extColor = Color.BLACK;
	private Color intColor = Color.BLACK;

	private HBox hbox;
	private StackPane stack;
	private Label lblQuantity;
	private Rectangle borderRect;
	private Rectangle fillRect;
	private double height;

	protected QuantityBarSkin(QuantityBar control, int max, Color fillColor) {
		super(control);
		this.max = max;
		this.fillColor = fillColor;
		
		initComponents();
		
		drawComponent(0);
	}
	
	public void setInsettingTextColor(Color color) {
		intColor = color;
	}

	private void initComponents() {
		lblQuantity = new Label("0");
		lblQuantity.setAlignment(Pos.CENTER_RIGHT);
		height = lblQuantity.getFont().getSize();
		
		borderRect = new Rectangle(5, height + 6);
		fillRect = new Rectangle(3, height + 4, fillColor);
		
		hbox = new HBox(8);
		stack = new StackPane();
		stack.getChildren().addAll(borderRect, fillRect);
		hbox.getChildren().addAll(stack, lblQuantity);

		this.getChildren().add(hbox);
	}

	public void drawComponent(int quantity) {
		lblQuantity.setText(String.valueOf(quantity));
		double width = 100 * quantity / max;
		if (quantity == 0) {
			borderRect.setWidth(5);
			fillRect.setWidth(3);
		} else {
			borderRect.setWidth(width + 2);
			fillRect.setWidth(width);
		}
		((Pane)lblQuantity.getParent()).getChildren().remove(lblQuantity);
		if (width > 15) {
			stack.getChildren().add(lblQuantity);
			lblQuantity.setTextFill(intColor);
		} else {
			hbox.getChildren().add(lblQuantity);
			lblQuantity.setTextFill(extColor);
		}
	}

}
