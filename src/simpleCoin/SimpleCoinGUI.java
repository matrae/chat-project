package simpleCoin;

import javafx.application.Application;
import javafx.stage.Stage;
import simpleCoin.controller.SimpleCoinController;
import simpleCoin.model.SimpleCoinModel;
import simpleCoin.view.SimpleCoinView;

public class SimpleCoinGUI extends Application {
	SimpleCoinModel model;
	SimpleCoinView view;
	SimpleCoinController controller;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Initialize the GUI
		model = new SimpleCoinModel();
		view = new SimpleCoinView(primaryStage, model);
		controller = new SimpleCoinController(model, view);
		
		// Display the GUI after all initialization is complete
		view.start();
	}

}
