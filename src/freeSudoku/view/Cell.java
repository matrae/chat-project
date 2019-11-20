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
package freeSudoku.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import freeSudoku.CONSTS;
import freeSudoku.FreeSudoku;

/**
 * @author gos
 *
 */
public class Cell extends JTextArea
{
    private static final long serialVersionUID = 1L;
    protected static final Insets smallCellSingleLineMargin =   new Insets(8,2,2,2);
    protected static final Insets smallCellDoubleLineMargin =   new Insets(2,2,2,2);
    private static final Insets bigCellMargin =               new Insets(2,9,2,9);
    private KeyEvent keyEvt = null;

    private final int   i;              // Coordenades inicials
    private final int   j;
    
    protected boolean		initPos = false;    // Si es va mostrar inicialment
    public static Board		board  = null;     	// Referencia per comunicarse amb el board

    private static Cell		currPos  = null;
    protected boolean 		isSmall  = false;
    
    
    /**
     * @param i
     * @param j
     */
    public Cell(final int i, final int j)
    {
        super();
        this.i = i;
        this.j = j;
        initCell();
    }
    
    private void initCell()
    {
        setEditable( false);
        setForeground( Color.BLUE);
        setTypeNormal();
        setMargin( bigCellMargin);
        setLineWrap( true);
        setWrapStyleWord( false);

        this.addMouseListener( new MouseAdapter() {
            public void mouseClicked( MouseEvent evt) {
                casellaMouseClicked( evt);
            }

        });
        this.addKeyListener( new KeyAdapter(){
            public void keyTyped( KeyEvent evt) 
            {
                casellaKeyTyped( evt);
            }
        });
    }
    
    /* (non-Javadoc)
     * @see java.awt.Component#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        if ( g instanceof Graphics2D )
        {
        Graphics2D g2d = (Graphics2D)g;

        // antialiasing the text
        g2d.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING,
                              RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

        g = g2d;
        }
     super.paint( g );
    }
    private void casellaKeyTyped(KeyEvent evt)
    {
        keyEvt = evt;
        board.setMessage("");
        if(     currPos == null || board.disabled || 
                ( isSmall && getText().length() >= 8 && evt.getKeyChar() >= '1' && evt.getKeyChar() <= '9') ||
                (( evt.getKeyChar() < '1' || evt.getKeyChar() > '9') && 
                   evt.getKeyChar() != ' ') &&
                   evt.getKeyChar() != KeyEvent.VK_BACK_SPACE)
        {
            if( getText().length() <= 4)    // switch single line
                setMargin( smallCellSingleLineMargin);
            evt.consume();
            return;
        }
        char charPressed = evt.getKeyChar() == ' ' ? '0' : evt.getKeyChar();
        if ( charPressed >= '0' && charPressed <= '9')
        {
            int val = new Integer( String.valueOf( charPressed)).intValue();
            if( isSmall)
            {
            	if( getText().indexOf( charPressed) != -1 || !FreeSudoku.inst.posKeyTypedSmall( i, j, val)) // if not legal
                {
                    keyEvt.consume();
                    return;
                }
                if( getText().length() == 4)    // switch double line
                    setMargin( smallCellDoubleLineMargin);
            }
            else
            {
                FreeSudoku.inst.posKeyTyped( i, j, val);
            }

        } else
        {
            if( getText().length() <= 5)    // switch single line
                setMargin( smallCellSingleLineMargin);
        }
        
    }
    
    private void casellaMouseClicked(MouseEvent evt)
    {
        board.setMessage("");
        if( board.disabled) return;
        if( currPos != null)
        {
        	if( currPos.getText().length() == 0)  // If user types nothing -> back to normal
        	{
        		currPos.isSmall = false;
        		currPos.setTypeNormal();
        	}
        	currPos.setBgColorNormal();
        }
        	
        if( !initPos) 
        {
        	if( evt.getClickCount() == 2) 
        	{
        		isSmall = !isSmall;
        		if( isSmall)
                {
                    FreeSudoku.inst.posKeyTyped( i, j, 0);  // Maybe there was a value
        			setTypeSmall();
                }
        		else
        			setTypeNormal();
        	}
    		setBgColorEdit();
            currPos = this;
            if (board.isTrainingMode())
            {
                if (evt.isControlDown() && evt.isShiftDown() && evt.isAltDown())
                {
                    board.showAllPossibleVals();
                }
                else if (evt.isControlDown() && evt.isShiftDown()
                        && (isSmall || getText().length() == 0))
                {
                    setSmallText(FreeSudoku.inst.getPossibleVals(i, j, false));
                }
                else if (evt.isControlDown())
                    board.setMessage(FreeSudoku.inst.getPossibleVals(i, j, true));
            }
        }
        else
        {
            currPos = null;
        }
        requestFocusInWindow();
    }
    
    public void setValue( int val)
    {
    	if( isSmall)
    	{
    		if( val == 0) 
            {
                setText("");
                if( keyEvt != null) keyEvt.consume();
            }
    	}
    	else
    	{
            setText( val == 0 ? "" : String.valueOf( val));
    	}
    }
    
    protected void setTypeSmall()
    {
        isSmall = true;
        setEditable( true);
        getCaret().setVisible( true);
        setFont(new Font( CONSTS.FONT_NAME, Font.PLAIN, CONSTS.SMALL_TEXT_SIZE));
        setBackground( Color.YELLOW);
        setMargin( smallCellSingleLineMargin);
    }
    
    protected void setSmallText( String text)
    {
        setTypeSmall();
        setText( text);
        if( getText().length() > 4)    // switch double line
            setMargin( Cell.smallCellDoubleLineMargin);
        getCaret().setVisible(false);
        
    }

    public void setTypeNormal()
    {
        isSmall = false;
        setEditable( false);
        getCaret().setVisible( false);
        setFont(new Font( CONSTS.FONT_NAME, Font.BOLD, CONSTS.BIG_TEXT_SIZE));
        setBackground( CONSTS.BLUE_CLEAR);
        setMargin( bigCellMargin);
        setText("");  // Clean up
    }
    
    private void setBgColorEdit()
    {
		if( isSmall)
			setBackground( Color.YELLOW);
		else
			setBackground( new Color(252, 252, 252));  // TODO: color to consts
    }
    private void setBgColorNormal()
    {
		if( isSmall)
			setBackground( Color.YELLOW);
		else
			setBackground( CONSTS.BLUE_CLEAR);
    	
    }
    
    protected String getPosSmall()
    {
        if( isSmall) return getText();
        return null;
    }
}
