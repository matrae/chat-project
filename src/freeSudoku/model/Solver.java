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
package freeSudoku.model;

import java.util.*;

import freeSudoku.CONSTS;

/**
 * @author gos
 * 
 */
public class Solver {

	private int[][] game = null;
	private int filled = 0;
	protected String strategiesUsed = null;
	protected int difficulty = 0;

	/**
	 * Comment for <code>possVals</code> For each i,j position in board contains
	 * an array of possible values: possVals[i][j][0] -> number of possible values
	 * possVals[i][j][n] -> 1 if n is a possible value where 1 <= n <= 9
	 */
	private int[][][] possVals = null;

	/**
	 * Comment for <code>groups</code> There are 27 groups of 9 cells; 9 rows +
	 * 9 columns + 9 3x3_blocks For each group we can apply the different
	 * strategies. Each position is just a pointer to a possVals element.
	 */
	private int[][][] groups = null;

	protected Solver() {
		game = new int[9][9];
		possVals = new int[9][9][10];
	}

	protected void setGame(int[][] pmatrix) {
		strategiesUsed = "";
		difficulty = 0;
		filled = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				game[i][j] = pmatrix[i][j];
				if (game[i][j] != 0)
					filled++;
			}
		}
		fillPossVals();
		setGroups();
		// CONSTS.log( " *** matrix initializated:\n" + matrixToString( game));
	}

	private void setGroups() {
		groups = new int[27][9][];
		int cont = 0;

		for (int n = 0; n < 9; n++) {
			// add rows
			for (int m = 0; m < 9; m++) {
				groups[cont][m] = possVals[n][m];
			}
			cont++;

			// add cols
			for (int m = 0; m < 9; m++) {
				groups[cont][m] = possVals[m][n];
			}
			cont++;
		}

		// add boxes
		for (int i = 0; i < 9; i += 3) {
			for (int j = 0; j < 9; j += 3) {
				groups[cont][0] = possVals[i][j];
				groups[cont][1] = possVals[i][j + 1];
				groups[cont][2] = possVals[i][j + 2];
				groups[cont][3] = possVals[i + 1][j];
				groups[cont][4] = possVals[i + 1][j + 1];
				groups[cont][5] = possVals[i + 1][j + 2];
				groups[cont][6] = possVals[i + 2][j];
				groups[cont][7] = possVals[i + 2][j + 1];
				groups[cont][8] = possVals[i + 2][j + 2];
				cont++;
			}
		}

	}

	protected static int[][] dupArray(int[][] pArray) {
		int[][] res = new int[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				res[i][j] = pArray[i][j];
			}
		}
		return res;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[][] aux = {

				// Multiline test: 
				{ 0, 0, 9, 0, 3, 0, 6, 0, 0 },
				{ 0, 3, 6, 0, 1, 4, 0, 8, 9 },
				{ 1, 0, 0, 8, 6, 9, 0, 3, 5 },
				{ 0, 9, 0, 0, 0, 0, 8, 0, 0 },
				{ 0, 1, 0, 0, 0, 0, 0, 9, 0 },
				{ 0, 6, 8, 0, 9, 0, 1, 7, 0 },
				{ 6, 0, 1, 9, 0, 3, 0, 0, 2 },
				{ 9, 7, 2, 6, 4, 0, 3, 0, 0 },
				{ 0, 0, 3, 0, 2, 0, 9, 0, 0 } };

				// Swordfish test: 
//				{ 1, 9, 5, 3, 6, 7, 2, 4, 8 },
//				{ 0, 7, 8, 0, 5, 0, 3, 6, 9 },
//				{ 3, 0, 6, 0, 9, 8, 1, 5, 7 },
//				{ 0, 0, 3, 7, 8, 0, 5, 9, 0 },
//				{ 7, 0, 9, 0, 0, 5, 0, 0, 6 },
//				{ 5, 8, 4, 9, 0, 6, 7, 1, 0 },
//				{ 8, 3, 2, 5, 4, 9, 6, 7, 1 },
//				{ 9, 0, 7, 0, 1, 3, 0, 2, 5 },
//				{ 0, 5, 1, 0, 7, 2, 9, 0, 0 } };
				
				// X-Wing test: Filled: 81, difficulty 88, strategies used: 4549111111111111111111111111
//				{ 9, 0, 0, 0, 5, 1, 7, 3, 0 },
//				{ 1, 0, 7, 3, 9, 8, 2, 0, 5 },
//				{ 5, 0, 0, 0, 7, 6, 0, 9, 1 },
//				{ 8, 1, 0, 7, 2, 4, 3, 5, 0 },
//				{ 2, 0, 0, 1, 6, 5, 0, 0, 7 },
//				{ 0, 7, 5, 9, 8, 3, 0, 1, 2 },
//				{ 0, 2, 1, 5, 3, 7, 0, 0, 0 },
//				{ 7, 5, 8, 6, 4, 9, 1, 2, 3 },
//				{ 3, 9, 0, 8, 1, 2, 5, 7, 0 } };

				// Result: Filled: 81, using strategies 1111111111111333333444421111111111111111111111111111111111111111111
//				{ 0, 9, 0, 5, 0, 0, 0, 0, 0 },
//				{ 3, 0, 0, 0, 2, 0, 4, 0, 0 },
//				{ 0, 0, 0, 9, 6, 0, 0, 0, 7 },
//				{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 1, 0, 0, 7, 0, 0, 0, 6 },
//				{ 0, 0, 0, 0, 0, 6, 5, 8, 0 },
//				{ 0, 7, 0, 0, 9, 0, 0, 1, 0 },
//				{ 0, 4, 5, 0, 0, 0, 0, 0, 9 },
//				{ 0, 0, 0, 0, 4, 0, 7, 0, 2 } };

				// Result: Filled: 35, using strategies 1111111113334734466
//				{ 0, 0, 6, 0, 0, 0, 0, 8, 0 },
//				{ 8, 9, 0, 0, 3, 0, 0, 4, 0 },
//				{ 0, 0, 0, 9, 0, 6, 0, 0, 2 },
//				{ 0, 0, 5, 3, 0, 0, 2, 0, 7 },
//				{ 0, 0, 0, 0, 0, 0, 0, 0, 0 },
//				{ 2, 0, 4, 0, 0, 1, 6, 0, 0 },
//				{ 5, 0, 0, 1, 0, 9, 0, 0, 0 },
//				{ 0, 6, 0, 0, 5, 0, 0, 2, 9 },
//				{ 0, 7, 0, 0, 0, 0, 4, 0, 0 } };

				// Result: Filled: 41, using strategies	11111111111113344663
