package com.uantwerp.algorithms;

import java.io.IOException;
import java.util.Iterator;
import java.util.Timer;

import com.uantwerp.algorithms.Efficiency.VariablesTimer;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.procedures.base.BuildMotif;
import com.uantwerp.algorithms.procedures.fsg.FSG;
import com.uantwerp.algorithms.procedures.gspan.GSpan;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.FileUtility;
import com.uantwerp.algorithms.utilities.PrintUtility;
import com.uantwerp.algorithms.visualisation.HTMLCreator;
import com.uantwerp.algorithms.visualisation.MotifToJsonConversion;

public class AlgorithmFunctionality {

	public void mainProcedure(Timer t1){
//		Import graphs, labels, set of interest and background files and store as HashMaps and HashSets
		HashGeneration.graphGeneration();
//		if (GraphParameters.verbose == 1) 
		PrintUtility.printSummary();
		GraphParameters.supportcutoff = AlgorithmUtility.supportTreshold();
		if (GraphParameters.typeAlgorithm.equals("base")){
			naiveRepresentationStart(t1);
		}else if (GraphParameters.typeAlgorithm.equals("gspan")) {
			gspanRepresentationStart(t1);
		}else if (GraphParameters.typeAlgorithm.equals("fsg")) {
			aprioriRepresentation(t1);
		}else {
			SubGraphMiningException.exceptionWrongAlgorithmChoice(GraphParameters.typeAlgorithm);
		}
	}

	/**
	 * Loop through all possible starting motifs (i.e. 1 edge sized) by iterating over
	 * all possible label pair combinations, e.g. 1VAL-1VAL, 1VAL-2VAL, etc.
	 * Each proposed edge is sent to the build_motif(_und) function which checks and
	 * extends these proposed edges recursively.
	 * For directed graphs, self-loops and both directions of an edge are proposed.
	 * motif/forwardmotif/backwardmotif is a DFSCode object, an ArrayList of DFSEdges.
	 */
	private void naiveRepresentationStart(Timer t1){
		for (int i = 0; i<GraphParameters.graph.possibleLabels.size(); i++){
			if (GraphParameters.undirected == 0){
//				For directed graphs, generate potential self-loops i -> i (cannot occur in undirected graph)
				DFScode<DFSedge> motif = new DFScode<>();
				motif.add(new DFSedge(1,GraphParameters.graph.possibleLabels.get(i),1,GraphParameters.graph.possibleLabels.get(i),true));				
				BuildMotif.build_motif(motif,GraphParameters.graph.bgnodes);
			}
			for (int j = 0; j < GraphParameters.graph.possibleLabels.size(); j++){
				if (GraphParameters.undirected == 1){
//				For undirected graphs, generate motifs i -> j
					DFScode<DFSedge> forwardmotif = new DFScode<>();
					forwardmotif.add(new DFSedge(1,GraphParameters.graph.possibleLabels.get(i),2,GraphParameters.graph.possibleLabels.get(j),true));
					BuildMotif.build_motif_und(forwardmotif,GraphParameters.graph.bgnodes);
				}else{
//				For directed graphs, generate both motifs i -> j and j -> i
//					Single edge to start building from
					DFScode<DFSedge> forwardmotif = new DFScode<>();
					forwardmotif.add(new DFSedge(1,GraphParameters.graph.possibleLabels.get(i),2,GraphParameters.graph.possibleLabels.get(j),true));
					BuildMotif.build_motif(forwardmotif,GraphParameters.graph.bgnodes);
//					Single edge to start building from
					DFScode<DFSedge> backwardmotif = new DFScode<>();
					backwardmotif.add(new DFSedge(2,GraphParameters.graph.possibleLabels.get(i),1,GraphParameters.graph.possibleLabels.get(j),true));
					BuildMotif.build_motif(backwardmotif,GraphParameters.graph.bgnodes);
				}
			}
		}
		printStatistics();
		recalculateAndPrintResults(t1);
	}
	
//	for a start this representation works only with undirected graphs, if you pick this with directed we get an exception
	private void gspanRepresentationStart(Timer t1){
		GSpan.startAlgorithm();
		printStatistics();
		recalculateAndPrintResults(t1);
	}
	
	private void aprioriRepresentation(Timer t1){
		FSG fsg = new FSG();
		fsg.fsg();
		printStatistics();
		recalculateAndPrintResults(t1);
	}
	
//	public void printGraphVariables(){
//		System.out.println("print graph");
//		PrintUtility.printHasMapHashSet(GraphParameters.graph.edgeHash);
//		System.out.println("print reverse graph");
//		PrintUtility.printHasMapHashSet(GraphParameters.graph.reverseEdgeHash);
//		System.out.println("print labels");
//		PrintUtility.printHasMapHashSet(GraphParameters.graph.vertex);
//		System.out.println("print reverse labels");
//		PrintUtility.printHasMapHashSet(GraphParameters.graph.reverseVertex);
//		System.out.println("print possible labels");
//		PrintUtility.printListString(GraphParameters.graph.possibleLabels);
//		System.out.println("print group, size: " + GraphParameters.graph.group.size());
//		PrintUtility.printHSetString(GraphParameters.graph.group);
//		System.out.println("print bgnodes");
//		PrintUtility.printHashSet(GraphParameters.graph.bgnodes);
//		System.out.println("Label hash");
//		PrintUtility.printHasMap2(GraphParameters.graph.labelHash);
//	}
	
