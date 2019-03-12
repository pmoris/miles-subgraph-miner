package com.uantwerp.algorithms;

/** Class governing the GUI
*
* @author Pieter Moris & Danh Bui
* @version 1.0 Feb 2019
*/

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.net.URI;

import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.ActionListener;
import javax.swing.JRadioButtonMenuItem;

public class SubgraphMiningGUI {

	private JFrame frame;
	private JTextField textGraphFile;
	private JTextField textLabels;
	private JTextField textInterestingVertices;
	private JTextField textBackground;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SubgraphMiningGUI window = new SubgraphMiningGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SubgraphMiningGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
//		Define window size
		frame = new JFrame();
		frame.setBounds(100, 100, 758, 640);
		frame.setMinimumSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		Define menu bar and panes		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("Save results");
		menuBar.add(mnFile);
		
		JMenu mnEdit = new JMenu("Export log");
		menuBar.add(mnEdit);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
//		Add link to online readme
		JMenuItem mntmHelpContents = new JMenuItem(new AbstractAction("Visit help page") {
			public void actionPerformed(ActionEvent e) {
		        // Button pressed logic goes here
				String url = new String("https://github.com/pmoris/subgraph-miner/blob/master/README.md");
		    	try {
                    Desktop.getDesktop().browse(new URI(url));
		    	} catch (Exception ex) {
		    		ex.printStackTrace();
		    	}				
			}
		});
		mnHelp.add(mntmHelpContents);
		
//		Add about pop-up
		JMenuItem mntmAbout = new JMenuItem(new AbstractAction("About") {
			public void actionPerformed(ActionEvent e) {
		        // Button pressed logic goes here
				
				try {
					JOptionPane.showMessageDialog(frame, 
							"SigMap v1.0- Adrem Data Lab - 2019");
				} catch (Exception ex) {
		    		ex.printStackTrace();
		    	}		
			}
		});
		mnHelp.add(mntmAbout);
		
//		Define separate toolbar for start button
		JToolBar toolBar = new JToolBar();
		menuBar.add(toolBar);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);
		
//		Define run button
		JButton btnRun = new JButton("Run analysis");
		toolBar.add(btnRun);

//		Define grid for multiple panels
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {30, 100, 100, 250, 20};
		gridBagLayout.rowHeights = new int[] {30, 160, 170, 200, 30, 20};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
