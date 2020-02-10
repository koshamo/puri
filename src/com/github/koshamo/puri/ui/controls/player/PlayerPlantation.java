package com.github.koshamo.puri.ui.controls.player;

import java.util.ArrayList;
import java.util.List;

import com.github.koshamo.puri.setup.PlantationType;
import com.github.koshamo.puri.setup.State;

import javafx.geometry.Insets;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/*private*/ class PlayerPlantation extends Region {

	private final Player player;
	private List<PlantationField> plantations;
	
	private HBox rowOne;
	private HBox rowTwo;
	private HBox rowThree;
	
	public PlayerPlantation(Player player) {
		this.player = player;
		plantations = new ArrayList<>(12);
		initGui();
		initPlantations();
		update();
	}

	public void addPlantation(PlantationType type) {
		if (!plantations.get(11).type().equals(PlantationType.NONE))
			return;
		plantations.get(11).addPlantation(type);
		sort();
	}

	public void activateColonistsDnD() {
		updateDragging();
	}

	public void deactivateColonistsDnD() {
		cancelDragging();
	}
	
	private void updateDragging() {
		for (PlantationField field : plantations) {
			if (field.type() != PlantationType.NONE) {
				if (field.state() == State.ACTIVE)
					addColonistDragTarget(field);
				else
					addColonistDropTarget(field);
			}
		}
	}

	private static void addColonistDropTarget(PlantationField field) {
		field.setOnDragOver(ev -> {
		    if (ev.getGestureSource() != field &&
		            ev.getDragboard().hasString()) {
		        ev.acceptTransferModes(TransferMode.MOVE);
		    }
		    ev.consume();
		});
		field.setOnDragEntered(ev -> {
			// TODO: show drop possible
			ev.consume();
		});
		field.setOnDragExited(ev -> {
			// TODO: end show drop possible
			ev.consume();
		});
		field.setOnDragDropped(ev -> {
		    Dragboard db = ev.getDragboard();
		    boolean success = false;
		    if (db.hasString()) {
		       field.activate();
		       success = true;
		    }
		    ev.setDropCompleted(success);
		    ev.consume();
		});
	}

	private void addColonistDragTarget(PlantationField field) {
		field.setOnDragDetected(ev -> {
			Dragboard db = field.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent cc = new ClipboardContent();
			cc.putString("1");
			db.setContent(cc);
			ev.consume();
		});
		field.setOnDragDone(ev -> {
			field.deactivate();
			player.distributeColonists();
			ev.consume();
		});
	}

	private void cancelDragging() {
		for (PlantationField field : plantations) {
			if (field.type() != PlantationType.NONE) {
				field.setOnDragDetected(null);
				field.setOnDragDone(null);
				field.setOnDragEntered(null);
				field.setOnDragExited(null);
				field.setOnDragDropped(null);
			}
		}
	}

	private void initGui() {
		VBox vbox = new VBox(3);
		vbox.setPadding(new Insets(2, 0, 0, 0));
		rowOne = new HBox(3);
		rowTwo = new HBox(3);
		rowThree = new HBox(3);
		vbox.getChildren().addAll(rowOne, rowTwo, rowThree);
		this.getChildren().add(vbox);
	}

	private void initPlantations() {
		for (int i = 0; i < 12; i++)
			plantations.add(new PlantationField());
	}
	
	private void update() {
		rowOne.getChildren().clear();
		rowTwo.getChildren().clear();
		rowThree.getChildren().clear();
		
		for (int i = 0; i < 4; i++) {
			rowOne.getChildren().add(plantations.get(i));
			rowTwo.getChildren().add(plantations.get(i+4));
			rowThree.getChildren().add(plantations.get(i+8));
		}
	}

	private void sort() {
		plantations.sort((p1, p2) -> {
			int comp = p1.type().compareTo(p2.type());
			if (comp == 0)
				comp = p1.state().compareTo(p2.state());
			return comp;
		});
		update();
	}
	
}
