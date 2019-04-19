package com.uantwerp.algorithms.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.procedures.base.EdgesLoop;
import com.uantwerp.algorithms.procedures.base.OptimizeParameter;

public abstract class AlgorithmUtility {
	
	/**
	 *
	 * @param fileContent	A String representation of an input file's contents
	 * @return	False when the file content is empty or the file is not supplied (null), true otherwise
	 */
	public static boolean checkNotEmptyFileContent(String fileContent){
		if (fileContent == null)	// in case no file was supplied
			return false;
		if (fileContent.equals(""))	// in case an empty file was supplied
			return false;
		return true;
	}
	
	/**
	 * 
	 * @return	all possible labels, including an empty label for graph structure (unless single label mode is used) 
	 */
	public static List<String> getPossibleLables(){
		List<String> possibleLabels = new ArrayList<String>();
		if (GraphParameters.singleLabel == 0){
			possibleLabels.add(" ");
		}
		Set<String> keys = GraphParameters.graph.reverseVertex.keySet();
	    Iterator<String> itr = keys.iterator();
	    while (itr.hasNext()) { 
	        possibleLabels.add(itr.next());
	     } 
		return possibleLabels;
	}
	
	/**
	 * 
	 * @return	support threshold calculation
	 */
	public static int supportTreshold() {
		int supportcutoffResult = 0;
		if (GraphParameters.supportcutoff == 0) {	// default value if user did not supply a cutoff value

			//Estimate number of subgraphs to be tested
			Double maxEdges;
			if (GraphParameters.undirected == 1) {
				maxEdges = ((double)GraphParameters.maxsize * ((double)GraphParameters.maxsize - 1.0)) / 2.0;
			} else {
				maxEdges = (double)GraphParameters.maxsize * ((double)GraphParameters.maxsize - 1.0);
			}
			double estimate = (Math.pow(2, maxEdges)
					* Math.pow(GraphParameters.graph.possibleLabels.size(), GraphParameters.maxsize));
			double corrpval = (double)GraphParameters.pvalue / estimate;

			//Estimate corresponding support
			for (int i = 1; i <= GraphParameters.graph.group.size(); i++) {
				double prob = HypergeomDist.getProbability(
						GraphParameters.graph.bgnodes.size() - GraphParameters.graph.group.size(),
						GraphParameters.graph.group.size(), i, i);
				if (prob < corrpval) {
					supportcutoffResult = i;
					break;
				}
			}
			if (supportcutoffResult > 0) {
				System.out.println("Subgraph support set at " + supportcutoffResult + " due to upper-bound P-value");
			} else {
				supportcutoffResult = GraphParameters.graph.group.size();//interestingVertices;
				System.out.println(
						"Subgraph support set at " + supportcutoffResult + " for number of interesting vertices\n");
			}
		}
		// if the user supplied a support cutoff, keep it
		else {
			return GraphParameters.supportcutoff;
		}
		return supportcutoffResult;
	}
	
	/**
	 * 
	 * @param hash	A HashMap of node ids mapping to node names e.g. {1=2V3Z0}
	 * @return		A list of all the node names.
	 */
	public static List<String> getValuesFromHash(HashMap<Integer,String> hash){
		List<String> list = new ArrayList<>();
		Iterator<Integer> it = hash.keySet().iterator();
		while(it.hasNext()){
			Integer keyValue = it.next();
			if (!list.contains(hash.get(keyValue)))
				list.add(hash.get(keyValue));
		}
		return list;
	}
	
	public static String getStringMotifs(List<DFSedge> motifs){
		String totalConstruc ="";
		for (int i=0; i<motifs.size(); i++){
			if (totalConstruc.equals("")){
				totalConstruc = motifs.get(i).getSourceId()+motifs.get(i).getSourceLabel()+"-"+motifs.get(i).getTargetId()+motifs.get(i).getTargetLabel();
			}else{
				totalConstruc = totalConstruc+","+motifs.get(i).getSourceId()+motifs.get(i).getSourceLabel()+"-"+motifs.get(i).getTargetId()+motifs.get(i).getTargetLabel();
			}
		}
		return totalConstruc;
	}
	
	public static boolean checkContainsEdge(List<DFSedge> edges, DFSedge edge){
		boolean check = false;
		for (int i=0; i < edges.size(); i++){
			if(edges.get(i).getSourceId() ==edge.getSourceId()
					&& edges.get(i).getSourceLabel().equals(edge.getSourceLabel())
					&& edges.get(i).getTargetId() == edge.getTargetId()
					&& edges.get(i).getTargetLabel().equals(edge.getTargetLabel()))
				return true;
		}
		return check;
	}
	
	/**
	 * Perform a hypergeometric test where the proportion of motif source vertices in the nodes of interest
	 * is compared to the proportion in the background set.
	 * For the nested p-value configuration, the background and interest set are reduced to the current
	 * number of visited nodes (of interest).
	 * 
	 * @param prevHits
	 * @param checkedGroupNodes
	 * @param totalSupport
	 * @param interestSupport
	 * @return
	 */
	public static double getProbability(int prevHits, int checkedGroupNodes, int totalSupport, int interestSupport){
		double prob = 0.0;
		if (GraphParameters.nestedpval==1){
			// Nested P-value behaviour: Enrichment P-value is calculated against parent subgraph matches
			prob = HypergeomDist.getProbability(prevHits-checkedGroupNodes,checkedGroupNodes, totalSupport, interestSupport);

		}else{
			// Normal behaviour: Enrichment p-value is calculated against the entire GraphPathParameters.graph
			prob = HypergeomDist.getProbability(GraphParameters.graph.bgnodes.size()-GraphParameters.graph.group.size(),GraphParameters.graph.group.size(), totalSupport, interestSupport);
		}
		return prob;
	}
	
