/**
 * 
 */
package com.uantwerp.algorithms.procedures.base;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.HashFuctions;
import com.uantwerp.algorithms.utilities.MapUtil;

/**
 * @author gerardo
 *
 */
public abstract class OptimizeMotif {

	public static DFScode<DFSedge> optimizeMotif(List<DFSedge> motifs){
		HashMap<Integer,OptimizeParameter> params = new HashMap<Integer, OptimizeParameter>();
		HashMap<Integer,HashSet<Integer>> edgeByNode = new HashMap<>();
		HashMap<Integer,HashSet<Integer>> edgeByTarget = new HashMap<>();
		HashMap<Integer, String> motifLabel = new HashMap<>();
		for (int i=0; i < motifs.size(); i++){
			motifLabel.put(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel());
			motifLabel.put(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel());
			edgeByNode=HashFuctions.updateHashHashSet(edgeByNode, motifs.get(i).getSourceId(), motifs.get(i).getTargetId());
			edgeByTarget=HashFuctions.updateHashHashSet(edgeByTarget, motifs.get(i).getTargetId(),motifs.get(i).getSourceId());
			if (motifs.get(i).getSourceId() == 1){
				if (motifs.get(i).getTargetId() != 1){
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), 1, 0, params);
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), 0, 1, params);
				}
			}else if (motifs.get(i).getTargetId() == 1){
				AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), 0.9, 0, params);
				AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), 0, 2, params);
			}else{
				AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), 0, 1, params);
				AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), 0, 2, params);
				
				if ((params.get(motifs.get(i).getSourceId()).getLenghtToOne()!=0) 
						&& (params.get(motifs.get(i).getTargetId()).getLenghtToOne()!=0)){
					if  (params.get(motifs.get(i).getTargetId()).getLenghtToOne() > params.get(motifs.get(i).getSourceId()).getLenghtToOne()){
						AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), params.get(motifs.get(i).getSourceId()).getLenghtToOne()+1, 0, params);
					}
					if  (params.get(motifs.get(i).getSourceId()).getLenghtToOne() > params.get(motifs.get(i).getTargetId()).getLenghtToOne()){
						AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), params.get(motifs.get(i).getTargetId()).getLenghtToOne()+1, 0, params);
					}
				}else if (params.get(motifs.get(i).getSourceId()).getLenghtToOne()!=0){
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), params.get(motifs.get(i).getSourceId()).getLenghtToOne()+1, 0, params);
				}else if (params.get(motifs.get(i).getTargetId()).getLenghtToOne()!=0){
					AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), params.get(motifs.get(i).getTargetId()).getLenghtToOne()+1, 0, params);
				}else{
					//This should not happen as motifs are constructed away from the root node; but just in case
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), GraphPathParameters.maxsize, 0, params);
					AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), GraphPathParameters.maxsize, 0, params);
				}
			}
		}

		//Sort source nodes based on the distance to the root node, number of outgoing edges and the label
		HashMap<Integer,Integer> transform = new HashMap<Integer, Integer>();
		transform.put(1, 1);
		List<Map.Entry<Integer, OptimizeParameter>> neworder = MapUtil.sortByValue(params);
		for (int i = 0; i < neworder.size(); i++){
			if (neworder.get(i).getKey().equals(1))
				SubGraphMiningException.exceptionOldId(String.valueOf(i+2));
			transform.put(neworder.get(i).getKey(), i+2);
		}
		DFScode<DFSedge> newMotif = new DFScode<>();
		if (edgeByNode.containsKey(1)){
			if (edgeByNode.get(1).contains(1)){
				newMotif.add(new DFSedge(1, motifLabel.get(1), 1, motifLabel.get(1),true));
			}
		}
		
		//Sorted by proximity to the root node and number of edges
		ListIterator<Map.Entry<Integer, OptimizeParameter>> listIterator = neworder.listIterator();
		while (listIterator.hasNext()){
			Map.Entry<Integer, OptimizeParameter> entry = listIterator.next();
			//only save edge if node is source node, or root node is source node
			if (edgeByNode.containsKey(1)){
				if (edgeByNode.get(1).contains(entry.getKey())){
					newMotif.add(new DFSedge(1, motifLabel.get(1), transform.get(entry.getKey()), motifLabel.get(entry.getKey()),true));
				}
			}
			if (edgeByNode.containsKey(entry.getKey()))
				if (edgeByNode.get(entry.getKey()).contains(1)){
					newMotif.add(new DFSedge(transform.get(entry.getKey()), motifLabel.get(entry.getKey()),1, motifLabel.get(1),true));
				}
			if (!edgeByNode.containsKey(entry.getKey()) && !edgeByTarget.containsKey(entry.getKey())){
				continue;
			}
			if (edgeByNode.containsKey(entry.getKey())){
				List<Integer> orderEdgeByNode = MapUtil.sortByValueHashSet(edgeByNode.get(entry.getKey()), transform);
				for (int i = 0; i < orderEdgeByNode.size(); i++)
					if (transform.get(entry.getKey()).compareTo(transform.get(orderEdgeByNode.get(i))) <= 0){
						newMotif.add(new DFSedge(transform.get(entry.getKey()), motifLabel.get(entry.getKey()),transform.get(orderEdgeByNode.get(i)),motifLabel.get(orderEdgeByNode.get(i)),true));
					}
			}
			if (edgeByTarget.containsKey(entry.getKey())){
				List<Integer> orderEdgeByTarget = MapUtil.sortByValueHashSet(edgeByTarget.get(entry.getKey()), transform);
				for (int i = 0; i < orderEdgeByTarget.size(); i++)
					if (transform.get(entry.getKey()).compareTo(transform.get(orderEdgeByTarget.get(i))) < 0){
						newMotif.add(new DFSedge(transform.get(orderEdgeByTarget.get(i)),motifLabel.get(orderEdgeByTarget.get(i)),transform.get(entry.getKey()), motifLabel.get(entry.getKey()),true));
					}
			}
		}
		return newMotif;
	}
	
	public static DFScode<DFSedge> optimizeMotif_und(DFScode<DFSedge> motifs){
		HashMap<Integer,OptimizeParameter> params = new HashMap<Integer, OptimizeParameter>();
		HashMap<Integer,HashSet<Integer>> edgeByNode = new HashMap<>();
		HashMap<Integer,HashSet<Integer>> edgeByTarget = new HashMap<>();
		HashMap<Integer, String> motifLabel = new HashMap<>();
		for (int i=0; i < motifs.size(); i++){
			motifLabel.put(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel());
			motifLabel.put(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel());
			edgeByNode=HashFuctions.updateHashHashSet(edgeByNode, motifs.get(i).getSourceId(), motifs.get(i).getTargetId());
			edgeByTarget=HashFuctions.updateHashHashSet(edgeByTarget, motifs.get(i).getTargetId(),motifs.get(i).getSourceId());
			if (motifs.get(i).getSourceId() == 1){
				if (motifs.get(i).getTargetId() != 1){
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), 1, 0, params);
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), 0, 1, params);
				}
			}else if (motifs.get(i).getTargetId() == 1){
				AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), 1, 0, params);
				AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), 0, 2, params);
			}else{
				AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), 0, 2, params);
				AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), 0, 2, params);
				
				if ((params.get(motifs.get(i).getSourceId()).getLenghtToOne() != 0) 
						&& (params.get(motifs.get(i).getTargetId()).getLenghtToOne() != 0)){
					if  (params.get(motifs.get(i).getTargetId()).getLenghtToOne() > params.get(motifs.get(i).getSourceId()).getLenghtToOne()){
						AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), params.get(motifs.get(i).getSourceId()).getLenghtToOne()+1, 0, params);
					}
					if  (params.get(motifs.get(i).getSourceId()).getLenghtToOne() > params.get(motifs.get(i).getTargetId()).getLenghtToOne()){
						AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), params.get(motifs.get(i).getTargetId()).getLenghtToOne()+1, 0, params);
					}
				}else if (params.get(motifs.get(i).getSourceId()).getLenghtToOne()!=0){
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), params.get(motifs.get(i).getSourceId()).getLenghtToOne()+1, 0, params);
				}else if (params.get(motifs.get(i).getTargetId()).getLenghtToOne()!=0){
					AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), params.get(motifs.get(i).getTargetId()).getLenghtToOne()+1, 0, params);
				}else{
					//This should not happen as motifs are constructed away from the root node; but just in case
					AlgorithmUtility.updateParams(motifs.get(i).getTargetId(), motifs.get(i).getTargetLabel(), GraphPathParameters.maxsize, 0, params);
					AlgorithmUtility.updateParams(motifs.get(i).getSourceId(), motifs.get(i).getSourceLabel(), GraphPathParameters.maxsize, 0, params);
				}
			}
		}

		//Sort source nodes based on the distance to the root node, number of outgoing edges and the label
		HashMap<Integer,Integer> transform = new HashMap<Integer, Integer>();
		transform.put(1, 1);
		List<Map.Entry<Integer, OptimizeParameter>> neworder = MapUtil.sortByValue_und(params);
		for (int i = 0; i < neworder.size(); i++){
			if (neworder.get(i).getKey().equals(1))
				SubGraphMiningException.exceptionOldId(String.valueOf(i + 2));
			transform.put(neworder.get(i).getKey(), i + 2);
		}
		DFScode<DFSedge> newMotif = new DFScode<>();
		if (edgeByNode.containsKey(1)){
			if (edgeByNode.get(1).contains(1)){
				newMotif.add(new DFSedge(1, motifLabel.get(1), 1, motifLabel.get(1),true));
			}
		}
		
		//Sorted by proximity to the root node and number of edges
		ListIterator<Map.Entry<Integer, OptimizeParameter>> listIterator = neworder.listIterator();
		while (listIterator.hasNext()){
			Map.Entry<Integer, OptimizeParameter> entry = listIterator.next();
			//only save edge if node is source node, or root node is source node
			if (edgeByNode.containsKey(1)){
				if (edgeByNode.get(1).contains(entry.getKey()))
					newMotif.add(new DFSedge(1, motifLabel.get(1), transform.get(entry.getKey()), motifLabel.get(entry.getKey()),true));
			}
			if (edgeByNode.containsKey(entry.getKey()))
				if (edgeByNode.get(entry.getKey()).contains(1))
					newMotif.add(new DFSedge(1, motifLabel.get(1),transform.get(entry.getKey()), motifLabel.get(entry.getKey()),true));
			if (!edgeByNode.containsKey(entry.getKey()) && !edgeByTarget.containsKey(entry.getKey()))
				continue;
			if (edgeByNode.containsKey(entry.getKey())){
				List<Integer> orderEdgeByNode = MapUtil.sortByValueHashSet(edgeByNode.get(entry.getKey()), transform);
				for (int i = 0; i < orderEdgeByNode.size(); i++)
					if (transform.get(entry.getKey()).compareTo(transform.get(orderEdgeByNode.get(i))) <= 0)
						newMotif.add(new DFSedge(transform.get(entry.getKey()), motifLabel.get(entry.getKey()),transform.get(orderEdgeByNode.get(i)),motifLabel.get(orderEdgeByNode.get(i)),true));
			}
			if (edgeByTarget.containsKey(entry.getKey())){
				List<Integer> orderEdgeByTarget = MapUtil.sortByValueHashSet(edgeByTarget.get(entry.getKey()), transform);
				for (int i = 0; i < orderEdgeByTarget.size(); i++)
					if (transform.get(entry.getKey()).compareTo(transform.get(orderEdgeByTarget.get(i))) < 0)
						newMotif.add(new DFSedge(transform.get(entry.getKey()), motifLabel.get(entry.getKey()),transform.get(orderEdgeByTarget.get(i)),motifLabel.get(orderEdgeByTarget.get(i)),true));
			}
		}
		return newMotif;
	}
}
