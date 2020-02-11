package com.github.koshamo.puri.ui.controls;

import com.github.koshamo.puri.setup.PlantationType;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ExtraProductDialog extends Dialog<PlantationType> {

	private final boolean[] products;
	private ToggleGroup group;

	public ExtraProductDialog(boolean[] products) {
		this.products = products;
		for (boolean b : products)
			System.out.println(b);
		drawDialog();
		initResultConverter();
	}

	private void drawDialog() {
		 this.setTitle("Privileg");
		 drawHeaderPane();
		 drawButtonPane();
		 drawContentPane();
	}

	private void drawHeaderPane() {
		this.getDialogPane().setHeaderText("Wähle Ware als Privileg");
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
		if (products[0]) {
			RadioButton btnIndigo = new RadioButton("Indigo");
			btnIndigo.setToggleGroup(group);
			vbox.getChildren().add(btnIndigo);
		}

		if (products[1]) {
			RadioButton btnSugar = new RadioButton("Zucker");
			btnSugar.setToggleGroup(group);
			vbox.getChildren().add(btnSugar);
		}

		if (products[2]) {
			RadioButton btnCorn = new RadioButton("Mais");
			btnCorn.setToggleGroup(group);
			vbox.getChildren().add(btnCorn);
		}

		if (products[3]) {
			RadioButton btnTobacco = new RadioButton("Tabak");
			btnTobacco.setToggleGroup(group);
			vbox.getChildren().add(btnTobacco);
		}

		if (products[4]) {
			RadioButton btnCoffee = new RadioButton("Kaffee");
			btnCoffee.setToggleGroup(group);
			vbox.getChildren().add(btnCoffee);
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
