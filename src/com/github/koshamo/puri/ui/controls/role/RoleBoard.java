package com.github.koshamo.puri.ui.controls.role;

import com.github.koshamo.puri.setup.RoleType;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class RoleBoard extends Region {

	private final int numPlayers;
	
	private HBox row1;
	private HBox row2;
	private HBox row3;
	
	private RoleCard builder;
	private RoleCard settler;
	private RoleCard gouvernor;
	private RoleCard producer;
	private RoleCard captain;
	private RoleCard trader;
	private RoleCard goldfinder1;
	private RoleCard goldfinder2;
	
	public RoleBoard(int numPlayers) {
		this.numPlayers = numPlayers;
		
		drawComponent();
	}

	private void drawComponent() {
		VBox vbox = new VBox(5);
		vbox.setPadding(new Insets(5));
		vbox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.MEDIUM, new Insets(3))));
		
		vbox.getChildren().addAll(initRow1(), initRow2(), initRow3());
		this.getChildren().add(vbox);
	}

	private Node initRow1() {
		row1 = new HBox(5);
		builder = new RoleCard(RoleType.BAUMEISTER); 
		settler = new RoleCard(RoleType.SIEDLER);
		gouvernor = new RoleCard(RoleType.BUERGERMEISTER);
		
		row1.getChildren().addAll(builder, settler, gouvernor);
		return row1;
	}

	private Node initRow2() {
		row2 = new HBox(5);
		producer = new RoleCard(RoleType.AUFSEHER); 
		captain = new RoleCard(RoleType.KAPITAEN); 
		trader = new RoleCard(RoleType.HAENDLER);

		row2.getChildren().addAll(producer, captain, trader);
		return row2;
	}

	private Node initRow3() {
		row3 = new HBox(5);
		if (numPlayers > 3) {
			goldfinder1 = new RoleCard(RoleType.GOLDSUCHER);
			row3.getChildren().add(goldfinder1);
		}
		if (numPlayers > 4) {
			goldfinder2 = new RoleCard(RoleType.GOLDSUCHER);
			row3.getChildren().add(goldfinder2);
		}
		return row3;
	}
}