	public static void printStatistics(){
//		if (GraphParameters.verbose == 1){
			System.out.println("After looking through the graph the following statistics were found:");
			System.out.println(MiningState.checkedMotifsGroupSupport.size() + " candidate subgraphs were discovered");
			System.out.println("Of which " + MiningState.supportedMotifsGraphSupport.size() + " meet the support threshold " + GraphParameters.supportcutoff);
			System.out.println("Of which " + MiningState.supportedMotifsPValues.size() + " are significant before Bonferroni-correction");
//		}
	}
	
	private void recalculateAndPrintResults(Timer t1){
		int i = 0;
		// if a file with interesting vertices was provided
		if (GraphParameters.interestFile != null && GraphParameters.interestFile.length() != 0) {	// evaluates to 0 for both empty or non-existent files

			Double bonferroni = GraphParameters.pvalue / MiningState.supportedMotifsPValues.size();

//			if (GraphParameters.verbose == 1) {
				System.out.println("Checked: " + MiningState.supportedMotifsPValues.size() + " subgraphs");
				System.out.println("Bonferonni-corrected P-value cutoff =  " + bonferroni);
				if (GraphParameters.allPValues == 1) System.out.println(
						"Retrieving all frequent motifs regardless of whether they pass the Bonferroni-adjusted significance threshold...");
				else System.out.println(
						"Retrieving all frequent motifs that pass the Bonferroni-adjusted significance threshold...");
//			}

			// generate table with motifs, freqs and p-values
			String message;
			if (GraphParameters.allPValues == 1)
				message = "Motif\tFreq Interest\tFreq Total\tP-value\tBonferroni-adjusted P-value";
			else
				message = "Motif\tFreq Interest\tFreq Total\tP-value";

			Iterator<String> it = MiningState.supportedMotifsPValues.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (GraphParameters.allPValues == 1) {
					message = message + "\n" + key + "\t" + MiningState.checkedMotifsGroupSupport.get(key) + "\t" + 
							MiningState.supportedMotifsGraphSupport.get(key) + "\t" + MiningState.supportedMotifsPValues.get(key) +
							"\t" + MiningState.supportedMotifsPValues.get(key) * MiningState.supportedMotifsPValues.size();
					if (MiningState.supportedMotifsPValues.get(key) <= bonferroni) i++;
				}

				else {
					if (MiningState.supportedMotifsPValues.get(key) <= bonferroni) {
						message = message + "\n" + key + "\t" + MiningState.checkedMotifsGroupSupport.get(key) + "\t" + 
								MiningState.supportedMotifsGraphSupport.get(key) + "\t" + MiningState.supportedMotifsPValues.get(key);
						i++;
					}
				}
			}

			// write output file or print to stdout
			if (!GraphParameters.output.equals("none")){
				FileUtility.writeFile(GraphParameters.output, message.replace(" ", "_"));
			}else{
				System.out.println(message.replace(" ", "_"));
			}

			// convert motifs to JSON format for cytoscape.js
			String JSON = MotifToJsonConversion.convertAllMotifs(bonferroni);			

			// write html visualisation file
			if (!GraphParameters.output.equals("none")){
				try {
					String htmlVisualisation = HTMLCreator.createHTML(JSON, message, bonferroni, i);
					FileUtility.writeFile(GraphParameters.output + ".html", htmlVisualisation);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			System.out.println("Significant motifs after Bonferroni-correction: " + i);			
		}
		// if no interesting vertices were provided
		else{
//			if (GraphParameters.verbose == 1) {
				System.out.println("Retrieving frequencies for all motifs...");
//			}

			String message = "Motif\tSupport";

			Iterator<String> it = MiningState.supportedMotifsGraphSupport.keySet().iterator();
			while (it.hasNext()){
				String key = it.next();
				message = message + "\n" + key + "\t" + MiningState.supportedMotifsGraphSupport.get(key);
			}
			
			// write output file or print to stdout
			if (!GraphParameters.output.equals("none")){
				FileUtility.writeFile(GraphParameters.output, message.replace(" ", "_"));
			}else{
				System.out.println(message.replace(" ", "_"));
			}

			// convert motifs to JSON format for cytoscape.js
			Double bonferroni = Double.NaN; // set bonferroni to NaN to instruct JSON converter to add all motifs
			String JSON = MotifToJsonConversion.convertAllMotifs(bonferroni);			

			// write html visualisation file
			if (!GraphParameters.output.equals("none")){
				try {
					String htmlVisualisation = HTMLCreator.createHTML(JSON, message, bonferroni, i);
					FileUtility.writeFile(GraphParameters.output + ".html", htmlVisualisation);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.out.println("Computed frequencies (support) for " + MiningState.supportedMotifsGraphSupport.size() + " subgraphs.");

		}
		
		VariablesTimer.finishProcess();
		t1.cancel();
	}
	
}
