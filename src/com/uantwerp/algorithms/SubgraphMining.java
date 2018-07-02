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
		resetVariables();
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
		resetVariables();
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
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);			
			transformComandLine(cmd,options);
			runProcesses();
		}catch(Exception exp){
			exp.printStackTrace();
			System.err.println(exp.getMessage());
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
	
	private static void transformComandLine(CommandLine cmd, Options options){

		if(cmd.hasOption("h")){
			printHelp(options);
		}else{
			if(!cmd.hasOption("g")){
				SubGraphMiningException.exceptionNoFile("graph");
				printHelp(options);
			}else{
				String graph = FileUtility.readFile(cmd.getOptionValue('g'));
				GraphPathParameters.pathGraph = graph;
			}
			if(cmd.hasOption('l')){
				String labels = FileUtility.readFile(cmd.getOptionValue('l'));
				GraphPathParameters.pathLabels = labels;
			}
			if(cmd.hasOption('b')){
				String file = FileUtility.readFile(cmd.getOptionValue('b'));
				GraphPathParameters.pathBgNodes = file;
			}
			if(cmd.hasOption('p')){
				String file = FileUtility.readFile(cmd.getOptionValue('p'));
				GraphPathParameters.pathGroupFile = file;
			}
			if(cmd.hasOption('s'))
				GraphPathParameters.supportcutoff = Integer.valueOf(cmd.getOptionValue('s'));
			else
				GraphPathParameters.setDefaultSupport();
			if(cmd.hasOption('m'))
				GraphPathParameters.maxsize = Integer.valueOf(cmd.getOptionValue('m'));
			else
				GraphPathParameters.setDefaultMaxSize();
			if(cmd.hasOption('i'))
				GraphPathParameters.singleLabel = 1;
			else
				GraphPathParameters.singleLabel = 0;
			if(cmd.hasOption('u'))
				GraphPathParameters.undirected = 1;
			else
				GraphPathParameters.undirected = 0;
			if(cmd.hasOption('d'))
				GraphPathParameters.verbose = 1;
			else
				GraphPathParameters.verbose = 0;
			if(cmd.hasOption('n'))
				GraphPathParameters.nestedpval = 1;
			else
				GraphPathParameters.nestedpval = 0;
			if(cmd.hasOption('v'))
				GraphPathParameters.pvalue = Double.valueOf(cmd.getOptionValue('v'));
			else
				GraphPathParameters.setDefaultPValue();
			if(cmd.hasOption('o'))
				GraphPathParameters.output = cmd.getOptionValue('o');
			else
				GraphPathParameters.output = "none";
			if (cmd.hasOption('a'))
				GraphPathParameters.typeAlgorithm = cmd.getOptionValue('a');
			else
				GraphPathParameters.typeAlgorithm = "base";
			/****************************************************************************/
			//Find a way to make this part dynamical in case of no parameter
			/****************************************************************************/			
			if(cmd.hasOption('t'))
				GraphPathParameters.statistics = cmd.getOptionValue('t');
			else
				GraphPathParameters.statistics = "";

		}
	}
	
	private static void printHelp(Options options){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("subgraphmining", options);
	}
	
	public static void resetVariables(){
		GraphPathParameters.reset();
		GraphPathParameters.graph.resetGraph();
		MiningState.resetMiningState();
	}
}
