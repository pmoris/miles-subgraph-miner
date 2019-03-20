package com.uantwerp.gui;

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
import javax.swing.JCheckBox;
import javax.swing.JProgressBar;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.uantwerp.algorithms.ParameterConfig;

import java.awt.event.ActionEvent;
import java.net.URI;

import javax.swing.JComboBox;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JToolBar;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JTabbedPane;

public class SubgraphMiningGUI {

	private JFrame frame;
	private JTextField textGraphFile;
	private JTextField textLabels;
	private JTextField textInterestingVertices;
	private JTextField textBackground;
	private JTextField textFieldSupport;
	private JTextField textFieldPValue;
	private JTextField textFieldVerticesSize;
	private JTextField textFieldSavePath;

	/**
	 * Launch the application.
	 */
	public static void launchGUI(String[] args) {
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
		frame.setBounds(100, 100, 400, 800);
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
		gridBagLayout.columnWidths = new int[] {30, 10, 123, 100, 250, 10, 20};
		gridBagLayout.rowHeights = new int[] {10, 200, 10, 150, 10, 75, 100, 0, 20, 20};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 1.0, 0.0, 1.0, 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		frame.getContentPane().setLayout(gridBagLayout);
		
//		First panel of input files
		JPanel pnlInput = new JPanel();
		pnlInput.setBorder(new TitledBorder(null, "Input Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_pnlInput = new GridBagConstraints();
		gbc_pnlInput.gridwidth = 3;
		gbc_pnlInput.fill = GridBagConstraints.HORIZONTAL;
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
//		Browse for file
		buttonGraph.addActionListener(new OpenFileAction(textGraphFile, frame));
//		Set layout
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
		
//		Options pane
		JTabbedPane tabbedPaneOptions = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.gridwidth = 3;
		gbc_tabbedPane.insets = new Insets(0, 0, 5, 5);
		gbc_tabbedPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_tabbedPane.gridx = 2;
		gbc_tabbedPane.gridy = 3;
		frame.getContentPane().add(tabbedPaneOptions, gbc_tabbedPane);
		
//		Options panel
		JPanel panelOptions = new JPanel();
		tabbedPaneOptions.addTab("Options", null, panelOptions, null);
		panelOptions.setBorder(new TitledBorder(null, null, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_panelOptions = new GridBagLayout();
		gbl_panelOptions.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panelOptions.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_panelOptions.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelOptions.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelOptions.setLayout(gbl_panelOptions);
		
////		Support field
//		JLabel labelSupport = new JLabel("Minimum support:");
//		GridBagConstraints gbc_labelSupport = new GridBagConstraints();
//		gbc_labelSupport.anchor = GridBagConstraints.EAST;
//		gbc_labelSupport.insets = new Insets(0, 0, 5, 5);
//		gbc_labelSupport.gridx = 0;
//		gbc_labelSupport.gridy = 0;
//		panelOptions.add(labelSupport, gbc_labelSupport);
//		
//		textFieldSupport = new JTextField();
//		textFieldSupport.setColumns(10);
//		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
//		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
//		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
//		gbc_textField_1.gridx = 1;
//		gbc_textField_1.gridy = 0;
//		panelOptions.add(textFieldSupport, gbc_textField_1);
		
		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.gridheight = 4;
		gbc_horizontalStrut.insets = new Insets(0, 0, 0, 5);
		gbc_horizontalStrut.gridx = 2;
		gbc_horizontalStrut.gridy = 0;
		panelOptions.add(horizontalStrut, gbc_horizontalStrut);
		
//		P-value
		JLabel labelPValue = new JLabel("p-value:");
		GridBagConstraints gbc_labelPValue = new GridBagConstraints();
		gbc_labelPValue.anchor = GridBagConstraints.EAST;
		gbc_labelPValue.insets = new Insets(0, 0, 5, 5);
		gbc_labelPValue.gridx = 0;
		gbc_labelPValue.gridy = 1;
		panelOptions.add(labelPValue, gbc_labelPValue);
		
		textFieldPValue = new JTextField("0.05");
		textFieldPValue.setColumns(10);
		GridBagConstraints gbc_textFieldPValue = new GridBagConstraints();
		gbc_textFieldPValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPValue.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPValue.gridx = 1;
		gbc_textFieldPValue.gridy = 1;
		panelOptions.add(textFieldPValue, gbc_textFieldPValue);
		
//		Single label
		JCheckBox radioButtonSingleLabel = new JCheckBox("Run with single label ");
		GridBagConstraints gbc_radioButtonSingleLabel = new GridBagConstraints();
		gbc_radioButtonSingleLabel.anchor = GridBagConstraints.WEST;
		gbc_radioButtonSingleLabel.insets = new Insets(0, 0, 5, 0);
		gbc_radioButtonSingleLabel.gridx = 3;
		gbc_radioButtonSingleLabel.gridy = 1;
		panelOptions.add(radioButtonSingleLabel, gbc_radioButtonSingleLabel);
		
//		Vertices
		JLabel labelVerticesSize = new JLabel("Maximum motif size:");
		labelVerticesSize.setToolTipText("Maximum number of nodes in a motif.");
		GridBagConstraints gbc_labelVerticesSize = new GridBagConstraints();
		gbc_labelVerticesSize.anchor = GridBagConstraints.EAST;
		gbc_labelVerticesSize.insets = new Insets(0, 0, 5, 5);
		gbc_labelVerticesSize.gridx = 0;
		gbc_labelVerticesSize.gridy = 2;
		panelOptions.add(labelVerticesSize, gbc_labelVerticesSize);
		
		textFieldVerticesSize = new JTextField();
		textFieldVerticesSize.setColumns(10);
		GridBagConstraints gbc_textField_3 = new GridBagConstraints();
		gbc_textField_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_3.insets = new Insets(0, 0, 5, 5);
		gbc_textField_3.gridx = 1;
		gbc_textField_3.gridy = 2;
		panelOptions.add(textFieldVerticesSize, gbc_textField_3);
		
//		Undirected
		JCheckBox radioButtonUndirected = new JCheckBox("Undirected graph");
		GridBagConstraints gbc_radioButtonUndirected = new GridBagConstraints();
		gbc_radioButtonUndirected.anchor = GridBagConstraints.WEST;
		gbc_radioButtonUndirected.insets = new Insets(0, 0, 5, 0);
		gbc_radioButtonUndirected.gridx = 3;
		gbc_radioButtonUndirected.gridy = 2;
		panelOptions.add(radioButtonUndirected, gbc_radioButtonUndirected);
		
//		Algorithm
		JLabel labelAlgorithm = new JLabel("Algorithm:");
		GridBagConstraints gbc_labelAlgorithm = new GridBagConstraints();
		gbc_labelAlgorithm.anchor = GridBagConstraints.EAST;
		gbc_labelAlgorithm.insets = new Insets(0, 0, 0, 5);
		gbc_labelAlgorithm.gridx = 0;
		gbc_labelAlgorithm.gridy = 3;
		panelOptions.add(labelAlgorithm, gbc_labelAlgorithm);
		
		JComboBox<String> comboBox = new JComboBox<>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		panelOptions.add(comboBox, gbc_comboBox);
		comboBox.addItem("Base");
		comboBox.addItem("gSpan");
		comboBox.addItem("FSG");

//		comboBox.addItem(new ComboItem("Visible String 1", "Value 1"));
//		comboBox.addItem(new ComboItem("Visible String 2", "Value 2"));
//		comboBox.addItem(new ComboItem("Visible String 3", "Value 3"));
		
//		Nested P-values
		JCheckBox radioButtonNested = new JCheckBox("Nested p-value", true);
		GridBagConstraints gbc_radioButtonNested = new GridBagConstraints();
		gbc_radioButtonNested.anchor = GridBagConstraints.WEST;
		gbc_radioButtonNested.gridx = 3;
		gbc_radioButtonNested.gridy = 3;
		panelOptions.add(radioButtonNested, gbc_radioButtonNested);
		
//		Advanced options
		JPanel panelAdvanced = new JPanel();
		tabbedPaneOptions.addTab("Advanced options", null, panelAdvanced, null);
		GridBagLayout gbl_panelAdvanced = new GridBagLayout();
		gbl_panelAdvanced.columnWidths = new int[]{10, 10, 200, 10};
		gbl_panelAdvanced.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelAdvanced.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE, 0.0};
		gbl_panelAdvanced.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelAdvanced.setLayout(gbl_panelAdvanced);
		
		JLabel labelSupport2 = new JLabel("Minimum support:");
		GridBagConstraints gbc_labelSupport2 = new GridBagConstraints();
		gbc_labelSupport2.insets = new Insets(0, 0, 5, 5);
		gbc_labelSupport2.gridx = 1;
		gbc_labelSupport2.gridy = 1;
		panelAdvanced.add(labelSupport2, gbc_labelSupport2);
		
		textFieldSupport = new JTextField();
		GridBagConstraints gbc_textFieldSupport = new GridBagConstraints();
		gbc_textFieldSupport.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldSupport.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSupport.gridx = 2;
		gbc_textFieldSupport.gridy = 1;
		panelAdvanced.add(textFieldSupport, gbc_textFieldSupport);
		textFieldSupport.setColumns(10);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.gridwidth = 2;
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 2;
		panelAdvanced.add(verticalStrut, gbc_verticalStrut);
		
		JCheckBox rdbtnShowStatistics = new JCheckBox("Show memory statistics");
		GridBagConstraints gbc_rdbtnShowStatistics = new GridBagConstraints();
		gbc_rdbtnShowStatistics.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnShowStatistics.anchor = GridBagConstraints.NORTHWEST;
		gbc_rdbtnShowStatistics.gridx = 1;
		gbc_rdbtnShowStatistics.gridy = 3;
		gbc_rdbtnShowStatistics.gridwidth = 2;
		panelAdvanced.add(rdbtnShowStatistics, gbc_rdbtnShowStatistics);
		
		JCheckBox rdbtnVerbose = new JCheckBox("Verbose");
		GridBagConstraints gbc_rdbtnVerbose = new GridBagConstraints();
		gbc_rdbtnVerbose.insets = new Insets(0, 0, 0, 5);
		gbc_rdbtnVerbose.anchor = GridBagConstraints.WEST;
		gbc_rdbtnVerbose.gridwidth = 2;
		gbc_rdbtnVerbose.gridx = 1;
		gbc_rdbtnVerbose.gridy = 4;
		panelAdvanced.add(rdbtnVerbose, gbc_rdbtnVerbose);
		
		
		
		
		
		
		
//		Output files panel
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder(null, "Output files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_outputPanel = new GridBagConstraints();
		gbc_outputPanel.gridwidth = 3;
		gbc_outputPanel.insets = new Insets(0, 0, 5, 5);
		gbc_outputPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_outputPanel.gridx = 2;
		gbc_outputPanel.gridy = 5;
		frame.getContentPane().add(outputPanel, gbc_outputPanel);
		GridBagLayout gbl_outputPanel = new GridBagLayout();
		gbl_outputPanel.columnWidths = new int[]{80, 400, 0, 0};
		gbl_outputPanel.rowHeights = new int[]{0, 0};
		gbl_outputPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_outputPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		outputPanel.setLayout(gbl_outputPanel);
		
		JLabel labelSavePath = new JLabel("Savepath:");
		labelSavePath.setToolTipText("Location where output files will be stored.");
		GridBagConstraints gbc_labelSavePath = new GridBagConstraints();
		gbc_labelSavePath.anchor = GridBagConstraints.EAST;
		gbc_labelSavePath.insets = new Insets(0, 0, 0, 5);
		gbc_labelSavePath.gridx = 0;
		gbc_labelSavePath.gridy = 0;
		outputPanel.add(labelSavePath, gbc_labelSavePath);
		
		textFieldSavePath = new JTextField();
		textFieldSavePath.setColumns(10);
		GridBagConstraints gbc_textFieldSavePath = new GridBagConstraints();
		gbc_textFieldSavePath.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSavePath.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldSavePath.gridx = 1;
		gbc_textFieldSavePath.gridy = 0;
		outputPanel.add(textFieldSavePath, gbc_textFieldSavePath);
		
		JButton buttonSavePath = new JButton("...");
		GridBagConstraints gbc_buttonSavePath = new GridBagConstraints();
		gbc_buttonSavePath.anchor = GridBagConstraints.WEST;
		gbc_buttonSavePath.gridx = 2;
		gbc_buttonSavePath.gridy = 0;
		outputPanel.add(buttonSavePath, gbc_buttonSavePath);
		
//		Define report panel
		JPanel panelReport = new JPanel();
		panelReport.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Progress Report", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelReport = new GridBagConstraints();
		gbc_panelReport.gridwidth = 3;
		gbc_panelReport.insets = new Insets(0, 0, 5, 5);
		gbc_panelReport.fill = GridBagConstraints.BOTH;
		gbc_panelReport.gridx = 2;
		gbc_panelReport.gridy = 6;
		frame.getContentPane().add(panelReport, gbc_panelReport);
		panelReport.setLayout(new BorderLayout(0, 0));
		
		JTextArea reportArea = new JTextArea();
		panelReport.add(reportArea);
		
//		Define progress and status panel
		JPanel panelProgress = new JPanel();
		GridBagConstraints gbc_panelProgress = new GridBagConstraints();
		gbc_panelProgress.insets = new Insets(0, 0, 0, 5);
		gbc_panelProgress.gridwidth = 3;
		gbc_panelProgress.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelProgress.gridx = 2;
		gbc_panelProgress.gridy = 8;
		frame.getContentPane().add(panelProgress, gbc_panelProgress);
		GridBagLayout gbl_panelProgress = new GridBagLayout();
		gbl_panelProgress.columnWidths = new int[] {75, 351, 0};
		gbl_panelProgress.rowHeights = new int[]{100, 0};
//		Double weight for progress bar so it takes up two columns
		gbl_panelProgress.columnWeights = new double[]{0.0, 1, Double.MIN_VALUE};
		gbl_panelProgress.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelProgress.setLayout(gbl_panelProgress);
				
//		Progress bar
		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.insets = new Insets(0, 0, 0, 5);
		gbc_progressBar.gridx = 1;
		gbc_progressBar.gridy = 0;
		panelProgress.add(progressBar, gbc_progressBar);
				
//		Status panel
		JLabel lblStatus = new JLabel("status");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.gridx = 0;
		gbc_lblStatus.gridy = 0;
		panelProgress.add(lblStatus, gbc_lblStatus);
	}
	
//	Methods
	private void openAction(JTextField textField) {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(frame);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			String filePath = chooser.getSelectedFile().getAbsolutePath();
			textField.setText(filePath);	
		}
	}
}
