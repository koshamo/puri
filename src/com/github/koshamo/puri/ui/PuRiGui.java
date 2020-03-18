package com.github.koshamo.puri.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.GameController;
import com.github.koshamo.puri.ai.AbstractAi;
import com.github.koshamo.puri.gamedata.PlayerSetup;
import com.github.koshamo.puri.gamedata.StartupConstants;
import com.github.koshamo.puri.setup.GameSet;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.ui.controls.StartupDialog;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;
import com.github.koshamo.puri.utils.ListUtils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PuRiGui extends Application {

	private Stage primaryStage;
	private BorderPane mainPane;
	
	private List<Player> players;
	private Board gameBoard;
	private RoleBoard roleBoard;
	private GameController controller;
	private GameSet gameSet;
	private StartupConstants constants;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initGui();
		runPlayerSetup();
		
		controller = new GameController(constants, players, gameBoard, roleBoard);
		controller.start();
	}

	private void runPlayerSetup() {
		StartupDialog dialog = new StartupDialog();
		Optional<List<PlayerSetup>> players = dialog.showAndWait();
		
		if (!players.isPresent())
			System.exit(0);
		
		constants = new StartupConstants(players.get().size());
		initGameBoardPanels(constants.NUM_PLAYERS); 
		initPlayers(players.get());
		buildGui();
	}

	private void initPlayers(List<PlayerSetup> playerList) {
		List<PlayerSetup> list = ListUtils.generateRandomList(playerList);
		players = new LinkedList<>();
		gameSet = list.get(0).gameSet;
		
		for (int i = 0; i < list.size(); i++) {
			AbstractAi ai = list.get(i).ai ? list.get(i).aiType : null;
			Player p = new Player(list.get(i).name, ai, PrColors.getPlayerColor(i));
			p.addPlantation(constants.INITIAL_PLANTATIONS[i]);
			players.add(p);
		}
	}

	private void initGui() {
		mainPane = new BorderPane();
		
		primaryStage.setScene(new Scene(mainPane));
		primaryStage.setTitle("Puerto Rico Board Game");
	}

	private void buildGui() {
		buildPlayerPane();
		buildBoardPane();
		primaryStage.sizeToScene();
		primaryStage.show();
}

	private void buildPlayerPane() {
		VBox playerBox = new VBox();
		for (int i = 0; i < players.size(); i++)
			playerBox.getChildren().add(players.get(i));
		
		mainPane.setLeft(playerBox);
	}

	private void buildBoardPane() {
		VBox gameBox = new VBox();
		int numPlayers = players.size();
		gameBox.getChildren().addAll(
				gameBoard, roleBoard);
		
		mainPane.setCenter(gameBox);
	}

	private void initGameBoardPanels(int numPlayers) {
		gameBoard = new Board(constants);
		roleBoard = new RoleBoard(numPlayers);
	}

}
