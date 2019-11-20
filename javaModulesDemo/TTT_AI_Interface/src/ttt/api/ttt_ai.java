package ttt.api;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import ttt.commons.Move;
import ttt.commons.Pieces;

public interface ttt_ai {
	// The interface required for all AI players
	public String getName();
	public void setPiece(Pieces myPiece);
	public Move makeMove(Pieces[][] board);

	/**
	 * The static method used to discover available implementations on the module
	 * path
	 */
	static List<ttt_ai> getInstances() {
		ServiceLoader<ttt_ai> services = ServiceLoader.load(ttt_ai.class);
		List<ttt_ai> list = new ArrayList<>();
		services.iterator().forEachRemaining(list::add);
		return list;
	}
}
