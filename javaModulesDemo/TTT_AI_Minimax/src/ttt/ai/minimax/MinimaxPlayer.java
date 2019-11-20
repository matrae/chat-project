package ttt.ai.minimax;

import ttt.api.ttt_ai;
import ttt.commons.Move;
import ttt.commons.Pieces;
import ttt.commons.Result;

public class MinimaxPlayer implements ttt_ai {
	private Pieces myPiece = Pieces.X; // Which player are we?
	
	private enum Evaluation { LOSS, DRAW, WIN };

	private class MoveEval {
		Move move;
		Evaluation evaluation;

		public MoveEval(Move move, Evaluation evaluation) {
			this.move = move;
			this.evaluation = evaluation;
		}
	}

	@Override
	public String getName() {
		return "Minimax";
	}

	@Override
	public void setPiece(Pieces myPiece) {
		this.myPiece = myPiece;
	}

	@Override
	public Move makeMove(Pieces[][] board) {
		return makeMove(board, myPiece).move ;
	}

	private MoveEval makeMove(Pieces[][] board, Pieces toMove) {
		Move bestMove = null;
		Evaluation bestEval = Evaluation.LOSS;

		Result result = Result.getResult(board);
		if (result != null) { // game is over
			if (result.getWinner() == toMove) return new MoveEval(null, Evaluation.WIN);
			else if (result.getWinner() != null) return new MoveEval(null, Evaluation.LOSS);
			else return new MoveEval(null, Evaluation.DRAW);
		} else {
			// Find best move for piece toMove
			for (int col = 0; col < 3; col++) {
				for (int row = 0; row < 3; row++) {
					if (board[col][row] == null) {
						// Possible move found
						Pieces[][] possBoard = copyBoard(board);
						possBoard[col][row] = toMove;
						MoveEval tempMR = makeMove(possBoard, (toMove == Pieces.X) ? Pieces.O : Pieces.X);
						Evaluation possEval = invertResult(tempMR.evaluation); // Their win is our loss, etc.
						if (possEval.ordinal() > bestEval.ordinal()) {
							bestEval = possEval;
							bestMove = new Move(toMove, col, row);
						}
					}
				}
			}
			return new MoveEval(bestMove, bestEval); 
		}
	}
	
	private Evaluation invertResult(Evaluation in) {
		if (in == Evaluation.DRAW) return Evaluation.DRAW;
		else if (in == Evaluation.WIN) return Evaluation.LOSS;
		return Evaluation.WIN;
	}
	
	private Pieces[][] copyBoard(Pieces[][] board) {
		Pieces[][] newboard = new Pieces[board.length][];
		for (int i = 0; i < board.length; i++) {
			newboard[i] = new Pieces[board[i].length];
			for (int j = 0; j < board[i].length; j++) {
				newboard[i][j] = board[i][j];
			}
		}
		
		
		
		return newboard;
	}
}
