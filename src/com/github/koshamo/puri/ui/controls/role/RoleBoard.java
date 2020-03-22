package com.github.koshamo.puri.ui.controls.role;

import java.util.ArrayList;
import java.util.List;

import com.github.koshamo.puri.GameController;
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
	private GameController controller;

	private HBox row1;
	private HBox row2;
	private HBox row3;

	List<RoleCard> roleCards;
	
	public RoleBoard(int numPlayers) {
		this.numPlayers = numPlayers;
		roleCards = new ArrayList<>();
		
		drawComponent();
	}
	
	public void connectController(GameController controller) {
		this.controller = controller;
	}
	
	public List<RoleCard> roleCards() {
		return roleCards;
	}

	public void activate() {
		for (RoleCard rc : roleCards) {
			if (!rc.isUsed())
				rc.setOnMouseClicked(ev -> { handleRoleSelected(rc); });
		}
	}
	
	/*private*/ void handleRoleSelected(RoleCard card) {
		for (RoleCard rc : roleCards)
			rc.setOnMouseClicked(null);
		
		controller.chooseRole(card.type(), card.removeGuldenAndDeactivateCard());
	}
	
	public void prepareNextTurn() {
		for (RoleCard rc : roleCards)
			rc.prepareNextTurn();
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
		RoleCard builder = new RoleCard(RoleType.BAUMEISTER); 
		roleCards.add(builder);
		RoleCard settler = new RoleCard(RoleType.SIEDLER);
		roleCards.add(settler);
		RoleCard gouvernor = new RoleCard(RoleType.BUERGERMEISTER);
		roleCards.add(gouvernor);
		
		row1.getChildren().addAll(builder, settler, gouvernor);
		return row1;
	}

	private Node initRow2() {
		row2 = new HBox(5);
		RoleCard producer = new RoleCard(RoleType.AUFSEHER); 
		roleCards.add(producer);
		RoleCard captain = new RoleCard(RoleType.KAPITAEN); 
		roleCards.add(captain);
		RoleCard trader = new RoleCard(RoleType.HAENDLER);
		roleCards.add(trader);

		row2.getChildren().addAll(producer, captain, trader);
		return row2;
	}

	private Node initRow3() {
		row3 = new HBox(5);
		if (numPlayers > 3) {
			RoleCard goldfinder1 = new RoleCard(RoleType.GOLDSUCHER);
			roleCards.add(goldfinder1);
			row3.getChildren().add(goldfinder1);
		}
		if (numPlayers > 4) {
			RoleCard goldfinder2 = new RoleCard(RoleType.GOLDSUCHER);
			roleCards.add(goldfinder2);
			row3.getChildren().add(goldfinder2);
		}
		return row3;
	}
}