//				{ 5, 0, 3, 1, 0, 0, 2, 0, 0 },
//				{ 0, 2, 0, 0, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 2, 5, 7, 0, 3, 0 },
//				{ 0, 0, 0, 0, 8, 1, 5, 4, 7 },
//				{ 7, 0, 0, 0, 0, 3, 0, 0, 0 },
//				{ 0, 6, 0, 7, 0, 0, 8, 0, 0 },
//				{ 9, 8, 0, 0, 1, 6, 0, 0, 4 },
//				{ 0, 0, 0, 9, 3, 4, 0, 0, 0 },
//				{ 0, 0, 6, 0, 0, 0, 0, 0, 0 } };

				// Result: Filled: 81, using strategies 11123336611213112211111111111111111111111111111111111111111
//				{ 0, 0, 3, 0, 8, 2, 1, 5, 0 },
//				{ 0, 0, 0, 7, 0, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 4, 0, 9, 0, 6 },
//				{ 1, 0, 5, 0, 0, 0, 0, 0, 9 },
//				{ 0, 0, 4, 2, 0, 7, 5, 0, 0 },
//				{ 3, 0, 0, 0, 0, 0, 6, 0, 2 },
//				{ 5, 0, 1, 0, 3, 0, 0, 0, 0 },
//				{ 0, 0, 0, 0, 0, 1, 0, 0, 0 },
//				{ 0, 9, 6, 8, 2, 0, 3, 0, 0 } };

				// Result: Filled: 81, using strategies	1111123351111111111111111111111111111111111111111111111111

