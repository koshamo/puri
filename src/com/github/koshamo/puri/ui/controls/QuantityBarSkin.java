package com.github.koshamo.puri.ui.controls;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class QuantityBarSkin extends SkinBase<QuantityBar> {

	private final int max;
	private final Color fillColor;
	private final Color extColor = Color.BLACK;
	private Color intColor = Color.BLACK;

	private Pane pane;
	private StackPane stack;
	private Text txtQuantity;
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
		txtQuantity = new Text("0");
		height = txtQuantity.getFont().getSize();
		
		borderRect = new Rectangle(5, height + 6);
		fillRect = new Rectangle(3, height + 4, fillColor);
		
		pane = new Pane();
		stack = new StackPane();
		stack.getChildren().addAll(borderRect, fillRect);
		pane.getChildren().addAll(stack, txtQuantity);

		this.getChildren().add(pane);
	}

	public void drawComponent(int quantity) {
		txtQuantity.setText(String.valueOf(quantity));
		double width = 100 * quantity / max;
		if (quantity == 0) {
			borderRect.setWidth(5);
			fillRect.setWidth(3);
		} else {
			borderRect.setWidth(width + 2);
			fillRect.setWidth(width);
		}
		if (width > 15) {
			txtQuantity.setFill(intColor);
			txtQuantity.relocate(width-10, height/4);
		} else {
			txtQuantity.setFill(extColor);
			txtQuantity.relocate(width+8, height/4);
		}
	}

}
