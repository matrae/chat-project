package simpleCoin.controller;

import simpleCoin.model.SimpleCoinModel;
import simpleCoin.view.SimpleCoinView;
import simpleCoin.view.BlockExplorerView;

public class SimpleCoinController {
	
	final private SimpleCoinModel model;
	final private SimpleCoinView view;
	private PayloadPoolController payloadPoolController;
	private BlockExplorerController blockExplorerController;
	private MiningController miningController;
	
	public SimpleCoinController(SimpleCoinModel model, SimpleCoinView view) {
		this.model = model;
		this.view = view;
		payloadPoolController = new PayloadPoolController(model, view.getPayloadPoolView());
		blockExplorerController = new BlockExplorerController(model, view.getBlockExplorerView());
		miningController = new MiningController(model, view.getMiningControlView());
	}

}
