package com.uantwerp.algorithms.exceptions;

public abstract class SubGraphMiningException {
	
	public static void exceptionEmptyFile(){
		throw new CustomizedUncheckedException("The graph file is empty, try again");
		//try {
			//throw new Exception("The graph file is empty, try again");
		//} catch (Exception e) {
			//e.printStackTrace();
			
	public static void exceptionNoFileProvided(String nameFile){
		throw new CustomizedUncheckedException("Please provide a " + nameFile + " file.");
	}
	
	public static void exceptionFileNotExists(String filepath){
		throw new CustomizedUncheckedException(filepath + "could not be found.");
//		throw new CustomizedUncheckedException(e + "\n" + filepath + " could not be found.");
//		try {
//			throw new Exception(e);
//		} catch (Exception message) {
//			message.printStackTrace();
//			System.out.println("\nThe file " + filepath + " could not be found.");
//			System.out.println("Use the --help flag to display usage information or omit all parameters to launch in GUI mode.\n");
//			System.exit(1);	
//		}
	}
	
	public static void exceptionNoFileSingleLabel(String message){
		try {
			throw new Exception(message);
		} catch (Exception e) { 
			e.printStackTrace(); 
			System.exit(1);
		} 
	}
	
	public static void exceptionNoVertexInLabels(String node){
		throw new CustomizedUncheckedException(node + " not in graph, excluded from interesting vertices");
		/*
		try {
			System.out.println(node + " not in graph, excluded from interesting vertices");
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
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
			System.out.println("Undefined targetid and sourceid in motif "+edge+". Continuing anyway.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionDirectedGraphInUndirectedAlgorithm(){
		try {
			throw new Exception("You are trying to run an algorithm which only works with undirected graph in a directed mode, recalculate in another mode.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exceptionDFSCodesDifferenSizes(){
		try {
			throw new Exception("When determining the minimum DFS code the two motif lists have a different size.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
