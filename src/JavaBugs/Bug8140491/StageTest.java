package JavaBugs.Bug8140491;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class StageTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Hello World!");
		Button btn = new Button();
		btn.setText("Show Alert");
		btn.setOnAction(e -> {
			Alert alert = new Alert(AlertType.WARNING, "This is an alert", ButtonType.YES);
			alert.showAndWait();
		});
		StackPane root = new StackPane();
		root.getChildren().add(btn);
		primaryStage.setScene(new Scene(root, 1000, 850));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}