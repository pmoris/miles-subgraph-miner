/**
 * 
 */
package com.uantwerp.algorithms.procedures.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.rits.cloning.Cloner;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;

/**
 * @author gerardo
 *
 */
public 	abstract class MatchSubgraph {

	private static final Cloner cloner = new Cloner();
	//Check if a node set is a potential match for a given subgraph
	//Is called iteratively by always extending with new nodes until all nodes in the subgraph are covered
	//motifref the motifs which are pairs of nodes
	//match the keyvalues that have to be evaluated
	public static HashMap<Integer,String> matchSubgraph(DFScode<DFSedge> motifref, HashMap<Integer,String> match){
		
		EdgesLoop edgesLoop = AlgorithmUtility.generateEdges(motifref);

		for (int i=0; i < motifref.size(); i++){
			DFSedge ep = motifref.get(i);
			if (!match.containsKey(ep.getSourceId()) && !match.containsKey(ep.getTargetId())){
				SubGraphMiningException.exceptionNoMatchSourceTarget(ep.getSourceId()+ep.getSourceLabel()+"-"+ep.getTargetId()+ep.getTargetLabel());
				continue;
			}
			if(!match.containsKey(ep.getTargetId())){
				int matchFound = 0;
				if (GraphPathParameters.graph.edgeHash.get(match.get(ep.getSourceId()))!=null) {
					Iterator<String> it = GraphPathParameters.graph.edgeHash.get(match.get(ep.getSourceId())).iterator();
					GRAPHTARGET: while(it.hasNext()){
						String graphtarget = it.next();
						
						if (AlgorithmUtility.checkTargetLabel(graphtarget,ep.getTargetLabel())){continue GRAPHTARGET; }
						List<String> machedIds = AlgorithmUtility.getValuesFromHash(match);
						if (machedIds.contains(graphtarget)){	continue GRAPHTARGET; }
						
						if (AlgorithmUtility.checkLoops(ep.getTargetId(),edgesLoop.getFowloopedge(),edgesLoop.getBackloopedge(),match,graphtarget)){ continue GRAPHTARGET; }
						
						//If it is used later as a source node it needs to have at least some outgoing edges
						if (edgesLoop.getEdgecountbynode().containsKey(ep.getTargetId())){
							if (GraphPathParameters.graph.edgeHash.containsKey(graphtarget)){
								if (GraphPathParameters.graph.edgeHash.get(graphtarget).size() < edgesLoop.getEdgecountbynode().get(ep.getTargetId())){ continue GRAPHTARGET; }
							}else{ continue GRAPHTARGET; }
						}
						
						if (GraphPathParameters.graph.reverseEdgeHash.containsKey(graphtarget)){
							if (GraphPathParameters.graph.reverseEdgeHash.get(graphtarget).size()<edgesLoop.getEdgecountbytarget().get(ep.getTargetId())){ continue GRAPHTARGET; }
						}else { continue GRAPHTARGET; }
						
						HashMap<Integer,String> trymatch = cloner.deepClone(match);
						trymatch.put(ep.getTargetId(), graphtarget);
						
						
						HashMap<Integer,String> returnmatchref = matchSubgraph(motifref,trymatch);
						if (returnmatchref != null){
							matchFound = 1;
							match = returnmatchref;
							break GRAPHTARGET;
						}
					}
				}
				if (matchFound==0){
					return null;
				}
			}else if(!match.containsKey(ep.getSourceId())){
				int matchFound = 0;
				if (GraphPathParameters.graph.reverseEdgeHash.get(match.get(ep.getTargetId()))!=null) { 
					Iterator<String> it = GraphPathParameters.graph.reverseEdgeHash.get(match.get(ep.getTargetId())).iterator();
					GRAPHTARGET: while(it.hasNext()){
						String graphtarget = it.next();
						
						if (AlgorithmUtility.checkTargetLabel(graphtarget,ep.getSourceLabel())){ continue GRAPHTARGET; }
						List<String> machedIds = AlgorithmUtility.getValuesFromHash(match);
						if (machedIds.contains(graphtarget)){ continue GRAPHTARGET; }
						
						if (AlgorithmUtility.checkLoops(ep.getSourceId(),edgesLoop.getFowloopedge(),edgesLoop.getBackloopedge(),match,graphtarget)){ continue GRAPHTARGET; }
						
						//If it is used later as a source node it needs to have at least some outgoing edges					
						if (GraphPathParameters.graph.edgeHash.containsKey(graphtarget)){
							if (GraphPathParameters.graph.edgeHash.get(graphtarget).size() < edgesLoop.getEdgecountbynode().get(ep.getSourceId())){ continue GRAPHTARGET; }
						}else{ continue GRAPHTARGET; }
						
						//If it is used later as a target node it needs to have at least some incoming edges
						if (edgesLoop.getEdgecountbytarget().containsKey(ep.getSourceId())){
							if (GraphPathParameters.graph.reverseEdgeHash.containsKey(graphtarget)){
								if (GraphPathParameters.graph.reverseEdgeHash.get(graphtarget).size() < edgesLoop.getEdgecountbytarget().get(ep.getSourceId())){ continue GRAPHTARGET; }
							}else { continue GRAPHTARGET; }
						}
						
						HashMap<Integer,String> trymatch = cloner.deepClone(match);
						trymatch.put(ep.getSourceId(), graphtarget);
						
						HashMap<Integer,String> returnmatchref = matchSubgraph(motifref,trymatch);
						if (returnmatchref != null){
							matchFound = 1;
							match = returnmatchref;
							break GRAPHTARGET;
						}
					}
				}
				if (matchFound==0){
					return null;
				}
			}else{
				if (GraphPathParameters.graph.edgeHash.containsKey(match.get(ep.getSourceId()))){
					if (!GraphPathParameters.graph.edgeHash.get(match.get(ep.getSourceId())).contains(match.get(ep.getTargetId()))){
						return null;
					}
				}else{
					return null;
				}
			}
			
		}
		return match;
	}
	
