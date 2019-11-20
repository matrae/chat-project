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

import java.awt.*;
import java.awt.FlowLayout;
import java.io.UnsupportedEncodingException;
import javax.swing.*;
/*
 * Cirilic.java
 *
 * Created on 11 de octubre de 2005, 9:09
 */

/**
 *
 * @author 
 */
public class Cirilic extends JFrame {
    
    // Variables declaration
    private JButton jButton1;
    private JButton jButton2;
    private JButton jButton3;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane2;
    private JTextArea jTextArea1;
    private JTextArea jTextArea2;
    // End of variables declaration

	private static final long serialVersionUID = 1L;
	/** Creates new form Cirilic */
    public Cirilic() 
    {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
    	int cAmple = 500;
    	int cAlt = 600;
        jScrollPane1 = new JScrollPane();
        jScrollPane2 = new JScrollPane();
        jTextArea1 = new JTextArea();
        jTextArea2 = new JTextArea();
        jButton1 = new JButton();
        jButton2 = new JButton();
        jButton3 = new JButton();

        setTitle("JCirilic");
        getContentPane().setLayout(new FlowLayout());
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - cAmple) / 2, (screenSize.height - cAlt) / 2,
				cAmple, cAlt);
		jTextArea1.setColumns(40);
		jTextArea1.setRows(16);
		jTextArea1.setLineWrap(true);
        jScrollPane1.setViewportView(jTextArea1);

		jTextArea2.setColumns(40);
		jTextArea2.setRows(16);
		jTextArea2.setLineWrap(true);
        jTextArea2.setBackground(new Color(232, 232, 232));
        jTextArea2.setEditable( false);
        jScrollPane2.setViewportView(jTextArea2);

        jButton1.setText("Convert");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Clear");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Exit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        getContentPane().add( jScrollPane1);
        getContentPane().add( jScrollPane2);
        getContentPane().add( jButton1);
        getContentPane().add( jButton2);
        getContentPane().add( jButton3);
        
    }
    // </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextArea1.setText("");
        jTextArea2.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        convert();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void convert()
    {
        // JOptionPane.showMessageDialog( this, jTextArea1.getText());
        String text = jTextArea1.getText();
        StringBuffer sb = new StringBuffer();
        try
        {
            for (int i = 0; i < text.length(); i++) 
            {
                char c = text.charAt(i);
                if( c==' ' || c=='.' || c==',' || c=='\n' )
                {
                    sb.append(c);
                    continue;
                }
                sb.append( getUnicode(c));
            }
//            jTextArea2.append( new String( new byte[]{(byte)0xd3, (byte)0xd4,(byte)0xd5,(byte)0xd6}, "ISO-8859-5"));
//            jTextArea2.append( new String( "\u00d4 \u00d6".getBytes(), "ISO-8859-5"));
             jTextArea2.append( sb.toString());
//             jTextArea2.append( "\n- - - - - - - - - - - - - - - - - - - - - - -\n");
//             jTextArea2.append( new String(" qweqw qwe \u00d4\u00d6\u00d8\u00d3\u00d2 qwer qwe".getBytes(), "ISO-8859-5"));
        } catch( Exception e)
        {
            e.printStackTrace();
        }
            

    }
    
    private String getUnicode(char c) throws UnsupportedEncodingException
    {
        String res = null;
        int n = String.valueOf(c).getBytes("ISO-8859-5")[0];
        if( n > 0) return String.valueOf(c);
        res = "\\u00" + Integer.toHexString( n).substring( 6);
        
        return res;
    }
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
	System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cirilic().setVisible(true);
            }
        });
    }
}
