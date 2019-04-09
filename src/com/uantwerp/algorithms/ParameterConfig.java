package com.uantwerp.algorithms;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.uantwerp.algorithms.common.GraphParameters;
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
				GraphParameters.graphFileContents = graph;
			}
			if(cmd.hasOption('l')){
				String labels = FileUtility.readFile(cmd.getOptionValue('l'));
				GraphParameters.labelsFileContents = labels;
			}
			if(cmd.hasOption('b')){
				String file = FileUtility.readFile(cmd.getOptionValue('b'));
				GraphParameters.backgroundFileContents = file;
			}
			if(cmd.hasOption('i')){
				String file = FileUtility.readFile(cmd.getOptionValue('i'));
				GraphParameters.interestFileContents = file;
			}
			if(cmd.hasOption('s')) {
				if (Integer.valueOf(cmd.getOptionValue('s')) >= 0)
					GraphParameters.supportcutoff = Integer.valueOf(cmd.getOptionValue('s'));
				else
					SubGraphMiningException.exceptionInvalidValue("support");
			}
			else
				GraphParameters.setDefaultSupport();
			if(cmd.hasOption('m'))
				GraphParameters.maxsize = Integer.valueOf(cmd.getOptionValue('m'));
			else
				GraphParameters.setDefaultMaxSize();
			if(cmd.hasOption("singlelabel"))
				GraphParameters.singleLabel = 1;
			else
				GraphParameters.singleLabel = 0;
			if(cmd.hasOption('u'))
				GraphParameters.undirected = 1;
			else
				GraphParameters.undirected = 0;
			if(cmd.hasOption('v'))
				GraphParameters.verbose = 1;
			else
				GraphParameters.verbose = 0;
			if(cmd.hasOption('n'))
				GraphParameters.nestedpval = 1;
			else
				GraphParameters.nestedpval = 0;
			if(cmd.hasOption('p')) {
				if (Double.valueOf(cmd.getOptionValue('p')) <= 1 && Double.valueOf(cmd.getOptionValue('p')) >= 0)
					GraphParameters.pvalue = Double.valueOf(cmd.getOptionValue('p'));
				else
					SubGraphMiningException.exceptionInvalidValue("p-value");
			}
			else
				GraphParameters.setDefaultPValue();
			if(cmd.hasOption('o')) {
				if(new File(cmd.getOptionValue('o')).getParentFile().exists())
					GraphParameters.output = cmd.getOptionValue('o');
				else
					SubGraphMiningException.exceptionDirNotExists(new File(cmd.getOptionValue('o')).getParentFile().getAbsolutePath());
			}
			else
				GraphParameters.output = "none";
			if (cmd.hasOption('a'))
				GraphParameters.typeAlgorithm = cmd.getOptionValue('a');
			else
				GraphParameters.typeAlgorithm = "base";
			/****************************************************************************/
			//Find a way to make this part dynamical in case of no parameter
			/****************************************************************************/			
			if(cmd.hasOption("statistics"))
				GraphParameters.statistics = cmd.getOptionValue("statistics");
			else
				GraphParameters.statistics = "";

		}
	}
	
	public static void printHelp(Options options){
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("subgraphmining", options);
	}
	
	public static void resetVariables(){
		GraphParameters.reset();
		GraphParameters.graph.resetGraph();
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
			GraphParameters.graphFileContents = graph;
		}
		if(!labelsPath.isEmpty()){
			String labels = FileUtility.readFile(labelsPath);
			GraphParameters.labelsFileContents = labels;
		}
		if(!backgroundPath.isEmpty()){
			String background = FileUtility.readFile(backgroundPath);
			GraphParameters.backgroundFileContents = background;
		}
		if(!interestingPath.isEmpty()){
			String interesting = FileUtility.readFile(interestingPath);
			GraphParameters.interestFileContents = interesting;
		}
		if(!support.isEmpty()) {
			if (Integer.valueOf(support) >= 0)
				GraphParameters.supportcutoff = Integer.valueOf(support);
			else
				SubGraphMiningException.exceptionInvalidValue("support");
		}
		else
			GraphParameters.setDefaultSupport();
		if(!verticesSize.isEmpty())
			GraphParameters.maxsize = Integer.valueOf(verticesSize);
		else
			GraphParameters.setDefaultMaxSize();
		if(singleLabel)
			GraphParameters.singleLabel = 1;
		else
			GraphParameters.singleLabel = 0;
		if(undirected)
			GraphParameters.undirected = 1;
		else
			GraphParameters.undirected = 0;
		if(verbose)
			GraphParameters.verbose = 1;
		else
			GraphParameters.verbose = 0;
		if(nestedPValue)
			GraphParameters.nestedpval = 1;
		else
			GraphParameters.nestedpval = 0;
		if(!pValue.isEmpty()) {
			if (Double.valueOf(pValue) <= 1 && Double.valueOf(pValue) >= 0)
				GraphParameters.pvalue = Double.valueOf(pValue);
			else
				SubGraphMiningException.exceptionInvalidValue("p-value");
		}
		else
			GraphParameters.setDefaultPValue();
		if(!savePath.isEmpty()) {
			if(new File(savePath).getParentFile().exists())			
				GraphParameters.output = savePath;
			else
				SubGraphMiningException.exceptionDirNotExists(new File(savePath).getParentFile().getAbsolutePath());
		}
		else
			GraphParameters.output = "none";
		GraphParameters.typeAlgorithm = algorithm;
//		if(showStatistics)
//			GraphPathParameters.statistics = ;
//		else
//			GraphPathParameters.statistics = "";
	}

}
