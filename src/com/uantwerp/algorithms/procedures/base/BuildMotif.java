/**
 * 
 */
package com.uantwerp.algorithms.procedures.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;

/**
 * @author gerardo
 *
 */
public abstract class BuildMotif {
	
	/**
	 * Iteratively builds motifs, starting from 1 edge (e.g. 1VAL-2VAL) and recursively extending it.
	 * Throughout the process motifs that occur in the graph are tracked and stored alongside their
	 * frequency and hypergeometric test p-value.
	 * 
	 * @param 	motif		a DFSCode (Array) of DFSedges e.g. [1 -1 ,1 -2 ]
	 * @param 	prevHits	nodes set, initially consists of the background set
	 * 						(defaults to the entire graph)
	 * 						during recursion it will consist of the previously encountered nodes (hitNodes)
	 * @return	biggerfim	
	 */
	public static int build_motif(DFScode<DFSedge> motif, HashSet<String> prevHits){
		int interestSupport = 0;
		int checkedGroupNodes = 0;
		int biggerfim = 0;
		int totalSupport = 0;	// Support with all nodes
		HashSet<String> hitNodes = new HashSet<>();
		HashMap<Integer,String> motifLabels = new HashMap<>();	// maps node id to its labels in the motif
		HashMap<Integer,Integer> nodeTargets = new HashMap<>(); // maps target id to its degree in the motif
		HashMap<Integer,Integer> nodeSources = new HashMap<>();	// maps source id to its degree in the motif
		String motifString = AlgorithmUtility.getStringMotifs(motif);	// store a string representation of the motif

		// Loop through edges in motif and store all labels and source/target nodes
		for (int i = 0; i < motif.size(); i++){
			motifLabels.put(motif.get(i).getSourceId(), motif.get(i).getSourceLabel());
			motifLabels.put(motif.get(i).getTargetId(), motif.get(i).getTargetLabel());
			nodeTargets.put(motif.get(i).getTargetId(), (nodeTargets.containsKey(motif.get(i).getTargetId())?nodeTargets.get(motif.get(i).getTargetId())+1:1));
			nodeSources.put(motif.get(i).getSourceId(), (nodeSources.containsKey(motif.get(i).getSourceId())?nodeSources.get(motif.get(i).getSourceId())+1:1));
		}
		int maxId = motifLabels.size();
		if (GraphParameters.verbose==1)
			System.out.println("Starting on "+motifString);
		
		// loop through all nodes of interest and check if match the motif source node
		// if so, try and match the current motif by building upon it
		Iterator<String> it = GraphParameters.graph.group.iterator();
		checkgroup: while(it.hasNext()){	
			// check each interesting node if they are a root node
			String keyValue = it.next();

			// check if part of the prevHit (initially background) set, otherwise move on to next node of interest
			// this prevents nodes from being counted as checked when they don't occur in the background/prevHits set
			if(!prevHits.contains(keyValue)){ continue checkgroup; }
			checkedGroupNodes++;
			
			// store node as a match if its label matches the first label in the motif (id -> node name), otherwise continue to next node
			if (!GraphParameters.graph.vertex.get(keyValue).contains(motifLabels.get(1))){continue checkgroup;	}
			HashMap<Integer,String> supMatch = new HashMap<>();
			supMatch.put(1, keyValue);

			// check if starting node can be extended to a full match of the motif
			HashMap<Integer, String> supMatchRef = new HashMap<>();			
			supMatchRef = MatchSubgraph.matchSubgraph(motif, supMatch);
			if (supMatchRef!=null){
				interestSupport++;
				hitNodes.add(keyValue);
			}
		}
		
		// Add motif to list of checked motifs
		MiningState.checkedMotifsGroupSupport.put(motifString, interestSupport);
		
		// Stop with this subgraph motif if it did not pass the required support
		if (interestSupport < GraphParameters.supportcutoff){ 
			return biggerfim;
		}
		
		biggerfim++;

		// if it passes the support threshold, continue and
		// count the occurrences of the motif in the prevHits set (initially the background)
		Iterator<String> it2 = prevHits.iterator();
		while (it2.hasNext()){
			String node = it2.next();
			
			// Check if proposed prevHits node occurs in the group of interest and was hit (avoids having to check label again),
			// then increase the total support
			if (GraphParameters.graph.group.contains(node)){ // ???? prev hits will be smaller for growing motif???
				if (hitNodes.contains(node)){
					totalSupport++;
				}
				continue;
			}
			// Check if proposed prevHits node matches label of first node in motif and has the required edges
			if (!GraphParameters.graph.vertex.get(node).contains(motifLabels.get(1))){continue;}
			if (nodeSources.containsKey(1)){
				if (GraphParameters.graph.edgeHash.containsKey(node)){
					if (GraphParameters.graph.edgeHash.get(node).size() < nodeSources.get(1)) continue;
				}else continue;
			}
			if (nodeTargets.containsKey(1)){
				if (GraphParameters.graph.reverseEdgeHash.containsKey(node)){
					if (GraphParameters.graph.reverseEdgeHash.get(node).size() < nodeTargets.get(1)) continue;
				}else continue;
			}
			
			HashMap<Integer,String> supMatch = new HashMap<>();
			supMatch.put(1, node);
			HashMap<Integer, String> supMatchRef = new HashMap<>();
			supMatchRef = MatchSubgraph.matchSubgraph(motif,supMatch);
			if (supMatchRef!=null){
				totalSupport++;
				hitNodes.add(node);
			}
		}
		
		// Calculate significance of the association between the motif and the nodes of interest through a hypergeometric test
		double pvalue = AlgorithmUtility.getProbability(prevHits.size(),checkedGroupNodes, totalSupport, interestSupport);
		
		if(GraphParameters.verbose==1)
			System.out.println(motifString+"\t"+interestSupport+"\t"+totalSupport+"\t"+pvalue);
		
		MiningState.supportedMotifsPValues.put(motifString, pvalue);
		MiningState.supportedMotifsGraphSupport.put(motifString, totalSupport);
		MiningState.supportedMotifsDFScode.put(motifString, motif);


		// Extend the motif
		Iterator<Integer> it3 = motifLabels.keySet().iterator();
		while (it3.hasNext()){
			List<DFSedge> potentialEdges = new ArrayList<>();
			Integer sourceId = it3.next();
			
			// Try connecting each node in motif to each other node
			Iterator<Integer> it4 = motifLabels.keySet().iterator();
			LABEL2: while (it4.hasNext()){
				Integer targetId = it4.next();
				DFSedge edge = new DFSedge(sourceId, motifLabels.get(sourceId), targetId, motifLabels.get(targetId));
				if (AlgorithmUtility.checkContainsEdge(motif, edge)) 
					continue LABEL2;
				else 
					potentialEdges.add(edge);
			}
			
			// Try adding a new edge with every possible label
			for (int i = 0; i<GraphParameters.graph.possibleLabels.size(); i++){
				DFSedge forwardEdge = new DFSedge(sourceId, motifLabels.get(sourceId), maxId+1, GraphParameters.graph.possibleLabels.get(i));
				potentialEdges.add(forwardEdge);
				DFSedge backwardEdge = new DFSedge(maxId+1, GraphParameters.graph.possibleLabels.get(i),sourceId, motifLabels.get(sourceId));
				potentialEdges.add(backwardEdge);
			}
			
			TARGETLOOP: for(int i=0; i<potentialEdges.size(); i++){
				DFScode<DFSedge> newMotif = new DFScode<>();
				newMotif.addAll(motif);
				newMotif.add(potentialEdges.get(i));
				if (newMotif.maxVertexIndexNaive() > GraphParameters.maxsize)
					continue TARGETLOOP;
				String newMotifString = AlgorithmUtility.getStringMotifs(newMotif);
				DFScode<DFSedge> optNewMotif = new DFScode<>();
				if(MiningState.motifTransformations.containsKey(newMotifString)){
					optNewMotif = newMotif;
				}else{
					optNewMotif = OptimizeMotif.optimizeMotif(newMotif);
					MiningState.motifTransformations.put(newMotifString, AlgorithmUtility.getStringMotifs(optNewMotif));
					if (GraphParameters.verbose==1)
						System.out.println("Transformed "+AlgorithmUtility.getStringMotifs(newMotif)+" into: "+AlgorithmUtility.getStringMotifs(optNewMotif));
					newMotifString = AlgorithmUtility.getStringMotifs(optNewMotif); // only used to check presence, can be changed by storing list of dfsCode motifs in mining state instead of string, then call tostring inside printing function
				}
				if (MiningState.checkedMotifsGroupSupport.containsKey(newMotifString)){
					continue TARGETLOOP;
				}else{
					build_motif(optNewMotif, hitNodes);
					biggerfim++;
				}
			}
		}
		return biggerfim;
	}
	
