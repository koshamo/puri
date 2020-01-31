package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.BuildingsModel;
import com.github.koshamo.puri.setup.StartupConstants;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class BuildingsDialog extends Dialog<BuildingTypeList> {

	private final StartupConstants gameConstants;
	
	public BuildingsDialog(StartupConstants gameConstants) {
		this.gameConstants = gameConstants;
		drawDialog();
		initResultConverter();
	}

	private void drawDialog() {
		this.setTitle("Gebäudeauswahl");
		drawButtonPane();
		drawContentPane();
	}

	private void drawButtonPane() {
		this.getDialogPane().getButtonTypes().
			addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	private void drawContentPane() {
		TableView<BuildingsModel> table = new TableView<>(); 
		table.getColumns().addAll(
				initiateNameCol(), 
				initiateDescCol(), 
				initiateCostCol(), 
				initiateVPCol(), 
				initiateLeftCol());

		table.setItems(gameConstants.availableBuildings);
		table.setPrefWidth(700);
		table.setPrefHeight(730);

		this.getDialogPane().setContent(table);
		this.setResizable(true);
	}

	private TableColumn<BuildingsModel, String> initiateNameCol() {
		TableColumn<BuildingsModel, String> colName = new TableColumn<>();
		colName.setText("Gebäude");
		colName.setCellValueFactory(new PropertyValueFactory<BuildingsModel, String>("name"));
		return colName;
	}
	
	private TableColumn<BuildingsModel, String> initiateDescCol() {
		TableColumn<BuildingsModel, String> colDesc = new TableColumn<>();
		colDesc.setText("Wirkung");
		colDesc.setCellValueFactory(new PropertyValueFactory<BuildingsModel, String>("shortDescription"));
		return colDesc;
	}
	
	private TableColumn<BuildingsModel, Integer> initiateCostCol() {
		TableColumn<BuildingsModel, Integer> colCost = new TableColumn<>();
		colCost.setText("Kosten");
		colCost.setCellValueFactory(new PropertyValueFactory<BuildingsModel, Integer>("cost"));
		return colCost;
	}
	
	private TableColumn<BuildingsModel, Integer> initiateVPCol() {
		TableColumn<BuildingsModel, Integer> colVP = new TableColumn<>();
		colVP.setText("Siegpunkte");
		colVP.setCellValueFactory(new PropertyValueFactory<BuildingsModel, Integer>("victoryPoints"));
		return colVP;
	}
	
	private TableColumn<BuildingsModel, Integer> initiateLeftCol() {
		TableColumn<BuildingsModel, Integer> colLeft = new TableColumn<>();
		colLeft.setText("verfügbar");
		colLeft.setCellValueFactory(new PropertyValueFactory<BuildingsModel, Integer>("left"));
		return colLeft;
	}
	
	private void initResultConverter() {
		// TODO Auto-generated method stub
		
	}
}
