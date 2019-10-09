package com.uantwerp.algorithms;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.FileUtility;

/**
 * Functionality to parse the cmd or gui flags and assign them to GraphParameter attributes.
 * File contents are stored as Strings. Omitted flags will default to null.
 * Invalid options will raise exceptions.
 * Empty files will raise exceptions (see FileUtility class).
 *
 */
public class ParameterConfig {

	public static void transformCommandLine(CommandLine cmd, Options options){

		if(cmd.hasOption("debug"))
			SubgraphMining.DEBUG = true;
		if(cmd.hasOption("h")){
			printHelp(options);
			System.exit(0);
		}else{
			if(!cmd.hasOption("g")){
				SubGraphMiningException.exceptionNoFileProvided("network");
				printHelp(options);
			}else{
				File graph = FileUtility.readFile(cmd.getOptionValue('g'));
				GraphParameters.graphFile = graph;
			}
			if(cmd.hasOption('l')){
				File labels = FileUtility.readFile(cmd.getOptionValue('l'));
				GraphParameters.labelsFile = labels;
			}
			if(cmd.hasOption('b')){
				File file = FileUtility.readFile(cmd.getOptionValue('b'));
				GraphParameters.backgroundFile = file;
			}
			if(cmd.hasOption('i')){
				File file = FileUtility.readFile(cmd.getOptionValue('i'));
				GraphParameters.interestFile = file;
			}
			if(cmd.hasOption('s')) {
				try {
					if (Integer.valueOf(cmd.getOptionValue('s')) >= 0)
						GraphParameters.supportcutoff = Integer.valueOf(cmd.getOptionValue('s'));
					else
						SubGraphMiningException.exceptionInvalidValue("support");
				} catch (NumberFormatException e) {
					SubGraphMiningException.exceptionInvalidValue("support");
				}
			}
			else
				GraphParameters.setDefaultSupport();
			if(cmd.hasOption('m')) {
				try {
					if (Integer.valueOf(cmd.getOptionValue('m')) >= 1)
						GraphParameters.maxsize = Integer.valueOf(cmd.getOptionValue('m'));
					else
						SubGraphMiningException.exceptionInvalidValue("max-size");
				} catch (NumberFormatException e) {
					SubGraphMiningException.exceptionInvalidValue("max-size");
				}
			}
			else
				GraphParameters.setDefaultMaxSize();
			if(cmd.hasOption("single-label"))
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
				try {
					if (Double.valueOf(cmd.getOptionValue('p')) <= 1 && Double.valueOf(cmd.getOptionValue('p')) >= 0)
						GraphParameters.pvalue = Double.valueOf(cmd.getOptionValue('p'));
					else
						SubGraphMiningException.exceptionInvalidValue("alpha");
				} catch (NumberFormatException e) {
					SubGraphMiningException.exceptionInvalidValue("alpha");
				}
			}
			else
				GraphParameters.setDefaultPValue();
			if(cmd.hasOption("correction-method"))
				GraphParameters.correctionMethod = cmd.getOptionValue("correction-method").toLowerCase();
			else // set holm as the default correction method
				GraphParameters.correctionMethod = "holm";
			if(cmd.hasOption("all-pvalues"))
				GraphParameters.allPValues = 1;
			else
				GraphParameters.allPValues = 0;
			if(cmd.hasOption('o')) {
				// convert filename into an File object after mapping it to the absolute filepath
				File outFile = new File(cmd.getOptionValue('o')).getAbsoluteFile();
				// retrieve parent directory
				File outParent = outFile.getParentFile();
				// check if parent directory exists
				if (!outParent.exists())
					SubGraphMiningException.exceptionDirNotExists(outParent.getAbsolutePath());
				// check if parent directory is writable
				if (!outParent.canWrite())
					SubGraphMiningException.exceptionPermissions(outParent.getAbsolutePath());
				// check if file already exists and is writable
				if ( outFile.exists() && !outFile.canWrite() )
						SubGraphMiningException.exceptionPermissions(outFile.getAbsolutePath());
				GraphParameters.output = outFile.getPath();
			}
			else
				GraphParameters.output = "none";
			if (cmd.hasOption('a'))
				GraphParameters.typeAlgorithm = cmd.getOptionValue('a').toLowerCase();
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
			String alpha,
			String correctionMethod,
			Boolean allPValues,
			String verticesSize,
			String savePath,
			String algorithm,
			Boolean singleLabel,
			Boolean undirected,
			Boolean nestedPValue,
			Boolean verbose,
			String areaProgressReport,
			Boolean debugCheckBox) {

		if (debugCheckBox)
			SubgraphMining.DEBUG = true;
		if(graphPath.isEmpty()){
			SubGraphMiningException.exceptionNoFileProvided("graph");
		}else{
			File graph = FileUtility.readFile(graphPath);
			GraphParameters.graphFile = graph;
		}
		if(!labelsPath.isEmpty()){
			File labels = FileUtility.readFile(labelsPath);
			GraphParameters.labelsFile = labels;
		}
		if(!backgroundPath.isEmpty()){
			File background = FileUtility.readFile(backgroundPath);
			GraphParameters.backgroundFile = background;
		}
		if(!interestingPath.isEmpty()){
			File interesting = FileUtility.readFile(interestingPath);
			GraphParameters.interestFile = interesting;
		}
		if(!support.isEmpty()) {
			try {
				if (Integer.valueOf(support) >= 0)
					GraphParameters.supportcutoff = Integer.valueOf(support);
				else
					SubGraphMiningException.exceptionInvalidValue("support");
			} catch (NumberFormatException e) {
				SubGraphMiningException.exceptionInvalidValue("support");
			}
		}
		else
			GraphParameters.setDefaultSupport();
		if(!verticesSize.isEmpty()) {
			try {
				if (Integer.valueOf(verticesSize) >= 1)
					GraphParameters.maxsize = Integer.valueOf(verticesSize);
				else
					SubGraphMiningException.exceptionInvalidValue("maximum subgraph size");
			} catch (NumberFormatException e) {
				SubGraphMiningException.exceptionInvalidValue("maximum subgraph size");
			}
		}
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
		if(!alpha.isEmpty()) {
			try {
				if (Double.valueOf(alpha) <= 1 && Double.valueOf(alpha) >= 0)
					GraphParameters.pvalue = Double.valueOf(alpha);
				else
					SubGraphMiningException.exceptionInvalidValue("significance level");
			} catch (NumberFormatException e) {
				SubGraphMiningException.exceptionInvalidValue("significance level");
			}
		}
		else
			GraphParameters.setDefaultPValue();
		GraphParameters.correctionMethod = correctionMethod.toLowerCase();
		if(allPValues)
			GraphParameters.allPValues = 1;
		else
			GraphParameters.allPValues = 0;

		if(!savePath.isEmpty()) {
			// convert filename into an File object after mapping it to the absolute filepath
			File outFile = new File(savePath).getAbsoluteFile();
			// retrieve parent directory
			File outParent = outFile.getParentFile();
		
			// check if parent directory exists
			if (!outParent.exists())
				SubGraphMiningException.exceptionDirNotExists(outParent.getAbsolutePath());
			// check if parent directory is writable
			if (!outParent.canWrite())
				SubGraphMiningException.exceptionPermissions(outParent.getAbsolutePath());
			// check if file already exists and is writable
			if ( outFile.exists() && !outFile.canWrite() )
					SubGraphMiningException.exceptionPermissions(outFile.getAbsolutePath());
			GraphParameters.output = outFile.getPath();
		}
		else {
			GraphParameters.output = "none";
		}
		GraphParameters.typeAlgorithm = algorithm.toLowerCase();

	}

}