	public static int build_motif_und(DFScode<DFSedge> motifs, HashSet<String> prevHits){
		int interestSupport = 0;
		int checkedGroupNodes = 0;
		int biggerfim = 0;
		int totalSupport = 0; //Support with all nodes
		HashSet<String> hitNodes = new HashSet<>();
		HashMap<Integer,String> motifLabels = new HashMap<>();
		HashMap<Integer,Integer> nodeSources = new HashMap<>();
		String motifString = AlgorithmUtility.getStringMotifs(motifs);
		
		for (int i = 0; i < motifs.size(); i++){
			motifLabels.put(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel());
			motifLabels.put(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel());
			nodeSources.put(motifs.get(i).getTargetId(), (nodeSources.containsKey(motifs.get(i).getTargetId())?nodeSources.get(motifs.get(i).getTargetId())+1:1));
			nodeSources.put(motifs.get(i).getSourceId(), (nodeSources.containsKey(motifs.get(i).getSourceId())?nodeSources.get(motifs.get(i).getSourceId())+1:1));
		}
		int maxId = motifLabels.size();
		if (GraphParameters.verbose==1)
			System.out.println("Starting on "+ motifString);
		
		Iterator<String> it = GraphParameters.graph.group.iterator();
		checkgroup: while(it.hasNext()){//Check each interesting node if they are a root node
			String keyValue = it.next();
			//Check if part of the prevHit set
			if(!prevHits.contains(keyValue)){ continue checkgroup; }	
			
			checkedGroupNodes++;
			
			//Check if first node matches label (otherwise skip)
			if (!GraphParameters.graph.vertex.get(keyValue).contains(motifLabels.get(1))){ continue checkgroup;}
			
			HashMap<Integer,String> supMatch = new HashMap<>();
			supMatch.put(1, keyValue);
			HashMap<Integer, String> supMatchRef = new HashMap<>();
			supMatchRef = MatchSubgraph.matchSubgraph_und(motifs,supMatch);
			if(supMatchRef!=null){
				interestSupport++;
				hitNodes.add(keyValue);
			}
		}
		
		MiningState.checkedMotifsGroupSupport.put(motifString, interestSupport);
		
		//Stop with this subgraph if it did not pass the required support
		if (interestSupport < GraphParameters.supportcutoff){ 
			return biggerfim;
		}
		
		biggerfim++;
		Iterator<String> it2 = prevHits.iterator();
		while (it2.hasNext()){
			String node = it2.next();
			//Check if already run with group nodes
			if (GraphParameters.graph.group.contains(node)){
				if (hitNodes.contains(node)){
					totalSupport++;
				}
				continue;
			}
			//Check if first node matches label (otherwise skip) and has the required edges
			if (!GraphParameters.graph.vertex.get(node).contains(motifLabels.get(1))){ continue; }
			if (nodeSources.containsKey(1)){
				int c = 0;
				if (GraphParameters.graph.edgeHash.containsKey(node))
					c += GraphParameters.graph.edgeHash.get(node).size();
				if (GraphParameters.graph.reverseEdgeHash.containsKey(node))
					c += GraphParameters.graph.reverseEdgeHash.get(node).size();
				if (c < nodeSources.get(1)) { continue; }
			}
			
			HashMap<Integer,String> supMatch = new HashMap<>();
			supMatch.put(1, node);
			HashMap<Integer, String> supMatchRef = new HashMap<>();
			supMatchRef = MatchSubgraph.matchSubgraph_und(motifs,supMatch);
			if(supMatchRef!=null){
				totalSupport++;
				hitNodes.add(node);
			}
		}
		
		// Calculate significance of the association between the motif and the nodes of interest through a hypergeometric test
		double pvalue = AlgorithmUtility.getProbability(prevHits.size(),checkedGroupNodes, totalSupport, interestSupport);
		
		if(GraphParameters.verbose==1)
			System.out.println(motifString+"\t"+interestSupport+"\t"+totalSupport+"\t"+pvalue);
		
		MiningState.supportedMotifsPValues.put(motifString, pvalue);
		MiningState.supportedMotifsGraphSupport.put(motifString, totalSupport);
		
		Iterator<Integer> it3 = motifLabels.keySet().iterator();
		while (it3.hasNext()){
			List<DFSedge> potentialEdges = new ArrayList<>();
			int sourceId = it3.next();
			
			//Try connecting each node in motif to each other node
			Iterator<Integer> it4 = motifLabels.keySet().iterator();
			LABEL2: while (it4.hasNext()){
				Integer targetId = it4.next();

				if (sourceId >= targetId) { continue LABEL2; }
				
				DFSedge edge = new DFSedge(sourceId, motifLabels.get(sourceId), targetId, motifLabels.get(targetId));
				if (AlgorithmUtility.checkContainsEdge(motifs, edge)) 
					continue LABEL2;
				else 
					potentialEdges.add(edge);
			}
			
			//Try adding a new edge with every possible label
			for (int i = 0; i<GraphParameters.graph.possibleLabels.size(); i++){
				DFSedge forwardEdge = new DFSedge(sourceId, motifLabels.get(sourceId), maxId+1, GraphParameters.graph.possibleLabels.get(i));
				potentialEdges.add(forwardEdge);
			}
			
			TARGETLOOP: for(int i=0; i<potentialEdges.size(); i++){
				DFScode<DFSedge> newMotif = new DFScode<>();
				newMotif.addAll(motifs);
				newMotif.add(potentialEdges.get(i));
				if (newMotif.maxVertexIndexNaive() > GraphParameters.maxsize)
					continue TARGETLOOP;
				String newMotifString = AlgorithmUtility.getStringMotifs(newMotif);
				DFScode<DFSedge> optNewMotif = new DFScode<>();
				if(MiningState.motifTransformations.containsKey(newMotifString)){
					optNewMotif = newMotif;
				}else{
					optNewMotif = OptimizeMotif.optimizeMotif_und(newMotif);
					MiningState.motifTransformations.put(newMotifString, AlgorithmUtility.getStringMotifs(optNewMotif));
					if (GraphParameters.verbose==1)
						System.out.println("Transformed "+AlgorithmUtility.getStringMotifs(newMotif)+" into: "+AlgorithmUtility.getStringMotifs(optNewMotif));
					newMotifString = AlgorithmUtility.getStringMotifs(optNewMotif);
				}
				if (MiningState.checkedMotifsGroupSupport.containsKey(newMotifString)){
					continue TARGETLOOP;
				}else{
					build_motif_und(optNewMotif, hitNodes);
					biggerfim++;
				}
			}
		}
		return biggerfim;
	}
}
