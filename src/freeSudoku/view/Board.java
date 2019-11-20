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
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

import freeSudoku.*;
import freeSudoku.translation.MSG;

/**
 * 
 */
public class Board extends JPanel {
	// Variables declaration

	private JPanel[][] cPanel = new JPanel[3][3];
	private Cell[][] cPos = new Cell[9][9];

	// Menu
	private JMenuItem aboutMenuItem;
	private JMenuItem contentsMenuItem;
	private JMenuItem optionsMenuItem;
	private JMenu optionsMenu;
	private JMenuItem exitMenuItem;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenu langMenu;
	private JMenuBar menuBar;
	private JMenuItem newMenuItem;
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem printMenuItem;
	private JMenuItem[] langItem;
	private JFileChooser fileDialog;

	// Buttons
	private JButton startButton;
	private JButton stopButton;

	private JPanel cPanelButtons;
	private JPanel cPanelOptions;
	
	// Highlight panel
	private JPanel cHighlight;
	private JButton[] highlightButtons;

	// Messages
	private JPanel cPanelMessages;
	private JTextField cTextFieldMessages;

	private JSlider diffSlider;
	private OptionsDialog optionsDialog = null;

	private static final long serialVersionUID = 1L;
	protected boolean disabled = true;
	private int estimatedDiff = -1;
	private int highlight = 0; // the currently highlighted number
	// End of variables declaration

	private JProgressBar progressBar;

	/**
	 * Creates new form Board
	 */
	public Board(Object parentFrame) {
		initComponents(parentFrame);
		disableAll();
	}

	/**
	 * This method is called from within the constructor to initialize the
	 * elements of the board.
	 */
	private void initComponents(Object parentFrame) {
		final Board selfReference = this; // required for the ActionListeners below

		this.setLayout(new FlowLayout());

		// Buttons & Messages
		cPanelButtons = new JPanel();
		cPanelOptions = new JPanel();
		cHighlight = new JPanel();
		startButton = new JButton();
		stopButton = new JButton();
		cPanelMessages = new JPanel();
		cTextFieldMessages = new JTextField();
		progressBar = new JProgressBar(0, 100);

		// Menus
		menuBar = new JMenuBar();
		fileMenu = new JMenu();
		newMenuItem = new JMenuItem();
		openMenuItem = new JMenuItem();
		saveMenuItem = new JMenuItem();
		exitMenuItem = new JMenuItem();
		optionsMenu = new JMenu();
		optionsMenuItem = new JMenuItem();
		langMenu = new JMenu();
		helpMenu = new JMenu();
		contentsMenuItem = new JMenuItem();
		aboutMenuItem = new JMenuItem();
		printMenuItem = new JMenuItem();

		cPanelButtons.setLayout(new FlowLayout(FlowLayout.LEFT));
		cPanelOptions.setLayout(new FlowLayout(FlowLayout.LEFT));

		cPanelButtons.setPreferredSize(new Dimension(165, 50));
		cPanelOptions.setPreferredSize(new Dimension(135, 60));
		startButton.setFont(new Font(CONSTS.FONT_NAME, Font.BOLD, 12));
		startButton.setForeground(CONSTS.GREEN_DARK);
		cPanelButtons.add(startButton);
		stopButton.setForeground(CONSTS.RED_DARK);
		cPanelButtons.add(stopButton);

		diffSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, 2);
		diffSlider.setMajorTickSpacing(1);
		diffSlider.setMinorTickSpacing(1);
		diffSlider.setPaintTicks(true);
		diffSlider.setPaintLabels(true);
		diffSlider.setSnapToTicks(true);
		diffSlider.setPreferredSize(new Dimension(100, 45));

