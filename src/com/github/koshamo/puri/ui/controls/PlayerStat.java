package com.github.koshamo.puri.ui.controls;

import com.github.koshamo.puri.setup.PrColors;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PlayerStat extends Region {

	private final String name;
	private final Color color;
	
	private IntegerProperty victoryPoints = new SimpleIntegerProperty(0);
	private IntegerProperty gulden = new SimpleIntegerProperty(0);

	private IntegerProperty corn = new SimpleIntegerProperty(0);
	private IntegerProperty indigo = new SimpleIntegerProperty(0);
	private IntegerProperty sugar = new SimpleIntegerProperty(0);
	private IntegerProperty tobacco = new SimpleIntegerProperty(0);
	private IntegerProperty coffee = new SimpleIntegerProperty(0);
	private IntegerProperty colonists = new SimpleIntegerProperty(0);

	private Label lblGouvernor;
	private Label lblActive;
	
	Label lblVictoryPoints;
	Label lblGulden;
	Label lblCorn;
	Label lblIndigo;
	Label lblSugar;
	Label lblTobacco;
	Label lblCoffee;
	Label lblColonists;
	
	public PlayerStat(String name, PrColors color) {
		this.name = name;
		this.color = color.getColor();
		
		this.getChildren().add(drawComponent());
	}

	private Node drawComponent() {
		GridPane grid = new GridPane();
		grid.setHgap(15.0);
		grid.setVgap(5.0);
		
		Label lblName = new Label(name);
		Font defaultFont = lblName.getFont();
		Font nameFont = Font.font(defaultFont.getFamily(), FontWeight.BOLD, 1.5*defaultFont.getSize());
		lblName.setFont(nameFont);
		lblName.setTextFill(color);
				
		grid.add(lblName, 0, 0, 1, 2);
		
		lblGouvernor = new Label("Gouverneur");
		lblGouvernor.setTextFill(PrColors.GOUVERNOR_TXT.getColor());
		lblGouvernor.setBackground(new Background(new BackgroundFill(PrColors.GOUVERNOR_BGD.getColor(), null, null)));

		grid.add(lblGouvernor, 0, 2, 1, 3);
		
		lblActive = new Label("aktiv");
		lblActive.setTextFill(PrColors.ACTIVE_TXT.getColor());
		lblActive.setBackground(new Background(new BackgroundFill(PrColors.ACTIVE_BGD.getColor(), null, null)));
		
		grid.add(lblActive, 0, 5);
		
		grid.add(new Label("Siegpunkte:"), 1, 0);
		grid.add(new Label("Gulden:"), 1, 1);
		grid.add(new Label("Mais:"), 1, 2);
		grid.add(new Label("Indigo:"), 1, 3);
		grid.add(new Label("Zucker:"), 1, 4);
		grid.add(new Label("Tabak:"), 1, 5);
		grid.add(new Label("Kaffee:"), 1, 6);
		grid.add(new Label("Kolonisten:"), 1, 7);
		
		Font counterFont = Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize());
		
		lblVictoryPoints = new Label("0");
		lblVictoryPoints.setTextFill(color);
		lblVictoryPoints.setFont(counterFont);
		grid.add(lblVictoryPoints, 2, 0);
		
		lblGulden = new Label("0");
		lblGulden.setTextFill(color);
		lblGulden.setFont(counterFont);
		grid.add(lblGulden, 2, 1);
		
		lblCorn = new Label("0");
		lblCorn.setTextFill(PrColors.CORN_TXT.getColor());
		lblCorn.setBackground(new Background(new BackgroundFill(PrColors.CORN.getColor(), null, null)));
		grid.add(lblCorn, 2, 2);
		
		lblIndigo = new Label("0");
		lblIndigo.setTextFill(PrColors.INDIGO_TXT.getColor());
		lblIndigo.setBackground(new Background(new BackgroundFill(PrColors.INDIGO.getColor(), null, null)));
		grid.add(lblIndigo, 2, 3);
		
		lblSugar = new Label("0");
		lblSugar.setTextFill(PrColors.SUGAR_TXT.getColor());
		lblSugar.setBackground(new Background(new BackgroundFill(PrColors.SUGAR.getColor(), null, null)));
		grid.add(lblSugar, 2, 4);
		
		lblTobacco = new Label("0");
		lblTobacco.setTextFill(PrColors.TOBACCO_TXT.getColor());
		lblTobacco.setBackground(new Background(new BackgroundFill(PrColors.TOBACCO.getColor(), null, null)));
		grid.add(lblTobacco, 2, 5);
		
		lblCoffee = new Label("0");
		lblCoffee.setTextFill(PrColors.COFFEE_TXT.getColor());
		lblCoffee.setBackground(new Background(new BackgroundFill(PrColors.COFFEE.getColor(), null, null)));
		grid.add(lblCoffee, 2, 6);
		
		lblColonists = new Label("0");
		lblColonists.setTextFill(PrColors.COLONIST_TXT.getColor());
		lblColonists.setBackground(new Background(new BackgroundFill(PrColors.COLONIST.getColor(), null, null)));
		grid.add(lblColonists, 2, 7);

		victoryPoints.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblVictoryPoints.setText(newValue.toString());				
			}
		});
		
		gulden.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblGulden.setText(newValue.toString());				
			}
		});

		corn.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblCorn.setText(newValue.toString());				
			}
		});
		
		indigo.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblIndigo.setText(newValue.toString());				
			}
		});
		
		sugar.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblSugar.setText(newValue.toString());				
			}
		});
		
		tobacco.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblTobacco.setText(newValue.toString());				
			}
		});
		
		coffee.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblCoffee.setText(newValue.toString());				
			}
		});
		
		colonists.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblColonists.setText(newValue.toString());				
			}
		});

		return grid;
	}
	
	
}
