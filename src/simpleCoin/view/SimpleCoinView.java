package simpleCoin.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simpleCoin.model.SimpleCoinModel;

public class SimpleCoinView {
    private SimpleCoinModel model;
    private Stage stage;
    private BlockExplorerView blockExplorerView;
    private PayloadPoolView payloadPoolView;
    private MiningControlView miningControlView;

	public SimpleCoinView(Stage stage, SimpleCoinModel model) {
		this.stage = stage;
		this.model = model;
		
		stage.setTitle("Simple Coin GUI");
		
		GridPane root = new GridPane();
		root.setPadding(new Insets(10, 10, 10, 10));

		blockExplorerView = new BlockExplorerView(model.getBlockChain());
		root.add(blockExplorerView, 0,0, 1, 2);
		
		payloadPoolView = new PayloadPoolView();
		root.add(payloadPoolView, 1, 1);
		
		miningControlView = new MiningControlView();
		root.add(miningControlView, 1, 0);

		Scene scene = new Scene(root);
		stage.setScene(scene);;
	}
	
	public void start() {
		blockExplorerView.start();
		stage.show();
	}
	
	/**
	 * Stopping the view - just make it invisible
	 */
	public void stop() {
		stage.hide();
	}
	
	/**
	 * Getter for the stage, so that the controller can access window events
	 */
	public Stage getStage() {
		return stage;
	}

	public BlockExplorerView getBlockExplorerView() {
		return blockExplorerView;
	}

	public PayloadPoolView getPayloadPoolView() {
		return payloadPoolView;
	}

	public MiningControlView getMiningControlView() {
		return miningControlView;
	}
}