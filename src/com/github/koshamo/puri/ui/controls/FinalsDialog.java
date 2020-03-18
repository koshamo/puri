package com.github.koshamo.puri.ui.controls;

import java.util.List;

import com.github.koshamo.puri.gamedata.PlayerVictoryPoints;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class FinalsDialog extends Dialog<ButtonType> {

	private final ObservableList<PlayerVictoryPoints> playerFinals;
	private final int turnCount;
	
	public FinalsDialog(List<PlayerVictoryPoints> playerStats, int turnCount) {
		 playerFinals = FXCollections.observableArrayList();
		 for (PlayerVictoryPoints pvp : playerStats)
			 playerFinals.add(pvp);
		 sortPlayers();
		 this.turnCount = turnCount;
		 
		 drawDialog();
	}

	private void sortPlayers() {
		playerFinals.sort((p1, p2) -> {
			 int cmp = Integer.valueOf(p2.getTotalVP()).
					 compareTo(Integer.valueOf(p1.getTotalVP()));
			 if (cmp == 0)
				 return Integer.valueOf(p2.getProducts()).compareTo(Integer.valueOf(p1.getProducts()));
			 return cmp;
		 });
	}

	private void drawDialog() {
		this.setTitle("Spiel Statistik");
		drawButtonPane();
		drawContentPane();
	}

	private void drawButtonPane() {
		this.getDialogPane().getButtonTypes().add(ButtonType.OK);
	}

	private void drawContentPane() {
		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(drawTurnCount(), drawTable());

		this.getDialogPane().setContent(vbox);
		this.setResizable(true);
	}

	private Node drawTurnCount() {
		HBox hbox = new HBox(15);
		
		Label lblTitle = new Label("Anzahl Spielzüge: ");
		Label lblCount = new Label(String.valueOf(turnCount));
		
		Font defFont = lblTitle.getFont();
		Font font = new Font(defFont.getName(), 2 * defFont.getSize());
		
		lblTitle.setFont(font);
		lblCount.setFont(font);
		
		hbox.getChildren().addAll(lblTitle, lblCount);
		return hbox;
	}

	// TODO: check why annotation is neccessary
	@SuppressWarnings("unchecked")
	private Node drawTable() {
		TableView<PlayerVictoryPoints> table = new TableView<>();
		table.getColumns().addAll(
				nameColumn(), 
				totalVpColumn(), 
				playerColumn(),
				shippedVpColumn(),
				buildingVpColumn(),
				specialVpColumn());
		
		table.setItems(playerFinals);
		// TODO: calc size
		table.setPrefWidth(730);
		table.setPrefHeight(200);
		
		return table;
	}

	private static TableColumn<PlayerVictoryPoints, String> nameColumn() {
		TableColumn<PlayerVictoryPoints, String> colName = new TableColumn<>();
		colName.setText("Spielername");
		colName.setPrefWidth(120);
		colName.setCellValueFactory(new PropertyValueFactory<PlayerVictoryPoints, String>("name"));
		return colName;
	}

	private static TableColumn<PlayerVictoryPoints, String> totalVpColumn() {
		TableColumn<PlayerVictoryPoints, String> colTotalVP = new TableColumn<>();
		colTotalVP.setText("Siegpunkte");
		colTotalVP.setPrefWidth(120);
		colTotalVP.setCellValueFactory(new PropertyValueFactory<PlayerVictoryPoints, String>("totalVP"));
		return colTotalVP;
	}

	private static TableColumn<PlayerVictoryPoints, String> playerColumn() {
		TableColumn<PlayerVictoryPoints, String> colKi = new TableColumn<>();
		colKi.setText("KI");
		colKi.setPrefWidth(120);
		colKi.setCellValueFactory(new PropertyValueFactory<PlayerVictoryPoints, String>("ki"));
		return colKi;
	}

	private static TableColumn<PlayerVictoryPoints, String> shippedVpColumn() {
		TableColumn<PlayerVictoryPoints, String> colShippedVP = new TableColumn<>();
		colShippedVP.setText("VP Verschiffen");
		colShippedVP.setPrefWidth(120);
		colShippedVP.setCellValueFactory(new PropertyValueFactory<PlayerVictoryPoints, String>("shippedVP"));
		return colShippedVP;
	}

	private static TableColumn<PlayerVictoryPoints, String> buildingVpColumn() {
		TableColumn<PlayerVictoryPoints, String> colBuildinVP = new TableColumn<>();
		colBuildinVP.setText("VP Gebäude");
		colBuildinVP.setPrefWidth(120);
		colBuildinVP.setCellValueFactory(new PropertyValueFactory<PlayerVictoryPoints, String>("buildingVP"));
		return colBuildinVP;
	}

	private static TableColumn<PlayerVictoryPoints, String> specialVpColumn() {
		TableColumn<PlayerVictoryPoints, String> colSpecialVP = new TableColumn<>();
		colSpecialVP.setText("VP Extra");
		colSpecialVP.setPrefWidth(120);
		colSpecialVP.setCellValueFactory(new PropertyValueFactory<PlayerVictoryPoints, String>("specialVP"));
		return colSpecialVP;
	}
}
