package simpleCoin.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import simpleCoin.model.Block;
import simpleCoin.model.BlockChain;
import simpleCoin.model.BlockHeader;
import simpleCoin.utility.HelperMethods;
import simpleCoin.utility.Integer256;

public class BlockExplorerView extends VBox {
	// General controls
	final String STR_UP = " blocks above this one";
	final String STR_DOWN = " blocks below this one";

	Label lblTitle = new Label("Block Explorer");
	Label lblUp = new Label(0 + STR_UP);
	Label lblDown = new Label(0 + STR_DOWN);
	Button btnUp = new Button("\u2191");
	Button btnDown = new Button("\u2193");

	// Controls for info about the block itself
	Label lblVersionNumber = new Label("Version number");
	Label lblPreviousBlockHash = new Label("Previous block hash");
	Label lblPayloadHash = new Label("Payload hash");
	Label lblPayloadSize = new Label("Payload size (items)");
	Label lblTimestamp = new Label("Timestamp");
	Label lblTargetValue = new Label("Target value");
	Label lblBlockHash = new Label("Block hash");

	// One label must have contents, so that initial window size is sensible
	Label lblValueVersionNumber = new Label();
	Label lblValuePreviousBlockHash = new Label();
	Label lblValuePayloadHash = new Label();
	Label lblValuePayloadSize = new Label();
	Label lblValueTimestamp = new Label();
	Label lblValueTargetValue = new Label();
	Label lblValueBlockHash = new Label();
	
	private BlockChain blockchain;
	int currentBlock = 0;

	BlockExplorerView(BlockChain blockchain) {
		super();
		this.blockchain = blockchain;

		HBox upRow = new HBox(btnUp, lblUp);
		HBox downRow = new HBox(btnDown, lblDown);

		GridPane gridHeaderInfo = new GridPane();
		gridHeaderInfo.addRow(0, lblVersionNumber, lblValueVersionNumber);
		gridHeaderInfo.addRow(1, lblPreviousBlockHash, lblValuePreviousBlockHash);
		gridHeaderInfo.addRow(2, lblPayloadHash, lblValuePayloadHash);
		gridHeaderInfo.addRow(3, lblPayloadSize, lblValuePayloadSize);
		gridHeaderInfo.addRow(4, lblTimestamp, lblValueTimestamp);
		gridHeaderInfo.addRow(5, lblTargetValue, lblValueTargetValue);
		gridHeaderInfo.addRow(6, lblBlockHash, lblValueBlockHash);

		this.getChildren().addAll(lblTitle, upRow, gridHeaderInfo, downRow);

		// Formatting
		lblTitle.setStyle("-fx-font-size: 24");

		this.setSpacing(10);
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));

		upRow.setSpacing(10);
		downRow.setSpacing(10);

		// We need to determine the required width for the columns
		Text text = new Text("Xxxxxxxxxxxxxxxxxxxx");
		text.applyCss();
		ColumnConstraints cc0 = new ColumnConstraints(text.getLayoutBounds().getWidth());
		text = new Text("0000000000000000000000000000000000000000000000000000000000000000");
		text.applyCss();
		ColumnConstraints cc1 = new ColumnConstraints(text.getLayoutBounds().getWidth());
		gridHeaderInfo.getColumnConstraints().addAll(cc0, cc1);
		
		gridHeaderInfo.setPadding(new Insets(10, 10, 10, 10));
		gridHeaderInfo.setBorder(
				new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(2))));
		gridHeaderInfo.setVgap(10);
		gridHeaderInfo.setHgap(10);
	}

	/**
	 * We can only display the first block after all initialization is complete
	 */
	public void start() {
		currentBlock = 0;
		displayBlock();
		updateBlockCounts();
	}
	
	public void moveOneBlockUp() {
		if (currentBlock < (blockchain.getBlockChainSize() - 1)) { // Should always be the case!
			currentBlock++;
			displayBlock();
			updateBlockCounts();
		}
	}
	
	public void moveOneBlockDown() {
		if (currentBlock > 0) { // Should always be the case!
			currentBlock--;
			displayBlock();
			updateBlockCounts();
		}
	}

	private void displayBlock() {
		Block block = blockchain.getBlock(currentBlock);
		BlockHeader header = block.getBlockHeader();

		// Display the block
		String strValueVersionNumber = Integer.toString(header.getVersionNumber());

		String strValuePreviousBlockHash;
		if (currentBlock > 0)
			strValuePreviousBlockHash = HelperMethods.bytesToHex(block.getHash());
		else
			strValuePreviousBlockHash = "";

		String strValuePayloadHash = HelperMethods.bytesToHex(block.getPayload().getHash());

		String strValuePayloadSize = Integer.toString(block.getPayload().size());
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSS");
		String strValueTimestamp = formatter.format(header.getTimeStamp());

		Integer256 tv = new Integer256(header.getTargetValue());
		String strValueTargetValue = tv.toString();

		String strValueBlockHash = HelperMethods.bytesToHex(block.getHash());

		Platform.runLater(() -> {
			lblValueVersionNumber.setText(strValueVersionNumber);
			lblValuePreviousBlockHash.setText(strValuePreviousBlockHash);
			lblValuePayloadHash.setText(strValuePayloadHash);
			lblValuePayloadSize.setText(strValuePayloadSize);
			lblValueTimestamp.setText(strValueTimestamp);
			lblValueTargetValue.setText(strValueTargetValue);
			lblValueBlockHash.setText(strValueBlockHash);
		});
	}
	
	public void updateBlockCounts() {
		int blocksAbove = blockchain.getBlockChainSize() - currentBlock - 1;
		int blocksBelow = currentBlock;
		
		Platform.runLater(() -> {
			lblUp.setText(blocksAbove + STR_UP);
			btnUp.setDisable(blocksAbove <= 0);
			
			lblDown.setText(blocksBelow + STR_DOWN);
			btnDown.setDisable(blocksBelow <= 0);
		});
	}

	public Button getBtnUp() {
		return btnUp;
	}

	public Button getBtnDown() {
		return btnDown;
	}
	
}
