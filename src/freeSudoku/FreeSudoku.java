/**
* Copyright 2005 Victor Ferrer
* 
* Copyright 2006, 2007 Brad Richards (http://richards.kri.ch/)
* 
* This file is part of FreeSudoku.
* 
* FreeSudoku is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
* 
* FreeSudoku is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with FreeSudoku; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA*
*/

package freeSudoku;

import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JPanel;

import freeSudoku.model.Game;
import freeSudoku.translation.MSG;
import freeSudoku.view.Board;

public class FreeSudoku {
	public static FreeSudoku inst = null;
	private Board board = null;
	private Game game = null;
	private java.util.Date startTime;
	private java.util.Date pauseStarted;
	private long totalPauseTime;

	public FreeSudoku(Object parentFrame) {
		FreeSudoku.inst = this;
		board = new Board(parentFrame);
		board.setVisible(true);
		game = new Game();
		pauseStarted = null;
		totalPauseTime = 0;
	}
	
	public JPanel getBoard() {
		return board;
	}

	public void posKeyTyped(int i, int j, int val) {
		if (!board.isConsistency() | game.isLegal(i, j, val)) {
			board.setPosValue(i, j, val);
			game.setPosVal(i, j, val);
			if (game.resolved())
				solved();
			if (val != 0 & board.isConsistency())
				board.checkConsistence(i, j);
		} else {
			board.setMessage(MSG.getString("gui.board.incorrectNumber"));
		}
	}

	public boolean posKeyTypedSmall(int i, int j, int val) {
		// TODO: check this and prev method
		if (!board.isConsistency() | game.isLegal(i, j, val)) {
			board.setPosValue(i, j, val);
			return true;
		}
		board.setMessage(MSG.getString("gui.board.incorrectNumber"));
		return false;
	}

	public String getPossibleVals(int i, int j, boolean blanks) {
		StringBuffer aux = new StringBuffer();
		ArrayList<Integer> al = game.getAllLegalVals(i, j);
		for (int k = 0; k < al.size(); k++) {
			if (blanks)
				aux.append(al.get(k) + " ");
			else
				aux.append(al.get(k));
		}

		return aux.toString().trim();
	}

	private void solved() {
		board.disableAll();
		java.util.Date endTime = new java.util.Date();
		int difficulty = game.getEstimatedDiff();
		long seconds = (endTime.getTime() - startTime.getTime() - totalPauseTime) / 1000;
		long score = ( difficulty * 10000) / seconds;
		long minutes = seconds / 60;
		seconds = seconds - minutes * 60;
		long hours = minutes / 60;
		minutes = minutes - hours * 60;
		String time = String.format("%1$2d:%2$02d:%3$02d", hours, minutes, seconds);
		board.setMessage(MSG.getString("gui.board.congratulations") + " " + score + " (difficulty " + difficulty + " in " + time + ")");
	}

	public void restart() {
		board.setWaitState();
		game.generateNewGame(board.getNivel(), board.isSymmetric());
		board.setVisible(true);
	}

	public void endWait() {
		board.setBoard(game.getGame(), game.getInitPos(), game.getEstimatedDiff());
		startTime = new java.util.Date();
	}

	public void restart(Properties storedGame) {
		game.setGame(storedGame.getProperty(CONSTS.INITPOS_PROP), storedGame.getProperty(CONSTS.ADDEDPOS_PROP));
		board.setBoard(game.getGame(), game.getInitPos(), game.getEstimatedDiff());
		board.setTypedSmallVals(storedGame.getProperty(CONSTS.POSIBLEPOS_PROP));
		startTime = new java.util.Date();
		board.setVisible(true);
	}

	public String partidaToString() {
		return game.toString();
	}

	public Properties getGameAndBoardState() {
		Properties result = game.getGameState();
		result.setProperty(CONSTS.POSIBLEPOS_PROP, board.getAllPosSmall());
		return result;
	}

	public void setProgress(int progress) {
		board.setProgress(progress);
	}
	
	public void startPause() {
		pauseStarted = new java.util.Date();
		CONSTS.log("pause started");
	}
	
	public void stopPause() {
		java.util.Date pauseStopped;
		long pauseTime;
		
		// If pauseStarted has a value, then we are resuming after a pause,
		// and need to deduct the pause-time from the accumulated time in
		// the game. If pauseStarted is null, then we are just starting up
		// for the first time (e.g., Applet.start has been called)
		if (pauseStarted != null) {
			pauseStopped = new java.util.Date();
			pauseTime = pauseStopped.getTime() - pauseStarted.getTime();
			totalPauseTime += pauseTime;
			pauseStarted = null;
			CONSTS.log("pause ended");
		}
	}
}
