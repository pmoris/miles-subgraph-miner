package com.uantwerp.algorithms;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.uantwerp.algorithms.Efficiency.VariablesTimer;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.FileUtility;

/** Class in charge of running the process of the algorithm  
*
* @author gerito
* @version 1.0 2016
*/
public class SubgraphMining {
	
	public SubgraphMining(String graphPath, String labelsPath, String groupfilePath, String bgFilePath, 
								int support, int singleLabel, int undirected, int maxsize, int verbose,
								double pvalue, int nestedpval, String output, String typeAlgorithm) {
		super();
		CmdConfig.resetVariables();
		GraphPathParameters.pathGraph = graphPath;
		GraphPathParameters.pathLabels = labelsPath;
		GraphPathParameters.pathBgNodes = bgFilePath;
		GraphPathParameters.pathGroupFile = groupfilePath;
		GraphPathParameters.supportcutoff = support;
		GraphPathParameters.singleLabel = singleLabel;
		GraphPathParameters.undirected = undirected;
		GraphPathParameters.verbose = verbose;
		GraphPathParameters.maxsize = maxsize;
		GraphPathParameters.pvalue = pvalue;
		GraphPathParameters.nestedpval = nestedpval;
		GraphPathParameters.output = output;
		GraphPathParameters.typeAlgorithm = typeAlgorithm;
		runProcesses();		
	}		
	
	public static void main(String[] args) {
		CmdConfig.resetVariables();

		Options options = new Options();
		options.addOption("h", "help", false, "Print help");
		options.addOption("g", "graph", true, "path of the graph");
		options.addOption("l", "labels", true, "path of the labels");
		options.addOption("p", "groupfile", true, "path of the group file");
		options.addOption("b", "bgFile", true, "path of the bgfile");
		options.addOption("s", "support", true, "expected support of the algorithm");
		options.addOption("i", "singleLabel", false, "Variant where each node has exactly one label and this label must exactly match for the motif");
		options.addOption("u", "undirected", false, "Undirected option where A->B = B->A and self-loops aren't allowed");			
		options.addOption("d", "verbose", false, "verbose");	
		options.addOption("m", "maxsize", true, "Maximum number of vertixes allowed in the subgraph");
		options.addOption("v", "pvalue", true, "Maximum pvalue allowed (default 0.05)");
		options.addOption("n", "nestedpvalue", false, "Variant where the significance of the child motif is based on the parent matches");
		options.addOption("o", "output", true, "Outputfile to print significant motifs");
		options.addOption("a", "typeAlgorithm", true, "The type of algorithm to run the signficant subgraph mining, the options are \"naive\", \"gspan\" and \"apriori\"");
		options.addOption("t", "statistics", true, "Path for the statistics of memory usage");

		try{			
//			Launch GUI if no options are passed
			if (args.length == 0) {
//				start GUI
				System.out.println("GUI LAUNCHED");
			} else {
//				Parse command line
				CommandLineParser parser = new DefaultParser();
				CommandLine cmd = parser.parse(options, args);			
				CmdConfig.transformCommandLine(cmd, options);
				runProcesses();
		    }
		} catch(Exception exp){
			exp.printStackTrace();
			System.err.println(exp.getMessage());
			System.out.println("Use the --help flag to display usage information or omit all parameters to launch in GUI mode.");
			System.exit(1);
		}
	}
	
	private static void runProcesses(){
		VariablesTimer.initializeVariable();
		Timer t1 = new Timer();
		Thread thread1 = new Thread() {
		    public void run() {
		        new AlgorithmFunctionality().mainProcedure(t1);
		    }
		};
		if (!GraphPathParameters.statistics.equals("")){
			t1.scheduleAtFixedRate(new TimerTask() {
		        @Override
		         public void run(){ 
		        	VariablesTimer.addStatistic();
		         }
			 },1000,1000);
		}
		thread1.start();
		try {
			thread1.join();
			if (VariablesTimer.stateFinish)
				VariablesTimer.writeResults(GraphPathParameters.statistics);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}
