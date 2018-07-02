package com.uantwerp.algorithms;

import java.io.BufferedReader;
//import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.HashFuctions;

public abstract class HashGeneration {
	
	public static void graphGeneration(){		
		readGraph(GraphPathParameters.pathGraph, 1);
		readGraph(GraphPathParameters.pathLabels, 2);
		readGraph(GraphPathParameters.pathGroupFile, 3);
		readGraph(GraphPathParameters.pathBgNodes, 4);
		GraphPathParameters.graph.possibleLabels = AlgorithmUtility.getPossibleLables();
	}

	public static void readGraph(String file, int type){
		if (file == null){
			file="";
		}
		BufferedReader reader = new BufferedReader(new StringReader(file));
		if (type==1)
			AlgorithmUtility.checkGraphFile(reader.toString());
		String[] lines = file.split("\r\n|\r|\n");
		for (String line : lines) {
			generateHashTable(type, line);
		}
	}
	
	public static void generateHashTable(int type, String node){
		if (node != null){
			String[] nodeArray = new String[2];
			if (node != null){
				nodeArray = node.split("\t");
			}
			if (type==1){
				GraphPathParameters.graph.edgeHash = HashFuctions.updateHashHashSet(GraphPathParameters.graph.edgeHash,nodeArray[0],nodeArray[1]);
				GraphPathParameters.graph.reverseEdgeHash = HashFuctions.updateHashHashSet(GraphPathParameters.graph.reverseEdgeHash,nodeArray[1],nodeArray[0]);
				if (GraphPathParameters.singleLabel == 0){
					GraphPathParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphPathParameters.graph.vertex,nodeArray[0]," ");
					GraphPathParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphPathParameters.graph.vertex,nodeArray[1]," ");
					if (!GraphPathParameters.graph.labelHash.containsKey(" "))
						GraphPathParameters.graph.labelHash.put(" ", 1);
					else
						GraphPathParameters.graph.labelHash.put(" ", GraphPathParameters.graph.labelHash.get(" ")+1);
				}
			}else if (type==2){
				if (AlgorithmUtility.checkEmptyGraph(GraphPathParameters.pathLabels)){
					GraphPathParameters.graph.reverseVertex = HashFuctions.updateHashHashSet(GraphPathParameters.graph.reverseVertex,nodeArray[1],nodeArray[0]);
					GraphPathParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphPathParameters.graph.vertex,nodeArray[0],nodeArray[1]);
					GraphPathParameters.graph.vertexOneLabel.put(nodeArray[0], nodeArray[1]);
					GraphPathParameters.graph.reverseVertexOneLabel.put(nodeArray[1], nodeArray[0]);
					if (!GraphPathParameters.graph.labelHash.containsKey(nodeArray[1]))
						GraphPathParameters.graph.labelHash.put(nodeArray[1], 1);
					else
						GraphPathParameters.graph.labelHash.put(nodeArray[1], GraphPathParameters.graph.labelHash.get(nodeArray[1])+1);
				}else{
					if (GraphPathParameters.singleLabel == 1)
						SubGraphMiningException.exceptionNoFile("Labels file is needed when the single label parameter is activated");					
				}
			}else if (type==3){
				if (AlgorithmUtility.checkEmptyGraph(GraphPathParameters.pathGroupFile)){
					if (GraphPathParameters.graph.vertex.containsKey(nodeArray[0])){
						GraphPathParameters.graph.group.add(nodeArray[0]);
					}else
						SubGraphMiningException.exceptionNoVertexInLabels(nodeArray[0]);
				}else{
					GraphPathParameters.graph.group = HashFuctions.returnKeySetHash(GraphPathParameters.graph.vertex);
					Iterator<String> it = GraphPathParameters.graph.vertex.keySet().iterator();
					while (it.hasNext())
						GraphPathParameters.graph.bgnodes.add(it.next());
				}
			}else if (type==4){
				if (AlgorithmUtility.checkEmptyGraph(GraphPathParameters.pathGroupFile)){
					if (GraphPathParameters.pathBgNodes != null){
						if (AlgorithmUtility.checkEmptyGraph(GraphPathParameters.pathBgNodes))
							GraphPathParameters.graph.bgnodes.add(nodeArray[0]);
					}else{
						Iterator<String> it = GraphPathParameters.graph.vertex.keySet().iterator();
						while (it.hasNext())
							GraphPathParameters.graph.bgnodes.add(it.next());
					}
				}
			}
		}
	}
	
	
}
