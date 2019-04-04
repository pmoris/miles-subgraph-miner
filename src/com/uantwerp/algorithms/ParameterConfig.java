package com.uantwerp.algorithms;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.FileUtility;

public class ParameterConfig {

	public static void transformCommandLine(CommandLine cmd, Options options){

		if(cmd.hasOption("h")){
			printHelp(options);
			System.exit(0);
		}else{
			if(!cmd.hasOption("g")){
				SubGraphMiningException.exceptionNoFileProvided("network");
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
			if(cmd.hasOption('i')){
				String file = FileUtility.readFile(cmd.getOptionValue('i'));
				GraphPathParameters.pathInterestFile = file;
			}
			if(cmd.hasOption('s')) {
				if (Integer.valueOf(cmd.getOptionValue('s')) >= 0)
					GraphPathParameters.supportcutoff = Integer.valueOf(cmd.getOptionValue('s'));
				else
					SubGraphMiningException.exceptionInvalidValue("support");
			}
			else
				GraphPathParameters.setDefaultSupport();
			if(cmd.hasOption('m'))
				GraphPathParameters.maxsize = Integer.valueOf(cmd.getOptionValue('m'));
			else
				GraphPathParameters.setDefaultMaxSize();
			if(cmd.hasOption("singlelabel"))
				GraphPathParameters.singleLabel = 1;
			else
				GraphPathParameters.singleLabel = 0;
			if(cmd.hasOption('u'))
				GraphPathParameters.undirected = 1;
			else
				GraphPathParameters.undirected = 0;
			if(cmd.hasOption('v'))
				GraphPathParameters.verbose = 1;
			else
				GraphPathParameters.verbose = 0;
			if(cmd.hasOption('n'))
				GraphPathParameters.nestedpval = 1;
			else
				GraphPathParameters.nestedpval = 0;
			if(cmd.hasOption('p')) {
				if (Double.valueOf(cmd.getOptionValue('p')) <= 1 && Double.valueOf(cmd.getOptionValue('p')) >= 0)
					GraphPathParameters.pvalue = Double.valueOf(cmd.getOptionValue('p'));
				else
					SubGraphMiningException.exceptionInvalidValue("p-value");
			}
			else
				GraphPathParameters.setDefaultPValue();
			if(cmd.hasOption('o')) {
				if(new File(cmd.getOptionValue('o')).getParentFile().exists())
					GraphPathParameters.output = cmd.getOptionValue('o');
				else
					SubGraphMiningException.exceptionDirNotExists(new File(cmd.getOptionValue('o')).getParentFile().getAbsolutePath());
			}
			else
				GraphPathParameters.output = "none";
			if (cmd.hasOption('a'))
				GraphPathParameters.typeAlgorithm = cmd.getOptionValue('a');
			else
				GraphPathParameters.typeAlgorithm = "base";
			/****************************************************************************/
			//Find a way to make this part dynamical in case of no parameter
			/****************************************************************************/			
			if(cmd.hasOption("statistics"))
				GraphPathParameters.statistics = cmd.getOptionValue("statistics");
			else
				GraphPathParameters.statistics = "";

		}
	}
	
	public static void printHelp(Options options){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("subgraphmining", options);
	}
	
	public static void resetVariables(){
		GraphPathParameters.reset();
		GraphPathParameters.graph.resetGraph();
		MiningState.resetMiningState();
	}

	public static void transformGUI(
			String graphPath,
			String labelsPath,
			String interestingPath,
			String backgroundPath,
			String support,
			String pValue,
			String verticesSize,
			String savePath,
			String algorithm,
			Boolean singleLabel,
			Boolean undirected,
			Boolean nestedPValue,
			Boolean showStatistics,
			Boolean verbose,
			String areaProgressReport) {

		if(graphPath.isEmpty()){
			SubGraphMiningException.exceptionNoFileProvided("network");
		}else{
			String graph = FileUtility.readFile(graphPath);
			GraphPathParameters.pathGraph = graph;
		}
		if(!labelsPath.isEmpty()){
			String labels = FileUtility.readFile(labelsPath);
			GraphPathParameters.pathLabels = labels;
		}
		if(!backgroundPath.isEmpty()){
			String background = FileUtility.readFile(backgroundPath);
			GraphPathParameters.pathBgNodes = background;
		}
		if(!interestingPath.isEmpty()){
			String interesting = FileUtility.readFile(interestingPath);
			GraphPathParameters.pathInterestFile = interesting;
		}
		if(!support.isEmpty()) {
			if (Integer.valueOf(support) >= 0)
				GraphPathParameters.supportcutoff = Integer.valueOf(support);
			else
				SubGraphMiningException.exceptionInvalidValue("support");
		}
		else
			GraphPathParameters.setDefaultSupport();
		if(!verticesSize.isEmpty())
			GraphPathParameters.maxsize = Integer.valueOf(verticesSize);
		else
			GraphPathParameters.setDefaultMaxSize();
		if(singleLabel)
			GraphPathParameters.singleLabel = 1;
		else
			GraphPathParameters.singleLabel = 0;
		if(undirected)
			GraphPathParameters.undirected = 1;
		else
			GraphPathParameters.undirected = 0;
		if(verbose)
			GraphPathParameters.verbose = 1;
		else
			GraphPathParameters.verbose = 0;
		if(nestedPValue)
			GraphPathParameters.nestedpval = 1;
		else
			GraphPathParameters.nestedpval = 0;
		if(!pValue.isEmpty()) {
			if (Double.valueOf(pValue) <= 1 && Double.valueOf(pValue) >= 0)
				GraphPathParameters.pvalue = Double.valueOf(pValue);
			else
				SubGraphMiningException.exceptionInvalidValue("p-value");
		}
		else
			GraphPathParameters.setDefaultPValue();
		if(!savePath.isEmpty()) {
			if(new File(savePath).getParentFile().exists())			
				GraphPathParameters.output = savePath;
			else
				SubGraphMiningException.exceptionDirNotExists(new File(savePath).getParentFile().getAbsolutePath());
		}
		else
			GraphPathParameters.output = "none";
		GraphPathParameters.typeAlgorithm = algorithm;
//		if(showStatistics)
//			GraphPathParameters.statistics = ;
//		else
//			GraphPathParameters.statistics = "";
	}

}
