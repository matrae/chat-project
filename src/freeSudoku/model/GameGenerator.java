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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import freeSudoku.CONSTS;
import freeSudoku.FreeSudoku;

/**
 * @author gos
 *
 */
public class GameGenerator
{
    private int[][]     solvedBoard     = null;     // posicion del resultado.
    private int[][]     currentBoard    = null;    // current generated game position.
    private Random      cGenerator      = null;
    private int         diffLevel       = 5;
    private static Solver solver        = null;
    private boolean     isSymmetric     = true;
    private int         estimatedDiff   = -1;
    public int          totalSteps      = 0;
    public int          currentStep     = 0;

    /**
     * 
     */
    public GameGenerator()
    {
        solvedBoard = new int[9][9];
        cGenerator = new Random();
        solver = new Solver();
    }

    private void clearArray( int[][] pArray)
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                pArray[i][j] = 0;
            }
        }
    }



    /**
     * Recursive method to generate a complete new game
     * @param pPos
     * @return
     */
    private boolean generateCompletedGame(int pPos)
    {
        // CONSTS.log( taulerToString( solvedBoard) + "\n");
        // CONSTS.log( pPos);
        if( pPos == 9*9 ) return true;
        ArrayList<Integer> possibleVals = getAllCorrectVals( pPos/9, pPos%9);
        if( possibleVals.isEmpty()) return false;
        
        while( !possibleVals.isEmpty())
        {
            int candidate = cGenerator.nextInt( possibleVals.size());
            solvedBoard[pPos/9][pPos%9] = ((Integer )possibleVals.get( candidate)).intValue();
            if( Game.isCorrect( pPos/9, pPos%9, solvedBoard[pPos/9][pPos%9], solvedBoard))
            {
                if( generateCompletedGame( pPos + 1)) 
                {
                    return true;
                }
            }
            possibleVals.remove( candidate);
        }
        solvedBoard[pPos/9][pPos%9] = 0;
        // CONSTS.log(" --------- ");
        return false;
    }

    

    private ArrayList<Integer> getAllCorrectVals( int i, int j)
    {
        return Game.getCorrectValsForArray( i, j, solvedBoard);
    }

    /**
     * Generates a initial position from a complete game 
     * by eliminating cells 
     */
    private void generateInitPos()
    {
        int maxPosToFill = 33 - diffLevel*3;
        boolean[] used = new boolean[81];
        int usedCount = 81;

        solver.setGame( solvedBoard);
        Arrays.fill( used, false);
        while ( solver.getFilled() > maxPosToFill && usedCount > 1)
        {
            int i = cGenerator.nextInt( 81);
            do
            {
                if( i < 80 ) i++;
                else i = 0;
            } while( used[i]);
            
            used[i] = true;
            usedCount--;
            solver.clearVal( i/9, i%9);
            if ( isSymmetric && (i/9 != 4 || i%9 != 4 ))
            {
                solver.clearVal( 8 - i/9, 8 - i%9);
                used[ 9*(8-i/9) + (8 - i%9) ] = true;
                usedCount--;
            }
            solver.fillPossVals();
            int sols = solver.findAllSols( -1, true);
            if( sols > 1)
            {
                solver.setVal( i/9, i%9, solvedBoard[i/9][i%9]);
                if (isSymmetric && (i/9 != 4 || i%9 != 4 ))
                {
                    solver.setVal( 8-i/9, 8-i%9, solvedBoard[8 - i/9][8 - i%9]);
                }
            }
        }
        currentBoard = Solver.dupArray( solver.getGame());
    }

    /**
     * Initializes a new game trying to get the most aproximated
     * difficulty level
     * @param pNivel Difficulty level
     * @param pIsSymmetric True if you want a symmetric game 
     */
    public void generateInitGame(int pNivel, boolean pIsSymmetric)
    {
        diffLevel = pNivel;
        isSymmetric = pIsSymmetric;
        int bestDifficultyFound = -1;
        int maxRes[][] = null;
        int maxPos[][] = null;
        boolean found = false;
        clearArray( solvedBoard);
        
        // For each valid complete board you can generate different initial
        // positions by eliminating cells.  Most of the generated games are 
        // pretty easy to solve, only a few are harder.
        //
        // Anyway there is no guarantee that we will get a game hard enougth,
        // is just probable.
        //
        // We try 1000 times (50 boards, each time 20 different starting
        // positions). We stop as soon as we find a suitable game.
        // This admittedly makes the progress bar kind of meaningless...
        int firstLoop = 50;
        int secondLoop = 20;
        totalSteps = firstLoop * secondLoop;
        currentStep = 0;

        // Here generates a complete initial game for each loop
        for (int part = 0; part < firstLoop & !found; part++)
        {
          generateCompletedGame(0);

          // This loop generates several initial positions for a given complete board.
          for (int pos = 0; pos < secondLoop & !found; pos++)
          {
            generateInitPos(); // generate an initial position
            solver.loopAllStg(); // try to solve it
            //if (solver.difficulty > 0) CONSTS.log("Difficulty " + solver.difficulty + ", strategies used: " + solver.strategiesUsed);
            
            int minDifficulty = levelDifficulty(diffLevel-1);
            int maxDifficulty = levelDifficulty(diffLevel);
            
            // for level 1, anything over a zero
            if (diffLevel <= 1) minDifficulty = 1;
            
            // for level 5, take the hardest we can generate
            if (diffLevel >= 5) maxDifficulty = 999999;

            // We accept a game immediately if it is in the given difficulty range.
            // If it is too hard, we discard it. If it is too easy, we retain it but
            // keep trying - if we run out of tries, we will return the most difficult
            // game we have generated.
            
            if (solver.difficulty <= maxDifficulty) {
              if (solver.difficulty >= bestDifficultyFound) { // keep it
                bestDifficultyFound = solver.difficulty;
                maxRes = Solver.dupArray( solvedBoard);
                maxPos = Solver.dupArray( currentBoard);
                CONSTS.log("Difficulty " + solver.difficulty + ", strategies used: " + solver.strategiesUsed);
              }
              if (solver.difficulty > minDifficulty) { // use it
                found = true;
              }
            }
            currentStep++;
            int woof = 100*currentStep/totalSteps;
            if (woof == 100) woof = 99; // bug causes NullPointerException if we actually use 100 (in Java 1.5.0.5 anyway)
            FreeSudoku.inst.setProgress( woof);
          }
        }

        // We take the game that fits better: maxRes, maxPos 
        if (maxRes != null) {
          solvedBoard = maxRes;
          currentBoard = maxPos;
          estimatedDiff = bestDifficultyFound;
        }
    }

    /**
     * @return
     */
    public int[][] getGame()
    {
        return currentBoard;
    }
    
    public static int getNumberOfSolutions(int[][] game, boolean checkOnlyUnicity)
    {
        if( solver == null)
            solver = new Solver();
        solver.setGame( game);
        return solver.findAllSols( -1, checkOnlyUnicity);
    }
    /**
     * @return Returns the estimatedDiff.
     */
    public int getEstimatedDiff()
    {
        return estimatedDiff;
    }

    /**
     * Calculate the difficulty for the given level.
     * For levels 1, 2 and 3 this gives 10, 30, and 70
     */
    private int levelDifficulty(int level) {
      int difficulty = 1;
      for (int i = 1; i <= level+2; i++) difficulty *= 2;
      difficulty = difficulty - 1;
      return difficulty;
    }
}
