package com.github.koshamo.puri;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import com.github.koshamo.puri.gamedata.PlayerSetup;
import com.github.koshamo.puri.setup.PrColors;
import com.github.koshamo.puri.ui.controls.StartupDialog;
import com.github.koshamo.puri.ui.controls.board.Board;
import com.github.koshamo.puri.ui.controls.player.Player;
import com.github.koshamo.puri.ui.controls.role.RoleBoard;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PuRiGui extends Application {

	private Stage primaryStage;
	private BorderPane mainPane;
	private List<Player> players;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		
		initGui();
		runPlayerSetup();
	}

	private void runPlayerSetup() {
		StartupDialog dialog = new StartupDialog();
		Optional<List<PlayerSetup>> players = dialog.showAndWait();
		
		if (!players.isPresent())
			System.exit(0);
		
		initPlayers(players.get());
		buildGui();
	}

	private void initPlayers(List<PlayerSetup> playerList) {
		List<PlayerSetup> list = playerList;
		players = new LinkedList<>();
		int listSize = list.size();
		
		for (int i = 0; i < listSize; i++) {
			double d = Math.random() * list.size();
			PlayerSetup ps = list.remove((int)d);
			Player p = new Player(ps.name, PrColors.getPlayerColor(i));
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
				new Board(numPlayers), new RoleBoard(numPlayers));
		
		mainPane.setCenter(gameBox);
	}

}
