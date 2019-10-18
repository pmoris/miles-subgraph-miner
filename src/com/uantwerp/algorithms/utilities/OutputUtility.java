package com.uantwerp.algorithms.utilities;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.io.FilenameUtils;

import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.visualisation.HTMLCreator;
import com.uantwerp.algorithms.visualisation.MotifToJsonConversion;

public class OutputUtility {

	public static void preResultStatistics() {
//		if (GraphParameters.verbose == 1) {
		PrintUtility.print2LogView(MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were checked.");
		PrintUtility.print2LogView(MiningState.supportedMotifsPValues.size() 
				+ " subgraphs meet the support threshold " + GraphParameters.supportcutoff + ".");
		if (GraphParameters.allPValues == 1) {
			PrintUtility.print2LogView("Retrieving all subgraphs without filtering on the (adjusted) p-value threshold for enrichment: "
					+ GraphParameters.pvalue + "...");
		} else {
			PrintUtility.print2LogView("Retrieving all subgraphs that meet the adjusted p-value threshold for enrichment (" 
					+ GraphParameters.pvalue + ") after " + correctionMethodPrettyPrint() + " correction...");
			}
//		}
	}
	
	public static void preResultStatisticsFrequent() {
//		if (GraphParameters.verbose == 1) {
		PrintUtility.print2LogView(MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were checked.");
		PrintUtility.print2LogView(MiningState.supportedMotifsPValues.size() 
				+ " subgraphs meet the support threshold " + GraphParameters.supportcutoff + ".");
		PrintUtility.print2LogView("No enrichment testing was performed...");
//		}
	}

	public static String createTable() {
//		create table header
		String message = "Subgraph\tFreq interest\tFreq total\tRaw P-value\t"
				+ correctionMethodPrettyPrint() + " adjusted P-value";

//		counter for the number of significant subgraphs after multiple testing correction
		int adjustedPValueCounter = 0;
		int unadjustedPValueCounter = 0;

//		iterate through subgraphs in order they were generated (i.e. growing from root vertex)
		Iterator<String> it = MiningState.supportedMotifsPValues.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();

//			if the all-p-values option was selected, store all (supported) subgraphs,
//			otherwise only store those that meet the adjusted p-value threshold
			if (GraphParameters.allPValues == 1) {
				message = generateTableRow(key, message);
			} else {
				if (MiningState.supportedMotifsAdjustedPValues.get(key) <= GraphParameters.pvalue) {
					message = generateTableRow(key, message);
				}
			}

//			increment significant subgraph count if adjusted p-value threshold is met
			if (MiningState.supportedMotifsAdjustedPValues.get(key) <= GraphParameters.pvalue)
				adjustedPValueCounter++;
//			increment unadjusted significant subgraph count if raw p-value threshold is met
			if (MiningState.supportedMotifsPValues.get(key) <= GraphParameters.pvalue)
				unadjustedPValueCounter++;
		}
		MiningState.significantAdjustedSubgraphCounter = adjustedPValueCounter;
		MiningState.significantRawSubgraphCounter = unadjustedPValueCounter;

		return message;
	}

	private static String generateTableRow(String key, String message) {
		message = message + "\n" + key + "\t" + MiningState.checkedMotifsGroupSupport.get(key) + "\t"
				+ MiningState.supportedMotifsGraphSupport.get(key) + "\t" + MiningState.supportedMotifsPValues.get(key)
				+ "\t" + MiningState.supportedMotifsAdjustedPValues.get(key);
		return message;
	}
	
	public static String createTableFrequent() {
//		create table header
		String message = "Subgraph\tSupport";

//		iterate through subgraphs in order they were generated (i.e. growing from root vertex)
		Iterator<String> it = MiningState.supportedMotifsGraphSupport.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			message = message + "\n" + key + "\t" + MiningState.supportedMotifsGraphSupport.get(key);
		}
		return message;
	}
	
	
	/**
	 * Write output file to stdout or output file.
	 * Generate JSON and write HTML visualisation file.
	 * @param outputTable
	 */
	public static void writeOutputFiles(String outputTable) {

		// if output file is specified, write to it and create a visualsation HTML file
		if (!GraphParameters.output.equals("none")){
			try {
				// write output file
				FileUtility.writeFile(GraphParameters.output, outputTable.replace(" ", "_"));
				PrintUtility.print2LogView("\nSaved output file to " + GraphParameters.output);
				
				// convert motifs to JSON format for cytoscape.js
				String JSON = MotifToJsonConversion.convertAllMotifs();
				
				// write HTML visualisation file
				String htmlVisualisation = HTMLCreator.createHTML(JSON, outputTable);
				String htmlFilePath = FilenameUtils.removeExtension(GraphParameters.output) + ".html";
				FileUtility.writeFile(htmlFilePath, htmlVisualisation);
				PrintUtility.print2LogView("Saved visualisation file to " + htmlFilePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// otherwise print to stdout
		else {
			PrintUtility.print2LogView("\n" + outputTable.replace(" ", "_"));
		}
	}
	
	public static void printStatistics(){
//		if (GraphParameters.verbose == 1){
//			PrintUtility.print2LogView("After looking through the graph the following statistics were found:");
			PrintUtility.print2LogView("\n" + MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were checked.");
			PrintUtility.print2LogView(MiningState.supportedMotifsGraphSupport.size() + " subgraphs meet the support threshold " + GraphParameters.supportcutoff + ".");
			PrintUtility.print2LogView(MiningState.significantRawSubgraphCounter + " are significant before multiple testing correction (alpha = " + GraphParameters.pvalue + ").");
			PrintUtility.print2LogView(MiningState.significantAdjustedSubgraphCounter + " are significant after " + correctionMethodPrettyPrint() + " correction.");
			if (GraphParameters.allPValues == 1) {
				PrintUtility.print2LogView("Listing all subgraphs without filtering on the (adjusted) p-value threshold for enrichment: "
						+ GraphParameters.pvalue + ".");
			} else {
				PrintUtility.print2LogView("Listing all subgraphs that meet the adjusted p-value threshold for enrichment (" 
						+ GraphParameters.pvalue + ") after " + correctionMethodPrettyPrint() + " correction.");
				}
//		}
	}
	
	public static void printStatisticsFrequent(){
//		if (GraphParameters.verbose == 1){
//			PrintUtility.print2LogView("After looking through the graph the following statistics were found:");
			PrintUtility.print2LogView("\n" + MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were checked.");
			PrintUtility.print2LogView(MiningState.supportedMotifsGraphSupport.size() + " subgraphs meet the support threshold " + GraphParameters.supportcutoff + ".");
			PrintUtility.print2LogView("No enrichment testing was performed.");
//		}
	}
	
	
	/**
	 * @return Full name of the chosen multiple correction method
	 */
	public static String correctionMethodPrettyPrint() {
		switch (GraphParameters.correctionMethod) {
			case "bonferroni":
				return "Bonferroni";
			case "holm":
				return "Holm";
			case "BH":
				return "Benjamini–Hochberg";
			case "BY":
				return "Benjamini–Yekutieli";
		}
		return null;
	}
}