		cPanelOptions.add(diffSlider);
		this.add(cPanelButtons);
		this.add(cPanelOptions);

		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				FreeSudoku.inst.restart();
			}
		});
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				disableAll();
			}
		});

		cHighlight.setLayout(new FlowLayout(FlowLayout.LEFT));
		cHighlight.setPreferredSize(new Dimension(280, 40));
		
		highlightButtons = new JButton[9];
		for (int i = 0; i <= 8; i++) {
			JButton tmpButton = new JButton();
			tmpButton.setText(Integer.toString(i+1));
			tmpButton.setPreferredSize(new Dimension(25, 25));
			tmpButton.setMargin(new Insets(0,0,0,0));
			tmpButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					if (!disabled) highlight( ((JButton) evt.getSource()).getText() );
				}
			});
			highlightButtons[i] = tmpButton;
			cHighlight.add(tmpButton);
		}
		
		this.add(cHighlight);
		
		Cell.board = this; // Inicialitza referencia a les caselles.
		// Init each 3x3 panel
		for (int px = 0; px < 3; px++) {
			for (int py = 0; py < 3; py++) {
				cPanel[px][py] = new JPanel();
				initPanel(cPanel[px][py]);

				// Init each position from panel
				for (int cx = 0; cx < 3; cx++) {
					for (int cy = 0; cy < 3; cy++) {
						int i = px * 3 + cx;
						int j = py * 3 + cy;
						cPos[i][j] = new Cell(i, j);
						cPanel[px][py].add(cPos[i][j]);
					}
				}

				// Add this 3x3 panel
				this.add(cPanel[px][py]);
			}
		}

		// Messages
		cPanelMessages.setPreferredSize(new Dimension(290, 50));
		cTextFieldMessages.setBackground(new Color(204, 204, 204));
		cTextFieldMessages.setPreferredSize(new Dimension(290, 40));
		cTextFieldMessages.setHorizontalAlignment(JTextField.CENTER);
		cTextFieldMessages.setFont(new Font(CONSTS.FONT_NAME, Font.BOLD, 14));
		cTextFieldMessages.setForeground(Color.BLUE);
		cPanelMessages.add(cTextFieldMessages);

		this.add(cPanelMessages);

		// Menus
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(printMenuItem);
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		optionsMenu.add(optionsMenuItem);
		menuBar.add(optionsMenu);
		langItem = new JMenuItem[MSG.getAvailableLocales().length];
		for (int i = 0; i < MSG.getAvailableLocales().length; i++) {
			final int langIdx = i;
			langItem[i] = new JMenuItem();
			langItem[i].setText(MSG.getAvailableLocaleNames(false)[i]);
			langItem[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					// Change the language for all items
					Locale newLocale = MSG.getAvailableLocales()[langIdx];
					Locale.setDefault(newLocale);
					JComponent.setDefaultLocale(newLocale);
					JFileChooser.setDefaultLocale(newLocale);
					fileDialog = new JFileChooser();
					MSG.setLocale(newLocale);
					setCompsText();
				}
			});

			langMenu.add(langItem[i]);
		}
		menuBar.add(langMenu);

		helpMenu.add(contentsMenuItem);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);

		openMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				Properties storedGame = null;
				fileDialog = new JFileChooser();
				int returnVal = fileDialog.showOpenDialog(openMenuItem);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileDialog.getSelectedFile();
					storedGame = new Properties();
					try {
						storedGame.load(new FileInputStream(selectedFile));
					} catch (IOException e) {
						setMessage(e.getMessage());
						e.printStackTrace();
					}
				}
				fileDialog = null;
				FreeSudoku.inst.restart(storedGame);
			}
		});
		saveMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				fileDialog = new JFileChooser();
				int returnVal = fileDialog.showSaveDialog(saveMenuItem);
				Properties game = FreeSudoku.inst.getGameAndBoardState();
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileDialog.getSelectedFile();
					try {
						game.store(new FileOutputStream(selectedFile), CONSTS.STOREGAME_DESC);
					} catch (IOException e) {
						setMessage(e.getMessage());
						e.printStackTrace();
					}
				}
				fileDialog = null;
			}
		});
		newMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				FreeSudoku.inst.restart();
			}
		});
		exitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		optionsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (optionsDialog == null)
					optionsDialog = new OptionsDialog();
				optionsDialog.setVisible(true);
			}
		});
		
		aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				StringBuffer mensaje = new StringBuffer();
				mensaje.append(MSG.getString("gui.help.about.text1"));
				mensaje.append(CONSTS.version);
				mensaje.append(MSG.getString("gui.help.about.text2"));
				if (estimatedDiff != -1)
					mensaje.append(MSG.getString("gui.help.about.estDiff") + estimatedDiff);
				JOptionPane.showMessageDialog(selfReference, mensaje.toString(), MSG.getString("gui.menu.help.about"),
						JOptionPane.PLAIN_MESSAGE);
			}
		});
		contentsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				StringBuffer mensaje = new StringBuffer();
				mensaje.append(MSG.getString("gui.help.contents.text"));
				JOptionPane.showMessageDialog(selfReference, MSG.getString("gui.help.contents.text"), MSG
						.getString("gui.menu.help.contents"), JOptionPane.PLAIN_MESSAGE);
			}
		});

		openMenuItem.setEnabled(true);
		saveMenuItem.setEnabled(false);
		printMenuItem.setEnabled(false);
		optionsMenuItem.setEnabled(true);
		
		if ( (JApplet.class.isInstance(parentFrame))) {
			((JApplet) parentFrame).setJMenuBar(menuBar);
		} else {
			((JFrame) parentFrame).setJMenuBar(menuBar);
		}
		setCompsText();
	}

	private void setCompsText() {
		startButton.setToolTipText(MSG.getString("gui.tooltip.button.start"));
		startButton.setText(MSG.getString("gui.button.start"));
		stopButton.setText(MSG.getString("gui.button.stop"));
		stopButton.setToolTipText(MSG.getString("gui.tooltip.button.stop"));
		diffSlider.setToolTipText(MSG.getString("gui.tooltip.slider.difficultyLevel"));
		fileMenu.setText(MSG.getString("gui.menu.file"));
		newMenuItem.setText(MSG.getString("gui.menu.file.newGame"));
		openMenuItem.setText(MSG.getString("gui.menu.file.open"));
		saveMenuItem.setText(MSG.getString("gui.menu.file.save"));
		printMenuItem.setText(MSG.getString("gui.menu.file.print"));
		exitMenuItem.setText(MSG.getString("gui.menu.file.exit"));
		optionsMenu.setText(MSG.getString("gui.menu.options"));
		optionsMenuItem.setText(MSG.getString("gui.menu.options"));
		langMenu.setText(MSG.getString("gui.menu.language"));
		helpMenu.setText(MSG.getString("gui.menu.help"));
		contentsMenuItem.setText(MSG.getString("gui.menu.help.contents"));
		aboutMenuItem.setText(MSG.getString("gui.menu.help.about"));

		// Establish the message of the board if it's neccesary
		if (disabled) {
			setMessage(MSG.getString("gui.board.clickStart"));
		}

		// Change the value of the language menu items
		for (int i = 0; i < MSG.getAvailableLocales().length; i++) {
			langItem[i].setText(MSG.getAvailableLocaleNames(false)[i]);
		}
	}

	private void initPanel(JPanel panel) {
		panel.setLayout(new GridLayout(3, 3, 1, 1));
		panel.setBorder(new EtchedBorder());
		panel.setPreferredSize(new Dimension(93, 93));
		panel.setRequestFocusEnabled(false);
		panel.setBackground(CONSTS.BLUE_MED);
	}

	private void exitMenuItemActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

	public void setPosValue(int i, int j, int val) {
		cPos[i][j].setValue(val);
	}

	/**
	 * Fills the board with the initial values
	 * 
	 * @param pos
	 * @param pInitPos
	 */
	private void setPos(int[][] pos, boolean[][] pInitPos) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				cPos[i][j].setTypeNormal();
				cPos[i][j].setBackground(CONSTS.BLUE_CLEAR);
				cPos[i][j].initPos = pInitPos[i][j];
				if (pInitPos[i][j]) {
					cPos[i][j].setText(String.valueOf(pos[i][j]));
					cPos[i][j].setForeground(Color.BLACK);
				} else {
					cPos[i][j].setText((pos[i][j] == 0 ? "" : String.valueOf(pos[i][j])));
					cPos[i][j].setForeground(Color.BLUE);
				}
			}

		}
	}

	public void setBoard(int[][] pos, boolean[][] pInitPos, int pEstimatedDiff) {
		cPanelMessages.remove(progressBar);
		cPanelMessages.add(cTextFieldMessages);
		setMessage("");
		setPos(pos, pInitPos);
		repaint();
		estimatedDiff = pEstimatedDiff;
		stopButton.setEnabled(true);
		startButton.setEnabled(false);
		newMenuItem.setEnabled(false);
		openMenuItem.setEnabled(false);
		saveMenuItem.setEnabled(true);
		// printMenuItem.setEnabled(true);
		disabled = false;
		diffSlider.setEnabled(false);
		optionsMenuItem.setEnabled(false);
		cPanelMessages.repaint();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	public void setWaitState() {
		cPanelMessages.remove(cTextFieldMessages);
		progressBar.setString("");
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		cPanelMessages.add(progressBar);
		progressBar.setStringPainted(true);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		startButton.setEnabled(false);
		newMenuItem.setEnabled(false);
		openMenuItem.setEnabled(false);
		saveMenuItem.setEnabled(false);
		printMenuItem.setEnabled(false);
		diffSlider.setEnabled(false);
		optionsMenuItem.setEnabled(false);
		cPanelMessages.repaint();
		progressBar.repaint();
	}

	/**
	 * Displays a message to the user in the area below
	 * 
	 * @param pMessage
	 */
	public void setMessage(String pMessage) {
		cTextFieldMessages.setText(pMessage);
	}

	public int getNivel() {
		return diffSlider.getValue();
	}

	/**
	 * Disable all elements from the board except start button and some menus
	 */
	public void disableAll() {
		cTextFieldMessages.setText("");
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		newMenuItem.setEnabled(true);
		openMenuItem.setEnabled(true);
		disabled = true;
		setPosBackgroundColor(CONSTS.GRAY_CLEAR);
		diffSlider.setEnabled(true);
		setMessage(MSG.getString("gui.board.clickStart"));
		optionsMenuItem.setEnabled(true);
	}
	
	/**
	 * Sets the background of all cells to a specific color.
	 * 
	 * @param pColor
	 *          color to set
	 */
	private void setPosBackgroundColor(Color pColor) {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				cPos[i][j].setBackground(pColor);
			}
		}
	}

	/**
	 * @return true if game is symmetric
	 */
	public boolean isSymmetric() {
		if (optionsDialog == null)
			optionsDialog = new OptionsDialog();
		return optionsDialog.isSymmetric();
	}

	/**
	 * @return true if game is training mode
	 */
	public boolean isTrainingMode() {
		if (optionsDialog == null)
			optionsDialog = new OptionsDialog();
		return optionsDialog.isTrainingMode();
	}

	/**
	 * @return true if game checks consistency
	 */
	public boolean isConsistency() {
		if (optionsDialog == null)
			optionsDialog = new OptionsDialog();
		return optionsDialog.isConsistency();
	}

	protected String getPosSmall(int i, int j) {
		return cPos[i][j].getPosSmall();
	}

	private void setPosSmall(int i, int j, String val) {
		cPos[i][j].setSmallText(val);
	}

	public String getAllPosSmall() {
		StringBuffer aux = new StringBuffer();
		String pos = null;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				pos = getPosSmall(i, j);
				aux.append((pos == null ? "-" : pos) + "|");
			}
		}
		return aux.toString();
	}

	/**
	 * Check if everything is still ok in the area of influence of one cell, only
	 * multivalue cell.
	 */
	public void checkConsistence(int i, int j) {
		if (cPos[i][j].isSmall)
			throw new RuntimeException("ERROR: checkConsistence. Shouldn't be small");
		String valInCell = cPos[i][j].getText();
		if (valInCell.equals(""))
			return;
		for (int x = 0; x < 9; x++) {
			if (cPos[i][x].isSmall && cPos[i][x].getText().indexOf(valInCell) != -1)
				setAlertInCell(i, x);
			if (cPos[x][j].isSmall && cPos[x][j].getText().indexOf(valInCell) != -1)
				setAlertInCell(x, j);
		}

		for (int x = i - i % 3; x < i - i % 3 + 3; x++) {
			for (int y = j - j % 3; y < j - j % 3 + 3; y++) {
				if (cPos[x][y].isSmall && cPos[x][y].getText().indexOf(valInCell) != -1)
					setAlertInCell(x, y);
			}
		}
	}

	/**
	 * Set a multi-val cell red, to show inconsistence of one of the posible
	 * values
	 */
	private void setAlertInCell(int i, int j) {
		cPos[i][j].setBackground(CONSTS.RED_CLEAR);
		// setMessage( "One optional value in cell is not consistent!");
	}

	/**
	 * Fills the board with all posible vals in all cells
	 */
	public void showAllPossibleVals() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (cPos[i][j].getText().length() == 0) {
					cPos[i][j].setSmallText(FreeSudoku.inst.getPossibleVals(i, j, false));
				}
			}
		}
	}

	/**
	 * @param property
	 */
	public void setTypedSmallVals(String typedVals) {
		StringTokenizer tokens = new StringTokenizer(typedVals, "|");
		String val = null;
		int i = 0;
		while (tokens.hasMoreTokens()) {
			val = tokens.nextToken();
			if (!val.equals("-")) {
				setPosSmall(i / 9, i % 9, val);
			}
			i++;
		}
	}

	/**
	 * @param progress
	 */
	public void setProgress(int progress) {
		progressBar.setValue(progress);
		cPanelMessages.repaint();
	}
	
	private void highlight(String buttonText) {
		int newHighlight = Integer.parseInt(buttonText);
		
		if (highlight == newHighlight) {
			// undo the existing highlight by matching nothing
			buttonText = "0";
			highlight = 0;
		} else {
			highlight = newHighlight;
		}
		
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (cPos[row][col].isSmall) {
					if (cPos[row][col].getText().indexOf(buttonText) > -1) {
						cPos[row][col].setBackground(Color.MAGENTA);
					} else {
						cPos[row][col].setBackground(Color.YELLOW);
					}
				} else {
					if (cPos[row][col].getText().equals(buttonText)) {
						cPos[row][col].setBackground(Color.GREEN);
					} else {
						cPos[row][col].setBackground(CONSTS.BLUE_CLEAR);
					}
				}
			}
		}
		System.out.println("Highlighting " + newHighlight);
	}
}
