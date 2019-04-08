package com.uantwerp.algorithms;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.uantwerp.algorithms.Efficiency.VariablesTimer;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.gui.SubgraphMiningGUI;

/** Class in charge of running the process of the algorithm  
*
* @author gerito
* @version 1.0 2016
*/
public class SubgraphMining {
	
	public static void main(String[] args) {
		ParameterConfig.resetVariables();

		Options options = new Options();
		options.addOption("h", "help", false, "Print help");
		options.addOption("g", "graph", true, "Path to the graph or network");
		options.addOption("l", "labels", true, "Path to the labels (optional)");
		options.addOption("i", "interest", true, "Path to nodes of interest (group file)");
		options.addOption("b", "background", true, "Path to the background nodes (optional)");
		options.addOption("s", "support", true, "Support threshold (default = dynamic)");
		options.addOption(null, "singlelabel", false, "Variant where each node has exactly one label and this label must exactly match for the motif");
		options.addOption("u", "undirected", false, "Undirected option where A->B = B->A and self-loops aren't allowed");			
		options.addOption("v", "verbose", false, "Verbose option to print more intermediary output");	
		options.addOption("m", "maxsize", true, "Maximum number of vertixes allowed in the subgraph (default = 5)");
		options.addOption("p", "pvalue", true, "Maximum p-value allowed (default = 0.05)");
		options.addOption("n", "nestedpvalue", false, "Variant where the significance of the child motif is based on the parent matches");
		options.addOption("o", "output", true, "Output file to store the significant motifs");
		options.addOption("a", "algorithm", true, "The type of algorithm to run the signficant subgraph mining, the options are \"base\", \"gspan\" and \"apriori\"");
		options.addOption(null, "statistics", true, "Path for the statistics of memory usage");

		try{			
//			Launch GUI if no options are passed
			if (args.length == 0) {
//				start GUI
				SubgraphMiningGUI.launchGUI(args);
			} else {
//				Parse command line
				CommandLineParser parser = new DefaultParser();
				CommandLine cmd = parser.parse(options, args);			
				ParameterConfig.transformCommandLine(cmd, options);
				runProcesses();
		    }
		} catch(Exception e){
			exceptionBehaviour(e);
		}
	}
	
	public static void runProcesses(){
		VariablesTimer.initializeVariable();
		Timer t1 = new Timer();
		Thread thread1 = new Thread() {
		    public void run() {
		    	try {
			        new AlgorithmFunctionality().mainProcedure(t1);
		    	} catch (Exception e) {
		    		exceptionBehaviour(e);
		    	}
		    }
		};
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
		thread1.start();
		try {
			thread1.join();
			if (VariablesTimer.stateFinish)
				VariablesTimer.writeResults(GraphParameters.statistics);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void exceptionBehaviour(Exception e) {
//		e.printStackTrace();
		System.err.println(e.getMessage());
		System.out.println("\nUse the --help flag to display usage information or omit all parameters to launch in GUI mode.\n");
		System.exit(1);
	}
}
