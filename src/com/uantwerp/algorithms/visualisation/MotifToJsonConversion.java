package com.uantwerp.algorithms.visualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gson.Gson;
import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphParameters;

public class MotifToJsonConversion {
	
	public static String convertAllMotifs() {
		
		// Gson object to store all JSON entries
		Gson gson = new Gson();
		
		// List to store all motif data
		ArrayList<JsonGraphElement> motifsData = new ArrayList<JsonGraphElement>();
		
//		// create DFScode objects for gspan algorithm (only necessary for visualisation)
//		if (GraphParameters.typeAlgorithm.equals("gspan")) {
//			Iterator<String> it = MiningState.supportedMotifsPValues.keySet().iterator();
//			while (it.hasNext()) {
//				String stringKey = it.next();
//				DFScode<DFSedge> motif = new DFScode<>();
//				// create motif
//				for (String edge : stringKey.split(",")) {
//					String[] edgeArray = edge.split(edge, '-');
//				}
//				motif.add(new DFSEdge)
//				motif.add(new DFSedge(1,GraphParameters.graph.possibleLabels.get(i),2,GraphParameters.graph.possibleLabels.get(j),true));
//				// add motif to hash map
//				MiningState.supportedMotifsDFScode.put(stringKey, motif);
//
//			}
//		}
//		
		// Loop through all stored motifs
		Iterator<String> it = MiningState.supportedMotifsAdjustedPValues.keySet().iterator();
		while (it.hasNext()) {
			String stringKey = it.next();
			
			Double adjustedPValue = MiningState.supportedMotifsAdjustedPValues.get(stringKey);
			
			// if all-p-values option is used, convert all subgraphs,
			// otherwise only those meeting the adjusted p-value threshold
			if (GraphParameters.allPValues == 0) {
				if ( adjustedPValue > GraphParameters.pvalue ) {
					continue;
				}
			}
			
			// Retrieve JSON data for current motif in list format
			HashMap<String, ArrayList<JsonGraphElement>> nodesEdgesMap = convertMotif(stringKey, Math.log(adjustedPValue) 
					/ Math.log(10));
			
			// append data to motifsList
			motifsData.addAll(nodesEdgesMap.get("nodes"));
			motifsData.addAll(nodesEdgesMap.get("edges"));
		}
		
		// convert motifs data to a gson object
		return gson.toJson(motifsData);
	}

	private static HashMap<String, ArrayList<JsonGraphElement>> convertMotif(String motifString, Double pValue) {

		// retrieve motif
		DFScode<DFSedge> motif = MiningState.supportedMotifsDFScode.get(motifString);		
		
		// keep track of encountered node ids in motif
		HashSet<Integer> encounteredNodeSet = new HashSet<Integer>();
		
		// store all JSON nodes
		ArrayList<JsonGraphElement> nodesList = new ArrayList<JsonGraphElement>();
		// store all JSON edges
		ArrayList<JsonGraphElement> edgesList = new ArrayList<JsonGraphElement>();
		
		// loop through motif edges and add nodes/edges to lists
		for (int i=0; i< motif.size(); i++) {
			
			// retrieve source and target node ids from current motif edge
			int sourceNode = motif.get(i).getSourceId();
			int targetNode = motif.get(i).getTargetId();
			// retrieve source and target node labels from current motif edge		
			String sourceLabel = motif.get(i).getSourceLabel();
			String targetLabel = motif.get(i).getTargetLabel();
			
			// set node ids, must be unique in the entire JSON file, but shared across edges inside a motif
			String idSource = motifString + "-" + sourceNode;
			String idTarget = motifString + "-" + targetNode;
			
			// check if source node was already encountered
			// if not, create its JSON entry
			if (!encounteredNodeSet.contains(sourceNode)) {
				// add to encountered list
				encounteredNodeSet.add(sourceNode);
				// store indicator signifying whether or not the current node is the source vertex of the motif
				String motifSourceIndicator = (sourceNode == 1) ? "source" : "not-source";
				// create JSON structure				
				JsonDataNode data = new JsonDataNode(idSource, sourceLabel, pValue, motifSourceIndicator);
				JsonGraphElement node = new JsonGraphElement("nodes", data);
				nodesList.add(node);
			}
			// check if target node was already encountered
			// if not, create its JSON entry
			if (!encounteredNodeSet.contains(targetNode)) {
				// add to encountered list
				encounteredNodeSet.add(targetNode);
				// store indicator signifying whether or not the current node is the source vertex of the motif
				String motifSourceIndicator = (targetNode == 1) ? "source" : "not-source";
				// create JSON structure
				JsonDataNode data = new JsonDataNode(idTarget, targetLabel, pValue, motifSourceIndicator);			
				JsonGraphElement node = new JsonGraphElement("nodes", data);
				nodesList.add(node);
			}
			
			// add edge
			String idEdge = motifString + "-" + sourceNode + "-" + targetNode;
			// note that source/target id must be set to a custom id string that is conserved
			// across the motif edge loop AND unique across all motifs
			JsonDataEdge data = new JsonDataEdge(idEdge, idSource, idTarget, pValue);
			JsonGraphElement edge = new JsonGraphElement("edges", data);
			edgesList.add(edge);
		}

		// return all nodes and edges in list format as an array for nodes/edges
		HashMap<String, ArrayList<JsonGraphElement>> nodesEdgesMap = new HashMap<String, ArrayList<JsonGraphElement>>();
		nodesEdgesMap.put("nodes", nodesList);
		nodesEdgesMap.put("edges", edgesList);

		return nodesEdgesMap;
	}
}