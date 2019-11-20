package simpleCoin.controller;

import simpleCoin.model.SimpleCoinModel;
import simpleCoin.view.BlockExplorerView;

public class BlockExplorerController {
	private SimpleCoinModel model;
	private BlockExplorerView view;

	public BlockExplorerController(SimpleCoinModel model, BlockExplorerView view) {
		this.model = model;
		this.view = view;
		
		// Handle up event
		view.getBtnUp().setOnAction((e) -> {
			view.moveOneBlockUp();
		});

		// Handle down event
		view.getBtnDown().setOnAction((e) -> {
			view.moveOneBlockDown();
		});

		// Monitor size of blockchain
		model.getBlockChain().getBlockChainSizeProperty().addListener((property, oldValue, newValue) -> {
			view.updateBlockCounts();
		});
	}
}
