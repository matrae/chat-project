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
import freeSudoku.FreeSudoku;

public class Game implements Runnable
{
	private int[][] 	game 			= null;		// posicion en la partida.
	private boolean[][]	isInitPos		= null;		// posiciones mostradas inicialmente.
	private int			cPosFilled		= 0;
    
    private GameGenerator gameGenerator   = null;
    private int         estimatedDiff = -1;
    private Thread thread;
    private int level;
    private boolean isSymmetric; 

	
	public Game()
	{
		game = new int[9][9];
		isInitPos = new boolean[9][9];
	}

	/**
	 * Initializes a new game with the given
	 * @param initPos
	 */
	public void setGame( String initPos, String addedPos ) throws IllegalArgumentException
	{
	    if ( initPos == null || initPos.length() != 81 )
	        throw new IllegalArgumentException("The stored initPos string must not be null and a length of 81 characters.");
	    for (int i = 0; i < 81; i++) 
        {
            int val = Integer.parseInt( initPos.substring(i,i+1));
            game[i/9][i%9]=val;
        }
	    countInitPos();
        if ( addedPos == null || addedPos.length() != 81 )
            throw new IllegalArgumentException("The stored addedPos string must not be null and a length of 81 characters.");
        for (int i = 0; i < 81; i++) 
        {
            int val = Integer.parseInt( addedPos.substring(i,i+1));
            if( val != 0)
                setPosVal( i/9, i%9, val);
        }
        
	    // TODO show message to user
	    if ( GameGenerator.getNumberOfSolutions( game, true) > 1 )
	    {
	        throw new IllegalArgumentException("Not a unique solution:\n " + toString());
	    }
	}

	public void generateNewGame( int pLevel, boolean pIsSymmetric)
    {
        if( gameGenerator ==null)
            gameGenerator = new GameGenerator();
        level = pLevel;
        isSymmetric = pIsSymmetric;
        thread = new Thread( this);
        thread.start();
    }
    
    
    /**
     * 
     */
    private void countInitPos() {
        cPosFilled = 0;
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
            	if(game[i][j] != 0)
            	{
            		isInitPos[i][j] = true;
            		cPosFilled++;
            	}
                else isInitPos[i][j] = false;
            }
        }
    }



	public int[][] getGame()
	{
		return game;
	}


	protected static String arrayToString( int[][] pArray)
	{
	    return arrayToString(pArray,true);
	}
	protected static String arrayToString( int[][] pArray, boolean isReadable)
	{
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < 9; i++)
		{
			for (int j = 0; j < 9; j++)
			{
//			    if ( isReadable)
//			        res.append( " " );
				res.append(pArray[i][j]);
			}
			if ( isReadable )
			    res.append("\n");
		}
		return res.toString();
	}

	public void setPosVal(int i, int j, int val)
	{
		if( 		game[i][j] == 0 && val != 0) cPosFilled++;
		else if( 	game[i][j] != 0 && val == 0) cPosFilled--;
		game[i][j] = val;
		CONSTS.log("cPosFilled " + cPosFilled);
	}

	public boolean[][] getInitPos()
	{
		return isInitPos;
	}

	public boolean resolved()
	{
		return cPosFilled == 9*9;
	}
	
	/**
	 * to save the sudoku in a simplified String
	 */  
	public Properties getGameState()
	{
        Properties result = new Properties();
        StringBuffer initPos = new StringBuffer();
        StringBuffer addedPos = new StringBuffer();
	    for (int i = 0; i < isInitPos.length; i++) {
            for (int j = 0; j < isInitPos[i].length; j++) {
                if ( isInitPos[i][j] )
                {
                    initPos.append( game[i][j]);
                    addedPos.append( 0);
                }
                else
                {
                    addedPos.append( game[i][j]);
                    initPos.append( 0);
                }
            }
        }
        result.setProperty( CONSTS.INITPOS_PROP, initPos.toString());
        result.setProperty( CONSTS.ADDEDPOS_PROP, addedPos.toString());
		return result;
	}

    public boolean isLegal( int i, int j, int val)
    {
        if( val == 0) return true;
        return isCorrect( i, j, val, game);
    }

    protected static boolean isCorrect( int i, int j, int val, int[][] pArray)
    {
        // CONSTS.log("isCorrect() " + i + " " + j + " " + val);
        for(int x = 0; x < 9; x++)
        {
            if( pArray[i][x] == val && x != j) return false;
            if( pArray[x][j] == val && x != i) return false;
        }
        
        for(int x = i - i%3; x < i - i%3 + 3; x++)
        {
            for(int y = j - j%3; y < j - j%3 + 3; y++)
            {
                if( pArray[x][y] == val && ( x != i || y != j)) return false;
            }
        }
        
        return true;
    }
    
    public ArrayList<Integer> getAllLegalVals( int i, int j)
    {
        return getCorrectValsForArray( i, j, game);
    }
    
    /**
     * Returns a list of posible values in cell (i,j) for a given game
     * @param i
     * @param j
     * @param pArray given game
     * @return
     */
    public static ArrayList<Integer> getCorrectValsForArray( int i, int j, int[][] pArray)
    {
        // TODO: optimize to work with int[]
        ArrayList<Integer> res = new ArrayList<Integer>();
        if( pArray[i][j] != 0) return res;
        for (int val = 1; val <= 9; val++)
        {
            if( Game.isCorrect( i, j, val, pArray))
                res.add( new Integer( val));
        }
        return res;
    }
    

    /**
     * @return Returns the estimatedDiff.
     */
    public int getEstimatedDiff()
    {
        return estimatedDiff;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        gameGenerator.generateInitGame( level, isSymmetric);
        game = gameGenerator.getGame();
        estimatedDiff  = gameGenerator.getEstimatedDiff();
        countInitPos();
        FreeSudoku.inst.endWait();
    }
}
