package interfaceDemo.mvc;

import interfaceDemo.opponents.AI_Player;
import interfaceDemo.opponents.Network_Player;

public class Controller {
	private final View view;
	private final Model model;
	
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		
		view.btnStart.setOnAction( e -> {
			// Load selected opponent module. Pass it a reference to our model, so that it
			// can communicate with us. Our model implements the Comm interface
			if (view.cmbMode.getSelectionModel().getSelectedItem().equals("AI player")) {
				model.setOpponent(new AI_Player(model));
			} else {
				boolean weAreServer = view.cmbMode.getSelectionModel().getSelectedItem().equals("Network server");
				model.setOpponent(new Network_Player(model, weAreServer));
			}
			
			// Enable/disable controls
			view.cmbMode.setDisable(true);
			view.btnStart.setDisable(true);
			view.chkUs.setDisable(false);
		});
		
		view.chkUs.setOnAction( e -> {
			// User action triggers model action
			model.setWeAreSelected(view.chkUs.isSelected());
		});
		
		model.getTheyAreSelected().addListener((p, oldValue, newValue) -> {
			// Update view with opponent's move
			view.chkThem.setSelected(newValue);
		});
	}
}
