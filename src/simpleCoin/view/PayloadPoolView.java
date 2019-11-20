package simpleCoin.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PayloadPoolView extends VBox {
	final String STR_PENDING = " pending messages";

	Label lblTitle = new Label("Message pool");

	TextField txtMessage = new TextField();
	Button btnAdd = new Button("Add");
	Button btnAdd1000 = new Button("Add x 1000");
	Label lblPending = new Label(0 + STR_PENDING);

	PayloadPoolView() {
		super();
		HBox messageRow = new HBox(txtMessage, btnAdd, btnAdd1000);

		this.getChildren().addAll(lblTitle, messageRow, lblPending);

		// Formatting
		lblTitle.setStyle("-fx-font-size: 24");

		this.setSpacing(10);
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));

		messageRow.setSpacing(10);
	}

	public void setPending(int pendingMessages) {
		Platform.runLater(() -> {
			lblPending.setText(pendingMessages + STR_PENDING);
		});
	}

	public TextField getTxtMessage() {
		return txtMessage;
	}

	public Button getBtnAdd() {
		return btnAdd;
	}

	public Button getBtnAdd1000() {
		return btnAdd1000;
	}
}
