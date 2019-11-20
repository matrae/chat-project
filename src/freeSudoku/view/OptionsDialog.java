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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import freeSudoku.translation.MSG;

public class OptionsDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private int cAmple = 170;
	private int cAlt = 210;
	private int marginRigth1 = 40;
	private int marginRigth2 = 15;
	private int marginTop1 = 30;
	private int marginTop2 = 60;
	private int marginTop3 = 90;

	private JLabel symmLabel = null;
	private JCheckBox symmCheckBox = null;
	private JLabel trainingLabel = null;
	private JCheckBox trainingCheckBox = null;
	private JLabel consistencyLabel = null;
	private JCheckBox consistencyCheckBox = null;

	public OptionsDialog() {
		setTitle(MSG.getString("gui.OptionsDialog.options"));
		setModal(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - cAmple) / 2, (screenSize.height - cAlt) / 2, cAmple, cAlt);
		initComps();
	}

	private void initComps() {
		setResizable(false);
		SpringLayout layout = new SpringLayout();
		getContentPane().setLayout(layout);

		symmLabel = initLabel("Symmetric Game:");
		symmCheckBox = new JCheckBox();
		symmCheckBox.setSelected(false);
		// TODO: externalize string and support change of lang
		symmLabel.setToolTipText("Generates games with symmetry");
		symmCheckBox.setToolTipText("Generates games with symmetry");
		getContentPane().add(symmCheckBox);
		layout.putConstraint(SpringLayout.EAST, symmLabel, -marginRigth1, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, symmCheckBox, -marginRigth2, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, symmLabel, marginTop1, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, symmCheckBox, marginTop1, SpringLayout.NORTH, getContentPane());

		trainingLabel = initLabel("Training mode:");
		trainingCheckBox = new JCheckBox();
		trainingCheckBox.setSelected(false);
		trainingLabel.setToolTipText("Enables tips and auto-complete optional values");
		trainingCheckBox.setToolTipText("Enables tips and auto-complete optional values");
		getContentPane().add(trainingCheckBox);
		layout.putConstraint(SpringLayout.EAST, trainingLabel, -marginRigth1, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, trainingCheckBox, -marginRigth2, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, trainingLabel, marginTop2, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, trainingCheckBox, marginTop2, SpringLayout.NORTH, getContentPane());

		consistencyLabel = initLabel("Consistency checking:");
		consistencyCheckBox = new JCheckBox();
		consistencyCheckBox.setSelected(false);
		consistencyLabel.setToolTipText("Enables consistency-checking and warnings");
		consistencyCheckBox.setToolTipText("Enables consistency-checking and warnings");
		getContentPane().add(consistencyCheckBox);
		layout.putConstraint(SpringLayout.EAST, consistencyLabel, -marginRigth1, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.EAST, consistencyCheckBox, -marginRigth2, SpringLayout.EAST, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, consistencyLabel, marginTop3, SpringLayout.NORTH, getContentPane());
		layout.putConstraint(SpringLayout.SOUTH, consistencyCheckBox, marginTop3, SpringLayout.NORTH, getContentPane());
		
		// TODO: externalize string and support change of lang
		JButton closeButton = new JButton("OK");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				setVisible(false);
			}
		});
		getContentPane().add(closeButton);
		layout.putConstraint(SpringLayout.SOUTH, closeButton, -15, SpringLayout.SOUTH, getContentPane());
		layout.putConstraint(SpringLayout.WEST, closeButton, cAmple / 2 - 30, SpringLayout.WEST, getContentPane());

	}

	private JLabel initLabel(String pText) {
		JLabel label = new JLabel(pText);
		// label.setPreferredSize( new Dimension(150, 30));
		label.setHorizontalAlignment(JLabel.RIGHT);
		getContentPane().add(label);
		return label;
	}

	public boolean isTrainingMode() {
		return trainingCheckBox.isSelected();
	}

	public boolean isSymmetric() {
		return symmCheckBox.isSelected();
	}
	
	public boolean isConsistency() {
		return consistencyCheckBox.isSelected();
	}
}
