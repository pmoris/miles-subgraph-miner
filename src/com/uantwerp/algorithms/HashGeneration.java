package com.uantwerp.algorithms;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Iterator;

import com.uantwerp.algorithms.common.GraphParameters;
import com.uantwerp.algorithms.exceptions.SubGraphMiningException;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;
import com.uantwerp.algorithms.utilities.HashFuctions;

public abstract class HashGeneration {
	
	/**
	 * Go through contents of input files and generate appropriate HashTables
	 * that are then stored inside the GraphParameters.graph attributes
	 */
	public static void graphGeneration(){
		readGraph(GraphParameters.pathGraph, 1);
		readGraph(GraphParameters.pathLabels, 2);
		readGraph(GraphParameters.pathInterestFile, 3);
		readGraph(GraphParameters.pathBgNodes, 4);
		GraphParameters.graph.possibleLabels = AlgorithmUtility.getPossibleLables();
	}

	/**
	 * 
	 * @param file	the content of the input files as a String (see ParameterConfig and FileUtility.readFile)
	 * @param type	the type of input file, e.g. graph, labels, interest and background file
	 */
	public static void readGraph(String file, int type){
		if (file == null){
			file="";
		}
		BufferedReader reader = new BufferedReader(new StringReader(file));
		if (type==1)		// throw exception if graph file is empty
			AlgorithmUtility.checkInputContentNotEmpty(reader.toString());
		String[] lines = file.split("\r\n|\r|\n");		// split on new line characters
		for (String line : lines) {		// generate hash tables by looping over every line
			generateHashTable(type, line);
		}
	}

	/**
	 * @param type	the type of input file, e.g. graph, labels, interest and background file
	 * @param line	a line from the input files, e.g. two nodes representing an edge, a node and its label, or a single node
	 */
	public static void generateHashTable(int type, String line){
		if (line != null){
			String[] nodeArray = new String[2];
			nodeArray = line.split("\\s+");		// split line on tab/space separation characters

			// Graph file: node -> node
			if (type==1){
				// Add forward and backward edges
				GraphParameters.graph.edgeHash = HashFuctions.updateHashHashSet(GraphParameters.graph.edgeHash,
																				nodeArray[0],nodeArray[1]);
				GraphParameters.graph.reverseEdgeHash = HashFuctions.updateHashHashSet(GraphParameters.graph.reverseEdgeHash,
																				nodeArray[1],nodeArray[0]);
				// If not in single label mode, add empty labels to HashSet value of vertex key in HashMap
				if (GraphParameters.singleLabel == 0){
					GraphParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphParameters.graph.vertex,
																				nodeArray[0]," ");
					GraphParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphParameters.graph.vertex,
																				nodeArray[1]," ");
					// Also add labels to HashMap of labels and assign them a value
					if (!GraphParameters.graph.labelHash.containsKey(" "))
						GraphParameters.graph.labelHash.put(" ", 1);
					else
					// the value will constantly be overwritten, so the final value will equal the number of nodes
						GraphParameters.graph.labelHash.put(" ", GraphParameters.graph.labelHash.get(" ")+1);
				}
				
			// Label file: node -> label
			}else if (type==2){
				// the label file is optional...
				if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathLabels)){
					// Labels will be added as a HashSet to a HashMap with vertex keys
					GraphParameters.graph.reverseVertex = HashFuctions.updateHashHashSet(GraphParameters.graph.reverseVertex,
																							nodeArray[1],nodeArray[0]);
					GraphParameters.graph.vertex = HashFuctions.updateHashHashSet(GraphParameters.graph.vertex,
																							nodeArray[0],nodeArray[1]);
					// The OneLabel HashMap will be overwritten with the last label value found for each node
					GraphParameters.graph.vertexOneLabel.put(nodeArray[0], nodeArray[1]);
					GraphParameters.graph.reverseVertexOneLabel.put(nodeArray[1], nodeArray[0]);
					// Labels will also be added to the label HashMap and given a value
					if (!GraphParameters.graph.labelHash.containsKey(nodeArray[1]))
						GraphParameters.graph.labelHash.put(nodeArray[1], 1);
					else
					// This will overwrite the previous value, so the final value for the label key will equal its frequency
						GraphParameters.graph.labelHash.put(nodeArray[1], GraphParameters.graph.labelHash.get(nodeArray[1])+1);
				}else{
				// ...unless the single label option is selected
					if (GraphParameters.singleLabel == 1) 
						SubGraphMiningException.exceptionNoFileSingleLabel();
				}
				
			// Interest file: node
			}else if (type==3){
				if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathInterestFile)){
					// Check if nodes of interest occur in graph and add them to HashSet
					if (GraphParameters.graph.vertex.containsKey(nodeArray[0])){
						GraphParameters.graph.group.add(nodeArray[0]);
					}else
						SubGraphMiningException.exceptionVertexNotFound(nodeArray[0], "interesting");
				}else{
				// If no vertices of interest (or an empty file) are provided, add all vertices to interest group
					GraphParameters.graph.group = HashFuctions.returnKeySetHash(GraphParameters.graph.vertex);
					Iterator<String> it = GraphParameters.graph.vertex.keySet().iterator();
					while (it.hasNext())
						GraphParameters.graph.bgnodes.add(it.next());
				}
				
			// Background file: node
			}else if (type==4){
				// Background nodes are of no use when there is no interest file
				if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathInterestFile)){
					if (GraphParameters.pathBgNodes != null){
						if (AlgorithmUtility.checkEmptyGraph(GraphParameters.pathBgNodes)) {
							// Check if background vertices occur in graph and add them to HashSet
							if(GraphParameters.graph.vertex.containsKey(nodeArray[0])) {
								GraphParameters.graph.bgnodes.add(nodeArray[0]);
							}else
								SubGraphMiningException.exceptionVertexNotFound(nodeArray[0], "background");
						}
					}else{
					// If no background file is provided, add all graph nodes
						Iterator<String> it = GraphParameters.graph.vertex.keySet().iterator();
						while (it.hasNext())
							GraphParameters.graph.bgnodes.add(it.next());
					}
				}
			}
		}
	}
	
}
