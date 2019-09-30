package com.uantwerp.algorithms;

import java.io.IOException;
import java.util.Timer;

import javax.swing.SwingUtilities;

import org.apache.commons.io.FilenameUtils;

import com.uantwerp.algorithms.Efficiency.VariablesTimer;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.gui.SubgraphMiningGUI;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.FileUtility;
import com.uantwerp.algorithms.utilities.MultipleTestingCorrection;
import com.uantwerp.algorithms.utilities.OutputUtility;
import com.uantwerp.algorithms.utilities.PrintUtility;
import com.uantwerp.algorithms.visualisation.HTMLCreator;
import com.uantwerp.algorithms.visualisation.MotifToJsonConversion;

public class BasicRepresentation extends Thread{

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
    		//Import graphs, labels, set of interest and background files and store as HashMaps and HashSets
    		HashGeneration.graphGeneration();
    		//if (GraphParameters.verbose == 1) 
    		PrintUtility.printSummary();
    		GraphParameters.supportcutoff = AlgorithmUtility.supportTreshold();
    		
    		//Check interrupted to stop the analysis 
    		if (!Thread.interrupted()) {
    			this.runAnalysis();
    		}
    		
    		//Check interrupted to stop the analysis 
    		if (!Thread.interrupted()) {
    			this.recalculateAndPrintResults();
    		}
	     
    	}catch (Exception e) {
    		SubgraphMining.exceptionBehaviour(e);
    	} finally {
    		
    		if (VariablesTimer.stateFinish)
				VariablesTimer.writeResults(GraphParameters.statistics);

    		PrintUtility.print2LogView("The analysis process ends!");
    		
    		this.updateGUI("stop");
    	}
		
	}
	
	private void updateGUI(String status) {
		if (mainThread == null) 
			return;
		SwingUtilities.invokeLater(new Runnable() {
	        public void run(){
	        	mainThread.updateGUI(status);
	       
	        }
	    });
	}
	
    
	protected void runAnalysis() {
		// Do nothing here
		// This should be overridden by sub-class.
	}

	protected void recalculateAndPrintResults(){
		
		// if a file with interesting vertices was provided
		// evaluates to 0 for both empty or non-existent files
		if (GraphParameters.interestFile != null && GraphParameters.interestFile.length() != 0) {
		
	//		print information on which type of subgraphs will be shown (all supported with raw p-values or only p <= alpha)
//			OutputUtility.preResultStatistics();
			
	//		perform multiple testing correction and store number of significant subgraphs
			MiningState.supportedMotifsAdjustedPValues = MultipleTestingCorrection.adjustPValues(MiningState.supportedMotifsPValues, GraphParameters.correctionMethod);
			
	//		generate table with motifs, freqs and p-values (also stores the number of significant subgraphs after correction)
			String outputTable = OutputUtility.createTable();
			
			// write output file or print to stdout
			// IO catcher is already present inside writeFile function
			if (!GraphParameters.output.equals("none")){
				FileUtility.writeFile(GraphParameters.output, outputTable.replace(" ", "_"));
				PrintUtility.print2LogView("\nSaved output file to " + GraphParameters.output);
			} else {
				PrintUtility.print2LogView("\n" + outputTable.replace(" ", "_"));
			}
			
			// convert motifs to JSON format for cytoscape.js
			String JSON = MotifToJsonConversion.convertAllMotifs();			
	
			// write html visualisation file
			if (!GraphParameters.output.equals("none")){
				try {
					String htmlVisualisation = HTMLCreator.createHTML(JSON, outputTable);
					String htmlFilePath = FilenameUtils.removeExtension(GraphParameters.output) + ".html";
					FileUtility.writeFile(htmlFilePath, htmlVisualisation);
					PrintUtility.print2LogView("Saved visualisation file to " + htmlFilePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// print summary statistics
			OutputUtility.printStatistics();
		}

		// if no interesting vertices were provided
		else {		
			// print information on which type of subgraphs will be shown (support > threshold)
//			OutputUtility.preResultStatisticsFrequent();
			
			String outputTable = OutputUtility.createTableFrequent();
			
			// write output file or print to stdout
			if (!GraphParameters.output.equals("none")){
				FileUtility.writeFile(GraphParameters.output, outputTable.replace(" ", "_"));
			}else{
				PrintUtility.print2LogView("\n" + outputTable.replace(" ", "_"));
			}
			
			// convert motifs to JSON format for cytoscape.js
			String JSON = MotifToJsonConversion.convertAllMotifs();			

			// write html visualisation file
			if (!GraphParameters.output.equals("none")){
				try {
					String htmlVisualisation = HTMLCreator.createHTML(JSON, outputTable);
					FileUtility.writeFile(FilenameUtils.removeExtension(GraphParameters.output) + ".html", htmlVisualisation);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// print summary statistics
			OutputUtility.printStatisticsFrequent();
		}

		VariablesTimer.finishProcess();
		myTimer.cancel();
	}
}
