package com.uantwerp.algorithms.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.procedures.base.EdgesLoop;
import com.uantwerp.algorithms.procedures.base.OptimizeParameter;

public abstract class AlgorithmUtility {
	
	public static void checkGraphFile(String graph){
		if (GraphPathParameters.graph.equals(""))
			SubGraphMiningException.exceptionEmptyFile();		
	}
	
	public static boolean checkEmptyGraph(String graph){
		if (graph == null)
			return false;
		if (GraphPathParameters.graph.equals(""))
			return false;	
		return true;
	}
	
	public static List<String> getPossibleLables(){
		List<String> possibleLabels = new ArrayList<String>();
		if (GraphPathParameters.singleLabel == 0){
			possibleLabels.add(" ");
		}
		Set<String> keys = GraphPathParameters.graph.reverseVertex.keySet();
	    Iterator<String> itr = keys.iterator();
	    while (itr.hasNext()) { 
	        possibleLabels.add(itr.next());
	     } 
		return possibleLabels;
	}
	
	//Support threshold calculation
	public static int supportTreshold(){
		int supportcutoffResult=0;
		if (GraphPathParameters.supportcutoff == 0){
			//Estimate number of subgraphs to be tested
			Double d = Math.pow(GraphPathParameters.maxsize,2)/2 ;
			double stimate = (Math.pow(     2,(     d.intValue()   )    ) *  Math.pow(GraphPathParameters.graph.possibleLabels.size(),GraphPathParameters.maxsize));
			double corrpval = GraphPathParameters.pvalue/stimate;
			//Estimate corresponding support
			for (int i = 1; i <= GraphPathParameters.graph.group.size();i++){
				double prob = HypergeomDist.getProbability(GraphPathParameters.graph.bgnodes.size() - GraphPathParameters.graph.group.size(), GraphPathParameters.graph.group.size(), i, i);
				if (prob < corrpval){
					supportcutoffResult = i;
					break;
				}
			}
			if (supportcutoffResult>0){
				System.out.println("Subgraph support set at " + supportcutoffResult + " due to upper bound Pvalue");
			}
			else{
				supportcutoffResult = GraphPathParameters.graph.group.size();//interestingVertices;
				System.out.println("Subgraph support set at " + supportcutoffResult + " for number of interesting vertices\n");
			}
		}else{
			return GraphPathParameters.supportcutoff;
		}
		return supportcutoffResult;
	}
	
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
	
	public static double getProbability(int prevhits, int checkedgroupnodes, int totalsupport, int hitSupport){
		double prob = 0.0;
		if (GraphPathParameters.nestedpval==1){
			//Nested P-value behaviour: Enrichment P-value is calculated against parent subgraph matches
			prob = HypergeomDist.getProbability(prevhits-checkedgroupnodes,checkedgroupnodes, totalsupport, hitSupport);

		}else{
			//Normal behaviour: Enrichment p-value is calculated against the entire GraphPathParameters.graph.
			prob = HypergeomDist.getProbability(GraphPathParameters.graph.bgnodes.size()-GraphPathParameters.graph.group.size(),GraphPathParameters.graph.group.size(), totalsupport, hitSupport);
		}
		return prob;
	}
	
	public static EdgesLoop generateEdges(List<DFSedge> motifref){
		HashSet<Integer> seen = new HashSet<>();
		EdgesLoop edges = new EdgesLoop();
		/*for created to check if there is a loop in the graph*/
		for (int i=0; i < motifref.size(); i++){
			DFSedge ep = motifref.get(i);
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
			if (GraphPathParameters.undirected==0)
				edges.getEdgecountbytarget().put(ep.getTargetId(), edges.getEdgecountbytarget().get(ep.getTargetId())==null ? 1 : edges.getEdgecountbytarget().get(ep.getTargetId())+1);
			else
				edges.getEdgecountbynode().put(ep.getTargetId(), edges.getEdgecountbynode().get(ep.getTargetId())==null ? 1 : edges.getEdgecountbynode().get(ep.getTargetId())+1);
		}
		return edges;
	}
	
	public static boolean checkTargetLabel(String graphtarget, String targetLabel){
		if (GraphPathParameters.singleLabel == 1){
			if (!GraphPathParameters.graph.vertex.containsKey(graphtarget)){
				return true;
			}
		}
		HashSet<String> labelsTargetGraph = GraphPathParameters.graph.vertex.get(graphtarget);
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
					if (GraphPathParameters.undirected == 1){
						boolean cond1 = false;
						boolean cond2 = false;
						if (GraphPathParameters.graph.edgeHash.containsKey(graphtarget))
							if (match.containsKey(fowlooptarget))
								cond1 = !GraphPathParameters.graph.edgeHash.get(graphtarget).contains(match.get(fowlooptarget));
						if (GraphPathParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							if (match.containsKey(fowlooptarget))
								cond2 = !GraphPathParameters.graph.reverseEdgeHash.get(graphtarget).contains(match.get(fowlooptarget));
						if (cond1 && cond2)
							return true;
					}else{
						if (!GraphPathParameters.graph.edgeHash.containsKey(graphtarget))
							return true;
						if (!GraphPathParameters.graph.edgeHash.get(graphtarget).contains(match.get(fowlooptarget)))
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
					if (GraphPathParameters.undirected == 1){
						boolean cond1 = false;
						boolean cond2 = false;
						if (GraphPathParameters.graph.edgeHash.containsKey(graphtarget))
							if (match.containsKey(backlooptarget))
								cond1 = !GraphPathParameters.graph.edgeHash.get(graphtarget).contains(match.get(backlooptarget));
						if (GraphPathParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							if (match.containsKey(backlooptarget))
								cond2 = !GraphPathParameters.graph.reverseEdgeHash.get(graphtarget).contains(match.get(backlooptarget));
						if (cond1 && cond2)
							return true;
					}else{
						if (!GraphPathParameters.graph.reverseEdgeHash.containsKey(graphtarget))
							return true;
						if (!GraphPathParameters.graph.reverseEdgeHash.get(graphtarget).contains(match.get(backlooptarget)))
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
