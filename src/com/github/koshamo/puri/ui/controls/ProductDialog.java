package com.github.koshamo.puri.ui.controls;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class ProductDialog extends Dialog<PlantationType> {

	public enum State {PRODUCTION, STORAGE}
	
	private final int[] products;
	private ToggleGroup group;
	private final State state;

	public ProductDialog(int[] products, State state) {
		this.products = products;
		this.state = state;
		drawDialog();
		initResultConverter();
	}

	private void drawDialog() {
		if (state == State.PRODUCTION)
			this.setTitle("Privileg");
		else 
			this.setTitle("Lagerung");
		drawHeaderPane();
		drawButtonPane();
		drawContentPane();
	}

	private void drawHeaderPane() {
		if (state == State.PRODUCTION)
			this.getDialogPane().setHeaderText("Wähle Ware als Privileg");
		else
			this.getDialogPane().setHeaderText("Wähle Ware zur Lagerung");
	}

	private void drawButtonPane() {
		this.getDialogPane().getButtonTypes().add(ButtonType.OK);
	}

	private void drawContentPane() {
		VBox vbox = new VBox(5);
	    group = new ToggleGroup();
	    
	    addButtons(vbox);
	    this.getDialogPane().setContent(vbox);

	    // always select first button
    	((RadioButton) vbox.getChildren().get(0)).setSelected(true);
	}

	private void addButtons(VBox vbox) {
		if (products[0] > 0) {
			RadioButton btnIndigo = new RadioButton("Indigo");
			btnIndigo.setToggleGroup(group);
			Rectangle rect = new Rectangle(15 * products[0], 15, PrColors.INDIGO.getColor());

			HBox hbox = new HBox(15);
			hbox.getChildren().addAll(btnIndigo, rect);
			vbox.getChildren().add(rect);
		}

		if (products[1] > 0) {
			RadioButton btnSugar = new RadioButton("Zucker");
			btnSugar.setToggleGroup(group);
			Rectangle rect = new Rectangle(15 * products[0], 15, PrColors.SUGAR.getColor());

			HBox hbox = new HBox(15);
			hbox.getChildren().addAll(btnSugar, rect);
			vbox.getChildren().add(rect);
		}

		if (products[2] > 0) {
			RadioButton btnCorn = new RadioButton("Mais");
			btnCorn.setToggleGroup(group);
			Rectangle rect = new Rectangle(15 * products[0], 15, PrColors.CORN.getColor());

			HBox hbox = new HBox(15);
			hbox.getChildren().addAll(btnCorn, rect);
			vbox.getChildren().add(rect);
		}

		if (products[3] > 0) {
			RadioButton btnTobacco = new RadioButton("Tabak");
			btnTobacco.setToggleGroup(group);
			Rectangle rect = new Rectangle(15 * products[0], 15, PrColors.TOBACCO.getColor());

			HBox hbox = new HBox(15);
			hbox.getChildren().addAll(btnTobacco, rect);
			vbox.getChildren().add(rect);
		}

		if (products[4] > 0) {
			RadioButton btnCoffee = new RadioButton("Kaffee");
			btnCoffee.setToggleGroup(group);
			Rectangle rect = new Rectangle(15 * products[0], 15, PrColors.COFFEE.getColor());

			HBox hbox = new HBox(15);
			hbox.getChildren().addAll(btnCoffee, rect);
			vbox.getChildren().add(rect);
		}
		
	}

	private void initResultConverter() {
		this.setResultConverter(cb -> {
			RadioButton btn = (RadioButton) group.getSelectedToggle();
			switch (btn.getText()) {
			case "Indigo": return PlantationType.INDIGO;
			case "Zucker": return PlantationType.SUGAR;
			case "Mais": return PlantationType.CORN;
			case "Tabak": return PlantationType.TOBACCO;
			case "Kaffee": return PlantationType.COFFEE;
			default: return null;
			}
		});
	}
	
}
