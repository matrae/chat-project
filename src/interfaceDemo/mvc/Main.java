package interfaceDemo.mvc;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		Model model = new Model();
		View view = new View(stage, model);
		Controller controller = new Controller(model, view);
		view.start();
	}

}
