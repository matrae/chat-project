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
package freeSudoku.test;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * @author gos
 *
 */
public class ShowSystemFonts
{

    public static void main(String[] args)
    {
        System.out.println( "\n- - - - - - - - - - - getAllFonts() - - - - - - - - - - - - ");
        Font[] f = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
        for (int i = 0; i < f.length; i++)
        {
            System.out.println( f[i].getFontName());
        }

        String[] g = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        System.out.println( "\n\n - - - - - - - - - getAvailableFontFamilyNames() - - - - - - - - ");
        for (int i = 0; i < g.length; i++)
        {
            System.out.println( g[i]);
        }        
    }
}
