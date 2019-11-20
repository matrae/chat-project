package interfaceDemo.mvc;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class View {
	Stage stage;
	Model model;
	
	ComboBox<String> cmbMode = new ComboBox<>();
	Button btnStart = new Button("Start");
	
	CheckBox chkUs = new CheckBox("us");
	CheckBox chkThem = new CheckBox("them");
	
	public View(Stage stage, Model model) {
		this.stage = stage;
		this.model = model;
		
		cmbMode.getItems().add("Network server");
		cmbMode.getItems().add("Network client");
		cmbMode.getItems().add("AI player");
		
		chkUs.setDisable(true);
		chkThem.setDisable(true);
		
		HBox topBox = new HBox(cmbMode, btnStart);
		topBox.getStyleClass().add("hbox");
		HBox bottomBox = new HBox(chkUs, chkThem);
		bottomBox.getStyleClass().add("hbox");
		
		chkUs.setSelected(model.isWeAreSelected());
		chkThem.setSelected(model.isTheyAreSelected());
		
		VBox root = new VBox(topBox, bottomBox);
		root.getStyleClass().add("vbox");
		
		Scene scene = new Scene(root);
        scene.getStylesheets().add(
                getClass().getResource("styles.css").toExternalForm());
        stage.setTitle("Interface Demo");
        stage.setScene(scene);
	}
	
	public void start() {
		stage.show();
	}
	
}
