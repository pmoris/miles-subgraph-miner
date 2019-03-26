package com.uantwerp.algorithms.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.uantwerp.algorithms.ParameterConfig;
import com.uantwerp.algorithms.SubgraphMining;

//Methods

//This action creates and shows a modal open-file dialog.
public class StartButton implements ActionListener {
	
	private SubgraphMiningGUI gui;
	
	public StartButton(SubgraphMiningGUI gui) {
		this.gui = gui;
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("pressed run analysis");
		System.out.println(gui.getTextGraphFile().getText());
//		System.out.println(textGraphFile);
//		System.out.println(textGraphFile.getText());
//		System.out.println(gui.getTextGraphFile().getText());

//		System.out.println(checkBoxNestedPValue.isSelected());

		System.out.println(gui.getTextGraphFile().getText()+
				gui.getTextLabels().getText()+
				gui.getTextInterestingVertices().getText()+
				gui.getTextBackground().getText()+
				gui.getTextFieldSupport().getText()+ 
				gui.getTextFieldPValue().getText()+
				gui.getTextFieldVerticesSize().getText()+
				gui.getTextFieldSavePath().getText()+
				gui.getComboBoxAlgorithm().getSelectedItem().toString()+
				gui.getCheckBoxSingleLabel().isSelected()+
				gui.getCheckBoxUndirected().isSelected()+
				gui.getCheckBoxNestedPValue().isSelected()+
				gui.getCheckBoxShowStatistics().isSelected()+
				gui.getCheckBoxVerbose().isSelected()+
				gui.getTextAreaProgressReport().getText());
		
		ParameterConfig.transformGUI(
				gui.getTextGraphFile().getText(),
				gui.getTextLabels().getText(), 
				gui.getTextInterestingVertices().getText(), 
				gui.getTextBackground().getText(),
				gui.getTextFieldSupport().getText(), 
				gui.getTextFieldPValue().getText(),
				gui.getTextFieldVerticesSize().getText(),
				gui.getTextFieldSavePath().getText(),
				gui.getComboBoxAlgorithm().getSelectedItem().toString(),
				gui.getCheckBoxSingleLabel().isSelected(),
				gui.getCheckBoxUndirected().isSelected(),
				gui.getCheckBoxNestedPValue().isSelected(),
				gui.getCheckBoxShowStatistics().isSelected(),
				gui.getCheckBoxVerbose().isSelected(),
				gui.getTextAreaProgressReport().getText()
				);
	
		SubgraphMining.runProcesses();
	}
};