	public static HashMap<Integer,String> matchSubgraph_und(DFScode<DFSedge> motifref, HashMap<Integer,String> match){
		
		
		EdgesLoop edgesLoop = AlgorithmUtility.generateEdges(motifref);
		
		for (int i=0; i < motifref.size(); i++){
			DFSedge ep = motifref.get(i);
			//Because of the way that the motifs are constructed and sorted, either the source or target should have a match
			if (!match.containsKey(ep.getSourceId()) && !match.containsKey(ep.getTargetId())){
				SubGraphMiningException.exceptionNoMatchSourceTarget(ep.getSourceId()+ep.getSourceLabel()+"-"+ep.getTargetId()+ep.getTargetLabel());
				continue;
			}
			if(!match.containsKey(ep.getTargetId())){
				int matchFound = 0;
				Iterator<String> it = Collections.emptyIterator();
				if (GraphPathParameters.graph.edgeHash.containsKey(match.get(ep.getSourceId()))){
					it = GraphPathParameters.graph.edgeHash.get(match.get(ep.getSourceId())).iterator();
				}
				Iterator<String> it2 = Collections.emptyIterator();
				if (GraphPathParameters.graph.reverseEdgeHash.containsKey(match.get(ep.getSourceId()))){
					it2 = GraphPathParameters.graph.reverseEdgeHash.get(match.get(ep.getSourceId())).iterator();
				}
				GRAPHTARGET: while(it.hasNext() || it2.hasNext()){
					String graphtarget = "";
					if (it.hasNext()){
						graphtarget = it.next();
					}else if (it2.hasNext()){
						graphtarget = it2.next();
					}
					
					if (AlgorithmUtility.checkTargetLabel(graphtarget,ep.getTargetLabel())){continue GRAPHTARGET; }

					List<String> machedIds = AlgorithmUtility.getValuesFromHash(match);
					if (machedIds.contains(graphtarget)){continue GRAPHTARGET; }
					
					if (AlgorithmUtility.checkLoops(ep.getTargetId(),edgesLoop.getFowloopedge(),edgesLoop.getBackloopedge(),match,graphtarget)){ continue GRAPHTARGET; }
					
					//If it is used later as a source node it needs to have at least some outgoing edges
					if (edgesLoop.getEdgecountbynode().containsKey(ep.getTargetId())){
						int c = 0;
						if (GraphPathParameters.graph.edgeHash.containsKey(graphtarget))
							c += GraphPathParameters.graph.edgeHash.get(graphtarget).size();
						if (GraphPathParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							c += GraphPathParameters.graph.reverseEdgeHash.get(graphtarget).size();
						if (c < edgesLoop.getEdgecountbynode().get(ep.getTargetId()))
							continue GRAPHTARGET;
					}
					
					HashMap<Integer,String> trymatch = cloner.deepClone(match);
					trymatch.put(ep.getTargetId(), graphtarget);
					
					HashMap<Integer,String> returnmatchref = matchSubgraph_und(motifref,trymatch);
					if (returnmatchref != null){
						matchFound = 1;
						match = returnmatchref;
						break GRAPHTARGET;
					}
				}
				//If no matches are found, this match set is invalid and should return false
				if (matchFound==0){
					return null;
				}
			}else{
				int check = 0; 
				if (GraphPathParameters.graph.edgeHash.containsKey(match.get(ep.getSourceId()))){
					if (GraphPathParameters.graph.edgeHash.get(match.get(ep.getSourceId())).contains(match.get(ep.getTargetId()))){
						check = 1;
					}
				}
				if (GraphPathParameters.graph.reverseEdgeHash.containsKey(match.get(ep.getSourceId()))){
					if (GraphPathParameters.graph.reverseEdgeHash.get(match.get(ep.getSourceId())).contains(match.get(ep.getTargetId()))){
						check = 1;
					}
				}
				if (check == 0){
					return null;
				}
			}
			
		}
		return match;
	}
}