	public static EdgesLoop generateEdges(List<DFSedge> motifRef){
		HashSet<Integer> seen = new HashSet<>();
		EdgesLoop edges = new EdgesLoop();
		/*for created to check if there is a loop in the graph*/
		for (int i=0; i < motifRef.size(); i++){
			DFSedge ep = motifRef.get(i);
			if ((seen.contains(ep.getSourceId())) && (seen.contains(ep.getTargetId()))){
				if (ep.getSourceId() > ep.getTargetId()){
					edges.setFowloopedge(HashFuctions.updateHashHashSet(edges.getFowloopedge(), ep.getSourceId(), ep.getTargetId()));
				}else{
					edges.setBackloopedge(HashFuctions.updateHashHashSet(edges.getBackloopedge(), ep.getTargetId(), ep.getSourceId()));
				}
			}
			seen.add(ep.getSourceId());
			seen.add(ep.getTargetId());
			edges.getEdgecountbynode().put(ep.getSourceId(), edges.getEdgecountbynode().get(ep.getSourceId())==null ? 1 : edges.getEdgecountbynode().get(ep.getSourceId())+1);
			if (GraphParameters.undirected==0)
				edges.getEdgecountbytarget().put(ep.getTargetId(), edges.getEdgecountbytarget().get(ep.getTargetId())==null ? 1 : edges.getEdgecountbytarget().get(ep.getTargetId())+1);
			else
				edges.getEdgecountbynode().put(ep.getTargetId(), edges.getEdgecountbynode().get(ep.getTargetId())==null ? 1 : edges.getEdgecountbynode().get(ep.getTargetId())+1);
		}
		return edges;
	}
	
	/**
	 * 
	 * @param graphtarget	name of a node
	 * @param targetLabel	label to be checked
	 * @return	true when the node does not contain the target label, false otherwise
	 */
	public static boolean checkTargetLabelAbsent(String graphtarget, String targetLabel){
		if (GraphParameters.singleLabel == 1){
			if (!GraphParameters.graph.vertex.containsKey(graphtarget)){
				return true;
			}
		}
		HashSet<String> labelsTargetGraph = GraphParameters.graph.vertex.get(graphtarget);
		if (!labelsTargetGraph.contains(targetLabel)){
			return true;
		}
		return false;
	}
	
	public static boolean checkLoops(Integer nodeId, HashMap<Integer, HashSet<Integer>> fowloopedge, HashMap<Integer, HashSet<Integer>> backloopedge, HashMap<Integer,String> match, String graphtarget){
		if (fowloopedge.containsKey(nodeId)){
			Iterator<Integer> it = fowloopedge.get(nodeId).iterator();
			while (it.hasNext()){
				Integer fowlooptarget = it.next();
				if (match.containsKey(fowlooptarget)){
					if (GraphParameters.undirected == 1){
						boolean cond1 = false;
						boolean cond2 = false;
						if (GraphParameters.graph.edgeHash.containsKey(graphtarget))
							if (match.containsKey(fowlooptarget))
								cond1 = !GraphParameters.graph.edgeHash.get(graphtarget).contains(match.get(fowlooptarget));
						if (GraphParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							if (match.containsKey(fowlooptarget))
								cond2 = !GraphParameters.graph.reverseEdgeHash.get(graphtarget).contains(match.get(fowlooptarget));
						if (cond1 && cond2)
							return true;
					}else{
						if (!GraphParameters.graph.edgeHash.containsKey(graphtarget))
							return true;
						if (!GraphParameters.graph.edgeHash.get(graphtarget).contains(match.get(fowlooptarget)))
							return true;
					}
				}
			}
		}
		if (backloopedge.containsKey(nodeId)){
			Iterator<Integer> it = backloopedge.get(nodeId).iterator();
			while (it.hasNext()){
				Integer backlooptarget = it.next();
				if (match.containsKey(backlooptarget)){
					if (GraphParameters.undirected == 1){
						boolean cond1 = false;
						boolean cond2 = false;
						if (GraphParameters.graph.edgeHash.containsKey(graphtarget))
							if (match.containsKey(backlooptarget))
								cond1 = !GraphParameters.graph.edgeHash.get(graphtarget).contains(match.get(backlooptarget));
						if (GraphParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							if (match.containsKey(backlooptarget))
								cond2 = !GraphParameters.graph.reverseEdgeHash.get(graphtarget).contains(match.get(backlooptarget));
						if (cond1 && cond2)
							return true;
					}else{
						if (!GraphParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							return true;
						if (!GraphParameters.graph.reverseEdgeHash.get(graphtarget).contains(match.get(backlooptarget)))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void updateParams(Integer key, String label, double value, int type, Map<Integer,OptimizeParameter> params){//0 lenghttoone, 1 incomingEdges, 2 outgoingEdges
		OptimizeParameter op =new OptimizeParameter(label);
		if (params.containsKey(key))
			op = params.get(key);
		else
			op =new OptimizeParameter(label);
		if (type==0){
			op.setLenghtToOne(value);
		}else if (type == 1){
			op.setIncomingEdges(op.getIncomingEdges()+1);
		}else{
			op.setOutgoingEdges(op.getOutgoingEdges()+1);
		}
		params.put(key, op);
	}
	
	

}
