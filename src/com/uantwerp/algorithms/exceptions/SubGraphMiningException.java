package com.uantwerp.algorithms.exceptions;

public abstract class SubGraphMiningException {
	
	public static void exceptionEmptyFile(){
		try {
			throw new Exception("The graph file is empty, try again");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionNoFile(String nameFile){
		try {
			throw new Exception("Please provide a "+nameFile+" file where each node has exactly one label in 'single-label-mode'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionNoVertexInLabels(String node){
		try {
			System.out.println(node + " not in graph, excluded from interesting vertices");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionOldId(String newId){
		try {
			throw new Exception("Old id is 1 to be transformed into " + newId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionNoMatchSourceTarget(String edge){
		try {
			System.out.println("Undefined targetid and sourceid in motif "+edge+". Continuing anyway");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionDirectedGraphInUndirectedAlgorithm(){
		try {
			throw new Exception("You are trying to run an Algorithm which only works with undirected graph in a directed mode, recalculate in another mode");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionDFSCodesDifferenSizes(){
		try {
			throw new Exception("When determining the minimum DFS code the two motif lists have a different size");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
