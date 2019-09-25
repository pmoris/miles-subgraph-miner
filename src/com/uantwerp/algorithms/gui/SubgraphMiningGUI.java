package com.uantwerp.algorithms.gui;

/** Class governing the GUI
*
* @author Pieter Moris & Danh Bui
* @version 1.0 Feb 2019
*/

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import java.awt.event.ActionEvent;
import java.io.FileWriter;
import java.io.PrintStream;
import java.net.URI;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.border.EtchedBorder;
import java.awt.Color;
import java.awt.Desktop;

import javax.swing.JTextArea;
import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;

public class SubgraphMiningGUI {
	
	private JFrame frame;
	private JTextField textGraphFile;
	private JTextField textLabels;
	private JTextField textInterestingVertices;
	private JTextField textBackground;
	private JTextField textFieldSupport;
	private JTextField textFieldAlpha;
	private JTextField textFieldVerticesSize;
	private JTextField textFieldSavePath;
	private JComboBox<String> comboBoxAlgorithm;
	private JCheckBox checkBoxSingleLabel;
	private JCheckBox checkBoxUndirected;
	private JCheckBox checkBoxNestedPValue;
	private JCheckBox checkBoxVerbose;
	private JTextArea textAreaLog;
	private JCheckBox checkBoxDebug;
	private JCheckBox checkBoxAllPvalues;
	private JButton btnRun;
	private JComboBox<String> comboBoxMultipleTesting;

	public JComboBox<String> getComboBoxMultipleTesting() {
		return comboBoxMultipleTesting;
	}

	public void setComboBoxMultipleTesting(JComboBox<String> comboBoxMultipleTesting) {
		this.comboBoxMultipleTesting = comboBoxMultipleTesting;
	}
	public JCheckBox getCheckBoxDebug() {
		return checkBoxDebug;
	}

	public void setCheckBoxDebug(JCheckBox checkBoxDebug) {
		this.checkBoxDebug = checkBoxDebug;
	}

	public JTextField getTextGraphFile() {
		return textGraphFile;
	}
	
	public String getTextGraphFile2() {
		return textGraphFile.getText();
	}


	public void setTextGraphFile(JTextField textGraphFile) {
		this.textGraphFile = textGraphFile;
	}

	public JTextField getTextLabels() {
		return textLabels;
	}

	public void setTextLabels(JTextField textLabels) {
		this.textLabels = textLabels;
	}

	public JTextField getTextInterestingVertices() {
		return textInterestingVertices;
	}

	public void setTextInterestingVertices(JTextField textInterestingVertices) {
		this.textInterestingVertices = textInterestingVertices;
	}

	public JTextField getTextBackground() {
		return textBackground;
	}

	public void setTextBackground(JTextField textBackground) {
		this.textBackground = textBackground;
	}

	public JTextField getTextFieldSupport() {
		return textFieldSupport;
	}

	public void setTextFieldSupport(JTextField textFieldSupport) {
		this.textFieldSupport = textFieldSupport;
	}

	public JTextField getTextFieldAlpha() {
		return textFieldAlpha;
	}

	public void setTextFieldAlpha(JTextField textFieldAlpha) {
		this.textFieldAlpha = textFieldAlpha;
	}

	public JTextField getTextFieldVerticesSize() {
		return textFieldVerticesSize;
	}

	public void setTextFieldVerticesSize(JTextField textFieldVerticesSize) {
		this.textFieldVerticesSize = textFieldVerticesSize;
	}

	public JTextField getTextFieldSavePath() {
		return textFieldSavePath;
	}

	public void setTextFieldSavePath(JTextField textFieldSavePath) {
		this.textFieldSavePath = textFieldSavePath;
	}

	public JComboBox<String> getComboBoxAlgorithm() {
		return comboBoxAlgorithm;
	}

	public void setComboBoxAlgorithm(JComboBox<String> comboBoxAlgorithm) {
		this.comboBoxAlgorithm = comboBoxAlgorithm;
	}
	
	public JCheckBox getCheckBoxSingleLabel() {
		return checkBoxSingleLabel;
	}

	public void setCheckBoxSingleLabel(JCheckBox checkBoxSingleLabel) {
		this.checkBoxSingleLabel = checkBoxSingleLabel;
	}

