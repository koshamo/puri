package com.github.koshamo.puri.ui.controls.board;

import com.github.koshamo.puri.setup.BuildingTypeList;
import com.github.koshamo.puri.setup.BuildingsModel;
import com.github.koshamo.puri.setup.StartupConstants;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class BuildingsDialog extends Dialog<BuildingTypeList> {

	private final StartupConstants gameConstants;
	private Callback<TableColumn<BuildingsModel,String>,TableCell<BuildingsModel,String>> cellFactory;
	
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

	/*
	 * Suppress warning for generic definition safety warning
	 * TableColumn<BuildingsModel,?> 
	 * when adding Columns of different generic types to TableView
	 */
	@SuppressWarnings("unchecked")
	private void drawContentPane() {
		TableView<BuildingsModel> table = new TableView<>(); 
		initTableCellCallback();

		table.getColumns().addAll(
				initiateNameCol(), 
				initiateDescCol(), 
				initiateCostCol(), 
				initiateVPCol(), 
				initiateLeftCol());

		table.setItems(gameConstants.availableBuildings);
		// TODO: calc size
		table.setPrefWidth(700);
		table.setPrefHeight(730);

		this.getDialogPane().setContent(table);
		this.setResizable(true);
	}

	private void initTableCellCallback() {
		cellFactory = new Callback<TableColumn<BuildingsModel,String>,TableCell<BuildingsModel,String>>() {
			@Override
			public TableCell<BuildingsModel, String> call(TableColumn<BuildingsModel, String> param) {
				TableCell<BuildingsModel, String> cell = new TableCell<BuildingsModel, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (!empty) {
							TableRow<BuildingsModel> row = getTableRow();
							
							if (row != null) {
								row.setTooltip(new Tooltip(row.getItem().getDescription()));
								setText(item);
								
								if (row.getItem().getLeft().equals("0"))
									setTextFill(Color.LIGHTGREY);
							}
						}
					}
				};
				return cell;
			}
		};
	}

	private TableColumn<BuildingsModel, String> initiateNameCol() {
		TableColumn<BuildingsModel, String> colName = new TableColumn<>();
		colName.setText("Gebäude");
		colName.setCellFactory(cellFactory);
		colName.setPrefWidth(120);
		colName.setCellValueFactory(new PropertyValueFactory<BuildingsModel, String>("name"));
		return colName;
	}
	
	private TableColumn<BuildingsModel, String> initiateDescCol() {
		TableColumn<BuildingsModel, String> colDesc = new TableColumn<>();
		colDesc.setText("Wirkung");
		colDesc.setCellFactory(cellFactory);
		colDesc.setPrefWidth(310);
		colDesc.setCellValueFactory(new PropertyValueFactory<BuildingsModel, String>("shortDescription"));
		return colDesc;
	}
	
	private TableColumn<BuildingsModel, String> initiateCostCol() {
		TableColumn<BuildingsModel, String> colCost = new TableColumn<>();
		colCost.setText("Kosten");
		colCost.setCellFactory(cellFactory);
		colCost.setPrefWidth(80);
		colCost.setCellValueFactory(new PropertyValueFactory<BuildingsModel, String>("cost"));
		return colCost;
	}
	
	private TableColumn<BuildingsModel, String> initiateVPCol() {
		TableColumn<BuildingsModel, String> colVP = new TableColumn<>();
		colVP.setText("Siegpunkte");
		colVP.setCellFactory(cellFactory);
		colVP.setPrefWidth(80);
		colVP.setCellValueFactory(new PropertyValueFactory<BuildingsModel, String>("victoryPoints"));
		return colVP;
	}
	
	private TableColumn<BuildingsModel, String> initiateLeftCol() {
		TableColumn<BuildingsModel, String> colLeft = new TableColumn<>();
		colLeft.setText("verfügbar");
		colLeft.setCellFactory(cellFactory);
		colLeft.setPrefWidth(80);
		colLeft.setCellValueFactory(new PropertyValueFactory<BuildingsModel, String>("left"));
		return colLeft;
	}
	
	private void initResultConverter() {
		// TODO Auto-generated method stub
		
	}
}
