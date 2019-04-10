package com.uantwerp.algorithms;

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

public class AlgorithmFunctionality {

	public void mainProcedure(Timer t1){
//		Import graphs, labels, set of interest and background files and store as HashMaps and HashSets
		HashGeneration.graphGeneration();
		if (GraphParameters.verbose == 1) PrintUtility.printSummary();
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
			System.out.println("After looking through the graph the following statistics were found");
			System.out.println(MiningState.checkedmotifs.size() + " checked graph were discovered");
			System.out.println("Of which " + MiningState.freqmotifs.size() + " are frequent");
			System.out.println("Of which " + MiningState.sigmotifs.size() + " are significant");
//		}
	}
	
	private void recalculateAndPrintResults(Timer t1){
		int i = 0;
		if (!GraphParameters.graph.group.isEmpty()){
			Double bonferonni = GraphParameters.pvalue / MiningState.sigmotifs.size();
			if (GraphParameters.verbose == 1) System.out.println("Checked: " + MiningState.sigmotifs.size() + " subgraphs");
			if (GraphParameters.verbose == 1) System.out.println("Bonferonni-corrected P-value cutoff =  "+bonferonni);
			Iterator<String> it = MiningState.sigmotifs.keySet().iterator();
			String message ="Motif \t FreqS \t FreqT \t Pvalue \t";
			while (it.hasNext()){
				String key = it.next();
				if (MiningState.sigmotifs.get(key) <= bonferonni){
					message = message + "\n" + key + "\t" + MiningState.checkedmotifs.get(key) + "\t" + MiningState.freqmotifs.get(key) + "\t" + MiningState.sigmotifs.get(key);
					i++;
				}
			}
			if (!GraphParameters.output.equals("none")){
				FileUtility.writeFile(GraphParameters.output, message.replace(" ", ""));
			}else{
				System.out.println(message.replace(" ", ""));
			}
		}else{
			Iterator<String> it = MiningState.freqmotifs.keySet().iterator();
			while (it.hasNext()){
				String key = it.next();
				System.out.println(key + "\t" + MiningState.freqmotifs.get(key));
			}
		}
//		if (GraphParameters.verbose == 1) 
			System.out.println("Significant graphs after Bonferroni: "+ i);
		VariablesTimer.finishProcess();
		t1.cancel();
	}
	

	
}
