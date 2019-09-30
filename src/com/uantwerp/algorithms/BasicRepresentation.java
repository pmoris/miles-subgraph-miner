package com.uantwerp.algorithms;

import java.util.Timer;

import javax.swing.SwingUtilities;

import com.uantwerp.algorithms.Efficiency.VariablesTimer;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.gui.SubgraphMiningGUI;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.MultipleTestingCorrection;
import com.uantwerp.algorithms.utilities.OutputUtility;
import com.uantwerp.algorithms.utilities.PrintUtility;

public class BasicRepresentation extends Thread {

	protected Timer myTimer;
	protected SubgraphMiningGUI mainThread;

	public BasicRepresentation(Timer t, SubgraphMiningGUI mainThread) {
		this.myTimer = t;
		this.mainThread = mainThread;
	}

	@Override
	public void run() {
		try {
			this.updateGUI("start");
			// Import graphs, labels, set of interest and background files and store as
			// HashMaps and HashSets
			HashGeneration.graphGeneration();
			// if (GraphParameters.verbose == 1)
			PrintUtility.printSummary();
			GraphParameters.supportcutoff = AlgorithmUtility.supportTreshold();

			// Check interrupted to stop the analysis
			if (!Thread.interrupted()) {
				this.runAnalysis();
			}

			// Check interrupted to stop the analysis
			if (!Thread.interrupted()) {
				this.recalculateAndPrintResults();
			}

		} catch (Exception e) {
			SubgraphMining.exceptionBehaviour(e);
		} finally {

			if (VariablesTimer.stateFinish)
				VariablesTimer.writeResults(GraphParameters.statistics);

			this.updateGUI("stop");
		}

	}

	private void updateGUI(String status) {
		if (mainThread == null)
			return;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainThread.updateGUI(status);
			}
		});
	}

	protected void runAnalysis() {
		// Do nothing here
		// This should be overridden by sub-class.
	}

	protected void recalculateAndPrintResults() {

		// if a file with interesting vertices was provided
		// evaluates to 0 for both empty or non-existent files
//		if (GraphParameters.interestFile != null && GraphParameters.interestFile.length() != 0) {
		if (GraphParameters.frequentMining == false) {

			// print information on which type of subgraphs will be shown (all supported
			// with raw p-values or only p <= alpha)
//			OutputUtility.preResultStatistics();

			// perform multiple testing correction and store number of significant subgraphs
			MiningState.supportedMotifsAdjustedPValues = MultipleTestingCorrection
					.adjustPValues(MiningState.supportedMotifsPValues, GraphParameters.correctionMethod);

			// generate table with motifs, freqs and p-values (also stores the number of
			// significant subgraphs after correction)
			String outputTable = OutputUtility.createTable();

			// write output files or print to stdout
			OutputUtility.writeOutputFiles(outputTable);

			// print summary statistics
			OutputUtility.printStatistics();
		}

		// if no interesting vertices were provided = frequent subgraph mode
		else {
			// print information on which type of subgraphs will be shown (support >
			// threshold)
//			OutputUtility.preResultStatisticsFrequent();

			// generate table with motifs and support values
			String outputTable = OutputUtility.createTableFrequent();

			// write output files or print to stdout
			OutputUtility.writeOutputFiles(outputTable);

			// print summary statistics
			OutputUtility.printStatisticsFrequent();
		}

		VariablesTimer.finishProcess();
		myTimer.cancel();
	}
}
