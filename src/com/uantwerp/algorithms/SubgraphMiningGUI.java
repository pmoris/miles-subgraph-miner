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
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JFormattedTextField;
import javax.swing.JTextPane;
import javax.swing.JInternalFrame;

public class SubgraphMiningGUI {

	private JFrame frame;
	private JTextField textGraphFile;
	private JTextField textLabels;
	private JTextField textInterestingVertices;
	private JTextField textBackground;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_7;

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
		frame.setBounds(100, 100, 400, 1000);
		frame.setMinimumSize(new Dimension(600, 400));
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
		gridBagLayout.columnWidths = new int[] {30, 50, 123, 100, 250, 50, 20};
		gridBagLayout.rowHeights = new int[] {30, 331, 0, 185, 0, 68, 0, 100, 0, 200, 0, 30, 20};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
//		First panel of input files
		JPanel pnlInput = new JPanel();
		pnlInput.setBorder(new TitledBorder(null, "Input Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_pnlInput = new GridBagConstraints();
		gbc_pnlInput.gridwidth = 3;
		gbc_pnlInput.fill = GridBagConstraints.BOTH;
		gbc_pnlInput.insets = new Insets(0, 0, 5, 5);
		gbc_pnlInput.gridx = 2;
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
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.gridwidth = 3;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 3;
		frame.getContentPane().add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		JLabel label_1 = new JLabel("Minimum support:");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 0;
		panel_1.add(label_1, gbc_label_1);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 0;
		panel_1.add(textField_1, gbc_textField_1);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.gridheight = 4;
		gbc_horizontalStrut.insets = new Insets(0, 0, 0, 5);
		gbc_horizontalStrut.gridx = 2;
		gbc_horizontalStrut.gridy = 0;
		panel_1.add(horizontalStrut, gbc_horizontalStrut);
		
		JRadioButton radioButton = new JRadioButton("Run with single label ");
		GridBagConstraints gbc_radioButton = new GridBagConstraints();
		gbc_radioButton.anchor = GridBagConstraints.WEST;
		gbc_radioButton.insets = new Insets(0, 0, 5, 0);
		gbc_radioButton.gridx = 3;
		gbc_radioButton.gridy = 0;
		panel_1.add(radioButton, gbc_radioButton);
		
		JLabel label_2 = new JLabel("p-value:");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 1;
		panel_1.add(label_2, gbc_label_2);
		
		textField_2 = new JTextField("0.05");
		textField_2.setColumns(10);
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.insets = new Insets(0, 0, 5, 5);
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 1;
		panel_1.add(textField_2, gbc_textField_2);
		
		JRadioButton radioButton_1 = new JRadioButton("Undirected graph");
		GridBagConstraints gbc_radioButton_1 = new GridBagConstraints();
		gbc_radioButton_1.anchor = GridBagConstraints.WEST;
		gbc_radioButton_1.insets = new Insets(0, 0, 5, 0);
		gbc_radioButton_1.gridx = 3;
		gbc_radioButton_1.gridy = 1;
		panel_1.add(radioButton_1, gbc_radioButton_1);
		
		JLabel label_3 = new JLabel("Number of vertices:");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 2;
		panel_1.add(label_3, gbc_label_3);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 2;
		panel_1.add(textField_3, gbc_textField_3);
		
		JRadioButton radioButton_2 = new JRadioButton("Nested p-value", true);
		GridBagConstraints gbc_radioButton_2 = new GridBagConstraints();
		gbc_radioButton_2.anchor = GridBagConstraints.WEST;
		gbc_radioButton_2.insets = new Insets(0, 0, 5, 0);
		gbc_radioButton_2.gridx = 3;
		gbc_radioButton_2.gridy = 2;
		panel_1.add(radioButton_2, gbc_radioButton_2);
		
		JLabel label_4 = new JLabel("Algorithm:");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.anchor = GridBagConstraints.EAST;
		gbc_label_4.insets = new Insets(0, 0, 0, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 3;
		panel_1.add(label_4, gbc_label_4);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		panel_1.add(comboBox, gbc_comboBox);
		
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder(null, "Output files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_outputPanel = new GridBagConstraints();
		gbc_outputPanel.gridwidth = 3;
		gbc_outputPanel.insets = new Insets(0, 0, 5, 5);
		gbc_outputPanel.fill = GridBagConstraints.BOTH;
		gbc_outputPanel.gridx = 2;
		gbc_outputPanel.gridy = 5;
		frame.getContentPane().add(outputPanel, gbc_outputPanel);
		GridBagLayout gbl_outputPanel = new GridBagLayout();
		gbl_outputPanel.columnWidths = new int[]{80, 400, 0, 0};
		gbl_outputPanel.rowHeights = new int[]{0, 0};
		gbl_outputPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_outputPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		outputPanel.setLayout(gbl_outputPanel);
		
		JLabel label_8 = new JLabel("Savepath:");
		label_8.setToolTipText("Location where output files will be stored.");
		GridBagConstraints gbc_label_8 = new GridBagConstraints();
		gbc_label_8.anchor = GridBagConstraints.EAST;
		gbc_label_8.insets = new Insets(0, 0, 0, 5);
		gbc_label_8.gridx = 0;
		gbc_label_8.gridy = 0;
		outputPanel.add(label_8, gbc_label_8);
		
		textField_7 = new JTextField();
		textField_7.setColumns(10);
		GridBagConstraints gbc_textField_7 = new GridBagConstraints();
		gbc_textField_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_7.insets = new Insets(0, 0, 0, 5);
		gbc_textField_7.gridx = 1;
		gbc_textField_7.gridy = 0;
		outputPanel.add(textField_7, gbc_textField_7);
		
		JButton button_3 = new JButton("...");
		GridBagConstraints gbc_button_3 = new GridBagConstraints();
		gbc_button_3.anchor = GridBagConstraints.WEST;
		gbc_button_3.gridx = 2;
		gbc_button_3.gridy = 0;
		outputPanel.add(button_3, gbc_button_3);
		
//		Advanced options split pane
		JSplitPane advancedOptionsSplitPane = new JSplitPane();
		advancedOptionsSplitPane.setBorder(new TitledBorder(null, "Advanced options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_advancedOptionsSplitPane = new GridBagConstraints();
		gbc_advancedOptionsSplitPane.gridwidth = 3;
		gbc_advancedOptionsSplitPane.insets = new Insets(0, 0, 5, 5);
		gbc_advancedOptionsSplitPane.fill = GridBagConstraints.BOTH;
		gbc_advancedOptionsSplitPane.gridx = 2;
		gbc_advancedOptionsSplitPane.gridy = 7;
		frame.getContentPane().add(advancedOptionsSplitPane, gbc_advancedOptionsSplitPane);
		
		JPanel panel = new JPanel();
		advancedOptionsSplitPane.setRightComponent(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 10, 200};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel label = new JLabel("Minimum support:");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 1;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.gridwidth = 2;
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 1;
		panel.add(verticalStrut, gbc_verticalStrut);
		
		JRadioButton rdbtnShowStatistics = new JRadioButton("Show memory statistics");
		GridBagConstraints gbc_rdbtnShowStatistics = new GridBagConstraints();
		gbc_rdbtnShowStatistics.insets = new Insets(0, 0, 5, 0);
		gbc_rdbtnShowStatistics.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnShowStatistics.gridx = 1;
		gbc_rdbtnShowStatistics.gridy = 2;
		gbc_rdbtnShowStatistics.gridwidth = 2;
		panel.add(rdbtnShowStatistics, gbc_rdbtnShowStatistics);
		
		JRadioButton rdbtnVerbose = new JRadioButton("Verbose");
		GridBagConstraints gbc_rdbtnVerbose = new GridBagConstraints();
		gbc_rdbtnVerbose.anchor = GridBagConstraints.WEST;
		gbc_rdbtnVerbose.gridwidth = 2;
		gbc_rdbtnVerbose.gridx = 1;
		gbc_rdbtnVerbose.gridy = 3;
		panel.add(rdbtnVerbose, gbc_rdbtnVerbose);
		
		
//		Toggle button for advanced options
		JToggleButton tglbtnShow = new JToggleButton("Hide");
		advancedOptionsSplitPane.setLeftComponent(tglbtnShow);
		tglbtnShow.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				 if (panel.isVisible()) {
				 	panel.setVisible(false);
				 	tglbtnShow.setText("Show");
	            } else {
	            	panel.setVisible(true);
				 	tglbtnShow.setText("Hide");
	            }
			 }
		});
		
//		Define report panel
		JPanel panelReport = new JPanel();
		panelReport.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Progress Report", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelReport = new GridBagConstraints();
		gbc_panelReport.gridwidth = 3;
		gbc_panelReport.insets = new Insets(0, 0, 5, 5);
		gbc_panelReport.fill = GridBagConstraints.BOTH;
		gbc_panelReport.gridx = 2;
		gbc_panelReport.gridy = 9;
		frame.getContentPane().add(panelReport, gbc_panelReport);
		panelReport.setLayout(new BorderLayout(0, 0));
		
		JTextArea reportArea = new JTextArea();
		panelReport.add(reportArea);
		
//		Define progress and status panel
		JPanel panelProgress = new JPanel();
		GridBagConstraints gbc_panelProgress = new GridBagConstraints();
		gbc_panelProgress.insets = new Insets(0, 0, 0, 5);
		gbc_panelProgress.gridwidth = 3;
		gbc_panelProgress.fill = GridBagConstraints.BOTH;
		gbc_panelProgress.gridx = 2;
		gbc_panelProgress.gridy = 11;
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
		gbc_lblStatus.anchor = GridBagConstraints.NORTH;
		gbc_lblStatus.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 0;
		panelProgress.add(lblStatus, gbc_lblStatus);
	}
}