	public JCheckBox getCheckBoxAllPvalues() {
		return checkBoxAllPvalues;
	}

	public void setCheckBoxAllPvalues(JCheckBox checkBoxAllPvalues) {
		this.checkBoxAllPvalues = checkBoxAllPvalues;
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JCheckBox getCheckBoxUndirected() {
		return checkBoxUndirected;
	}

	public void setCheckBoxUndirected(JCheckBox checkBoxUndirected) {
		this.checkBoxUndirected = checkBoxUndirected;
	}

	public JCheckBox getCheckBoxNestedPValue() {
		return checkBoxNestedPValue;
	}

	public void setCheckBoxNestedPValue(JCheckBox checkBoxNestedPValue) {
		this.checkBoxNestedPValue = checkBoxNestedPValue;
	}

	public JCheckBox getCheckBoxVerbose() {
		return checkBoxVerbose;
	}

	public void setCheckBoxVerbose(JCheckBox checkBoxVerbose) {
		this.checkBoxVerbose = checkBoxVerbose;
	}

	public JTextArea getTextAreaProgressReport() {
		return textAreaLog;
	}

	public void setTextAreaProgressReport(JTextArea textAreaProgressReport) {
		this.textAreaLog = textAreaProgressReport;
	}
	
	public JButton getRunButton() {
		return this.btnRun;
	}

	/**
	 * Launch the application.
	 */
	public static void launchGUI(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
		SwingUtilities.invokeLater(new Runnable() {	
			@Override
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
	 * Update the application when the analysis start or stop
	 * @param status 
	 */
	public void updateGUI(String status) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (status == "start") {
					btnRun.setText("Stop Analysis");
				}
				else {
					btnRun.setText("Run analysis");
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
		frame.setTitle("M(U)LES - enriched subgraph miner");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		Define menu bar and panes		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JButton btnExportLog = new JButton("Export log");
		btnExportLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String log = textAreaLog.getText();
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Specify a file to save the log");
				int returnVal = chooser.showSaveDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try(FileWriter fw = new FileWriter(chooser.getSelectedFile() + ".txt")) {
					    fw.write(log);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		
//		Define run button
		btnRun = new JButton("Run analysis");
		menuBar.add(btnRun);
		btnRun.addActionListener(new StartButton(SubgraphMiningGUI.this));
		menuBar.add(btnExportLog);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
//		Add link to online readme
		@SuppressWarnings("serial")
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
		@SuppressWarnings("serial")
		JMenuItem mntmAbout = new JMenuItem(new AbstractAction("About") {
			public void actionPerformed(ActionEvent e) {
		        // Button pressed logic goes here
				
				try {
					JOptionPane.showMessageDialog(frame, 
							"M(U)LES v1.0- Adrem Data Lab - 2019");
				} catch (Exception ex) {
		    		ex.printStackTrace();
		    	}		
			}
		});
		mnHelp.add(mntmAbout);

//		Define grid for multiple panels
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		gridBagLayout.columnWidths = new int[] {10, 100, 100, 200, 10};
		gridBagLayout.rowHeights = new int[] {200, 200, 75, 200};
		
//		Main content panel and scroll bar
		JPanel pnlMain = new JPanel();
		pnlMain.setLayout(gridBagLayout);

		JScrollPane scrMain = new JScrollPane(pnlMain);
		scrMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrMain.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
//		Add new scroll pane and embedded main panel to frame
		frame.getContentPane().add(scrMain);
		
//		First panel of input files
		JPanel pnlInput = new JPanel();
		pnlInput.setBorder(new TitledBorder(null, "Input Files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_pnlInput = new GridBagConstraints();
		gbc_pnlInput.gridwidth = 3;
		gbc_pnlInput.fill = GridBagConstraints.HORIZONTAL;
		gbc_pnlInput.insets = new Insets(0, 0, 5, 5);
		gbc_pnlInput.gridx = 1;
		gbc_pnlInput.gridy = 0;
		pnlMain.add(pnlInput, gbc_pnlInput);
		GridBagLayout gbl_pnlInput = new GridBagLayout();
		gbl_pnlInput.columnWeights = new double[]{0.0, 1.0, 0.0};
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
		lblInterestingVertices.setToolTipText("Location of the nodes of interest file (optional - omit for frequent subgraph mining)");
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
//		Browse for file
		buttonInterestingVertices.addActionListener(new OpenFileAction(textInterestingVertices, frame));
		GridBagConstraints gbc_buttonInterestingVertices = new GridBagConstraints();
		gbc_buttonInterestingVertices.anchor = GridBagConstraints.WEST;
		gbc_buttonInterestingVertices.insets = new Insets(0, 0, 5, 0);
		gbc_buttonInterestingVertices.gridx = 2;
		gbc_buttonInterestingVertices.gridy = 1;
		pnlInput.add(buttonInterestingVertices, gbc_buttonInterestingVertices);
		
//		Labels file
		JLabel lblLabels = new JLabel("Labels (optional):");
		lblLabels.setToolTipText("Location of the node labels file (optional).");
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
//		Browse for file
		buttonLabels.addActionListener(new OpenFileAction(textLabels, frame));
		GridBagConstraints gbc_buttonLabels = new GridBagConstraints();
		gbc_buttonLabels.anchor = GridBagConstraints.WEST;
		gbc_buttonLabels.insets = new Insets(0, 0, 5, 0);
		gbc_buttonLabels.gridx = 2;
		gbc_buttonLabels.gridy = 2;
		pnlInput.add(buttonLabels, gbc_buttonLabels);
		
//		Background file
		JLabel lblBackground = new JLabel("Reduced background (optional):");
		lblBackground.setToolTipText("Location of the reduced background file (optional).");
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
//		Browse for file
		buttonBackground.addActionListener(new OpenFileAction(textBackground, frame));
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
		gbc_tabbedPane.gridx = 1;
		gbc_tabbedPane.gridy = 1;
		pnlMain.add(tabbedPaneOptions, gbc_tabbedPane);
		
//		Options panel 1
		JPanel panelOptions = new JPanel();
		tabbedPaneOptions.addTab("Options", null, panelOptions, null);
		panelOptions.setBorder(new TitledBorder(null, null, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagLayout gbl_panelOptions = new GridBagLayout();
		gbl_panelOptions.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0};
		gbl_panelOptions.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		panelOptions.setLayout(gbl_panelOptions);
		
//		P-value
		JLabel labelAlpha = new JLabel("Significance level alpha:");
		labelAlpha.setToolTipText("Significance level to use for p-values (default = 0.05).");
		GridBagConstraints gbc_labelAlpha = new GridBagConstraints();
		gbc_labelAlpha.anchor = GridBagConstraints.EAST;
		gbc_labelAlpha.insets = new Insets(0, 0, 5, 5);
		gbc_labelAlpha.gridx = 0;
		gbc_labelAlpha.gridy = 0;
		panelOptions.add(labelAlpha, gbc_labelAlpha);
		
		textFieldAlpha = new JTextField("0.05");
		textFieldAlpha.setColumns(10);
		GridBagConstraints gbc_textFieldAlpha = new GridBagConstraints();
		gbc_textFieldAlpha.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldAlpha.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldAlpha.gridx = 1;
		gbc_textFieldAlpha.gridy = 0;
		panelOptions.add(textFieldAlpha, gbc_textFieldAlpha);
		
//		Single-label option
		checkBoxSingleLabel = new JCheckBox("Single-label mode");
		checkBoxSingleLabel.setToolTipText("Use this if each node has exactly one label.");
		GridBagConstraints gbc_checkBoxSingleLabel = new GridBagConstraints();
		gbc_checkBoxSingleLabel.anchor = GridBagConstraints.WEST;
		gbc_checkBoxSingleLabel.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxSingleLabel.gridx = 3;
		gbc_checkBoxSingleLabel.gridy = 0;
		panelOptions.add(checkBoxSingleLabel, gbc_checkBoxSingleLabel);
		
//		Multiple testing combobox
		JLabel lblMultipleTestingCorrection = new JLabel("Multiple testing correction method:");
		GridBagConstraints gbc_lblMultipleTestingCorrection = new GridBagConstraints();
		gbc_lblMultipleTestingCorrection.insets = new Insets(0, 0, 5, 5);
		gbc_lblMultipleTestingCorrection.gridx = 0;
		gbc_lblMultipleTestingCorrection.gridy = 1;
		panelOptions.add(lblMultipleTestingCorrection, gbc_lblMultipleTestingCorrection);
		
		comboBoxMultipleTesting = new JComboBox<>();
		GridBagConstraints gbc_comboBoxAlgorithm = new GridBagConstraints();
		gbc_comboBoxAlgorithm.insets = new Insets(0, 0, 0, 5);
		gbc_comboBoxAlgorithm.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBoxAlgorithm.gridx = 1;
		gbc_comboBoxAlgorithm.gridy = 1;
		panelOptions.add(comboBoxMultipleTesting, gbc_comboBoxAlgorithm);
		comboBoxMultipleTesting.addItem("Bonferroni");
		comboBoxMultipleTesting.addItem("Holm");
		comboBoxMultipleTesting.addItem("Benjamini–Hochberg");
		comboBoxMultipleTesting.addItem("Benjamini–Yekutieli");
		
//		undirected network checkbox
		checkBoxUndirected = new JCheckBox("Undirected network");
		checkBoxUndirected.setToolTipText("Treat the network as undirected, e.g. where A -> B = B -> A and self-loops aren't allowed.");
		GridBagConstraints gbc_checkBoxUndirected = new GridBagConstraints();
		gbc_checkBoxUndirected.anchor = GridBagConstraints.WEST;
		gbc_checkBoxUndirected.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxUndirected.gridx = 3;
		gbc_checkBoxUndirected.gridy = 1;
		panelOptions.add(checkBoxUndirected, gbc_checkBoxUndirected);
		
//		Vertices
		JLabel labelVerticesSize = new JLabel("Maximum subgraph size:");
		labelVerticesSize.setToolTipText("Maximum number of nodes in a subgraph or motif.");
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
		
//		Algorithm
		JLabel labelAlgorithm = new JLabel("Algorithm:");
		labelAlgorithm.setToolTipText("Type of algorithm to use (default = base).");
		GridBagConstraints gbc_labelAlgorithm = new GridBagConstraints();
		gbc_labelAlgorithm.anchor = GridBagConstraints.EAST;
		gbc_labelAlgorithm.insets = new Insets(0, 0, 0, 5);
		gbc_labelAlgorithm.gridx = 0;
		gbc_labelAlgorithm.gridy = 3;
		panelOptions.add(labelAlgorithm, gbc_labelAlgorithm);
		
		comboBoxAlgorithm = new JComboBox<>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		panelOptions.add(comboBoxAlgorithm, gbc_comboBox);
		comboBoxAlgorithm.addItem("base");
		comboBoxAlgorithm.addItem("gspan");
		comboBoxAlgorithm.addItem("fsg");

//		Advanced options
		JPanel panelAdvanced = new JPanel();
		tabbedPaneOptions.addTab("Advanced options", null, panelAdvanced, null);
		GridBagLayout gbl_panelAdvanced = new GridBagLayout();
		gbl_panelAdvanced.columnWeights = new double[]{0.0, Double.MIN_VALUE, 0.0, 0.0};
		gbl_panelAdvanced.rowWeights = new double[]{0.0, 0.0, 0.0};
		panelAdvanced.setLayout(gbl_panelAdvanced);
		
		JLabel labelSupport = new JLabel("Minimum support:");
		labelSupport.setToolTipText("Support value threshold to use (default = automatic calculation).");
		GridBagConstraints gbc_labelSupport = new GridBagConstraints();
		gbc_labelSupport.insets = new Insets(0, 0, 5, 5);
		gbc_labelSupport.gridx = 0;
		gbc_labelSupport.gridy = 0;
		panelAdvanced.add(labelSupport, gbc_labelSupport);
		
		textFieldSupport = new JTextField();
		GridBagConstraints gbc_textFieldSupport = new GridBagConstraints();
		gbc_textFieldSupport.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldSupport.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldSupport.gridx = 1;
		gbc_textFieldSupport.gridy = 0;
		panelAdvanced.add(textFieldSupport, gbc_textFieldSupport);
		textFieldSupport.setColumns(10);
		
		checkBoxNestedPValue = new JCheckBox("Nested p-value", false);
		checkBoxNestedPValue.setToolTipText("Use nested P-value configuration (default = true).");
		GridBagConstraints gbc_checkBoxPValue = new GridBagConstraints();
		gbc_checkBoxPValue.anchor = GridBagConstraints.WEST;
		gbc_checkBoxPValue.gridwidth = 2;
		gbc_checkBoxPValue.insets = new Insets(0, 0, 5, 5);
		gbc_checkBoxPValue.gridx = 0;
		gbc_checkBoxPValue.gridy = 1;
		panelAdvanced.add(checkBoxNestedPValue, gbc_checkBoxPValue);
		
		checkBoxDebug = new JCheckBox("Debug mode");
		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxNewCheckBox.gridx = 3;
		gbc_chckbxNewCheckBox.gridy = 1;
		panelAdvanced.add(checkBoxDebug, gbc_chckbxNewCheckBox);
		
		checkBoxAllPvalues = new JCheckBox("Return all p-values", false);
		checkBoxAllPvalues.setToolTipText("Return all motifs and their raw p-values alongside the "
				+ "bonferroni-corrected values, instead of only those passing the Bonferroni-adjusted "
				+ "significance threshold (default = no)");
		GridBagConstraints gbc_chckbxAllPvalues = new GridBagConstraints();
		gbc_chckbxAllPvalues.anchor = GridBagConstraints.WEST;
		gbc_chckbxAllPvalues.gridwidth = 2;
		gbc_chckbxAllPvalues.insets = new Insets(0, 0, 5, 5);
		gbc_chckbxAllPvalues.gridx = 0;
		gbc_chckbxAllPvalues.gridy = 2;
		panelAdvanced.add(checkBoxAllPvalues, gbc_chckbxAllPvalues);
		
		checkBoxVerbose = new JCheckBox("Verbose");
		checkBoxVerbose.setToolTipText("Additional logging information.");
		GridBagConstraints gbc_checkBoxVerbose = new GridBagConstraints();
		gbc_checkBoxVerbose.insets = new Insets(0, 0, 5, 0);
		gbc_checkBoxVerbose.anchor = GridBagConstraints.WEST;
		gbc_checkBoxVerbose.gridwidth = 2;
		gbc_checkBoxVerbose.gridx = 3;
		gbc_checkBoxVerbose.gridy = 2;
		panelAdvanced.add(checkBoxVerbose, gbc_checkBoxVerbose);
		
//		Output files panel
		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder(null, "Output files", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_outputPanel = new GridBagConstraints();
		gbc_outputPanel.gridwidth = 3;
		gbc_outputPanel.insets = new Insets(0, 0, 5, 5);
		gbc_outputPanel.fill = GridBagConstraints.HORIZONTAL;
		gbc_outputPanel.gridx = 1;
		gbc_outputPanel.gridy = 2;
		pnlMain.add(outputPanel, gbc_outputPanel);
		GridBagLayout gbl_outputPanel = new GridBagLayout();
		gbl_outputPanel.columnWeights = new double[]{0.0, 1.0, 0.0};
		gbl_outputPanel.rowWeights = new double[]{0.0};
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
		buttonSavePath.addActionListener(new SaveFileAction(textFieldSavePath, frame));
		GridBagConstraints gbc_buttonSavePath = new GridBagConstraints();
		gbc_buttonSavePath.anchor = GridBagConstraints.WEST;
		gbc_buttonSavePath.gridx = 2;
		gbc_buttonSavePath.gridy = 0;
		outputPanel.add(buttonSavePath, gbc_buttonSavePath);
		
//		Define report panel
		JPanel panelReport = new JPanel();
		panelReport.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Log", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		GridBagConstraints gbc_panelReport = new GridBagConstraints();
		gbc_panelReport.gridwidth = 3;
		gbc_panelReport.insets = new Insets(0, 0, 0, 5);
		gbc_panelReport.fill = GridBagConstraints.BOTH;
		gbc_panelReport.gridx = 1;
		gbc_panelReport.gridy = 3;
		pnlMain.add(panelReport, gbc_panelReport);
		panelReport.setLayout(new BorderLayout(0, 0));
		
		textAreaLog = new JTextArea();
		textAreaLog.setEditable(false);
		JScrollPane scrollPaneTextAreaLog = new JScrollPane(textAreaLog);
		panelReport.add(scrollPaneTextAreaLog, BorderLayout.CENTER);

//		Redirect console output
		PrintStream logStream = new PrintStream(new CustomOutputStream(textAreaLog));
		System.setOut(logStream);
		System.setErr(logStream);

//		Set size of frame based on its underlying components
		frame.pack();
	}
}
