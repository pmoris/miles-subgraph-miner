package com.uantwerp.algorithms.utilities;

import java.util.Iterator;

import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.GraphParameters;

public class OutputUtility {

	public static void preResultMessage() {
//		if (GraphParameters.verbose == 1) {
		System.out.println("Checked: " + MiningState.supportedMotifsPValues.size() + " subgraphs");

		if (GraphParameters.allPValues == 1) {
			System.out.println("Retrieving all subgraphs that meet the support threshold (" + GraphParameters.supportcutoff
					+ "), without filtering on the (adjusted) p-value threshold: " + GraphParameters.pvalue);
		} else {
			System.out.println("Retrieving all subgraphs that meet the adjusted p-value threshold (" + GraphParameters.pvalue 
					+ ") after " + GraphParameters.correctionMethod + "correction.");
			}
//		}
	}
	
	public static void preResultMessageFrequentMining() {
//		if (GraphParameters.verbose == 1) {
		System.out.println("Checked: " + MiningState.supportedMotifsPValues.size() + " subgraphs");
		System.out.println("No enrichment testing was performed.");
		System.out.println("Retrieving all subgraphs that meet the support threshold " + GraphParameters.supportcutoff + ".");
//		}
	}

	public static String createTable() {
//		create table header
		String message = "Subgraph\tFreq interest\tFreq total\tRaw P-value\t"
				+ GraphParameters.correctionMethod + "-adjusted P-value";

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
		String message = "Motif\tSupport";

//		iterate through subgraphs in order they were generated (i.e. growing from root vertex)
		Iterator<String> it = MiningState.supportedMotifsGraphSupport.keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			message = message + "\n" + key + "\t" + MiningState.supportedMotifsGraphSupport.get(key);
		}
		return message;
	}
	
	public static void printStatistics(){
//		if (GraphParameters.verbose == 1){
//			System.out.println("After looking through the graph the following statistics were found:");
			System.out.println(MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were discovered");
			System.out.println(MiningState.supportedMotifsGraphSupport.size() + " subgraphs meet the support threshold " + GraphParameters.supportcutoff);
			System.out.println(MiningState.significantRawSubgraphCounter + " are significant before multiple testing correction (alpha = " + GraphParameters.pvalue);
			System.out.println(MiningState.significantAdjustedSubgraphCounter + " are significant after " + GraphParameters.correctionMethod + " correction");
//		}
	}
	
	public static void printStatisticsFrequent(){
//		if (GraphParameters.verbose == 1){
//			System.out.println("After looking through the graph the following statistics were found:");
			System.out.println(MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were discovered");
			System.out.println(MiningState.supportedMotifsGraphSupport.size() + " subgraphs meet the support threshold " + GraphParameters.supportcutoff);
//		}
	}
}
