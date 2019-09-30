package com.uantwerp.algorithms;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.uantwerp.algorithms.Efficiency.VariablesTimer;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.gui.SubgraphMiningGUI;
import com.uantwerp.algorithms.procedures.fsg.FSG;
import com.uantwerp.algorithms.procedures.gspan.GSpan;
import com.uantwerp.algorithms.utilities.PrintUtility;

/** Class in charge of running the process of the algorithm  
*
* @author Pieter Moris
* @author Danh Bui
* @version 1.0 2019
* 
* @author gerito
* @version 0.1 2016
*/
public class SubgraphMining {
	
	public static Boolean GUI = false;
	public static Boolean DEBUG = false;
	public static Thread current_process = null;

	public static void main(String[] args) {
		ParameterConfig.resetVariables();

		Options options = new Options();
		options.addOption("h", "help", false, "Print this help text");
		options.addOption("g", "graph", true, "Path to a graph or network file");
		options.addOption("l", "labels", true, "Path to a file containing nodes and labels (optional)");
		options.addOption("i", "interest", true, "Path to a file containing nodes of interest (omit it for frequent subgraph mining)");
		options.addOption("b", "background", true, "Path to a file containing background nodes for the enrichment test (optional)");
		options.addOption("o", "output", true, "Output file");
		options.addOption("s", "support", true, "Specify a custom support threshold (default = automatic calculation)");
		options.addOption("p", "alpha", true, "Significance level (or q-value for FDR) for the hypergeometric tests (default = 0.05)");
		options.addOption("c", "correction-method", true, "Multiple testing correction method to use: 'bonferonni' (default), 'holm', 'BH' (Benjamini-Hochberg) or 'BY' (Benjamini-Yekutieli)");
		options.addOption(null, "all-pvalues", false, "Return all subgraphs, instead of"
				+ "only those passing the (multiple testing corrected) significance level");
		options.addOption("m", "max-size", true, "Maximum number of vertices allowed in the subgraph patterns (default = 3)");
		options.addOption(null, "single-label", false, "Variant where each node has exactly one label");
		options.addOption("u", "undirected", false, "Undirected option where A->B = B->A and self-loops aren't allowed");	
		options.addOption("n", "nested-pvalue", false, "Variant where the significance of the child subgraph is based on the parent matches");
		options.addOption("a", "algorithm", true, "The type of algorithm to run the signficant subgraph mining, the options are \"base\", \"gspan\" and \"apriori\"");
		options.addOption("v", "verbose", false, "Print additional intermediary output");	
		options.addOption(null, "statistics", true, "Path for the statistics of memory usage");
		options.addOption(null, "debug", false, "Print the full stack trace for debugging purposes");

		try{			
//			Launch GUI if no options are passed
			if (args.length == 0) {
				GUI = true;
				SubgraphMiningGUI.launchGUI(args);
//				ultimately, the SubgraphMiningGUI.StartButton class will invoke runProcesses()
			} else {
//				Parse command line
				CommandLineParser parser = new DefaultParser();
				CommandLine cmd = parser.parse(options, args);			
				ParameterConfig.transformCommandLine(cmd, options);
				runProcesses(null);
		    }
		} catch(Exception e){
			exceptionBehaviour(e);
		}
	}
	
	/*
	 * Stop the current analysis process (if yes)
	 */
	public static void stopProcess() {
		if (SubgraphMining.current_process  != null && SubgraphMining.current_process.isAlive()) {
			System.out.println("The analysis process is terminating...");
			SubgraphMining.current_process.interrupt();
		}
	}
	
	public static void runProcesses(SubgraphMiningGUI gui){
		VariablesTimer.initializeVariable();
		Timer t1 = new Timer();
		
		SubgraphMining.current_process = null;
		
		if (GraphParameters.typeAlgorithm.toLowerCase().equals("base")){
			SubgraphMining.current_process = new NaivePresentation(t1, gui);
		}else if (GraphParameters.typeAlgorithm.toLowerCase().equals("gspan")) {
			SubgraphMining.current_process = new GSpan(t1, gui);
		}else if (GraphParameters.typeAlgorithm.toLowerCase().equals("fsg")) {
			SubgraphMining.current_process = new FSG(t1, gui);
		}else {
			SubGraphMiningException.exceptionWrongAlgorithmChoice(GraphParameters.typeAlgorithm);
		}
		
		if (!GraphParameters.statistics.equals("")){
			t1.scheduleAtFixedRate(new TimerTask() {
		        @Override
		         public void run(){
		        	try {
			        	VariablesTimer.addStatistic();
		        	} catch (Exception e) {
			    		exceptionBehaviour(e);
			    	}
		         }
			 },1000,1000);
		}
		
		if (SubgraphMining.current_process != null) {
			SubgraphMining.current_process.start();
		}
	}

	public static void exceptionBehaviour(Exception e) {
		if (DEBUG)
			e.printStackTrace();
		System.err.println(e.getMessage());
		PrintUtility.print2LogView("\nUse the --help flag to display usage information or omit all parameters to launch in GUI mode.\n");

		if (!GUI)
			System.exit(1);	// exit with error code 1
	}
}
