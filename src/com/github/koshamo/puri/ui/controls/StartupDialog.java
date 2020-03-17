package com.github.koshamo.puri.ui.controls;

import java.util.LinkedList;
import java.util.List;

import com.github.koshamo.puri.ai.AiFactory;
import com.github.koshamo.puri.gamedata.PlayerSetup;
import com.github.koshamo.puri.setup.GameSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class StartupDialog extends Dialog<List<PlayerSetup>> {

	private final GridPane grid = new GridPane();
	
	private final CheckBox player4 = new CheckBox();
	private final CheckBox player5 = new CheckBox();
	private final CheckBox ai2 = new CheckBox();
	private final CheckBox ai3 = new CheckBox();
	private final CheckBox ai4 = new CheckBox();
	private final CheckBox ai5 = new CheckBox();
	
	private final TextField name1 = new TextField("dein Name");
	private final TextField name2 = new TextField("Mechthild");
	private final TextField name3 = new TextField("Hubertus");
	private final TextField name4 = new TextField("Aghata");
	private final TextField name5 = new TextField("Wigbert");
	
	private final ComboBox<String> aiType2 = new ComboBox<>();
	private final ComboBox<String> aiType3 = new ComboBox<>();
	private final ComboBox<String> aiType4 = new ComboBox<>();
	private final ComboBox<String> aiType5 = new ComboBox<>();
	
	private final ComboBox<GameSet> gameSet = new ComboBox<>();
	
	// TODO: ComboBoxes for aiType need to be filled
	
	public StartupDialog() {
		drawDialog();
		initResultConverter();
	}

	private void drawDialog() {
		this.setTitle("Puerto Rico Setup wählen");
		drawButtonPane();
		drawContentPane();
		this.setWidth(600);
		this.setHeight(400);
	}

	private void drawButtonPane() {
		this.getDialogPane().getButtonTypes().
			addAll(ButtonType.OK, ButtonType.CANCEL);
	}

	private void drawContentPane() {
		grid.setHgap(10);
		grid.setVgap(5);
		
		drawPlayerCount();
		drawPlayerName();
		drawAi();
		drawAiType();
		drawGameSet();
		
		this.getDialogPane().setContent(grid);
	}

	private void drawPlayerCount() {
		grid.add(new Label("Aktiv"), 0, 0);

		grid.add(player4, 0, 4);
		grid.add(player5, 0, 5);
	}

	private void drawPlayerName() {
		grid.add(new Label("Spielername"), 1, 0);
		
		grid.add(name1, 1, 1);
		grid.add(name2, 1, 2);
		grid.add(name3, 1, 3);
		grid.add(name4, 1, 4);
		grid.add(name5, 1, 5);
	}

	private void drawAi() {
		grid.add(new Label("KI"), 2, 0);
		
		grid.add(ai2, 2, 2);
		grid.add(ai3, 2, 3);
		grid.add(ai4, 2, 4);
		grid.add(ai5, 2, 5);
	}

	private void drawAiType() {
		grid.add(new Label("KI-Typ"), 3, 0);

		addAis();

		grid.add(aiType2, 3, 2);
		grid.add(aiType3, 3, 3);
		grid.add(aiType4, 3, 4);
		grid.add(aiType5, 3, 5);
	}

	private void addAis() {
		aiType2.getItems().addAll(AiFactory.AiNames());
		aiType2.getSelectionModel().select(0);
		
		aiType3.getItems().addAll(AiFactory.AiNames());
		aiType3.getSelectionModel().select(0);
		
		aiType4.getItems().addAll(AiFactory.AiNames());
		aiType4.getSelectionModel().select(0);
		
		aiType5.getItems().addAll(AiFactory.AiNames());
		aiType5.getSelectionModel().select(0);
	}

	private void drawGameSet() {
		grid.add(new Label("Karten-Set:"), 1, 7);
		grid.add(gameSet, 3, 7);
		
		ObservableList<GameSet> items = FXCollections.observableArrayList();
		for (GameSet gs : GameSet.values()) {
			items.add(gs);
		}
		gameSet.setItems(items);
		gameSet.getSelectionModel().selectFirst();
	}

	private void initResultConverter() {
		this.setResultConverter(c -> {
			if (c.getButtonData().isCancelButton())
				return null;
			List<PlayerSetup> setup = new LinkedList<>();
			addPlayer1(setup);
			addPlayer2(setup);
			addPlayer3(setup);
			addPlayer4(setup);
			addPlayer5(setup);
			return setup;
		});
	}

	private void addPlayer1(List<PlayerSetup> setup) {
		setup.add(new PlayerSetup(name1.getText(), false, null, GameSet.BASIS));
	}

	private void addPlayer2(List<PlayerSetup> setup) {
		setup.add(new PlayerSetup(name2.getText(), ai2.isSelected(), 
				aiType2.getSelectionModel().getSelectedItem(), GameSet.BASIS));
	}

	private void addPlayer3(List<PlayerSetup> setup) {
		setup.add(new PlayerSetup(name3.getText(), ai3.isSelected(), 
				aiType3.getSelectionModel().getSelectedItem(), GameSet.BASIS));
	}

	private void addPlayer4(List<PlayerSetup> setup) {
		if (player4.isSelected())
			setup.add(new PlayerSetup(name4.getText(), ai4.isSelected(), 
					aiType4.getSelectionModel().getSelectedItem(), GameSet.BASIS));
	}

	private void addPlayer5(List<PlayerSetup> setup) {
		if (player5.isSelected())
			setup.add(new PlayerSetup(name5.getText(), ai5.isSelected(), 
					aiType5.getSelectionModel().getSelectedItem(), GameSet.BASIS));
	}

}
