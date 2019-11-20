package ttt.program;

import java.util.List;
import java.util.stream.Collectors;

import ttt.api.ttt_ai;
import ttt.commons.Move;
import ttt.commons.Pieces;
import ttt.commons.Result;

public class TTT_Model {
	private final Pieces[][] board = new Pieces[3][3];
	
	private final List<ttt_ai> AI_PlayerList;
	private ttt_ai AI_player;
	
	public TTT_Model() {
		AI_PlayerList = ttt_ai.getInstances();
		select_AI_player(0);
	}

	public List<String> get_AI_PlayerNames() {
		return AI_PlayerList.stream().map(ttt_ai::getName).collect(Collectors.toList());
	}
	
	public void select_AI_player(String name) {
		for (ttt_ai p : AI_PlayerList) {
			if (p.getName().equals(name)) {
				AI_player = p;
				AI_player.setPiece(Pieces.O);
			}
		}
	}
	
	public void select_AI_player(int pNum) {
		AI_player = AI_PlayerList.get(pNum);
		AI_player.setPiece(Pieces.O);
	}
	
	public Result getResult() {
		return Result.getResult(board);
	}
	
	public void makeMove(Move move) {
		if (move != null) board[move.getX()][move.getY()] = move.getPiece();
	}
	
	public Move makeAImove() {
		Move move = AI_player.makeMove(board);
		makeMove(move);
		return move;
	}
}
