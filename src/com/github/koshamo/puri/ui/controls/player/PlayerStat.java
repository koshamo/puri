package com.github.koshamo.puri.ui.controls.player;

import java.util.Optional;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.setup.RoleType;
import com.github.koshamo.puri.ui.controls.ProductDialog;
import com.github.koshamo.puri.ui.controls.ProductDialog.State;
import com.github.koshamo.puri.ui.controls.QuantityBar;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/*private*/ class PlayerStat extends Region {

	private enum ProductDnD {CAPTAIN, TRADER}
	
	private final Player player;
	private final String name;
	private final Color color;
	
	private IntegerProperty victoryPoints = new SimpleIntegerProperty(0);
	private IntegerProperty gulden = new SimpleIntegerProperty(0);

	private Label lblGouvernor;
	private Label lblRole;
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
	private final String roleDefaultText = "Rolle wählen";
	private Button btnDone;
	
	public PlayerStat(Player player, String name, PrColors color) {
		this.player = player;
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

	public void addProduct(PlantationType type, int amount) {
		QuantityBar bar = selectProductComponent(type);
		bar.add(amount);
	}
	
	public void subProduct(PlantationType type, int amount) {
		QuantityBar bar = selectProductComponent(type);
		bar.sub(amount);
	}
	
	public int availableProducts(PlantationType type) {
		QuantityBar bar = selectProductComponent(type);
		return bar.quantity();
	}
	
	public boolean hasProduct(PlantationType type) {
		return availableProducts(type) > 0;
	}
	
	public void addColonists(int num) {
		qbColonists.add(num);
	}

	public void subColonists(int num) {
		qbColonists.sub(num);
	}
	
	public int colonists() {
		return qbColonists.quantity();
	}

	public void activateGouvernor() {
		lblGouvernor.setVisible(true);
	}
	
	public void deactivateGouvernor() {
		lblGouvernor.setVisible(false);
	}
	
	public void activateRole() {
		lblRole.setVisible(true);
	}
	
	public void chooseRole(RoleType type) {
		lblRole.setText(type.toString());
	}
	
	public void deactivateRole() {
		lblRole.setText(roleDefaultText);
		lblRole.setVisible(false);
	}
	
	public void activatePlayer() {
		lblActive.setVisible(true);
	}
	
	public void deactivatePlayer() {
		lblActive.setVisible(false);
	}

	public void activateColonistsDnD() {
		if (qbColonists.quantity() == 0) {
			btnDone.setVisible(true);
			btnDone.setOnAction(ev -> {player.distributionDone();});
		}

		updateColonistDragging();
	}

	public void deactivateColonistsDnD() {
		btnDone.setVisible(false);

		cancelColonistDragging();
	}

	public void activateCaptainDnD() {
		updateProductDragging(ProductDnD.CAPTAIN);
	}
	
	public void deactivateCaptainDnD() {
		cancelProductDragging();
	}
	
	public void checkProductStorage() {
		int cnt = countPossessedProducts();
		
		if (cnt == 0)
			return;
		if (cnt == 1) {
			reduceProductsToCapacity();
			return;
		}
		chooseProductsToKeep();
	}
	
	public void activateTraderDnD() {
		btnDone.setVisible(true);
		btnDone.setOnAction(ev -> {player.tradingDone();});

		updateProductDragging(ProductDnD.TRADER);
	}
	
	public void deactivateTraderDnD() {
		btnDone.setVisible(false);
		
		cancelProductDragging();
	}

	private int countPossessedProducts() {
		int cnt = 0;
		if (qbIndigo.quantity() > 0)
			cnt++;
		if (qbSugar.quantity() > 0)
			cnt++;
		if (qbCorn.quantity() > 0)
			cnt++;
		if (qbTobacco.quantity() > 0)
			cnt++;
		if (qbCoffee.quantity() > 0)
			cnt++;
		
		return cnt;
	}
	
	private void reduceProductsToCapacity() {
		if (qbIndigo.quantity() > 1) {
			player.dropProduct(PlantationType.INDIGO, qbIndigo.quantity() - 1);
			qbIndigo.changeQuantity(1);
		}
		if (qbSugar.quantity() > 1) {
			player.dropProduct(PlantationType.SUGAR, qbSugar.quantity() - 1);
			qbSugar.changeQuantity(1);
		}
		if (qbCorn.quantity() > 1) {
			player.dropProduct(PlantationType.CORN, qbCorn.quantity() - 1);
			qbCorn.changeQuantity(1);
		}
		if (qbTobacco.quantity() > 1) {
			player.dropProduct(PlantationType.TOBACCO, qbTobacco.quantity() - 1);
			qbTobacco.changeQuantity(1);
		}
		if (qbCoffee.quantity() > 1) {
			player.dropProduct(PlantationType.COFFEE, qbCoffee.quantity() - 1);
			qbCoffee.changeQuantity(1);
		}
	}

	private void chooseProductsToKeep() {
		int[] products = possessedProducts();

		ProductDialog dialog = new ProductDialog(products, State.STORAGE);
		Optional<PlantationType> toKeep = dialog.showAndWait();
		
		if (toKeep.isPresent()) {
			clearStorageExcept(toKeep.get());
		}
	}

	private void clearStorageExcept(PlantationType type) {
		removeProduct(PlantationType.INDIGO, type);
		removeProduct(PlantationType.SUGAR, type);
		removeProduct(PlantationType.CORN, type);
		removeProduct(PlantationType.TOBACCO, type);
		removeProduct(PlantationType.COFFEE, type);
	}

	private void removeProduct(PlantationType toReduce, PlantationType toKeep) {
		QuantityBar bar = selectProductComponent(toReduce);
		
		if (toReduce == toKeep) {
			player.dropProduct(toReduce, bar.quantity() - 1);
			bar.changeQuantity(1);
		}
		else {
			player.dropProduct(toReduce, bar.quantity());
			bar.changeQuantity(0);
		}
	}

	private int[] possessedProducts() {
		int[] products = new int[5];
		
		products[0] = qbIndigo.quantity();
		products[1] = qbSugar.quantity();
		products[2] = qbCorn.quantity();
		products[3] = qbTobacco.quantity();
		products[4] = qbCoffee.quantity();
		
		return products;
	}
	
	private QuantityBar selectProductComponent(PlantationType type ) {
		QuantityBar bar;
		
		switch (type) {
		case INDIGO: bar = qbIndigo; break;
		case SUGAR: bar = qbSugar; break;
		case CORN: bar = qbCorn; break;
		case TOBACCO: bar = qbTobacco; break;
		case COFFEE: bar = qbCoffee; break;
		default: bar = null;
		}

		return bar;
	}
	
	private void updateColonistDragging() {
		if (qbColonists.quantity() > 0) {
			qbColonists.setOnDragDetected(ev -> {
				Dragboard db = qbColonists.startDragAndDrop(TransferMode.MOVE);
				// TODO: db.setDragView(IMAGE);
				ClipboardContent cc = new ClipboardContent();
				cc.putString("1");
				db.setContent(cc);
				ev.consume();
			});
			qbColonists.setOnDragDone(ev -> {
				if (ev.getTransferMode() == TransferMode.MOVE
						&& ev.isAccepted()) {
					qbColonists.sub(1);
					player.distributeColonists();
				}
				ev.consume();
			});
		}
		else
			cancelColonistDragging();
	}
	
	private void cancelColonistDragging() {
		qbColonists.setOnDragDetected(null);
	}
	
	private void updateProductDragging(ProductDnD dnd) {
		updateProductDraggingFor(PlantationType.INDIGO, dnd);
		updateProductDraggingFor(PlantationType.SUGAR, dnd);
		updateProductDraggingFor(PlantationType.CORN, dnd);
		updateProductDraggingFor(PlantationType.TOBACCO, dnd);
		updateProductDraggingFor(PlantationType.COFFEE, dnd);
	}
	
	private void updateProductDraggingFor(PlantationType type, ProductDnD dnd) {
		QuantityBar bar = selectProductComponent(type);
		if (bar.quantity() > 0) {
			bar.setOnDragDetected(ev -> {
				Dragboard db = bar.startDragAndDrop(TransferMode.MOVE);
				// TODO: db.setDragView(IMAGE);
				ClipboardContent cc = new ClipboardContent();
				cc.putString(type.toString() + " " + bar.quantity());
				db.setContent(cc);
				ev.consume();
			});
			bar.setOnDragDone(ev -> {
				if (ev.getTransferMode() == TransferMode.MOVE
						&& ev.isAccepted()) {
					String[] shipped = ev.getDragboard().getString().split(" ");
					int delivered = Integer.valueOf(shipped[1]).intValue(); 
					if (dnd == ProductDnD.CAPTAIN) {
						bar.sub(delivered);
						victoryPoints.set(victoryPoints.get() + delivered);
						player.shippingDone();
					} else {
						addGulden(delivered);
						bar.sub(1);
						player.tradingDone();
					}
				}
				ev.consume();
			});
		}
		else
			bar.setOnDragDetected(null);
	}

	private void cancelProductDragging() {
		qbIndigo.setOnDragDetected(null);
		qbSugar.setOnDragDetected(null);
		qbCorn.setOnDragDetected(null);
		qbTobacco.setOnDragDetected(null);
		qbCoffee.setOnDragDetected(null);
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
		buildRoleLabel(grid);
		buildActiveLabel(grid);
		buildActionButton(grid);
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

		grid.add(lblGouvernor, 0, 2, 1, 2);
		lblGouvernor.setVisible(false);
	}

	private void buildRoleLabel(GridPane grid) {
		lblRole = new Label(roleDefaultText );
		lblRole.setTextFill(PrColors.ACTIVE_TXT.getColor());
		lblRole.setBackground(new Background(new BackgroundFill(PrColors.ACTIVE_BGD.getColor(), null, null)));
		
		grid.add(lblRole, 0, 4);
		lblRole.setVisible(false);
	}

	private void buildActiveLabel(GridPane grid) {
		lblActive = new Label("Aktion ausführen");
		lblActive.setTextFill(PrColors.ACTIVE_TXT.getColor());
		lblActive.setBackground(new Background(new BackgroundFill(PrColors.ACTIVE_BGD.getColor(), null, null)));
		
		grid.add(lblActive, 0, 5);
		lblActive.setVisible(false);
	}
	
	private void buildActionButton(GridPane grid) {
		btnDone = new Button("fertig");

		grid.add(btnDone, 0, 7);
		btnDone.setVisible(false);		
	}

	private static void buildSecondColumn(GridPane grid) {
		grid.add(new Label("Siegpunkte:"), 1, 0);
		grid.add(new Label("Gulden:"), 1, 1);
		grid.add(new Label("Indigo:"), 1, 2);
		grid.add(new Label("Zucker:"), 1, 3);
		grid.add(new Label("Mais:"), 1, 4);
		grid.add(new Label("Tabak:"), 1, 5);
		grid.add(new Label("Kaffee:"), 1, 6);
		grid.add(new Label("Kolonisten:"), 1, 7);
	}
	
	private void buildThirdColumn(GridPane grid) {
		Font counterFont = Font.font(defaultFont.getFamily(), FontWeight.BOLD, defaultFont.getSize());
		
		buildVictoryPointsDisplay(grid, counterFont);
		buildGuldenDisplay(grid, counterFont);
		
		buildIndigoDisplay(grid);
		buildSugarDisplay(grid);
		buildCornDisplay(grid);
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

	private void buildIndigoDisplay(GridPane grid) {
		qbIndigo = new QuantityBar(12, PrColors.INDIGO.getColor());
		qbIndigo.setTextColor(PrColors.INDIGO_TXT.getColor());
		grid.add(qbIndigo, 2, 2);
	}

	private void buildSugarDisplay(GridPane grid) {
		qbSugar = new QuantityBar(12, PrColors.SUGAR.getColor());
		qbSugar.setTextColor(PrColors.SUGAR_TXT.getColor());
		grid.add(qbSugar, 2, 3);
	}

	private void buildCornDisplay(GridPane grid) {
		qbCorn = new QuantityBar(12, PrColors.CORN.getColor());
		qbCorn.setTextColor(PrColors.CORN_TXT.getColor());
		grid.add(qbCorn, 2, 4);
	}

	private void buildTobaccoDisplay(GridPane grid) {
		qbTobacco = new QuantityBar(12, PrColors.TOBACCO.getColor());
		qbTobacco.setTextColor(PrColors.TOBACCO_TXT.getColor());
		grid.add(qbTobacco, 2, 5);
	}

	private void buildCoffeeDisplay(GridPane grid) {
		qbCoffee = new QuantityBar(12, PrColors.COFFEE.getColor());
		qbCoffee.setTextColor(PrColors.COFFEE_TXT.getColor());
		grid.add(qbCoffee, 2, 6);
	}

	private void buildColonistsDisplay(GridPane grid) {
		qbColonists = new QuantityBar(12, PrColors.COLONIST.getColor());
		qbColonists.setTextColor(PrColors.COLONIST_TXT.getColor());
		grid.add(qbColonists, 2, 7);
	}

}
