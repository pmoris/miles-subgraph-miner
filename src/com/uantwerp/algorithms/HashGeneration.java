package com.uantwerp.algorithms;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Iterator;

import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.HashFuctions;

public abstract class HashGeneration {
	
	public static void graphGeneration(){		
		readGraph(GraphParameters.pathGraph, 1);
		readGraph(GraphParameters.pathLabels, 2);
		readGraph(GraphParameters.pathInterestFile, 3);
		readGraph(GraphParameters.pathBgNodes, 4);
		GraphParameters.graph.possibleLabels = AlgorithmUtility.getPossibleLables();
	}

	public static void readGraph(String file, int type){
		if (file == null){
			file="";
		}
		BufferedReader reader = new BufferedReader(new StringReader(file));
		if (type==1)
			AlgorithmUtility.checkInputContentNotEmpty(reader.toString());
		String[] lines = file.split("\r\n|\r|\n");
		for (String line : lines) {
			generateHashTable(type, line);
		}
	}
	
	public static void generateHashTable(int type, String node){
		if (node != null){
			String[] nodeArray = new String[2];
			if (node != null){
				nodeArray = node.split("\\s+");
			}
			if (type==1){
				GraphParameters.graph.edgeHash = HashFuctions.updateHashHashSet(GraphParameters.graph.edgeHash,nodeArray[0],nodeArray[1]);
				GraphParameters.graph.reverseEdgeHash = HashFuctions.updateHashHashSet(GraphParameters.graph.reverseEdgeHash,nodeArray[1],nodeArray[0]);
				if (GraphParameters.singleLabel == 0){
					GraphParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphParameters.graph.vertex,nodeArray[0]," ");
					GraphParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphParameters.graph.vertex,nodeArray[1]," ");
					if (!GraphParameters.graph.labelHash.containsKey(" "))
						GraphParameters.graph.labelHash.put(" ", 1);
					else
						GraphParameters.graph.labelHash.put(" ", GraphParameters.graph.labelHash.get(" ")+1);
				}
			}else if (type==2){
				if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathLabels)){
					GraphParameters.graph.reverseVertex = HashFuctions.updateHashHashSet(GraphParameters.graph.reverseVertex,nodeArray[1],nodeArray[0]);
					GraphParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphParameters.graph.vertex,nodeArray[0],nodeArray[1]);
					GraphParameters.graph.vertexOneLabel.put(nodeArray[0], nodeArray[1]);
					GraphParameters.graph.reverseVertexOneLabel.put(nodeArray[1], nodeArray[0]);
					if (!GraphParameters.graph.labelHash.containsKey(nodeArray[1]))
						GraphParameters.graph.labelHash.put(nodeArray[1], 1);
					else
						GraphParameters.graph.labelHash.put(nodeArray[1], GraphParameters.graph.labelHash.get(nodeArray[1])+1);
				}else{
					if (GraphParameters.singleLabel == 1) 
						SubGraphMiningException.exceptionNoFileSingleLabel();
				}
			}else if (type==3){
				if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathInterestFile)){
					if (GraphParameters.graph.vertex.containsKey(nodeArray[0])){
						GraphParameters.graph.group.add(nodeArray[0]);
					}else
						SubGraphMiningException.exceptionVertexNotFound(nodeArray[0], "interesting");
				}else{
					GraphParameters.graph.group = HashFuctions.returnKeySetHash(GraphParameters.graph.vertex);
					Iterator<String> it = GraphParameters.graph.vertex.keySet().iterator();
					while (it.hasNext())
						GraphParameters.graph.bgnodes.add(it.next());
				}
			}else if (type==4){
				if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathInterestFile)){
					if (GraphParameters.pathBgNodes != null){
						if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathBgNodes)) {
							if(GraphParameters.graph.vertex.containsKey(nodeArray[0])) {
								GraphParameters.graph.bgnodes.add(nodeArray[0]);
							}else
								SubGraphMiningException.exceptionVertexNotFound(nodeArray[0], "background");
						}
					}else{
						Iterator<String> it = GraphParameters.graph.vertex.keySet().iterator();
						while (it.hasNext())
							GraphParameters.graph.bgnodes.add(it.next());
					}
				}
			}
		}
	}
	
	
}
