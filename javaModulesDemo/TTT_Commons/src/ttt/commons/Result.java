package ttt.commons;

/**
 * Result of evaluating a board: Win_O, Draw, Win_X
 * 
 * The static method for evaluating a board returns null if the game is not yet over.
 */
public enum Result {
	WIN_X, DRAW, WIN_O;
	
	/**
	 * Return the winning piece, if any
	 */
	public Pieces getWinner() {
		if (this == WIN_O) return Pieces.O;
		else if (this == WIN_X) return Pieces.X;
		else return null;
	}
	
	/**
	 * This method could be optimized, but it's fast enough...
	 */
	public static Result getResult(Pieces[][] board) {
		int numEmptyCells = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == null) numEmptyCells++;
			}
		}
		
		Pieces winner = null;
		Pieces rowPiece = null;
		Pieces colPiece = null;
		for (int i = 0; i < 3; i++) {
			rowPiece = board[0][i];
			colPiece = board[i][0];
			for (int j = 1; j < 3; j++) {
				if (board[j][i] != rowPiece) rowPiece = null;
				if (board[i][j] != colPiece) colPiece = null;
			}
			if (rowPiece != null) winner = rowPiece;
			if (colPiece != null) winner = colPiece;
		}
		
		Pieces diag1 = board[0][0];
		Pieces diag2 = board[2][0];
		for (int i = 1; i < 3; i++) {
			if (board[i][i] != diag1) diag1 = null;
			if (board[2-i][i] != diag2) diag2 = null;
		}
		if (diag1 != null) winner = diag1;
		if (diag2 != null) winner = diag2;
		
		if (winner == Pieces.X) return WIN_X;
		else if (winner == Pieces.O) return WIN_O;
		else if (numEmptyCells == 0) return DRAW;
		else return null;
	}
}
