/**
 * 
 */
package com.uantwerp.algorithms.procedures.base;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;

/**
 * @author gerardo
 *
 */
public 	abstract class MatchSubgraph { 
	
	/**
	 * Check if a given node set (id/position and name) can be extended to completely match
	 * the labels of a given subgraph motif.
	 * 
	 * The function will try to match each edge in the motif sequentially.
	 * Initially, the node set will consist of just a single node (node position -> node name), and if
	 * it can be extended so that it matches the motif edge, the function is then called recursively by
	 * always extending the current matches with new target nodes, 
	 * until eventually all nodes in the subgraph motif edge are covered.
	 * this is then repeated????? FOR EDGES???
	 * NOTE: the calling function already checks if the starting node's label matches that of the
	 * subgraph motif's first DFSEdge's source node. So technically this function checks if a given node
	 * (set) can be grown into a bigger set matching a full subgraph motif.
	 * 
	 * @param 	motifRef	DFSCode - the motif which consists of DFSEdges e.g. [1VAL-1VAL, 1VAL-1SER]
	 * @param 	match		A HashMap of a node name / key value that has to be evaluated to see if it matches.
	 * 						It maps a node id (i.e. position inside a motif) to node names e.g. {1=2V3Z0}.
	 * 						The node id will always be 1 initially (i.e. when called from BuildMotif), 
	 *	 					since it is the starting node for the potential match to the motif.
	 * @return	HashMap		a grown and validated match, a HashMap mapping node ids to names
	 * 						e.g. {1=2V3Z0, 2=2V3Z353}
	 * 						null if no match is found	
	 */
	public static HashMap<Integer,String> matchSubgraph(DFScode<DFSedge> motifRef, HashMap<Integer,String> match){
		
		EdgesLoop edgesLoop = AlgorithmUtility.generateEdges(motifRef);

		// loop through the edges of the motif
		for (int i=0; i < motifRef.size(); i++){
			DFSedge ep = motifRef.get(i);
			
			// skip if the matching node (set) does not contain the same id as the source
			// nor as the target of the current edge of the motif
			if (!match.containsKey(ep.getSourceId()) && !match.containsKey(ep.getTargetId())){
				SubGraphMiningException.exceptionNoMatchSourceTarget(ep.getSourceId()+ep.getSourceLabel()+"-"+ep.getTargetId()+ep.getTargetLabel());
				continue;
			}
			
			// if the matching node (set) does NOT contain the same id as the target
			// of the current edge of the motif
			// (e.g. the matching node set only consists of the source of the current edge of the motif
			// and it's not a self-loop)
			// match = {1=2V3Z0}; ep = 1VAL-2SER
			if(!match.containsKey(ep.getTargetId())){
				int matchFound = 0;

				// get the node name of the node in the matching node (set)
				// that has the same node id as the source node of 
				// the current edge (ep) of the motif and check if it has any targets in the graph
				// i.e. check if the node being matched to the edge source has targets in the graph
				if (GraphParameters.graph.edgeHash.get(match.get(ep.getSourceId()))!=null) {

					// loop through the edges (target node names) of this matching node
					Iterator<String> it = GraphParameters.graph.edgeHash.get(match.get(ep.getSourceId())).iterator();
					GRAPHTARGET: while(it.hasNext()){
						String graphtarget = it.next();
						
						// check if the target of the matching node has a label matching
						// that of the target in the current edge of the motif
						// if not, move to next iteration in loop
						if (AlgorithmUtility.checkTargetLabelAbsent(graphtarget,ep.getTargetLabel())){continue GRAPHTARGET; }
						
						// if the labels match, retrieve the node name(s) of the current proposed match
						// (NOT of the target node whose labels were just matched!)
						List<String> matchedIds = AlgorithmUtility.getValuesFromHash(match);
						
						// if the matchedIds already contains the newly matched target node, skip it
						if (matchedIds.contains(graphtarget)){	continue GRAPHTARGET; }
						
						// 
						if (AlgorithmUtility.checkLoops(ep.getTargetId(),edgesLoop.getFowloopedge(),edgesLoop.getBackloopedge(),match,graphtarget)){ continue GRAPHTARGET; }
						
						// if it is used later as a source node it needs to have at least some outgoing edges
						if (edgesLoop.getEdgecountbynode().containsKey(ep.getTargetId())){
							if (GraphParameters.graph.edgeHash.containsKey(graphtarget)){
								if (GraphParameters.graph.edgeHash.get(graphtarget).size() < edgesLoop.getEdgecountbynode().get(ep.getTargetId())){ continue GRAPHTARGET; }
							}else{ continue GRAPHTARGET; }
						}
						
						if (GraphParameters.graph.reverseEdgeHash.containsKey(graphtarget)){
							if (GraphParameters.graph.reverseEdgeHash.get(graphtarget).size()<edgesLoop.getEdgecountbytarget().get(ep.getTargetId())){ continue GRAPHTARGET; }
						}else { continue GRAPHTARGET; }
						
						// if all above checks were passed, add the new target node to the matching node set
						// with the same node id as the current edge's target id
						HashMap<Integer,String> trymatch = SerializationUtils.clone(match);
						trymatch.put(ep.getTargetId(), graphtarget);
						
						// extend the matching node set recursively
						// if it eventually leads to a fully grown and matching node set,
						// the status and the match will be stored.
						// NOTE: the function will still need to iterate over the other edges in the motif
						// in order to assess whether the entire motif matches
						HashMap<Integer,String> returnmatchref = matchSubgraph(motifRef,trymatch);
						if (returnmatchref != null){
							matchFound = 1;
							match = returnmatchref;
							break GRAPHTARGET;
						}
					}
				}
				
				// return null if no match is found
				if (matchFound==0){
					return null;
				}
			}

			// if the matching node (set) DOES contain a node with the same id as
			// the current edge's target id (see previous if statement) 
			// AND the matching set DOES NOT contain a node with the same id as
			// the current edge's source id
			// i.e. check if the node being matched to the edge target has targets in the graph
			// i.e. used for "descending" edges
			else if(!match.containsKey(ep.getSourceId())){
				int matchFound = 0;

				if (GraphParameters.graph.reverseEdgeHash.get(match.get(ep.getTargetId()))!=null) { 
					Iterator<String> it = GraphParameters.graph.reverseEdgeHash.get(match.get(ep.getTargetId())).iterator();
					GRAPHTARGET: while(it.hasNext()){
						String graphtarget = it.next();
						
						if (AlgorithmUtility.checkTargetLabelAbsent(graphtarget,ep.getSourceLabel())){ continue GRAPHTARGET; }
						List<String> matchedIds = AlgorithmUtility.getValuesFromHash(match);
						if (matchedIds.contains(graphtarget)){ continue GRAPHTARGET; }
						
						if (AlgorithmUtility.checkLoops(ep.getSourceId(),edgesLoop.getFowloopedge(),edgesLoop.getBackloopedge(),match,graphtarget)){ continue GRAPHTARGET; }
						
						//If it is used later as a source node it needs to have at least some outgoing edges					
						if (GraphParameters.graph.edgeHash.containsKey(graphtarget)){
							if (GraphParameters.graph.edgeHash.get(graphtarget).size() < edgesLoop.getEdgecountbynode().get(ep.getSourceId())){ continue GRAPHTARGET; }
						}else{ continue GRAPHTARGET; }
						
						//If it is used later as a target node it needs to have at least some incoming edges
						if (edgesLoop.getEdgecountbytarget().containsKey(ep.getSourceId())){
							if (GraphParameters.graph.reverseEdgeHash.containsKey(graphtarget)){
								if (GraphParameters.graph.reverseEdgeHash.get(graphtarget).size() < edgesLoop.getEdgecountbytarget().get(ep.getSourceId())){ continue GRAPHTARGET; }
							}else { continue GRAPHTARGET; }
						}
						
						HashMap<Integer,String> trymatch = SerializationUtils.clone(match);
						trymatch.put(ep.getSourceId(), graphtarget);
						
						HashMap<Integer,String> returnmatchref = matchSubgraph(motifRef,trymatch);
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
			}			
			// (see previous if statements)
			// the matching node contains nodes that match the source AND target id of the current edge
			// e.g. the function is recursing after having matched a full edge of the motif
			// i.e. match = {1=2V3Z0, 2=2V3Z353}; ep = 1VAL-2SER)
			// at this point, the match can be returned and the loop can continue to the next edge
			// OR default exit path when current match cannot be extended
			else{
				// check if match to edge source node has targets/outgoing edges
				if (GraphParameters.graph.edgeHash.containsKey(match.get(ep.getSourceId()))){
					// check if match to edge source node targets contain the match to edge target node
					if (!GraphParameters.graph.edgeHash.get(match.get(ep.getSourceId())).contains(match.get(ep.getTargetId()))){
						return null;
					}
				}else{
					return null;
				}
			}
			
		}
		return match;
	}
	
	public static HashMap<Integer,String> matchSubgraph_und(DFScode<DFSedge> motifRef, HashMap<Integer,String> match){

		EdgesLoop edgesLoop = AlgorithmUtility.generateEdges(motifRef);
		
		for (int i=0; i < motifRef.size(); i++){
			DFSedge ep = motifRef.get(i);
			
			// Because of the way that the motifs are constructed and sorted, either the source or target should have a match
			if (!match.containsKey(ep.getSourceId()) && !match.containsKey(ep.getTargetId())){
				SubGraphMiningException.exceptionNoMatchSourceTarget(ep.getSourceId()+ep.getSourceLabel()+"-"+ep.getTargetId()+ep.getTargetLabel());
				continue;
			}
			if(!match.containsKey(ep.getTargetId())){
				int matchFound = 0;
				Iterator<String> it = Collections.emptyIterator();
				if (GraphParameters.graph.edgeHash.containsKey(match.get(ep.getSourceId()))){
					it = GraphParameters.graph.edgeHash.get(match.get(ep.getSourceId())).iterator();
				}
				Iterator<String> it2 = Collections.emptyIterator();
				if (GraphParameters.graph.reverseEdgeHash.containsKey(match.get(ep.getSourceId()))){
					it2 = GraphParameters.graph.reverseEdgeHash.get(match.get(ep.getSourceId())).iterator();
				}
				GRAPHTARGET: while(it.hasNext() || it2.hasNext()){
					String graphtarget = "";
					if (it.hasNext()){
						graphtarget = it.next();
					}else if (it2.hasNext()){
						graphtarget = it2.next();
					}
					
					if (AlgorithmUtility.checkTargetLabelAbsent(graphtarget,ep.getTargetLabel())){continue GRAPHTARGET; }

					List<String> machedIds = AlgorithmUtility.getValuesFromHash(match);
					if (machedIds.contains(graphtarget)){continue GRAPHTARGET; }
					
					if (AlgorithmUtility.checkLoops(ep.getTargetId(),edgesLoop.getFowloopedge(),edgesLoop.getBackloopedge(),match,graphtarget)){ continue GRAPHTARGET; }
					
					// If it is used later as a source node it needs to have at least some outgoing edges
					if (edgesLoop.getEdgecountbynode().containsKey(ep.getTargetId())){
						int c = 0;
						if (GraphParameters.graph.edgeHash.containsKey(graphtarget))
							c += GraphParameters.graph.edgeHash.get(graphtarget).size();
						if (GraphParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							c += GraphParameters.graph.reverseEdgeHash.get(graphtarget).size();
						if (c < edgesLoop.getEdgecountbynode().get(ep.getTargetId()))
							continue GRAPHTARGET;
					}
					
					HashMap<Integer,String> trymatch = SerializationUtils.clone(match);
					trymatch.put(ep.getTargetId(), graphtarget);
					
					HashMap<Integer,String> returnmatchref = matchSubgraph_und(motifRef,trymatch);
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
				if (GraphParameters.graph.edgeHash.containsKey(match.get(ep.getSourceId()))){
					if (GraphParameters.graph.edgeHash.get(match.get(ep.getSourceId())).contains(match.get(ep.getTargetId()))){
						check = 1;
					}
				}
				if (GraphParameters.graph.reverseEdgeHash.containsKey(match.get(ep.getSourceId()))){
					if (GraphParameters.graph.reverseEdgeHash.get(match.get(ep.getSourceId())).contains(match.get(ep.getTargetId()))){
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
