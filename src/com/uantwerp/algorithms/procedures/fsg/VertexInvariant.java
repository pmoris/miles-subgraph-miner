package com.uantwerp.algorithms.procedures.fsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.uantwerp.algorithms.common.Graph;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.utilities.HashFuctions;
import com.uantwerp.algorithms.utilities.MapUtil;

public class VertexInvariant {

	public Integer[][] matrix;
	private String[] labels;
	private String[] identifier;
	private Integer[] degree;

	public void initializeMatrix(int size) {
		matrix = new Integer[size][size];
		labels = new String[size];
		identifier = new String[size];
		degree = new Integer[size];
	}

	public VertexInvariant(Graph graph) {
		if (!graph.edgeHash.containsKey("1") && !graph.reverseEdgeHash.containsKey("1"))
			return;
		initializeMatrix(graph.vertexOneLabel.keySet().size());
		fillZeros();
		identifier[0] = "1";
		labels[0] = graph.vertexOneLabel.get("1");
		int numberRecord = 1;
		for (String node: graph.getSortedNodesGraph(graph, new ArrayList<String>(graph.vertexOneLabel.keySet()))) {
			if (!node.equals("1")) {
				identifier[numberRecord] = node;
				labels[numberRecord] = graph.vertexOneLabel.get(node);
				numberRecord++;
			}
		}
		for (int i = 0; i < identifier.length; i++) {
			List<String> neighbords = graph.getDegreeVertex(identifier[i], graph);
			degree[i] = neighbords.size();
		}
		vertexIvariant(graph);
	}

	private void vertexIvariant(Graph graph) {		
		HashMap<Integer, List<MatrixUnit>> partitions = orderByDeegre();
		List<String> orderedIdentifiers = new ArrayList<>();
		List<String> orderedLabels = new ArrayList<>();
		LinkedHashMap<Integer, Integer> partitionNumberElements =  new LinkedHashMap<>();
		ArrayList<Integer> list = new ArrayList<Integer>(partitions.keySet());
		Collections.sort(list);
		for (int partition : list) {
			List<MatrixUnit> elements = partitions.get(partition);
			partitionNumberElements.put(partition, elements.size());
			Collections.sort(elements);
			for (MatrixUnit order : elements) {
				orderedIdentifiers.add(order.getIdentifier());
				orderedLabels.add(order.getLabel());
			}
		}
		for (int i = 1; i < identifier.length; i++) {
			identifier[i] = orderedIdentifiers.get(i-1);
			labels[i] = orderedLabels.get(i-1);
		}
		fillMatrix(graph,0,identifier.length);
		checkIdenticalLabelsByPartition(graph, partitionNumberElements);
	}
	
	private void fillMatrix(Graph graph, int from, int to){
		for (int i = from; i < to; i++) {
			List<String> neighbords = graph.getAllEdges(identifier[i], graph);
			for (String node : neighbords) {
				int index = getIndexElement(node);
				matrix[i][index] = 1;
				if (GraphPathParameters.undirected == 1) {
					matrix[index][i] = 1;
				}
			}				
		}
	}
	
	private HashMap<Integer, List<MatrixUnit>> orderByDeegre() {
		HashMap<Integer, List<MatrixUnit>> partitions = new HashMap<>();
		for (int i = 1; i < identifier.length; i++) {
			partitions = HashFuctions.updateMatrixUnit(partitions, new MatrixUnit(labels[i], identifier[i]), degree[i]);
		}
		return partitions;
	}
	
