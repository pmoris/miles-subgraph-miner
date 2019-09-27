package com.uantwerp.algorithms;

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
import com.uantwerp.algorithms.utilities.MultipleTestingCorrection;
import com.uantwerp.algorithms.utilities.OutputUtility;
import com.uantwerp.algorithms.utilities.PrintUtility;

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
		recalculateAndPrintResults(t1);
	}
	
//	for a start this representation works only with undirected graphs, if you pick this with directed we get an exception
	private void gspanRepresentationStart(Timer t1){
		GSpan.startAlgorithm();
		recalculateAndPrintResults(t1);
	}
	
	private void aprioriRepresentation(Timer t1){
		FSG fsg = new FSG();
		fsg.fsg();
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

	private void recalculateAndPrintResults(Timer t1){
		
		// if a file with interesting vertices was provided
		// evaluates to 0 for both empty or non-existent files
//		if (GraphParameters.interestFile != null && GraphParameters.interestFile.length() != 0) {
		if (GraphParameters.frequentMining == false) {

			// print information on which type of subgraphs will be shown (all supported with raw p-values or only p <= alpha)
//			OutputUtility.preResultStatistics();
			
			// perform multiple testing correction and store number of significant subgraphs
			MiningState.supportedMotifsAdjustedPValues = MultipleTestingCorrection.adjustPValues(MiningState.supportedMotifsPValues, GraphParameters.correctionMethod);
			
			// generate table with motifs, freqs and p-values (also stores the number of significant subgraphs after correction)
			String outputTable = OutputUtility.createTable();
			
			// write output files or print to stdout
			OutputUtility.writeOutputFiles(outputTable);

			// print summary statistics
			OutputUtility.printStatistics();
		}

		// if no interesting vertices were provided = frequent subgraph mode
		else {		
			// print information on which type of subgraphs will be shown (support > threshold)
//			OutputUtility.preResultStatisticsFrequent();

			// generate table with motifs and support values
			String outputTable = OutputUtility.createTableFrequent();

			// write output files or print to stdout
			OutputUtility.writeOutputFiles(outputTable);

			// print summary statistics
			OutputUtility.printStatisticsFrequent();
		}

		VariablesTimer.finishProcess();
		t1.cancel();
	}
}
