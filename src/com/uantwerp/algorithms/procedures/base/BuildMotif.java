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
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;

/**
 * @author gerardo
 *
 */
public abstract class BuildMotif {
	
	public static int build_motif(DFScode<DFSedge> motifs, HashSet<String> prevhits){
		int hitSupport = 0;
		int checkedgroupnodes = 0;
		int biggerfim = 0;
		int totalsupport = 0; //Support with all nodes
		HashSet<String> hitNodes = new HashSet<>();
		HashMap<Integer,String> motiflabels = new HashMap<>();
		HashMap<Integer,Integer> nodetargets = new HashMap<>();
		HashMap<Integer,Integer> nodesources = new HashMap<>();
		String motifString = AlgorithmUtility.getStringMotifs(motifs);

		for (int i = 0; i < motifs.size(); i++){
			motiflabels.put(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel());
			motiflabels.put(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel());
			nodetargets.put(motifs.get(i).getTargetId(), (nodetargets.containsKey(motifs.get(i).getTargetId())?nodetargets.get(motifs.get(i).getTargetId())+1:1));
			nodesources.put(motifs.get(i).getSourceId(), (nodesources.containsKey(motifs.get(i).getSourceId())?nodesources.get(motifs.get(i).getSourceId())+1:1));
		}
		int maxId = motiflabels.size();
		if (GraphPathParameters.verbose==1) System.out.println("Starting on "+motifString);
		
		Iterator<String> it = GraphPathParameters.graph.group.iterator();
		checkgroup: while(it.hasNext()){//Check each interesting node if they are a root node
			String keyValue = it.next();
			//Check if part of the prevhit set
			if(!prevhits.contains(keyValue)){ continue checkgroup; }	
			
			checkedgroupnodes++;
			
			//Check if first node matches label (otherwise skip)
			if (!GraphPathParameters.graph.vertex.get(keyValue).contains(motiflabels.get(1))){continue checkgroup;	}

			HashMap<Integer,String> supmatch = new HashMap<>();
			supmatch.put(1, keyValue);
			HashMap<Integer, String> supmatchref = new HashMap<>();			
			supmatchref = MatchSubgraph.matchSubgraph(motifs,supmatch);
			if (supmatchref!=null){
				hitSupport++;
				hitNodes.add(keyValue);
			}
		}
		
		MiningState.checkedmotifs.put(motifString, hitSupport);
		
		//Stop with this subgraph if it did not pass the required support
		if (hitSupport < GraphPathParameters.supportcutoff){ 
			return biggerfim;
		}
		
		biggerfim++;

		Iterator<String> it2 = prevhits.iterator();
		while (it2.hasNext()){
			String node = it2.next();
			//Check if already run with group nodes
			if (GraphPathParameters.graph.group.contains(node)){
				if (hitNodes.contains(node)){
					totalsupport++;
				}
				continue;
			}
			//Check if first node matches label (otherwise skip) and has the required edges
			if (!GraphPathParameters.graph.vertex.get(node).contains(motiflabels.get(1))){continue;}
			if (nodesources.containsKey(1)){
				if (GraphPathParameters.graph.edgeHash.containsKey(node)){
					if (GraphPathParameters.graph.edgeHash.get(node).size() < nodesources.get(1)) continue;
				}else continue;
			}
			if (nodetargets.containsKey(1)){
				if (GraphPathParameters.graph.reverseEdgeHash.containsKey(node)){
					if (GraphPathParameters.graph.reverseEdgeHash.get(node).size() < nodetargets.get(1)) continue;
				}else continue;
			}
			
			HashMap<Integer,String> supmatch = new HashMap<>();
			supmatch.put(1, node);
			HashMap<Integer, String> supmatchref = new HashMap<>();
			supmatchref = MatchSubgraph.matchSubgraph(motifs,supmatch);
			if (supmatchref!=null){
				totalsupport++;
				hitNodes.add(node);
			}
		}
		
		//Calculate significance
		double prob = AlgorithmUtility.getProbability(prevhits.size(),checkedgroupnodes, totalsupport, hitSupport);
		
		if(GraphPathParameters.verbose==1) System.out.println(motifString+"\t"+hitSupport+"\t"+totalsupport+"\t"+prob);
		
		MiningState.sigmotifs.put(motifString, prob);
		MiningState.freqmotifs.put(motifString, totalsupport);
		Iterator<Integer> it3 = motiflabels.keySet().iterator();
		while (it3.hasNext()){
			List<DFSedge> potentialedges = new ArrayList<>();
			Integer sourceId = it3.next();
			
			//Try connecting each node in motif to each other node
			Iterator<Integer> it4 = motiflabels.keySet().iterator();
			LABEL2: while (it4.hasNext()){
				Integer targetId = it4.next();
				DFSedge edge = new DFSedge(sourceId, motiflabels.get(sourceId), targetId, motiflabels.get(targetId));
				if (AlgorithmUtility.checkContainsEdge(motifs, edge)) 
					continue LABEL2;
				else 
					potentialedges.add(edge);
			}
			
			//Try adding a new edge with every possible label
			for (int i = 0; i<GraphPathParameters.graph.possibleLabels.size(); i++){
				DFSedge forwardedge = new DFSedge(sourceId, motiflabels.get(sourceId), maxId+1, GraphPathParameters.graph.possibleLabels.get(i));
				potentialedges.add(forwardedge);
				DFSedge backwardedge = new DFSedge(maxId+1, GraphPathParameters.graph.possibleLabels.get(i),sourceId, motiflabels.get(sourceId));
				potentialedges.add(backwardedge);
			}
			
			TARGETLOOP: for(int i=0; i<potentialedges.size(); i++){
				DFScode<DFSedge> newmotif = new DFScode<>();
				newmotif.addAll(motifs);
				newmotif.add(potentialedges.get(i));
				if (newmotif.maxVertexIndexNaive() > GraphPathParameters.maxsize)
					continue TARGETLOOP;
				String newmotifString = AlgorithmUtility.getStringMotifs(newmotif);
				DFScode<DFSedge> optnewmotif = new DFScode<>();
				if(MiningState.motiftransformations.containsKey(newmotifString)){
					optnewmotif = newmotif;
				}else{
					optnewmotif = OptimizeMotif.optimizeMotif(newmotif);
					MiningState.motiftransformations.put(newmotifString, AlgorithmUtility.getStringMotifs(optnewmotif));
					if (GraphPathParameters.verbose==1) System.out.println("Transformed "+AlgorithmUtility.getStringMotifs(newmotif)+" into: "+AlgorithmUtility.getStringMotifs(optnewmotif));
					newmotifString = AlgorithmUtility.getStringMotifs(optnewmotif);
				}
				if (MiningState.checkedmotifs.containsKey(newmotifString)){
					continue TARGETLOOP;
				}else{
					build_motif(optnewmotif, hitNodes);
					biggerfim++;
				}
			}
		}
		return biggerfim;
	}
	
