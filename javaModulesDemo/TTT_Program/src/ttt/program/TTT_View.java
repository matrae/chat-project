package ttt.program;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ttt.commons.Move;
import ttt.commons.Pieces;
import ttt.commons.Result;

public class TTT_View {
	private final TTT_Model model;
	private final Stage stage;
	private final Button[][] buttons = new Button[3][3];
	private final Label lblResult = new Label();
	
	public TTT_View(Stage stage, TTT_Model model) {
		this.model = model;
		this.stage = stage;
		
		GridPane playingBoard = new GridPane();

		// Create the buttons: 3 columns & 3 rows
		for (int col = 0; col < 3; col++) {
			for (int row = 0; row < 3; row++) {
				// Create
				Button btn = new Button();
				
				// Add to layout
				playingBoard.add(btn, col, row);
				
				// Add to array of buttons, for easy reference
				buttons[col][row] = btn;
				
				// Format
				btn.setPrefSize(120, 120);
			}
		}

		// Create the menu bar
		MenuBar menus = new MenuBar();
		Menu AI_menu = new Menu("AI Player");
		menus.getMenus().add(AI_menu);
		List<String> names = model.get_AI_PlayerNames();
		for (String name : names) {
			MenuItem mi = new MenuItem(name);
			AI_menu.getItems().add(mi);
			mi.setOnAction( event -> {
				MenuItem mItem = (MenuItem) event.getSource();
				model.select_AI_player(mItem.getText());
			});
		}
		
		// Create the root layout
		VBox root  = new VBox(15, menus, playingBoard, lblResult);
		
		// Create the scene using our layout; then display it
		Scene scene = new Scene(root);
		scene.getStylesheets().add(
				getClass().getResource("TicTacToe.css").toExternalForm());
		stage.setTitle("Tic-Tac-Toe");
		stage.setScene(scene);
		
		// Get opponent's first move
		Move move;
		
		stage.show();
		
	}
	
	public Button[][] getButtons() {
		return buttons;
	}
	
	public void showMove(int col, int row, Pieces piece) {
		buttons[col][row].setText(piece.toString());
		buttons[col][row].setDisable(true);
	}
	
	public void announceWinner(Result result) {
		if (result != null) { // game is over
			for (int col = 0; col < 3; col++) {
				for (int row = 0; row < 3; row++) {
					buttons[col][row].setDisable(true);
				}
			}
			lblResult.setText(result.toString());
		}
	}
}
