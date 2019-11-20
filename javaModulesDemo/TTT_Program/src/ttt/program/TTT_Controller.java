package ttt.program;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import ttt.commons.Move;
import ttt.commons.Pieces;
import ttt.commons.Result;

public class TTT_Controller {
	private final TTT_View view;
	private final TTT_Model model;
	
	public TTT_Controller(TTT_View view, TTT_Model model) {
		this.view = view;
		this.model = model;
		
		// Create the buttons: 3 columns & 3 rows
		for (int col = 0; col < 3; col++) {
			for (int row = 0; row < 3; row++) {
				view.getButtons()[col][row].setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent e) {
						Button btn = (Button) e.getSource();
						userMove(btn);
					}
				});
			}
		}		
	}
	
	private void userMove(Button btn) {
		int col = 0;
		int row = 0;
		boolean found = false;
		while (col < 3 && !found) {
			row = 0;
			while (row < 3 && !found) {
				found = (btn == view.getButtons()[col][row]);
				if (!found) row++;
			}
			if (!found) col++;
		}
		assert(found); // Cannot ever be false...
		
		// Player's move, followed by AI's move
		Move move = new Move(Pieces.X, col, row);
		model.makeMove(move);
		view.showMove(col, row, Pieces.X);
		
		Result result = model.getResult();
		if (result != null) {
			view.announceWinner(result);
		} else {
			move = model.makeAImove();
			if (move != null) {
				col = move.getX();
				row = move.getY();
				view.showMove(col, row, move.getPiece());
			}
			result = model.getResult();
			if (result != null) view.announceWinner(result);
		}
	}
}
