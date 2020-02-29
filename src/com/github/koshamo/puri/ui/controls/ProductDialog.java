package com.github.koshamo.puri.ui.controls;

import java.util.LinkedList;
import java.util.List;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;

import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class ProductDialog extends Dialog<List<PlantationType>> {

	public enum State {PRODUCTION, STORAGE}
	
	private final String name;
	private final int[] products;
	private ToggleGroup group;
	private final State state;
	private final int storage;
	private final List<CheckBox> boxes;
	private final boolean checker;

	public ProductDialog(String name, int[] products, State state, int storage) {
		this.name = name;
		this.products = products;
		this.state = state;
		this.storage = storage;
		checker = state == State.STORAGE && storage > 0;
		boxes = checker ? new LinkedList<>() : null;
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
			this.getDialogPane().setHeaderText(name + ": Wähle Ware als Privileg");
		else
			this.getDialogPane().setHeaderText(name + ": Wähle Ware zur Lagerung");
	}

	private void drawButtonPane() {
		this.getDialogPane().getButtonTypes().add(ButtonType.OK);
	}

	private void drawContentPane() {
		GridPane grid = new GridPane();
		grid.setHgap(5);
		grid.setVgap(5);
		
	    group = new ToggleGroup();
	    
	    addContent(grid);
	    this.getDialogPane().setContent(grid);
	}

	private void addContent(GridPane grid) {
		int index = 0;
		
		grid.add(new Label("1"), 0, index);
		if (checker)
			grid.add(new Label("all"), 1, index);
		grid.add(new Label("Product"), 2, index, 2, 1);
		index++;
		
		if (products[0] > 0) {
			RadioButton btnIndigo = new RadioButton();
			btnIndigo.setToggleGroup(group);
			btnIndigo.setSelected(true);
			btnIndigo.setUserData("INDIGO");
			grid.add(btnIndigo, 0, index);
			
			if (checker) {
				CheckBox cbxIndigo = new CheckBox();
				cbxIndigo.setUserData("INDIGO");
				grid.add(cbxIndigo, 1, index);
				boxes.add(cbxIndigo);
			}
			Rectangle rect = new Rectangle(15 * products[0], 15, PrColors.INDIGO.getColor());
			grid.add(rect, 2, index);
			
			grid.add(new Label("Indigo"), 3, index);
			index++;
		}

		if (products[1] > 0) {
			RadioButton btnSugar = new RadioButton("");
			btnSugar.setToggleGroup(group);
			btnSugar.setSelected(true);
			btnSugar.setUserData("SUGAR");
			grid.add(btnSugar, 0, index);

			if (checker) {
				CheckBox cbxSugar = new CheckBox();
				cbxSugar.setUserData("SUGAR");
				grid.add(cbxSugar, 1, index);
				boxes.add(cbxSugar);
			}
			Rectangle rect = new Rectangle(15 * products[1], 15, PrColors.SUGAR.getColor());
			grid.add(rect, 2, index);
			
			grid.add(new Label("Zucker"), 3, index);
			index++;
		}

		if (products[2] > 0) {
			RadioButton btnCorn = new RadioButton("");
			btnCorn.setToggleGroup(group);
			btnCorn.setSelected(true);
			btnCorn.setUserData("CORN");
			grid.add(btnCorn, 0, index);

			if (checker) {
				CheckBox cbxCorn = new CheckBox();
				cbxCorn.setUserData("CORN");
				grid.add(cbxCorn, 1, index);
				boxes.add(cbxCorn);
			}
			Rectangle rect = new Rectangle(15 * products[2], 15, PrColors.CORN.getColor());
			grid.add(rect, 2, index);
			
			grid.add(new Label("Mais"), 3, index);
			index++;
		}

		if (products[3] > 0) {
			RadioButton btnTobacco = new RadioButton("");
			btnTobacco.setToggleGroup(group);
			btnTobacco.setSelected(true);
			btnTobacco.setUserData("TOBACCO");
			grid.add(btnTobacco, 0, index);

			if (checker) {
				CheckBox cbxTobacco = new CheckBox();
				cbxTobacco.setUserData("TOBACCO");
				grid.add(cbxTobacco, 1, index);
				boxes.add(cbxTobacco);
			}
			Rectangle rect = new Rectangle(15 * products[3], 15, PrColors.TOBACCO.getColor());
			grid.add(rect, 2, index);
			
			grid.add(new Label("Tabak"), 3, index);
			index++;
		}

		if (products[4] > 0) {
			RadioButton btnCoffee = new RadioButton("");
			btnCoffee.setToggleGroup(group);
			btnCoffee.setSelected(true);
			btnCoffee.setUserData("COFFEE");
			grid.add(btnCoffee, 0, index);

			if (checker) {
				CheckBox cbxCoffee = new CheckBox();
				cbxCoffee.setUserData("COFFEE");
				grid.add(cbxCoffee, 1, index);
				boxes.add(cbxCoffee);
			}
			Rectangle rect = new Rectangle(15 * products[4], 15, PrColors.COFFEE.getColor());
			grid.add(rect, 2, index);
			
			grid.add(new Label("Kaffee"), 3, index);
			index++;
		}
		
	}

	private void initResultConverter() {
		this.setResultConverter(cb -> {
			List<PlantationType> result = new LinkedList<>();
			
			RadioButton btn = (RadioButton) group.getSelectedToggle();
			PlantationType type = PlantationType.getByString((String) btn.getUserData());
			result.add(type);
			
			if (checker) {
				for (CheckBox box : boxes) {
					if (box.isSelected()) {
						result.add(PlantationType.getByString((String) box.getUserData()));
					}
				}
				while (result.size() > storage + 1)
					result.remove(result.size() - 1);
			}
			
			return result;
			
		});
	}
	
}
