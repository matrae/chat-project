package ttt.ai.random;

import ttt.api.ttt_ai;
import ttt.commons.Move;
import ttt.commons.Pieces;

public class RandomPlayer implements ttt_ai {
	private Pieces myPiece = Pieces.X; // Which player are we?
	
	@Override
	public String getName() {
		return "Random";
	}

	@Override
	public void setPiece(Pieces myPiece) {
		this.myPiece = myPiece;
	}

	@Override
	public Move makeMove(Pieces[][] board) {
		// If board is full, return null
		boolean hasEmpty = false;
		for (int x = 0; x <= 2 && !hasEmpty; x++) {
			for (int y = 0; y <= 2 && !hasEmpty; y++) {
				hasEmpty = (board[x][y] == null);
			}
		}
		if (!hasEmpty) return null;

		// Generate random moves, until we find an empty spot
		boolean foundMove = false;
		Move move = null;
		while (!foundMove) {
			int x = (int) (Math.random() * 3); 
			int y = (int) (Math.random() * 3); 
			foundMove = (board[x][y] == null);
			if (foundMove) {
				move = new Move(myPiece, x, y);
			}
		}
		return move;
	}
}
