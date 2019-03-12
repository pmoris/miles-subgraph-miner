package com.uantwerp.algorithms;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.FileUtility;

public class CmdConfig {

	public static void transformCommandLine(CommandLine cmd, Options options){

		if(cmd.hasOption("h")){
			printHelp(options);
			System.exit(0);
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
