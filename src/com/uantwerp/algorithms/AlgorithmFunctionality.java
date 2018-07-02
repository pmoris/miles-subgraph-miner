package com.uantwerp.algorithms;

import java.util.Iterator;
import java.util.Timer;

import com.uantwerp.algorithms.Efficiency.VariablesTimer;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.procedures.base.BuildMotif;
import com.uantwerp.algorithms.procedures.fsg.FSG;
import com.uantwerp.algorithms.procedures.gspan.GSpan;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.FileUtility;
import com.uantwerp.algorithms.utilities.PrintUtility;

public class AlgorithmFunctionality {

	public void mainProcedure(Timer t1){
		HashGeneration.graphGeneration();
		if (GraphPathParameters.verbose == 1) PrintUtility.printSummary();
		GraphPathParameters.supportcutoff = AlgorithmUtility.supportTreshold();
		if (GraphPathParameters.typeAlgorithm.equals("base")){
			naiveRepresentationStart(t1);
		}else if (GraphPathParameters.typeAlgorithm.equals("gspan")) {
			gspanRepresentationStart(t1);
		}else if (GraphPathParameters.typeAlgorithm.equals("fsg")) {
			aprioriRepresentation(t1);
		}
	}
			
	private void naiveRepresentationStart(Timer t1){
		for (int i = 0; i<GraphPathParameters.graph.possibleLabels.size(); i++){
			if (GraphPathParameters.undirected == 0){
				DFScode<DFSedge> motif = new DFScode<>();
				motif.add(new DFSedge(1,GraphPathParameters.graph.possibleLabels.get(i),1,GraphPathParameters.graph.possibleLabels.get(i),true));				
				BuildMotif.build_motif(motif,GraphPathParameters.graph.bgnodes);
			}
			for (int j = 0; j < GraphPathParameters.graph.possibleLabels.size(); j++){
				if (GraphPathParameters.undirected == 1){
					DFScode<DFSedge> forwardmotif = new DFScode<>();
					forwardmotif.add(new DFSedge(1,GraphPathParameters.graph.possibleLabels.get(i),2,GraphPathParameters.graph.possibleLabels.get(j),true));
					BuildMotif.build_motif_und(forwardmotif,GraphPathParameters.graph.bgnodes);
				}else{
					//Single edge to start building from
					DFScode<DFSedge> forwardmotif = new DFScode<>();
					forwardmotif.add(new DFSedge(1,GraphPathParameters.graph.possibleLabels.get(i),2,GraphPathParameters.graph.possibleLabels.get(j),true));
					BuildMotif.build_motif(forwardmotif,GraphPathParameters.graph.bgnodes);
					//Single edge to start building from
					DFScode<DFSedge> backwardmotif = new DFScode<>();
					backwardmotif.add(new DFSedge(2,GraphPathParameters.graph.possibleLabels.get(i),1,GraphPathParameters.graph.possibleLabels.get(j),true));
					BuildMotif.build_motif(backwardmotif,GraphPathParameters.graph.bgnodes);
				}
			}
		}
		printStatistics();
		recalculateAndPrintResults(t1);
	}
	
	//for a start this representation works only with undirected graphs, if you pic this with directed we gen an exception
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
	
	public void printGraphVariables(){
		System.out.println("print graph");
		PrintUtility.printHasMapHashSet(GraphPathParameters.graph.edgeHash);
		System.out.println("print reverse graph");
		PrintUtility.printHasMapHashSet(GraphPathParameters.graph.reverseEdgeHash);
		System.out.println("print labels");
		PrintUtility.printHasMapHashSet(GraphPathParameters.graph.vertex);
		System.out.println("print reverse labels");
		PrintUtility.printHasMapHashSet(GraphPathParameters.graph.reverseVertex);
		System.out.println("print possible labels");
		PrintUtility.printListString(GraphPathParameters.graph.possibleLabels);
		System.out.println("print group, size: " + GraphPathParameters.graph.group.size());
		PrintUtility.printHSetString(GraphPathParameters.graph.group);
		System.out.println("print bgnodes");
		PrintUtility.printHashSet(GraphPathParameters.graph.bgnodes);
		System.out.println("Label hash");
		PrintUtility.printHasMap2(GraphPathParameters.graph.labelHash);
	}
	
	public static void printStatistics(){
//		if (GraphPathParameters.verbose == 1){
			System.out.println("After looking through the graph the following statistics were found");
			System.out.println(MiningState.checkedmotifs.size() + " checked graph were discovered");
			System.out.println("Of which " + MiningState.freqmotifs.size() + " are frequent");
			System.out.println("Of which " + MiningState.sigmotifs.size() + " are significant");
//		}
	}
	
	private void recalculateAndPrintResults(Timer t1){
		int i = 0;
		if (!GraphPathParameters.graph.group.isEmpty()){
			Double bonferonni = GraphPathParameters.pvalue / MiningState.sigmotifs.size();
			if (GraphPathParameters.verbose == 1) System.out.println("Checked: " + MiningState.sigmotifs.size() + " subgraphs");
			if (GraphPathParameters.verbose == 1) System.out.println("Bonferonni-corrected P-value cutoff =  "+bonferonni);
			Iterator<String> it = MiningState.sigmotifs.keySet().iterator();
			String message ="Motif \t FreqS \t FreqT \t Pvalue \t";
			while (it.hasNext()){
				String key = it.next();
				if (MiningState.sigmotifs.get(key) <= bonferonni){
					message = message + "\n" + key + "\t" + MiningState.checkedmotifs.get(key) + "\t" + MiningState.freqmotifs.get(key) + "\t" + MiningState.sigmotifs.get(key);
					i++;
				}
			}
			if (!GraphPathParameters.output.equals("none")){
				FileUtility.writeFile(GraphPathParameters.output, message.replace(" ", ""));
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
//		if (GraphPathParameters.verbose == 1) 
			System.out.println("Significant graphs after Bonferroni: "+ i);
		VariablesTimer.finishProcess();
		t1.cancel();
	}
	

	
}
