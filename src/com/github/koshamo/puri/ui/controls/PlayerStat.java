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

/*private*/ class PlayerStat extends Region {

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
	
	/*private*/ Label lblVictoryPoints;
	/*private*/ Label lblGulden;
	/*private*/ QuantityBar qbCorn;
	/*private*/ QuantityBar qbIndigo;
	/*private*/ QuantityBar qbSugar;
	/*private*/ QuantityBar qbTobacco;
	/*private*/ QuantityBar qbCoffee;
	/*private*/ QuantityBar qbColonists;
	
	private Font defaultFont;
	
	public PlayerStat(String name, PrColors color) {
		this.name = name;
		this.color = color.getColor();
		
		this.getChildren().add(drawComponent());
	}

	public void addVictoryPoints(int num) {
		victoryPoints.set(victoryPoints.get() + num);
	}
	
	public int currentGulden() {
		return gulden.get();
	}
	
	public void addGulden(int num) {
		gulden.set(gulden.get() + num);
	}

	public void subGulden(int num) {
		gulden.set(gulden.get() - num);
	}

	public void addCorn(int num) {
		corn.set(corn.get() + num);
	}

	public void subCorn(int num) {
		corn.set(corn.get() - num);
	}
	
	public void addIndigo(int num) {
		indigo.set(indigo.get() + num);
	}

	public void subIndigo(int num) {
		indigo.set(indigo.get() - num);
	}
	
	public void addSugar(int num) {
		sugar.set(sugar.get() + num);
	}

	public void subSugar(int num) {
		sugar.set(sugar.get() - num);
	}
	
	public void addTobacco(int num) {
		tobacco.set(tobacco.get() + num);
	}

	public void subTobacco(int num) {
		tobacco.set(tobacco.get() - num);
	}
	
	public void addCoffee(int num) {
		coffee.set(coffee.get() + num);
	}

	public void subCoffee(int num) {
		coffee.set(coffee.get() - num);
	}
	
	public void addColonists(int num) {
		colonists.set(colonists.get() + num);
	}

	public void subColonists(int num) {
		colonists.set(colonists.get() - num);
	}

	
	private Node drawComponent() {
		GridPane grid = new GridPane();
		grid.setHgap(15.0);
		grid.setVgap(5.0);
		
		buildFirstColumn(grid);
		buildSecondColumn(grid);
		buildThirdColumn(grid);

		return grid;
	}
	
	private void buildFirstColumn(GridPane grid) {
		buildPlayerLabel(grid);
		
		buildGouvernorLabel(grid);
		
		buildActiveLabel(grid);
	}

	private void buildPlayerLabel(GridPane grid) {
		Label lblName = new Label(name);
		defaultFont = lblName.getFont();
		Font nameFont = Font.font(defaultFont.getFamily(), FontWeight.BOLD, 1.5*defaultFont.getSize());
		lblName.setFont(nameFont);
		lblName.setTextFill(color);
		lblName.setPrefWidth(130);
				
		grid.add(lblName, 0, 0, 1, 2);
	}

	private void buildGouvernorLabel(GridPane grid) {
		lblGouvernor = new Label("Gouverneur");
		lblGouvernor.setTextFill(PrColors.GOUVERNOR_TXT.getColor());
		lblGouvernor.setBackground(new Background(new BackgroundFill(PrColors.GOUVERNOR_BGD.getColor(), null, null)));

		grid.add(lblGouvernor, 0, 2, 1, 3);
	}

	private void buildActiveLabel(GridPane grid) {
		lblActive = new Label("aktiv");
		lblActive.setTextFill(PrColors.ACTIVE_TXT.getColor());
		lblActive.setBackground(new Background(new BackgroundFill(PrColors.ACTIVE_BGD.getColor(), null, null)));
		
		grid.add(lblActive, 0, 5);
	}

	private static void buildSecondColumn(GridPane grid) {
		grid.add(new Label("Siegpunkte:"), 1, 0);
		grid.add(new Label("Gulden:"), 1, 1);
		grid.add(new Label("Mais:"), 1, 2);
		grid.add(new Label("Indigo:"), 1, 3);
		grid.add(new Label("Zucker:"), 1, 4);
		grid.add(new Label("Tabak:"), 1, 5);
		grid.add(new Label("Kaffee:"), 1, 6);
		grid.add(new Label("Kolonisten:"), 1, 7);
	}
	
	private void buildThirdColumn(GridPane grid) {
		Font counterFont = Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize());
		
		buildVictoryPointsDisplay(grid, counterFont);
		buildGuldenDisplay(grid, counterFont);
		
		buildCornDisplay(grid);
		buildIndigoDisplay(grid);
		buildSugarDisplay(grid);
		buildTobaccoDisplay(grid);
		buildCoffeeDisplay(grid);
		buildColonistsDisplay(grid);
	}

	private void buildVictoryPointsDisplay(GridPane grid, Font counterFont) {
		lblVictoryPoints = new Label("0");
		lblVictoryPoints.setTextFill(color);
		lblVictoryPoints.setFont(counterFont);
		lblVictoryPoints.setPrefWidth(105);
		grid.add(lblVictoryPoints, 2, 0);
		
		victoryPoints.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblVictoryPoints.setText(newValue.toString());				
			}
		});
	}

	private void buildGuldenDisplay(GridPane grid, Font counterFont) {
		lblGulden = new Label("0");
		lblGulden.setTextFill(color);
		lblGulden.setFont(counterFont);
		grid.add(lblGulden, 2, 1);
		
		gulden.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				lblGulden.setText(newValue.toString());				
			}
		});
	}

	private void buildCornDisplay(GridPane grid) {
		qbCorn = new QuantityBar(11, PrColors.CORN.getColor());
		qbCorn.setTextColor(PrColors.CORN_TXT.getColor());
		grid.add(qbCorn, 2, 2);

		corn.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				qbCorn.changeQuantity(newValue.intValue());				
			}
		});
	}

	private void buildIndigoDisplay(GridPane grid) {
		qbIndigo = new QuantityBar(11, PrColors.INDIGO.getColor());
		qbIndigo.setTextColor(PrColors.INDIGO_TXT.getColor());
		grid.add(qbIndigo, 2, 3);
		
		indigo.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				qbIndigo.changeQuantity(newValue.intValue());				
			}
		});	
	}

	private void buildSugarDisplay(GridPane grid) {
		qbSugar = new QuantityBar(11, PrColors.SUGAR.getColor());
		qbSugar.setTextColor(PrColors.SUGAR_TXT.getColor());
		grid.add(qbSugar, 2, 4);
		
		sugar.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				qbSugar.changeQuantity(newValue.intValue());				
			}
		});
	}

	private void buildTobaccoDisplay(GridPane grid) {
		qbTobacco = new QuantityBar(11, PrColors.TOBACCO.getColor());
		qbTobacco.setTextColor(PrColors.TOBACCO_TXT.getColor());
		grid.add(qbTobacco, 2, 5);
		
		tobacco.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				qbTobacco.changeQuantity(newValue.intValue());				
			}
		});
	}

	private void buildCoffeeDisplay(GridPane grid) {
		qbCoffee = new QuantityBar(11, PrColors.COFFEE.getColor());
		qbCoffee.setTextColor(PrColors.COFFEE_TXT.getColor());
		grid.add(qbCoffee, 2, 6);
		
		coffee.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				qbCoffee.changeQuantity(newValue.intValue());				
			}
		});
	}

	private void buildColonistsDisplay(GridPane grid) {
		qbColonists = new QuantityBar(11, PrColors.COLONIST.getColor());
		qbColonists.setTextColor(PrColors.COLONIST_TXT.getColor());
		grid.add(qbColonists, 2, 7);
		colonists.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				qbColonists.changeQuantity(newValue.intValue());				
			}
		});
	}

}
