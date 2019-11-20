package ttt.program;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ttt.commons.Move;

public class TicTacToe extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		TTT_Model model = new TTT_Model();
		TTT_View view = new TTT_View(primaryStage, model);
		TTT_Controller controller = new TTT_Controller(view, model);
	}
}
