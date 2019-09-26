package com.uantwerp.algorithms;

import java.util.HashMap;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;

public abstract class MiningState {
	/*
	 * This class will store all checked subgraphs.
	 * This class will contain all subgraphs for which the significance has been calculated.
	 * In other words, all subgraphs that have passsed the pruning algorithm
	 */
	
	// Contains the motifs that have been checked in the group of interest alongside their support/frequency,
	// no support threshold pruning is performed on this set
	public static HashMap<String,Integer> checkedMotifsGroupSupport = new HashMap<>();
	
	// Contains motifs that meet the support/frequency threshold,
	// in the form of motif string representations mapping to probabilities (p-value of hypergeometric test)
	public static HashMap<String,Double> supportedMotifsPValues = new HashMap<>();
	
	// Contains motifs that meet the support threshold,
	// in the form of motif string representations mapping to adjusted p-values
	public static HashMap<String,Double> supportedMotifsAdjustedPValues = new HashMap<>();
	
	// Stores the number of subgraphs whose raw p-value meets the unadjusted p-value threshold
	public static int significantRawSubgraphCounter;
	
	// Stores the number of subgraphs that meet the adjusted p-value threshold
	public static int significantAdjustedSubgraphCounter;
	
	// Contains motifs that meet the support threshold,
	// in the form of motif string representations mapping to support/frequency in the entire graph
	public static HashMap<String,Integer> supportedMotifsGraphSupport = new HashMap<>();
	
	public static HashMap<String,String> motifTransformations = new HashMap<>();
	
	// Contains motifs that meet the support threshold,
	// in the form of motif string representations mapping to DFScode motif objects
	public static HashMap<String, DFScode<DFSedge>> supportedMotifsDFScode = new HashMap<>();
	
	public static void resetMiningState(){
		checkedMotifsGroupSupport = new HashMap<>();
		supportedMotifsPValues = new HashMap<>();
		supportedMotifsGraphSupport = new HashMap<>();
		motifTransformations = new HashMap<>();
		supportedMotifsDFScode = new HashMap<>();
	}
}
