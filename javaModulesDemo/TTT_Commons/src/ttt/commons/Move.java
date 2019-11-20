package ttt.commons;

public class Move {
	private Pieces piece;
	private int x;
	private int y;
	
	public Move(Pieces piece, int x, int y) {
		this.piece = piece;
		this.x = x;
		this.y = y;
	}
	
	public Pieces getPiece() {
		return piece;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
}
