package JavaBugs.Bug8178368;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * http://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8178368
 * 
 * Original bug report from Florian Siffer, OOP2
 */
public class TextFieldBug extends Application {
	private final String SHORT_TEXT = "A short text";
	private final String LONG_TEXT = "Long text this is a long text this is a long text";
	
	private Label lblLeft = new Label("Left justified");
	private TextField txtLeftShort = new TextField(SHORT_TEXT);
	private TextField txtLeftLong = new TextField(SHORT_TEXT);
	
	private Label lblRight = new Label("Right justified");
	private TextField txtRightShort = new TextField(SHORT_TEXT);
	private TextField txtRightLong = new TextField(SHORT_TEXT);

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane root = new GridPane();
		root.addColumn(0, lblLeft, txtLeftShort, txtLeftLong);
		root.addColumn(1, lblRight, txtRightShort, txtRightLong);
		
		lblLeft.setAlignment(Pos.BASELINE_LEFT);
		txtLeftShort.setAlignment(Pos.BASELINE_LEFT);
		txtLeftLong.setAlignment(Pos.BASELINE_LEFT);
		
		lblRight.setMaxWidth(9999);
		lblRight.setAlignment(Pos.BASELINE_RIGHT);
		txtRightShort.setAlignment(Pos.BASELINE_RIGHT);
		txtRightLong.setAlignment(Pos.BASELINE_RIGHT);
		
		Scene scene = new Scene(root, 300, 200);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Text field alignment bug");
		primaryStage.show();
		
		// Changing text after "show"
		txtLeftLong.setText(LONG_TEXT);
		txtRightLong.setText(LONG_TEXT);			
	}
}