	public static int build_motif_und(DFScode<DFSedge> motifs, HashSet<String> prevhits){
		int hitSupport = 0;
		int checkedgroupnodes = 0;
		int biggerfim = 0;
		int totalsupport = 0; //Support with all nodes
		HashSet<String> hitNodes = new HashSet<>();
		HashMap<Integer,String> motiflabels = new HashMap<>();
		HashMap<Integer,Integer> nodesources = new HashMap<>();
		String motifString = AlgorithmUtility.getStringMotifs(motifs);
		
		for (int i = 0; i < motifs.size(); i++){
			motiflabels.put(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel());
			motiflabels.put(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel());
			nodesources.put(motifs.get(i).getTargetId(), (nodesources.containsKey(motifs.get(i).getTargetId())?nodesources.get(motifs.get(i).getTargetId())+1:1));
			nodesources.put(motifs.get(i).getSourceId(), (nodesources.containsKey(motifs.get(i).getSourceId())?nodesources.get(motifs.get(i).getSourceId())+1:1));
		}
		int maxId = motiflabels.size();
//		if (GraphPathParameters.verbose==1) System.out.println("Starting on "+ motifString);
		
		Iterator<String> it = GraphPathParameters.graph.group.iterator();
		checkgroup: while(it.hasNext()){//Check each interesting node if they are a root node
			String keyValue = it.next();
			//Check if part of the prevhit set
			if(!prevhits.contains(keyValue)){ continue checkgroup; }	
			
			checkedgroupnodes++;
			
			//Check if first node matches label (otherwise skip)
			if (!GraphPathParameters.graph.vertex.get(keyValue).contains(motiflabels.get(1))){ continue checkgroup;}
			
			HashMap<Integer,String> supmatch = new HashMap<>();
			supmatch.put(1, keyValue);
			HashMap<Integer, String> supmatchref = new HashMap<>();
			supmatchref = MatchSubgraph.matchSubgraph_und(motifs,supmatch);
			if(supmatchref!=null){
				hitSupport++;
				hitNodes.add(keyValue);
			}
		}
		
		MiningState.checkedmotifs.put(motifString, hitSupport);
		
		//Stop with this subgraph if it did not pass the required support
		if (hitSupport < GraphPathParameters.supportcutoff){ 
			return biggerfim;
		}
		
		biggerfim++;
		Iterator<String> it2 = prevhits.iterator();
		while (it2.hasNext()){
			String node = it2.next();
			//Check if already run with group nodes
			if (GraphPathParameters.graph.group.contains(node)){
				if (hitNodes.contains(node)){
					totalsupport++;
				}
				continue;
			}
			//Check if first node matches label (otherwise skip) and has the required edges
			if (!GraphPathParameters.graph.vertex.get(node).contains(motiflabels.get(1))){ continue; }
			if (nodesources.containsKey(1)){
				int c = 0;
				if (GraphPathParameters.graph.edgeHash.containsKey(node))
					c += GraphPathParameters.graph.edgeHash.get(node).size();
				if (GraphPathParameters.graph.reverseEdgeHash.containsKey(node))
					c += GraphPathParameters.graph.reverseEdgeHash.get(node).size();
				if (c < nodesources.get(1)) { continue; }
			}
			
			HashMap<Integer,String> supmatch = new HashMap<>();
			supmatch.put(1, node);
			HashMap<Integer, String> supmatchref = new HashMap<>();
			supmatchref = MatchSubgraph.matchSubgraph_und(motifs,supmatch);
			if(supmatchref!=null){
				totalsupport++;
				hitNodes.add(node);
			}
		}
		
		//Calculate significance
		double prob = AlgorithmUtility.getProbability(prevhits.size(),checkedgroupnodes, totalsupport, hitSupport);
		
		if(GraphPathParameters.verbose==1) System.out.println(motifString+"\t"+hitSupport+"\t"+totalsupport+"\t"+prob);
		
		MiningState.sigmotifs.put(motifString, prob);
		MiningState.freqmotifs.put(motifString, totalsupport);
		
		Iterator<Integer> it3 = motiflabels.keySet().iterator();
		while (it3.hasNext()){
			List<DFSedge> potentialedges = new ArrayList<>();
			int sourceId = it3.next();
			
			//Try connecting each node in motif to each other node
			Iterator<Integer> it4 = motiflabels.keySet().iterator();
			LABEL2: while (it4.hasNext()){
				Integer targetId = it4.next();

				if (sourceId >= targetId) { continue LABEL2; }
				
				DFSedge edge = new DFSedge(sourceId, motiflabels.get(sourceId), targetId, motiflabels.get(targetId));
				if (AlgorithmUtility.checkContainsEdge(motifs, edge)) 
					continue LABEL2;
				else 
					potentialedges.add(edge);
			}
			
			//Try adding a new edge with every possible label
			for (int i = 0; i<GraphPathParameters.graph.possibleLabels.size(); i++){
				DFSedge forwardedge = new DFSedge(sourceId, motiflabels.get(sourceId), maxId+1, GraphPathParameters.graph.possibleLabels.get(i));
				potentialedges.add(forwardedge);
			}
			
			TARGETLOOP: for(int i=0; i<potentialedges.size(); i++){
				DFScode<DFSedge> newmotif = new DFScode<>();
				newmotif.addAll(motifs);
				newmotif.add(potentialedges.get(i));
				if (newmotif.maxVertexIndexNaive() > GraphPathParameters.maxsize)
					continue TARGETLOOP;
				String newmotifString = AlgorithmUtility.getStringMotifs(newmotif);
				DFScode<DFSedge> optnewmotif = new DFScode<>();
				if(MiningState.motiftransformations.containsKey(newmotifString)){
					optnewmotif = newmotif;
				}else{
					optnewmotif = OptimizeMotif.optimizeMotif_und(newmotif);
					MiningState.motiftransformations.put(newmotifString, AlgorithmUtility.getStringMotifs(optnewmotif));
					
//					if (GraphPathParameters.verbose==1) System.out.println("Transformed "+AlgorithmUtility.getStringMotifs(newmotif)+" into: "+AlgorithmUtility.getStringMotifs(optnewmotif));
					newmotifString = AlgorithmUtility.getStringMotifs(optnewmotif);
				}
				if (MiningState.checkedmotifs.containsKey(newmotifString)){
					continue TARGETLOOP;
				}else{
					build_motif_und(optnewmotif, hitNodes);
					biggerfim++;
				}
			}
		}
		return biggerfim;
	}
}
