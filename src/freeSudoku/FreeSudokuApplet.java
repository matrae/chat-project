/**
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

import java.awt.BorderLayout;

import javax.swing.JApplet;

public class FreeSudokuApplet extends JApplet {
	private static final long serialVersionUID = -6380974202967404501L;
	private FreeSudoku game;
	
	public FreeSudokuApplet() {
		//setTitle("Free Sudoku");
		//setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		//setResizable(false);
		game = new FreeSudoku(this);
		this.setLayout(new BorderLayout());
		this.add(game.getBoard(), BorderLayout.CENTER);
		this.resize(CONSTS.appWidth, CONSTS.appHeight);
	}
	
	public void start() {
		game.stopPause();
	}
	
	public void stop() {
		game.startPause();
	}
}