	private void checkIdenticalLabelsByPartition(Graph graph, LinkedHashMap<Integer, Integer> partitions){
		int posMatrix = 1;		
		List<String> groupLabels = new ArrayList<>();				
		for (Integer degreeP: partitions.keySet()){		
			int posLabel = 0;
			int numberElements = partitions.get(degreeP);
			LABELS: for (int i= posMatrix; i < posMatrix + numberElements; i++){
				if (i+1 < posMatrix + numberElements)
					if (labels[i].equals(labels[i+1])){
						groupLabels.add(labels[i]);
						continue LABELS;
					}
				if (labels[i].equals(labels[i-1]) && groupLabels.size() > 0)
					groupLabels.add(labels[i]);
				if (groupLabels.size() > 1){
					HashMap<Integer, Integer> distanceToOrigin =  new HashMap<>();//par1 index, par2 distance
					for(int j=0; j < groupLabels.size(); j++){
						distanceToOrigin.put(j + posMatrix + posLabel, getPosFirstElementLabel(j + posMatrix + posLabel));
					}
					Map<Integer, Integer> sorted_map = new HashMap<>();
					sorted_map = MapUtil.sortByValueIntegers(distanceToOrigin);
					String[] labelsNew = new String[groupLabels.size()];
					String[] identifierNew = new String[groupLabels.size()];
					int k = 0;
					for (int keyValue: sorted_map.keySet()){
						labelsNew[k] = labels[keyValue];
						identifierNew[k] = identifier[keyValue];
						k++;
					}
					for (int j = 0; j < groupLabels.size(); j++){
						makeZeroColumn(posLabel + posMatrix + j);
						makeZeroRow(posLabel + posMatrix + j);
						labels[posLabel + posMatrix + j] = labelsNew[j];
						identifier[posLabel + posMatrix + j] = identifierNew[j];
					}
					fillMatrix(graph,0,identifier.length);
				}
				posLabel += groupLabels.size();
				groupLabels = new ArrayList<>();
			}
			posMatrix += numberElements;
		}
	}
	
	private Integer getPosFirstElementLabel(int i){
		int pos = 0;
		if (GraphPathParameters.undirected == 1){
			for (int j=i; j<identifier.length; j++){
				if (matrix[i][j] == 1){
					return j;
				}
			}
		}else{
			for (int j=0; j<identifier.length; j++){
				if (matrix[i][j] == 1){
					return j;
				}
			}
		}
		return pos;
	}
	
	private void makeZeroColumn(int i){
		for (int j = 0; j < identifier.length; j++){
			matrix[j][i] = 0;
		}
	}
	
	private void makeZeroRow(int i){
		for (int j = 0; j < identifier.length; j++){
			matrix[i][j] = 0;
		}
	}

	public String getRepresentation() {
		if (identifier == null)
			return "";
		if (GraphPathParameters.undirected == 1)
			return getUndirectedRepresentation();
		else
			return getDirectedRepresentation();
	}

	private String getUndirectedRepresentation() {
		String representation = "";
		int elements = identifier.length;
		for (int i = 0; i < elements; i++) {
			for (int j = 0; j < elements; j++) {
				if (j <= i) {
					continue;
				} else {
					if (matrix[i][j] == 1) {
						if (representation.equals("")) {
							representation = (i + 1) + labels[i] + "-" + (j + 1) + labels[j];
						} else {
							representation += "," + (i + 1) + labels[i] + "-" + (j + 1) + labels[j];
						}
					}
				}
			}
		}
		return representation;
	}

	private String getDirectedRepresentation() {
		String representation = "";
		for (int i = 0; i < identifier.length; i++){
			for (int j = 0; j < identifier.length; j++){
				if (matrix[i][j] == 1){
					if (representation.equals("")) {
						representation = (i+1) + labels[i] + "-" + (j+1) + labels[j];
					}else {
						representation += "," + (i+1) + labels[i] + "-" + (j+1) + labels[j];
					}
				}
			}
		}
		return representation;
	}

	private Integer getIndexElement(String element) {
		for (int i = 0; i < identifier.length; i++) {
			if (identifier[i].equals(element))
				return i;
		}
		return -1;
	}
	
	private void fillZeros(){
		for (int i = 0; i < identifier.length; i++){
			for (int j = 0; j < identifier.length; j++){
				matrix[i][j] = 0;
			}
		}
	}

}
