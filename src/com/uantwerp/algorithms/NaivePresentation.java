package com.uantwerp.algorithms;

import java.util.Timer;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.gui.SubgraphMiningGUI;
import com.uantwerp.algorithms.procedures.base.BuildMotif;

public class NaivePresentation extends BasicRepresentation{

	public NaivePresentation(Timer t, SubgraphMiningGUI mainThread) {
		super(t, mainThread);
	}
	
	/**
	 * Loop through all possible starting motifs (i.e. 1 edge sized) by iterating over
	 * all possible label pair combinations, e.g. 1VAL-1VAL, 1VAL-2VAL, etc.
	 * Each proposed edge is sent to the build_motif(_und) function which checks and
	 * extends these proposed edges recursively.
	 * For directed graphs, self-loops and both directions of an edge are proposed.
	 * motif/forwardmotif/backwardmotif is a DFSCode object, an ArrayList of DFSEdges.
	 */
	protected void runAnalysis() {
		for (int i = 0; i<GraphParameters.graph.possibleLabels.size() && !Thread.interrupted(); i++){
			if (GraphParameters.undirected == 0){
//				For directed graphs, generate potential self-loops i -> i (cannot occur in undirected graph)
				DFScode<DFSedge> motif = new DFScode<>();
				motif.add(new DFSedge(1,GraphParameters.graph.possibleLabels.get(i),1,GraphParameters.graph.possibleLabels.get(i),true));				
				BuildMotif.build_motif(motif,GraphParameters.graph.bgnodes);
			}
			for (int j = 0; j < GraphParameters.graph.possibleLabels.size() && !Thread.interrupted(); j++){
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
	}

}
