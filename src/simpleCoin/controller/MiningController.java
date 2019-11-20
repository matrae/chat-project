package simpleCoin.controller;

import javafx.concurrent.Worker.State;
import simpleCoin.model.MiningTask;
import simpleCoin.model.SimpleCoinModel;
import simpleCoin.view.MiningControlView;

public class MiningController {
	private SimpleCoinModel model;
	private MiningControlView view;
	private MiningTask miner;
	private Thread miningThread;

	public MiningController(SimpleCoinModel model, MiningControlView view) {
		this.model = model;
		this.view = view;

		// Handle "add" events
		view.getBtnStartStop().setOnAction((e) -> {
			if (miner != null && miner.isRunning()) {
				miner.cancel();
				view.updateBtnStartStop(true);
			} else {
				miner = new MiningTask(model.getBlockChain(), model.getPayloadPool());

				// Monitor hash-rate
				miner.getHashRateProperty().addListener((property, oldValue, newValue) -> {
					view.updateHashRate(miner.getHashRate());
				});
				
				// Monitor blockchain for new blocks
				model.getBlockChain().getBlockChainSizeProperty().addListener((property, oldValue, newValue) -> {
					view.updateNewBlockFound(model.getBlockChain());
				});				

				// Watch for task completion or cancellation
				miner.stateProperty().addListener((property, oldValue, newValue) -> {
					if (newValue == State.CANCELLED || newValue == State.SUCCEEDED) {
						view.updateHashRate(0);
						view.updateBtnStartStop(true);
					} else {
						// Ignore all other state changes
					}
				});

				// Change the start-button to a stop-button
				view.updateBtnStartStop(false);

				// Start the mining task
				miningThread = new Thread(miner, "Mining Thread");
				miningThread.start();
			}
		});
	}
}
