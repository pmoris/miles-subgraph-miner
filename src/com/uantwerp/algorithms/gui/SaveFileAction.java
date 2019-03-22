package com.uantwerp.algorithms.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

//Methods

//This action creates and shows a modal open-file dialog.
public class SaveFileAction implements ActionListener {
	
	private JTextField textField;
	private JFrame frame;
	
	public SaveFileAction(JTextField textField, JFrame frame) {
		this.textField = textField;
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
//		chooser.setDialogType(JFileChooser.SAVE_DIALOG);
		chooser.setDialogTitle("Specify a file to save the output");
		// Show dialog; this method does not return until dialog is closed
		int returnVal = chooser.showSaveDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filePath = chooser.getSelectedFile().getAbsolutePath();
			textField.setText(filePath);	
		}
	}
};
