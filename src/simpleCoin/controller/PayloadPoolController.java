package simpleCoin.controller;

import simpleCoin.model.SimpleCoinModel;
import simpleCoin.model.payload.Message;
import simpleCoin.model.payload.PayloadItem;
import simpleCoin.view.PayloadPoolView;

public class PayloadPoolController {
	private SimpleCoinModel model;
	private PayloadPoolView view;

	public PayloadPoolController(SimpleCoinModel model, PayloadPoolView view) {
		this.model = model;
		this.view = view;

		// Handle "add" events
		view.getBtnAdd().setOnAction((e) -> {
			PayloadItem item = new Message(view.getTxtMessage().getText());
			model.addPayloadItem(item);
		});
		
		// Handle "add" events
		view.getBtnAdd1000().setOnAction((e) -> {
			for (int i = 0; i < 1000; i++) {
				PayloadItem item = new Message(view.getTxtMessage().getText());
				model.addPayloadItem(item);
			}
		});

		// Monitor size of pool
		model.getPayloadPoolSizeProperty().addListener((property, oldValue, newValue) -> {
			view.setPending(newValue.intValue());
		});
	}
}