//				{ 0, 0, 0, 0, 1, 2, 0, 0, 0 },
//				{ 0, 0, 0, 5, 0, 4, 8, 0, 6 },
//				{ 0, 0, 3, 0, 0, 0, 0, 0, 7 },
//				{ 0, 8, 1, 0, 0, 0, 3, 0, 0 },
//				{ 6, 0, 7, 0, 0, 0, 4, 0, 1 },
//				{ 0, 0, 4, 0, 0, 0, 7, 2, 0 },
//				{ 9, 0, 0, 0, 0, 0, 6, 0, 0 },
//				{ 4, 0, 6, 9, 0, 5, 0, 0, 0 },
//				{ 0, 0, 0, 4, 8, 0, 0, 0, 0 } };
		
		Solver solver = new Solver();
		solver.setGame(aux);
		CONSTS.log(solver.possibleValsToString());
		solver.loopAllStg();
		CONSTS.log(solver.toString());
	}

	/**
	 * Returns a string-representation of a game-matrix
	 * 
	 * @param pArray the matrix to be represented
	 * @return the string representation of the matrix
	 */
	public static String matrixToString(int[][] pArray) {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				res.append(" " + pArray[i][j]);
			}
			res.append("\n");
		}
		return res.toString();
	}

	/**
	 * When a value has been discovered, this method sets
	 * the value in the game matrix. It also removes all
	 * corresponding possible values in the row, column
	 * and box.
	 * 
	 * @param i the row of the value
	 * @param j the column of the value
	 * @param val the value
	 */
	protected void setVal(int i, int j, int val) {
		if (game[i][j] != 0 || val <= 0)
			throw new RuntimeException("Cant setVal " + val);
		game[i][j] = val;
		filled++;

		Arrays.fill(possVals[i][j], 0); // remove all possible vals
		possVals[i][j][0] = -val;
		removeNumFromRow(val, i, -1); // remove from all 3 boxes
		removeNumFromCol(val, j, -1); // remove from all 3 boxes
		removeNumFromBox(val, i, j, -1, -1);

		// CONSTS.log("setVal " + i + " " + j + " -> " + val + " filled: " + filled);
}

	/**
	 * Record the strategy used to make the last discovery, and adjust the difficulty
	 * level of this puzzle. The list of strategies used shows the order of our
	 * discoveries, and is really useful only for debugging. The difficulty is
	 * derived from the strategies that we use.
	 * 
	 * @param diff - the strategy just applied
	 */
	private void logStrategy(int diff) {
		// Add strategy to list (this is a string)
		if (diff < 10) 
			strategiesUsed += diff;
		else if (diff == 10)
			strategiesUsed += 'a';
		else
			strategiesUsed += 'z';
		
		// Adapt the difficulty - this is rather arbitrary...
		// Strategy 1 is trivial, then in order of increasing sophistication:
		// 2, (3,4), (5,6), (7,8), (9, 10), 11, 12
		if (diff == 2)
			difficulty += 1;
		else if (diff == 3 | diff == 4)
			difficulty += 2;
		else if (diff == 5 | diff == 6)
			difficulty += 8;
		else if (diff == 7 | diff == 8)
			difficulty += 16;
		else if (diff == 9 | diff == 10)
			difficulty += 64;
		else if (diff == 11 )
			difficulty += 128;
		else if (diff == 12 )
			difficulty += 256;
		
		// CONSTS.log("*** Strategy " + diff + " appl. Difficulty: " + difficulty +
		// " filled " + filled + " - "+ strategiesUsed );
	}

	/**
	 * Strategy 1: look for places where one of the possible values is unique.
	 * For example, if this the only place in a row where a "3" appears as a
	 * possible value, then this square must be a 3.
	 * 
	 * We check rows, columns and boxes. The method ends as soon as anything is
	 * found.
	 *  
	 * @return true if we set a value, false if we cannot find anything.
	 */
	private boolean applyStrategy_1() {
		boolean valueSet = false;
		for (int i = 0; i < 9 & !valueSet; i++) { // for each row
			for (int j = 0; j < 9 & !valueSet; j++) { // for each column
				if (game[i][j] == 0) { // if no value yet for this square 
					for (int n = 1; n <= 9 & !valueSet; n++) { // check each possible value
						if (possVals[i][j][n] == 1) { // if a possible value for this square
							boolean found;
							
							// check block - does this possible value appear anywhere else?
							found = false;
							for (int x = i - i % 3; x < (i - i % 3 + 3) & !found; x++) {
								for (int y = j - j % 3; y < (j - j % 3 + 3) & !found; y++) {
									if (!(x == i & y == j)) { // don't compare to ourselves
										if (possVals[x][y][n] == 1) {
											found = true;
										}
									}
								}
							}
							
							if (found) { // wasn't unique in the block, so check the row
								found = false;
								for (int x = 0; x < 9 & !found; x++) {
									if (x != j) { // don't compare to ourselves
										if (possVals[i][x][n] == 1) {
											found = true;
										}
									}
								}
							}
		
							if (found) { // wasn't unique in the row, so check the column
								found = false;
								for (int x = 0; x < 9 & !found; x++) {
									if (x != i) { // don't compare to ourselves
										if (possVals[x][j][n] == 1) {
											found = true;
										}
									}
								}								
							}		
							
							if (!found) { // the value is unique in a box, row or column
								setVal(i, j, n);
								logStrategy(1);
								valueSet = true;
							}

						} // if possVals[i][j][n] == 1
					} // for n
				} // if game[i][j] == 0
			} // for j
		} // for i
		return valueSet;
	}

	/**
	 * Look for a square that has only one possible value.
	 * The method ends as soon as anything is found.
	 * 
	 * @return true if we set a value, false if we cannot find anything.
	 */
	private boolean applyStrategy_2() {
		for (int i = 0; i < 9; i++) { // for each row
			for (int j = 0; j < 9; j++) { // for each column
				if (game[i][j] == 0) { // if no value is set
					if (possVals[i][j][0] == 1) { // and there is only one possible
						for (int k = 1; k <= 9; k++) { // find the possible value
							if (possVals[i][j][k] == 1) {
								setVal(i, j, k);
								logStrategy(2);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Within a block: if a possible value appears only in a single row or
	 * a single column, then the value can be eliminated from the line or
	 * column in all neighbouring blocks.
	 * 
	 * Note that this method never sets a final value; it only eliminates
	 * possible values.
	 * 
	 * This method is written to be readable, not clever - hence the
	 * repetitive if-statements
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_3_locked_candidates_1() {
		boolean possValsChanged = false;
		for (int num = 1; num <= 9; num++) {

			// check rows - is this possible value in one row, but not in the others?
			for (int row = 0; row < 9 & !possValsChanged; row += 3) {
				for (int col = 0; col < 9 & !possValsChanged; col += 3) {
					int changeRow = -1;
					if ((possVals[row][col + 0][num] == 1 || possVals[row][col + 1][num] == 1 || possVals[row][col + 2][num] == 1)
							&& (possVals[row + 1][col + 0][num] != 1 && possVals[row + 1][col + 1][num] != 1 && possVals[row + 1][col + 2][num] != 1
							&& possVals[row + 2][col + 0][num] != 1 && possVals[row + 2][col + 1][num] != 1 && possVals[row + 2][col + 2][num] != 1)) {
						changeRow = row;
					} else if ((possVals[row + 1][col + 0][num] == 1 || possVals[row + 1][col + 1][num] == 1 || possVals[row + 1][col + 2][num] == 1)
							&& (possVals[row][col + 0][num] != 1	&& possVals[row][col + 1][num] != 1 && possVals[row][col + 2][num] != 1
							&& possVals[row + 2][col + 0][num] != 1 && possVals[row + 2][col + 1][num] != 1 && possVals[row + 2][col + 2][num] != 1)) {
						changeRow = row + 1;
					} else if ((possVals[row + 2][col + 0][num] == 1 || possVals[row + 2][col + 1][num] == 1 || possVals[row + 2][col + 2][num] == 1)
							&& (possVals[row][col + 0][num] != 1 && possVals[row][col + 1][num] != 1 && possVals[row][col + 2][num] != 1
							&& possVals[row + 1][col + 0][num] != 1 && possVals[row + 1][col + 1][num] != 1 && possVals[row + 1][col + 2][num] != 1)) {
						changeRow = row + 2;
					}
					// did we find something to try?
					if (changeRow >= 0) {
						possValsChanged = removeNumFromRow(num, changeRow, col);
						if (possValsChanged) {
							logStrategy(3);
							return true;
						}
					}					
				} // for col
			} // for row

			// check cols - is this possible value in one column, but not in the others?
			for (int col = 0; col < 9; col += 3) {
				for (int row = 0; row < 9; row += 3) {
					int changeCol = -1;
					if ((possVals[row + 0][col][num] == 1 || possVals[row + 1][col][num] == 1 || possVals[row + 2][col][num] == 1)
							&& (possVals[row + 0][col + 1][num] != 1 && possVals[row + 1][col + 1][num] != 1 && possVals[row + 2][col + 1][num] != 1
							&& possVals[row + 0][col + 2][num] != 1 && possVals[row + 1][col + 2][num] != 1 && possVals[row + 2][col + 2][num] != 1)) {
						changeCol = col;
					} else if ((possVals[row + 0][col + 1][num] == 1 || possVals[row + 1][col + 1][num] == 1 || possVals[row + 2][col + 1][num] == 1)
							&& (possVals[row + 0][col + 0][num] != 1 &&	possVals[row + 1][col + 0][num] != 1 && possVals[row + 2][col + 0][num] != 1
							&& possVals[row + 0][col + 2][num] != 1 && possVals[row + 1][col + 2][num] != 1 && possVals[row + 2][col + 2][num] != 1)) {
						changeCol = col + 1;
					} else if ((possVals[row + 0][col + 2][num] == 1 || possVals[row + 1][col + 2][num] == 1 || possVals[row + 2][col + 2][num] == 1)
							&& (possVals[row + 0][col + 1][num] != 1 &&	possVals[row + 1][col + 1][num] != 1 && possVals[row + 2][col + 1][num] != 1
							&& possVals[row + 0][col + 0][num] != 1 && possVals[row + 1][col + 0][num] != 1 && possVals[row + 2][col + 0][num] != 1)) {
						changeCol = col + 2;
					}
					if (changeCol >= 0) {
						possValsChanged = removeNumFromCol(num, changeCol, row);
						if (possValsChanged) {
							logStrategy(3);
							return true;
						}
					}
				} // for row
			} // for col
		} // for num
		return false;
	}

	/**
	 * If a number appears in only a single box within a line, then the number
	 * cannot appear on other lines within that box. This is followed by the
	 * same check for columns.
	 * 
	 * Note that this method never sets a final value; it only eliminates
	 * possible values.
	 * 
	 * This method is written to be readable, not clever - hence the
	 * repetitive if-statements
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_4_locked_candidates_2() {
		boolean possValsChanged = false;
		for (int num = 1; num <= 9; num++) {

			// check rows
			for (int row = 0; row < 9; row++) {
				int changeCol = -1;
				if ((possVals[row][0][num] == 1 || possVals[row][1][num] == 1 || possVals[row][2][num] == 1)
						&& (possVals[row][3][num] != 1 && possVals[row][4][num] != 1 && possVals[row][5][num] != 1
						&& possVals[row][6][num] != 1 && possVals[row][7][num] != 1 && possVals[row][8][num] != 1)) {
					changeCol = 0;
				} else if ((possVals[row][3][num] == 1 || possVals[row][4][num] == 1 || possVals[row][5][num] == 1)
						&& (possVals[row][0][num] != 1 && possVals[row][1][num] != 1 && possVals[row][2][num] != 1
						&& possVals[row][6][num] != 1 && possVals[row][7][num] != 1 && possVals[row][8][num] != 1)) {
					changeCol = 3;
				} else if ((possVals[row][6][num] == 1 || possVals[row][7][num] == 1 || possVals[row][8][num] == 1)
						&& (possVals[row][0][num] != 1 && possVals[row][1][num] != 1 && possVals[row][2][num] != 1
						&& possVals[row][3][num] != 1 && possVals[row][4][num] != 1 && possVals[row][5][num] != 1)) {
					changeCol = 6;
				}
				if (changeCol >= 0) {
					possValsChanged = removeNumFromBox(num, row, changeCol, row, -1);
					if (possValsChanged) {
						logStrategy(4);
						return true;
					}
				}
			}

			// check cols
			for (int col = 0; col < 9; col++) {
				int changeRow = -1;
				if ((possVals[0][col][num] == 1 || possVals[1][col][num] == 1 || possVals[2][col][num] == 1)
						&& (possVals[3][col][num] != 1 && possVals[4][col][num] != 1 && possVals[5][col][num] != 1
						&& possVals[6][col][num] != 1 && possVals[7][col][num] != 1 && possVals[8][col][num] != 1)) {
					changeRow = 0;
				} else if ((possVals[3][col][num] == 1 || possVals[4][col][num] == 1 || possVals[5][col][num] == 1)
						&& (possVals[0][col][num] != 1 && possVals[1][col][num] != 1 && possVals[2][col][num] != 1
								&& possVals[6][col][num] != 1 && possVals[7][col][num] != 1 && possVals[8][col][num] != 1)) {
					changeRow = 3;
				} else if ((possVals[6][col][num] == 1 || possVals[7][col][num] == 1 || possVals[8][col][num] == 1)
						&& (possVals[0][col][num] != 1 && possVals[1][col][num] != 1 && possVals[2][col][num] != 1
								&& possVals[3][col][num] != 1 && possVals[4][col][num] != 1 && possVals[5][col][num] != 1)) {
					changeRow = 6;
				}
				if (changeRow >= 0) {
					possValsChanged = removeNumFromBox(num, changeRow, col, -1, col);
					if (possValsChanged) {
						logStrategy(4);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Look for a situation where a definite value appears nowhere in
	 * three adjacent rows or columns (three which form the same three
	 * blocks). Take the situation where we have three columns:
	 * 
	 * Suppose the possible value appears in two of the blocks, in the
	 * same two columns but not in the third. In this case, the definite
	 * value in the third block must be in the third column. Which means
	 * that possible values in the other two columns can be removed from
	 * the third block.
	 * 
	 * See http://www.sudokuoftheday.com/pages/techniques-5.php
	 * Note that this subsumes technique-4 described at the same site.
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_5_multiline() {
		boolean possibleValsChanged = false;

		// We do not bother to check that the definite value does not
		// appear. If it does appear, then we will not have enough
		// possible values to trigger any change using this strategy.
		//
		// We search rows and columns interleaved, by searching groups
		// 0 through 17 - this means group numbers {0,2.4), (6,8,10),
		// {12,14,16}, {1,3,5}, {7,9,11}, 13,15,17}.
		
		// We track which groups the possible value appears
		// in for each block by using the following boolean arrays.
		boolean[] Block1_inGroup = new boolean[3];
		boolean[] Block2_inGroup = new boolean[3];
		boolean[] Block3_inGroup = new boolean[3];
		
		for (int possVal = 1; possVal <= 9 & !possibleValsChanged; possVal++) {
			for (int g1 = 0; g1 <= 1 & !possibleValsChanged; g1++) {
				for (int g2 = 0; g2 <= 12 & !possibleValsChanged; g2 += 6) {
					int firstGroup = g1 + g2;
					for (int i = 0; i <= 2; i++) {
						Block1_inGroup[i] = false;
						Block2_inGroup[i] = false;
						Block3_inGroup[i] = false;
					}
					
					// For the three selected groups, see where we find possVal
					for (int i = 0; i <= 2; i++) {
						int group = firstGroup + i*2;
						for (int j = 0; j < 9 ; j++) {
							if (groups[group][j][possVal] == 1) {
								if (j < 3) {
									Block1_inGroup[i] = true;
								} else if (j < 6) {
									Block2_inGroup[i] = true;
								} else {
									Block3_inGroup[i] = true;
								}
							}
						}
					}
					
					// See if two of the three have possVal in the same two columns
					// Note that, if this strategy applies at all, there can be only
					// one possible combination.
					int valBlock1 = multilineValue(Block1_inGroup);
					int valBlock2 = multilineValue(Block2_inGroup);
					int valBlock3 = multilineValue(Block3_inGroup);
					if (valBlock1 == valBlock2) {
						// match blocks 1 and 2 - check if 2 columns
						if ( (valBlock1 == 3) | (valBlock1 == 5) | (valBlock1 == 6) ) {
							possibleValsChanged = multilineFix(2, possVal, Block1_inGroup, groups[firstGroup], groups[firstGroup+2], groups[firstGroup+4]);
						}
					} else if (valBlock1 == valBlock3) {
						// match blocks 1 and 3 - check if 2 columns
						if ( (valBlock1 == 3) | (valBlock1 == 5) | (valBlock1 == 6) ) {
							possibleValsChanged = multilineFix(1, possVal, Block1_inGroup, groups[firstGroup], groups[firstGroup+2], groups[firstGroup+4]);
						}
					} else if (valBlock2 == valBlock3) {
						// match blocks 2 and 3 - check if 2 columns
						if ( (valBlock2 == 3) | (valBlock2 == 5) | (valBlock2 == 6) ) {
							possibleValsChanged = multilineFix(0, possVal, Block2_inGroup, groups[firstGroup], groups[firstGroup+2], groups[firstGroup+4]);
						}
					}
					if (possibleValsChanged) {
						logStrategy(5);
					}
				}				
			}
		}
		
		return possibleValsChanged;
	}
	
	// combine the inGroups array to produce an integer - the usual
	// powers-of-2 thing
	private int multilineValue(boolean[] inGroups) {
		int result = 0;
		for (int i = 0; i < inGroups.length; i++) {
			result *= 2;
			if (inGroups[i]) result += 1;
		}
		return result;
	}
	
	// For the given block (0-2), remove all occurrences of possVal from
	// the given two groups
	private boolean multilineFix(int block, int possVal, boolean[] inGroups, int[][] group1, int[][] group2, int[][] group3) {
		boolean changeMade = false;
		for (int i = block*3; i < (block*3+3); i++) {
			if (inGroups[0] && group1[i][possVal] == 1) {
				group1[i][possVal] = 0;
				group1[i][0]--;
				changeMade = true;
			}
			if (inGroups[1]&& group2[i][possVal] == 1) {
				group2[i][possVal] = 0;
				group2[i][0]--;
				changeMade = true;
			}
			if (inGroups[2]&& group3[i][possVal] == 1) {
				group3[i][possVal] = 0;
				group3[i][0]--;
				changeMade = true;
			}
		}
		return changeMade;
	}
	
	/**
	 * Look for two identical pairs of possible values within a group.
	 * If found, then neither of these values can appear elsewhere in
	 * the same group.
	 * 
	 * Note that this method never sets a final value; it only eliminates
	 * possible values.
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_6_naked_pairs() {
		boolean possibleValsChanged = false;

		for (int n = 0; n < 27 & !possibleValsChanged; n++) { // for each of the 27 groups
			possibleValsChanged = checkGroupForNakedPairs(groups[n]);
		}
		if (possibleValsChanged) {
			logStrategy(6);
		}
		return possibleValsChanged;
	}

	private boolean checkGroupForNakedPairs(int[][] group) {
		boolean possibleValsChanged = false;

		for (int n = 0; n < 8; n++) { // for each square in the group
			if (group[n][0] == 2) { // if there are exactly two possible values
				for (int m = n + 1; m < 9; m++) { // compare to remaining squares
					if (group[m][0] == 2) { // if also two possible values here
						boolean pairFound = true;
						int val1 = 0;
						int val2 = 0;
						
						// all possible values must be the same for m and n
						// also save the pair of values in val1/val2, so that
						// we can remove them from other squares
						for (int k = 1; k <= 9 & pairFound; k++) {
							if (group[n][k] != group[m][k]) {
								pairFound = false;
							} else if (group[n][k] == 1) {
								if (val1 == 0)
									val1 = k;
								else
									val2 = k;
							}
						}
						
						if (pairFound) { // remove pair-values from other cells
							for (int x = 0; x < group.length; x++) {
								if (x != n && x != m) {
									if (group[x][val1] == 1) {
										group[x][val1] = 0;
										group[x][0]--;
										possibleValsChanged = true;
									}
									if (group[x][val2] == 1) {
										group[x][val2] = 0;
										group[x][0]--;
										possibleValsChanged = true;
									}
								}
							}
						}
					}
				}
			}
		}
		return possibleValsChanged;
	}	
	
	/**
	 * Look for two identical pairs of possible values within a group,
	 * neither of which appear in any other square in the group. The
	 * pair may be "hidden" by the presence of other possible values
	 * in the squares.
	 * 
	 * If found, then remove any other possible values from these two
	 * squares.
	 * 
	 * Note that this method never sets a final value; it only eliminates
	 * possible values.
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_7_hidden_pairs() {
		boolean possibleValsChanged = false;

		for (int n = 0; n < 27 & !possibleValsChanged; n++) { // for each of the 27 groups
			possibleValsChanged = checkGroupForHiddenPairs(groups[n]);
		}
		if (possibleValsChanged) {
			logStrategy(7);
		}
		return possibleValsChanged;
	}

	private boolean checkGroupForHiddenPairs(int[][] group) {
		// iterate through all possible pairs val1/val2
		for (int val1 = 1; val1 <= 8; val1++) {
			for (int val2 = val1+1; val2 <= 9; val2++) {
				for (int n = 0; n < 8; n++) { // for each square in the group
					if (group[n][0] >= 2 // if at least these two possible values
							&& (group[n][val1] == 1) & (group[n][val2] == 1) ) {
						for (int m = n + 1; m < 9; m++) { // compare to remaining squares
							if (group[m][0] >= 2
									&& (group[m][val1] == 1) & (group[m][val2] == 1) ) { // if at least these two possible values
								boolean pairFound = true;
								
								// The values cannot exist for any other square in the group
								for (int x = 0; x < 9 & pairFound; x++) {
									if (x != n & x != m) {
										if (group[x][val1] == 1 || group[x][val2] == 1) {
											pairFound = false;
										}										
									}
								}
								
								if (pairFound) { // remove other possible values from these squares
									// CONSTS.log("HIDDEN PAIR found: " + val1 + " " + val2);
									if (group[n][0] > 2 || group[m][0] > 2) {
										Arrays.fill(group[n], 0); // clear array
										group[n][val1] = 1;
										group[n][val2] = 1;
										group[n][0] = 2;
										Arrays.fill(group[m], 0); // clear array
										group[m][val1] = 1;
										group[m][val2] = 1;
										group[m][0] = 2;
										// CONSTS.log("HIDDEN PAIR. Pos val removed!");
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Look for sets of three squares that each contain two or three of a
	 * set of three values (e.g.: 46, 48, 468). If found, then none of these
	 * values can appear elsewhere in the same group.
	 * 
	 * Note that this method never sets a final value; it only eliminates
	 * possible values.
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_8_naked_triples() {
		boolean possibleValsChanged = false;

		for (int n = 0; n < 27 & !possibleValsChanged; n++) {
			possibleValsChanged = checkGroupForNakedTriples(groups[n]);
		}
		if (possibleValsChanged) {
			logStrategy(8);
		}
		return possibleValsChanged;
	}

	private boolean checkGroupForNakedTriples(int[][] group) {
		boolean possibleValsChanged = false;

		ArrayList<int[]> triples = getAllTriples(group);

		int[] triple;
		int[] found = new int[3];
		int f = 0;
		for (int t = 0; t < triples.size(); t++) {
			triple = (int[]) triples.get(t);
			f = 0;
			for (int cell = 0; cell < 9; cell++) {
				if (group[cell][0] <= 3) { // either 2 or 3
					// Check that the only values present are members of the triple
					if (group[cell][triple[0]] + group[cell][triple[1]] + group[cell][triple[2]] == group[cell][0]) {
						found[f++] = cell;
						if (f == 3)	break; // optimization - can never be more than 3
					}
				}
			}

			if (f == 3) { // naked triple found
				// CONSTS.log("Naked triple found: " + triple[0] + triple[1] + triple[2]);
				for (int cell2 = 0; cell2 < 9; cell2++) { // remove triple-values from other cells
					if (cell2 != found[0] & cell2 != found[1] & cell2 != found[2]) {
						if (group[cell2][triple[0]] == 1 || group[cell2][triple[1]] == 1 || group[cell2][triple[2]] == 1) {
							group[cell2][0] -= group[cell2][triple[0]] + group[cell2][triple[1]] + group[cell2][triple[2]];
							group[cell2][triple[0]] = group[cell2][triple[1]] = group[cell2][triple[2]] = 0;
							possibleValsChanged = true;
						}
					}
				}
			}
		}
		return possibleValsChanged;
	}

	private ArrayList<int[]> getAllTriples(int[][] group) {
		ArrayList<int[]> res = new ArrayList<int[]>();

		// Find all possible-values used in the entire group
		int[] numbers = new int[9];
		int count = 0;
		for (int n = 1; n <= 9; n++) {
			for (int i = 0; i < 9; i++) {
				if (group[i][n] == 1) {
					numbers[count++] = n;
					break;
				}
			}
		}

		// Find all possible triples with these possible-values
		int[] aux = null;
		for (int t1 = 0; t1 < count; t1++) {
			for (int t2 = t1 + 1; t2 < count; t2++) {
				for (int t3 = t2 + 1; t3 < count; t3++) {
					aux = new int[3];
					aux[0] = numbers[t1];
					aux[1] = numbers[t2];
					aux[2] = numbers[t3];
					res.add(aux);
				}
			}
		}
		return res;
	}

	/**
	 * Look for triplets of possible values within a group, none of
	 * which appear in any other square in the group. The triplet may
	 * be "hidden" by the presence of other possible values in the squares.
	 * 
	 * If found, then remove any other possible values from these three
	 * squares.
	 * 
	 * Note that this method never sets a final value; it only eliminates
	 * possible values.
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_9_hidden_triples() {
		boolean possibleValsChanged = false;

		for (int n = 0; n < 27 & !possibleValsChanged; n++) {
			possibleValsChanged = checkGroupForHiddenTriples(groups[n]);
		}
		if (possibleValsChanged) {
			logStrategy(9);
		}
		return possibleValsChanged;
	}

	private boolean checkGroupForHiddenTriples(int[][] group) {
		boolean possibleValsChanged = false;
		boolean hiddenTripleFound = false;
		ArrayList<int[]> triples = getAllTriples(group);
		int[] triple = null;
		int[] found = new int[3];
		int f = 0;

		for (int t = 0; t < triples.size(); t++) {
			hiddenTripleFound = false;
			triple = (int[]) triples.get(t);
			f = 0;
			for (int cell = 0; cell < 9; cell++) {
				if (group[cell][triple[0]] + group[cell][triple[1]] + group[cell][triple[2]] >= 1) {
					found[f++] = cell;
				}
				if (f == 3)
					break; // possible triple found
			}

			if (f == 3) // we have to check that the triple is not in any other cell
			{
				hiddenTripleFound = true;
				for (int cell2 = 0; cell2 < 9; cell2++) {
					if (cell2 == found[0] || cell2 == found[1] || cell2 == found[2])
						continue;
					if (group[cell2][triple[0]] == 1 || group[cell2][triple[1]] == 1 || group[cell2][triple[2]] == 1) {
						// is not a hidden triple
						hiddenTripleFound = false;
						break;
					}
				}
			}

			if (hiddenTripleFound) { // hidden triple found -> remove other pos vals from these 3 cells
				// CONSTS.log("********* Hidden triple found: " + triple[0] + triple[1] + triple[2]);
				for (int n = 1; n <= 9; n++) {
					if (triple[0] != n & triple[1] != n & triple[2] != n) {
						for (int fnd = 0; fnd < 3; fnd++) {
							if (group[found[fnd]][n] == 1) {
								// CONSTS.log("Hidden triple. Remove number: " + n);
								group[found[fnd]][0]--;
								group[found[fnd]][n] = 0;
								possibleValsChanged = true;
							}
						}
					}
				}
			}
		} // for t
		return possibleValsChanged;
	}
	
	/**
	 * Look for x-wings. These are groups of four cells forming a
	 * rectangle, in which the same number is possible in all four
	 * cells, but nowhere else in the row. If such a pattern is found,
	 * then this number can be removed as a possible value for all other
	 * cells in the two columns.
	 * 
	 * The same check can be done for columns, eliminating candidates
	 * in rows.
	 * 
	 * Note that this method never sets a final value; it only eliminates
	 * possible values.
	 * 
	 * @return true if we found something, false otherwise
	 */
	private boolean applyStrategy_10_x_wing() {
		boolean possValsChanged = false;
		// For each possible value
		for (int possVal = 1; possVal <= 9 & !possValsChanged; possVal++) {
			
			// Check for horizontal X-Wings

			// Find a row where this possible value occurs exactly twice
			for (int row1 = 0; row1 < 8 & !possValsChanged; row1++) {
				int[] row1cols = findPairInGroup(groups[row1*2], possVal);				
				if (row1cols != null) {
					// Find a second row where this possible value occurs exactly twice
					for (int row2 = row1+1; row2 < 9 & !possValsChanged; row2++) {
						int[] row2cols = findPairInGroup(groups[row2*2], possVal);
						if (row2cols != null) {
							// we have two candidate rows - the columns must be identical
							// if the columns are the same, we have an x-wing!
							if ( (row1cols[0] == row2cols[0]) & (row1cols[1] == row2cols[1]) ) {
								//CONSTS.log("Horizontal X-wing on " + possVal + " rows " + row1 + "/" + row2 + ", cols " + row1cols[0] + "/" + row1cols[1]);								
								int[] rows = new int[] { row1, row2 };
								possValsChanged = removeNumFromCol(possVal, row1cols[0], rows);
								possValsChanged = possValsChanged | removeNumFromCol(possVal, row1cols[1], rows);
							}
						}
					}
				}
			}
			
			// check for vertical X-Wings
			if (!possValsChanged) {
				// Find a column where this possible value occurs exactly twice
				for (int col1 = 0; col1 < 8 & !possValsChanged; col1++) {
					int[] col1rows = findPairInGroup(groups[col1*2+1], possVal);				
					if (col1rows != null) {
						// Find a second column where this possible value occurs exactly twice
						for (int col2 = col1+1; col2 < 9 & !possValsChanged; col2++) {
							int[] col2rows = findPairInGroup(groups[col2*2+1], possVal);
							if (col2rows != null) {
								// we have two candidate columns - the rows must be identical
								// if the rows are the same, we have an x-wing!
								if ( (col1rows[0] == col2rows[0]) & (col1rows[1] == col2rows[1]) ) {
									//CONSTS.log("Vertical X-wing on " + possVal + " cols " + col1 + "/" + col2 + ", rows " + col1rows[0] + "/" + col1rows[1]);								
									int[] cols = new int[] { col1, col2 };
									possValsChanged = removeNumFromRow(possVal, col1rows[0], cols);
									possValsChanged = possValsChanged | removeNumFromRow(possVal, col1rows[1], cols);
								}
							}
						}
					}
				}
			}
		}	
		if (possValsChanged) {
			logStrategy(10);
		}
		return possValsChanged;
	}
	
	/** 
	 * Find the same possible value in exactly two locations within a group
	 * If the value occurs too few or two many times, return null
	 * @param Group - the group to search
	 * @param possVal - the possible-Value to find
	 * @return an integer array with two entries: the locations of the values (0-based),
	 * or null if no pair was found
	 */
	private int[] findPairInGroup(int[][] group, int possVal) {
		boolean havePair = false;
		int loc1 = -1;
		int loc2 = -1;
		
		// look through all cols, and hope to find exactly 2 occurrences...
		for (int loc = 0; loc < 9; loc++) {
			if (group[loc][possVal] == 1) {
				if (loc1 == -1) {
					loc1 = loc;
				} else if (loc2 == -1) {
					loc2 = loc;
					havePair = true;
				} else { // too many
					havePair = false;
				}
			}
		}				
		if (havePair) {
			return new int[] {loc1, loc2};
		} else {
			return null;
		}
	}
	
	
	
	
	private boolean applyStrategy_11_swordfish() {
		return false;
	}
	
	private boolean applyStrategy_12_forcing_chains() {
		return false;
	}
	
	/**
	 * We loop through the strategies, trying them in order of difficulty.
	 * Each strategy exits as soon as it has taken a single action, so that
	 * we can begin again with the simplest strategies. This is not efficient,
	 * but it allows us to apply the advanced strategies as few times as
	 * possible, which lets us measure the difficulty of the puzzle.
	 *
	 */
	protected void loopAllStg() {
		boolean changed = true;
		while (changed && filled < 81) {
			changed = true;
			if (!applyStrategy_1())
				if (!applyStrategy_2())
					if (!applyStrategy_3_locked_candidates_1())
						if (!applyStrategy_4_locked_candidates_2())
							if (!applyStrategy_5_multiline())
								if (!applyStrategy_6_naked_pairs())
									if (!applyStrategy_7_hidden_pairs())
										if (!applyStrategy_8_naked_triples())
											if (!applyStrategy_9_hidden_triples())
												if (!applyStrategy_10_x_wing())
													if (!applyStrategy_11_swordfish())
														if (!applyStrategy_12_forcing_chains())
															changed = false;
		}
		if (filled < 81)
			difficulty = -10000;
		
		//CONSTS.log("Filled: " + filled + ", difficulty " + difficulty + ", strategies used: " + strategiesUsed);

		//CONSTS.log(possibleValsToString());
	}
	
	// --- Various utility routines ---
	
	/**
	 * Remove a given number from the possible-values in an entire row,
	 * with the exception of the block containing the specified column
	 * 
	 * @param num - number to remove
	 * @param row - row to remove from
	 * @param except - column to leave as-is (-1 for no exception)
	 * @return true if any change was made
	 */
	private boolean removeNumFromRow(int num, int row, int except) {
		boolean possValsChanged = false;
		// CONSTS.log("removeNum " + num + " FromRow " + row);
		for (int n = 0; n < 9; n++) {
			if ((n / 3) * 3 != except) {// skip numbers in the same block
				if (possVals[row][n][num] == 1) {
					possVals[row][n][num] = 0;
					possVals[row][n][0]--;
					possValsChanged = true;
				}
			}
			// CONSTS.log("remain: " + possVals[row][n]);
		}
		return possValsChanged;
	}

	/**
	 * Remove a given number from the possible-values in an entire row,
	 * with the exception of the specified exceptions
	 * 
	 * @param num - number to remove
	 * @param row - row to remove from
	 * @param exception - colums to leave as-is
	 * @return true if any change was made
	 */
	private boolean removeNumFromRow(int num, int row, int[] except) {
		boolean possValsChanged = false;
		for (int n = 0; n < 9; n++) {
			boolean exception = false;
			for (int e = 0; e < except.length; e++) {
				if ( n == except[e] ) exception = true;
			}
			if (!exception) {
				if (possVals[row][n][num] == 1) {
					possVals[row][n][num] = 0;
					possVals[row][n][0]--;
					possValsChanged = true;
					//CONSTS.log("removed " + num + " from row " + row + ", col " + n);
				}
			}
		}
		return possValsChanged;
	}
	
	/**
	 * Remove a given number from the possible-values in an entire column,
	 * with the exception of the block containing the specified row
	 * 
	 * @param num - number to remove
	 * @param col - col to remove from
	 * @param except - row to leave as-is (-1 for no exception)
	 * @return true if any change was made
	 */
	private boolean removeNumFromCol(int num, int col, int except) {
		boolean possValsChanged = false;
		// CONSTS.log("removeNum " + num + " FromCol " + col);
		for (int n = 0; n < 9; n++) {
			if ((n / 3) * 3 != except) { // skip numbers in the same block
				if (possVals[n][col][num] == 1) {
					possVals[n][col][num] = 0;
					possVals[n][col][0]--;
					possValsChanged = true;
				}
			}
			// CONSTS.log("remain: " + possVals[n][col]);
		}
		return possValsChanged;
	}
	
	/**
	 * Remove a given number from the possible-values in an entire column,
	 * with the exception of the specified exceptions
	 * 
	 * @param num - number to remove
	 * @param col - col to remove from
	 * @param except - rows to leave as-is
	 * @return true if any change was made
	 */
	private boolean removeNumFromCol(int num, int col, int[] except) {
		boolean possValsChanged = false;
		for (int n = 0; n < 9; n++) {
			boolean exception = false;
			for (int e = 0; e < except.length; e++) {
				if ( n == except[e] ) exception = true;
			}
			if (!exception) {
				if (possVals[n][col][num] == 1) {
					possVals[n][col][num] = 0;
					possVals[n][col][0]--;
					possValsChanged = true;
					//CONSTS.log("removed " + num + " from row " + n + ", col " + col);
				}
			}
		}
		return possValsChanged;
	}

	/**
	 * Remove a given number from the box containing the given row and column,
	 * with the exception of the specified square
	 * 
	 * @param num - number to remove
	 * @param i - row identifying box
	 * @param j = column identifying box
	 * @param excludeRow - row identifying exception-square
	 * @param excludeCol - column identifying exception-square
	 * @return true if any change was made
	 */
	private boolean removeNumFromBox(int num, int i, int j, int excludeRow, int excludeCol) {
		boolean possValsChanged = false;
		for (int x = i - i % 3; x < i - i % 3 + 3; x++) {
			if (x != excludeRow) {
				for (int y = j - j % 3; y < j - j % 3 + 3; y++) {
					if (y != excludeCol && possVals[x][y][num] != 0) {
						possVals[x][y][num] = 0;
						possVals[x][y][0]--;
						possValsChanged = true;
					}
				}
			}
		}
		return possValsChanged;
	}

	/**
	 * Complete the possible-values for the entire game board
	 *
	 */
	protected void fillPossVals() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (game[i][j] >= 1) {
					possVals[i][j][0] = -game[i][j]; // we store the value but with minus sign
				} else {
					possVals[i][j][0] = 9; // maximum possible values
					for (int n = 1; n <= 9; n++) {
						possVals[i][j][n] = 1;
						for (int x = 0; x < 9; x++) {
							if (game[i][x] == n || game[x][j] == n || game[(i / 3) * 3 + (x / 3)][(j / 3) * 3 + (x % 3)] == n) {
								possVals[i][j][n] = 0; // no possible value
								possVals[i][j][0]--;
								break;
							}
						}
					}
				}
			}
		}
	}

	protected int[][] getGame() {
		return game;
	}

	protected int getFilled() {
		return filled;
	}

	public String toString() {
		return matrixToString(game);
	}

	private String possibleValsToString() {
		StringBuffer res = new StringBuffer();
		res.append("\n-----------------------------------------------------\n");
		for (int i = 0; i < 9; i++) {
			for (int subLine = 0; subLine < 3; subLine++) {
				for (int j = 0; j < 9; j++) {
					for (int subCol = 0; subCol < 3; subCol++) {
						int num = subLine * 3 + subCol + 1;
						if (possVals[i][j][num] == 1) {
							res.append(num);
						} else {
							res.append(" ");
						}
					}
					res.append(" | ");
				}
				res.append("\n");
			}
			res.append("-----------------------------------------------------\n");
		}
		return res.toString();
	}

	/**
	 * Note: be sure to call fillPossVals before using this method!
	 * 
	 */
	protected int findAllSols(int level, boolean checkOnlyUnicity) {
		int row = -1;
		int col = -1;
		for (int i = 0; i < 9; i++) // search pos with less possible values
		{
			for (int j = 0; j < 9; j++) {
				if (possVals[i][j][0] == 0)
					return 0; // no solution
				if (possVals[i][j][0] > 0 // (we ignore solved cells)
						&& (row == -1 || possVals[i][j][0] < possVals[row][col][0])) {
					row = i;
					col = j;
				}
			}
		}
		if (row == -1)
			return 1; // solution found

		int sols = 0; // number of solutions
		int numpossVals = possVals[row][col][0];
		for (int num = 1; num <= 9; num++) {
			if (possVals[row][col][num] != 1)
				continue;
			possVals[row][col][0] = -num;
			for (int i = 0; i < 9; i++) {
				int[] aux = possVals[row][i];
				if (aux[0] >= 0 && aux[num] == 1) {
					aux[0]--;
					aux[num] = level;
				}
				aux = possVals[i][col];
				if (aux[0] >= 0 && aux[num] == 1) {
					aux[0]--;
					aux[num] = level;
				}
				aux = possVals[(row / 3) * 3 + (i / 3)][(col / 3) * 3 + (i % 3)];
				if (aux[0] >= 0 && aux[num] == 1) {
					aux[0]--;
					aux[num] = level;
				}
			}
			sols += findAllSols(level - 1, checkOnlyUnicity);
			for (int i = 0; i < 9; i++) {
				int[] aux = possVals[row][i];
				if (aux[0] >= 0 && aux[num] == level) {
					aux[0]++;
					aux[num] = 1;
				}
				aux = possVals[i][col];
				if (aux[0] >= 0 && aux[num] == level) {
					aux[0]++;
					aux[num] = 1;
				}
				aux = possVals[(row / 3) * 3 + (i / 3)][(col / 3) * 3 + (i % 3)];
				if (aux[0] >= 0 && aux[num] == level) {
					aux[0]++;
					aux[num] = 1;
				}
			}
			if (checkOnlyUnicity && sols > 1) {
				break; // with 2 or more solutions finish
			}
		}
		possVals[row][col][0] = numpossVals;
		return sols;
	}

	protected int calcDifficulty() {
		loopAllStg();
		CONSTS.log(strategiesUsed + " difficulty " + difficulty);
		return difficulty;
	}

	protected void clearVal(int i, int j) {
		int num = game[i][j];
		if (num <= 0)
			throw new RuntimeException("Cant clearVal " + num);

		game[i][j] = 0;
		filled--;
	}
}
