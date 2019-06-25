package com.uantwerp.algorithms.visualisation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.google.gson.Gson;
import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;

public class MotifToJsonConversion {
	
	public static String convertAllMotifs(Double bonferroni) {
		
		// Gson object to store all JSON entries
		Gson gson = new Gson();
		
		// List to store all motif data
		ArrayList<JsonGraphElement> motifsData = new ArrayList<JsonGraphElement>();
		
		// Loop through all stored motifs
		Iterator<String> it = MiningState.supportedMotifsPValues.keySet().iterator();
		while (it.hasNext()) {
			String stringKey = it.next();
			
			Double pValue = MiningState.supportedMotifsPValues.get(stringKey);
			
			// If bonferroni option is used, skip all non-significant motifs
			if (!(bonferroni.isNaN())) {
				if (!(pValue <= bonferroni)) {
					continue;
				}
			}
			
			// Retrieve JSON data for current motif in list format
			HashMap<String, ArrayList<JsonGraphElement>> nodesEdgesMap = convertMotif(stringKey, Math.log(pValue) 
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
				// create JSON structure
				JsonDataNode data = new JsonDataNode(idSource, sourceLabel, pValue);
				JsonGraphElement node = new JsonGraphElement("nodes", data);
				nodesList.add(node);
			}
			// check if target node was already encountered
			// if not, create its JSON entry
			if (!encounteredNodeSet.contains(targetNode)) {
				// add to encountered list
				encounteredNodeSet.add(targetNode);
				// create JSON structure
				JsonDataNode data = new JsonDataNode(idTarget, targetLabel, pValue);			
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