//		First panel of input files
		JPanel pnlInput = new JPanel();
		pnlInput.setBorder(new TitledBorder(null, "Input Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_pnlInput = new GridBagConstraints();
		gbc_pnlInput.gridwidth = 3;
		gbc_pnlInput.fill = GridBagConstraints.BOTH;
		gbc_pnlInput.insets = new Insets(0, 0, 5, 0);
		gbc_pnlInput.gridx = 1;
		gbc_pnlInput.gridy = 1;
		frame.getContentPane().add(pnlInput, gbc_pnlInput);
		GridBagLayout gbl_pnlInput = new GridBagLayout();
		gbl_pnlInput.columnWidths = new int[]{80, 400, 0, 0};
		gbl_pnlInput.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_pnlInput.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_pnlInput.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlInput.setLayout(gbl_pnlInput);
		
//		Network file
		JLabel lblNetwork = new JLabel("Network:");
		lblNetwork.setToolTipText("Location of the network file");
		GridBagConstraints gbc_lblNetwork = new GridBagConstraints();
		gbc_lblNetwork.anchor = GridBagConstraints.EAST;
		gbc_lblNetwork.insets = new Insets(0, 0, 5, 5);
		gbc_lblNetwork.gridx = 0;
		gbc_lblNetwork.gridy = 0;
		pnlInput.add(lblNetwork, gbc_lblNetwork);
		
		textGraphFile = new JTextField();
		GridBagConstraints gbc_textGraphFile = new GridBagConstraints();
		gbc_textGraphFile.insets = new Insets(0, 0, 5, 5);
		gbc_textGraphFile.fill = GridBagConstraints.HORIZONTAL;
		gbc_textGraphFile.gridx = 1;
		gbc_textGraphFile.gridy = 0;
		pnlInput.add(textGraphFile, gbc_textGraphFile);
		textGraphFile.setColumns(10);
		
		JButton buttonGraph = new JButton("...");
		GridBagConstraints gbc_buttonGraphFile = new GridBagConstraints();
		gbc_buttonGraphFile.anchor = GridBagConstraints.WEST;
		gbc_buttonGraphFile.insets = new Insets(0, 0, 5, 0);
		gbc_buttonGraphFile.gridx = 2;
		gbc_buttonGraphFile.gridy = 0;
		pnlInput.add(buttonGraph, gbc_buttonGraphFile);

//		Interesting vertices file
		JLabel lblInterestingVertices = new JLabel("Nodes of interest:");
		lblInterestingVertices.setToolTipText("Location of the nodes of interest file");
		GridBagConstraints gbc_lblInterestingVertices = new GridBagConstraints();
		gbc_lblInterestingVertices.anchor = GridBagConstraints.EAST;
		gbc_lblInterestingVertices.insets = new Insets(0, 0, 5, 5);
		gbc_lblInterestingVertices.gridx = 0;
		gbc_lblInterestingVertices.gridy = 1;
		pnlInput.add(lblInterestingVertices, gbc_lblInterestingVertices);
		
		textInterestingVertices = new JTextField();
		GridBagConstraints gbc_textInterestingVertices = new GridBagConstraints();
		gbc_textInterestingVertices.insets = new Insets(0, 0, 5, 5);
		gbc_textInterestingVertices.fill = GridBagConstraints.HORIZONTAL;
		gbc_textInterestingVertices.gridx = 1;
		gbc_textInterestingVertices.gridy = 1;
		pnlInput.add(textInterestingVertices, gbc_textInterestingVertices);
		textInterestingVertices.setColumns(10);
		
		JButton buttonInterestingVertices = new JButton("...");
		GridBagConstraints gbc_buttonInterestingVertices = new GridBagConstraints();
		gbc_buttonInterestingVertices.anchor = GridBagConstraints.WEST;
		gbc_buttonInterestingVertices.insets = new Insets(0, 0, 5, 0);
		gbc_buttonInterestingVertices.gridx = 2;
		gbc_buttonInterestingVertices.gridy = 1;
		pnlInput.add(buttonInterestingVertices, gbc_buttonInterestingVertices);
		
//		Labels file
		JLabel lblLabels = new JLabel("Labels (optional):");
		lblLabels.setToolTipText("Location of the labels file (optional)");
		GridBagConstraints gbc_lblLabels = new GridBagConstraints();
		gbc_lblLabels.anchor = GridBagConstraints.EAST;
		gbc_lblLabels.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabels.gridx = 0;
		gbc_lblLabels.gridy = 2;
		pnlInput.add(lblLabels, gbc_lblLabels);
		
		textLabels = new JTextField();
		GridBagConstraints gbc_textLabels = new GridBagConstraints();
		gbc_textLabels.insets = new Insets(0, 0, 5, 5);
		gbc_textLabels.anchor = GridBagConstraints.WEST;
		gbc_textLabels.fill = GridBagConstraints.HORIZONTAL;
		gbc_textLabels.gridx = 1;
		gbc_textLabels.gridy = 2;
		pnlInput.add(textLabels, gbc_textLabels);
		textLabels.setColumns(10);
		
		JButton buttonLabels = new JButton("...");
		GridBagConstraints gbc_buttonLabels = new GridBagConstraints();
		gbc_buttonLabels.anchor = GridBagConstraints.WEST;
		gbc_buttonLabels.insets = new Insets(0, 0, 5, 0);
		gbc_buttonLabels.gridx = 2;
		gbc_buttonLabels.gridy = 2;
		pnlInput.add(buttonLabels, gbc_buttonLabels);
		
//		Background file
		JLabel lblBackground = new JLabel("Reduced background (optional):");
		lblBackground.setToolTipText("Location of the reduced background file (optional)");
		GridBagConstraints gbc_lblBackground = new GridBagConstraints();
		gbc_lblBackground.anchor = GridBagConstraints.EAST;
		gbc_lblBackground.insets = new Insets(0, 0, 0, 5);
		gbc_lblBackground.gridx = 0;
		gbc_lblBackground.gridy = 3;
		pnlInput.add(lblBackground, gbc_lblBackground);
		
		textBackground = new JTextField();
		GridBagConstraints gbc_testBackground = new GridBagConstraints();
		gbc_testBackground.insets = new Insets(0, 0, 0, 5);
		gbc_testBackground.fill = GridBagConstraints.HORIZONTAL;
		gbc_testBackground.gridx = 1;
		gbc_testBackground.gridy = 3;
		pnlInput.add(textBackground, gbc_testBackground);
		textBackground.setColumns(10);
		
		JButton buttonBackground = new JButton("...");
		GridBagConstraints gbc_buttonBackground = new GridBagConstraints();
		gbc_buttonBackground.anchor = GridBagConstraints.WEST;
		gbc_buttonBackground.gridx = 2;
		gbc_buttonBackground.gridy = 3;
		pnlInput.add(buttonBackground, gbc_buttonBackground);
		
//		Second panel for options
		JPanel pnlOptions1 = new JPanel();
		pnlOptions1.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_pnlOptions1 = new GridBagConstraints();
		gbc_pnlOptions1.gridwidth = 2;
		gbc_pnlOptions1.fill = GridBagConstraints.BOTH;
		gbc_pnlOptions1.insets = new Insets(0, 0, 5, 5);
		gbc_pnlOptions1.gridx = 1;
		gbc_pnlOptions1.gridy = 2;
		frame.getContentPane().add(pnlOptions1, gbc_pnlOptions1);
		GridBagLayout gbl_pnlOptions1 = new GridBagLayout();
		gbl_pnlOptions1.columnWidths = new int[]{0, 0, 0};
		gbl_pnlOptions1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_pnlOptions1.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_pnlOptions1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlOptions1.setLayout(gbl_pnlOptions1);
		
		JLabel lblPvalue = new JLabel("Minimum support:");
		GridBagConstraints gbc_lblPvalue = new GridBagConstraints();
		gbc_lblPvalue.insets = new Insets(0, 0, 5, 5);
		gbc_lblPvalue.anchor = GridBagConstraints.EAST;
		gbc_lblPvalue.gridx = 0;
		gbc_lblPvalue.gridy = 0;
		pnlOptions1.add(lblPvalue, gbc_lblPvalue);
		
		textField_3 = new JTextField();
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.insets = new Insets(0, 0, 5, 0);
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 0;
		pnlOptions1.add(textField_3, gbc_textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("p-value:");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 1;
		pnlOptions1.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		textField_4 = new JTextField();
		GridBagConstraints gbc_textField_4 = new GridBagConstraints();
		gbc_textField_4.insets = new Insets(0, 0, 5, 0);
		gbc_textField_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_4.gridx = 1;
		gbc_textField_4.gridy = 1;
		pnlOptions1.add(textField_4, gbc_textField_4);
		textField_4.setColumns(10);
		
		JLabel lblNumberOfVertices = new JLabel("Number of vertices:");
		GridBagConstraints gbc_lblNumberOfVertices = new GridBagConstraints();
		gbc_lblNumberOfVertices.anchor = GridBagConstraints.EAST;
		gbc_lblNumberOfVertices.insets = new Insets(0, 0, 5, 5);
		gbc_lblNumberOfVertices.gridx = 0;
		gbc_lblNumberOfVertices.gridy = 2;
		pnlOptions1.add(lblNumberOfVertices, gbc_lblNumberOfVertices);
		
		textField_5 = new JTextField();
		GridBagConstraints gbc_textField_5 = new GridBagConstraints();
		gbc_textField_5.insets = new Insets(0, 0, 5, 0);
		gbc_textField_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_5.gridx = 1;
		gbc_textField_5.gridy = 2;
		pnlOptions1.add(textField_5, gbc_textField_5);
		textField_5.setColumns(10);
		
		JLabel lblAlgorithm = new JLabel("Algorithm:");
		GridBagConstraints gbc_lblAlgorithm = new GridBagConstraints();
		gbc_lblAlgorithm.anchor = GridBagConstraints.EAST;
		gbc_lblAlgorithm.insets = new Insets(0, 0, 0, 5);
		gbc_lblAlgorithm.gridx = 0;
		gbc_lblAlgorithm.gridy = 3;
		pnlOptions1.add(lblAlgorithm, gbc_lblAlgorithm);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		pnlOptions1.add(comboBox, gbc_comboBox);
		
		JPanel pnlOptions2 = new JPanel();
		pnlOptions2.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_pnlOptions2 = new GridBagConstraints();
		gbc_pnlOptions2.insets = new Insets(0, 0, 5, 0);
		gbc_pnlOptions2.fill = GridBagConstraints.BOTH;
		gbc_pnlOptions2.gridx = 3;
		gbc_pnlOptions2.gridy = 2;
		frame.getContentPane().add(pnlOptions2, gbc_pnlOptions2);
		GridBagLayout gbl_pnlOptions2 = new GridBagLayout();
		gbl_pnlOptions2.columnWidths = new int[]{0, 0};
		gbl_pnlOptions2.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_pnlOptions2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_pnlOptions2.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		pnlOptions2.setLayout(gbl_pnlOptions2);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Run with single label ");
		GridBagConstraints gbc_rdbtnNewRadioButton = new GridBagConstraints();
		gbc_rdbtnNewRadioButton.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton.gridx = 0;
		gbc_rdbtnNewRadioButton.gridy = 0;
		pnlOptions2.add(rdbtnNewRadioButton, gbc_rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Undirected graph");
		GridBagConstraints gbc_rdbtnNewRadioButton_1 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNewRadioButton_1.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_1.gridx = 0;
		gbc_rdbtnNewRadioButton_1.gridy = 1;
		pnlOptions2.add(rdbtnNewRadioButton_1, gbc_rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNestedPvalue = new JRadioButton("Nested p-value");
		GridBagConstraints gbc_rdbtnNestedPvalue = new GridBagConstraints();
		gbc_rdbtnNestedPvalue.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnNestedPvalue.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNestedPvalue.gridx = 0;
		gbc_rdbtnNestedPvalue.gridy = 2;
		pnlOptions2.add(rdbtnNestedPvalue, gbc_rdbtnNestedPvalue);
		
		JRadioButton rdbtnStatistical = new JRadioButton("Memory statistics");
		GridBagConstraints gbc_rdbtnStatistical = new GridBagConstraints();
		gbc_rdbtnStatistical.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnStatistical.anchor = GridBagConstraints.WEST;
		gbc_rdbtnStatistical.gridx = 0;
		gbc_rdbtnStatistical.gridy = 3;
		pnlOptions2.add(rdbtnStatistical, gbc_rdbtnStatistical);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Verbose");
		GridBagConstraints gbc_rdbtnNewRadioButton_2 = new GridBagConstraints();
		gbc_rdbtnNewRadioButton_2.anchor = GridBagConstraints.WEST;
		gbc_rdbtnNewRadioButton_2.gridx = 0;
		gbc_rdbtnNewRadioButton_2.gridy = 4;
		pnlOptions2.add(rdbtnNewRadioButton_2, gbc_rdbtnNewRadioButton_2);
		
//		Define report panel
		JPanel panelReport = new JPanel();
		panelReport.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Progress Report", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelReport = new GridBagConstraints();
		gbc_panelReport.gridwidth = 3;
		gbc_panelReport.insets = new Insets(0, 0, 5, 0);
		gbc_panelReport.fill = GridBagConstraints.BOTH;
		gbc_panelReport.gridx = 1;
		gbc_panelReport.gridy = 3;
		frame.getContentPane().add(panelReport, gbc_panelReport);
		panelReport.setLayout(new BorderLayout(0, 0));
		
		JTextArea reportArea = new JTextArea();
		panelReport.add(reportArea);
		
//		Define progress and status panel
		JPanel panelProgress = new JPanel();
		GridBagConstraints gbc_panelProgress = new GridBagConstraints();
		gbc_panelProgress.gridwidth = 3;
		gbc_panelProgress.insets = new Insets(0, 0, 0, 5);
		gbc_panelProgress.fill = GridBagConstraints.BOTH;
		gbc_panelProgress.gridx = 1;
		gbc_panelProgress.gridy = 4;
		frame.getContentPane().add(panelProgress, gbc_panelProgress);
		GridBagLayout gbl_panelProgress = new GridBagLayout();
		gbl_panelProgress.columnWidths = new int[] {250, 351, 0};
		gbl_panelProgress.rowHeights = new int[]{100, 0};
//		Double weight for progress bar so it takes up two columns
		gbl_panelProgress.columnWeights = new double[]{0.0, 1, Double.MIN_VALUE};
		gbl_panelProgress.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelProgress.setLayout(gbl_panelProgress);
		
//		Progress bar
		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.BOTH;
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 0;
		panelProgress.add(progressBar, gbc_progressBar);
		
//		Status panel
		JLabel lblStatus = new JLabel("status");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.fill = GridBagConstraints.BOTH;
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 0;
		panelProgress.add(lblStatus, gbc_lblStatus);
	}

}
