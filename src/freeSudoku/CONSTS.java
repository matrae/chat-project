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

import java.awt.Color;

/**
 * @author gos
 *
 */
public final class CONSTS
{
		public static final int appWidth = 322;
		public static final int appHeight = 510;
	
    public static final String	version  	= "0.10.03";
    public static final boolean logEnabled  = true;
    public static final String  defLang = "en";
    
    public static final String FONT_NAME    = "SansSerif";
//    public static final String  FONT_NAME       = "Times new Roman";
    public static final int     SMALL_TEXT_SIZE = 10;
    public static final int     BIG_TEXT_SIZE   = 17;
        
    public static final Color BLUE_CLEAR    = new Color(204, 252, 252);
    public static final Color BLUE_MED      = new Color(160, 160, 200);
    public static final Color GRAY_CLEAR  	= new Color(200,220,220);
    public static final Color GREEN_DARK  	= new Color(20,140,20);
    public static final Color RED_DARK    	= new Color(140,20,20);
    public static final Color RED_CLEAR		= new Color(255,150,120);
    public static final String INITPOS_PROP = "initPos";
    public static final String ADDEDPOS_PROP = "addedPos";
    public static final String POSIBLEPOS_PROP = "posiblePos";
    public static final String STOREGAME_DESC = "FreeSudoku stored game (freesudoku.sourceforge.net)";

    public static void log( String s)
    {
        if( logEnabled) System.out.println( s);
    }
}
