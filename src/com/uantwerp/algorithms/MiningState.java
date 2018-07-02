package com.uantwerp.algorithms;

import java.util.HashMap;

public abstract class MiningState {
	//This will contain all subgraphs for which the significance has been calculated
	public static HashMap<String,Integer> checkedmotifs = new HashMap<>();
	public static HashMap<String,Double> sigmotifs = new HashMap<>();
	public static HashMap<String,Integer> freqmotifs = new HashMap<>();
	public static HashMap<String,String> motiftransformations = new HashMap<>();
	
	public static void resetMiningState(){
		checkedmotifs = new HashMap<>();
		sigmotifs = new HashMap<>();
		freqmotifs = new HashMap<>();
		motiftransformations = new HashMap<>();
	}
}
