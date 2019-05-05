package com.uantwerp.algorithms;

import java.util.HashMap;

public abstract class MiningState {
	//This will contain all subgraphs for which the significance has been calculated
	public static HashMap<String,Integer> checkedMotifsGroupSupport = new HashMap<>();	// Store the motifs that have been checked in the group of interest alongside their frequency
	public static HashMap<String,Double> supportedMotifsPValues = new HashMap<>();	// String representation of a supported motif mapping to its probability (p-value of hypergeometric test)
	public static HashMap<String,Integer> supportedMotifsGraphSupport = new HashMap<>();	// String representation of a supported motif mapping to its frequency in the entire graph
	public static HashMap<String,String> motifTransformations = new HashMap<>();
	
	public static void resetMiningState(){
		checkedMotifsGroupSupport = new HashMap<>();
		supportedMotifsPValues = new HashMap<>();
		supportedMotifsGraphSupport = new HashMap<>();
		motifTransformations = new HashMap<>();
	}
}
