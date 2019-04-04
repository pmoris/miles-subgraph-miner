package com.uantwerp.algorithms.exceptions;

public abstract class SubGraphMiningException {
	
	public static void exceptionEmptyFile(String nameFile){
		throw new CustomizedUncheckedException("The file " + nameFile + " appears to be empty.");
	}
		
	public static void exceptionNoFileProvided(String nameFile){
		throw new CustomizedUncheckedException("Please provide a " + nameFile + " file.");
	}
	
	public static void exceptionFileNotExists(String filepath){
		throw new CustomizedUncheckedException(filepath + " could not be found.");
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
	
	public static void exceptionDirNotExists(String dirpath){
		throw new CustomizedUncheckedException(dirpath + " directory could not be found.");
	}
	
	public static void exceptionNoFileSingleLabel(){
		throw new CustomizedUncheckedException("Label file is needed when the single-label parameter is selected.");
//		try {
//			throw new Exception(message);
//		} catch (Exception e) { 
//			e.printStackTrace(); 
//			System.exit(1);
//		} 
	}
	
	public static void exceptionWrongAlgorithmChoice(String algorithm){
//		try {
//			throw new Exception(algorithm);
//		} catch (Exception e) {
//			e.printStackTrace(); 
//			System.exit(1);
//		}
		throw new CustomizedUncheckedException(algorithm + " is not a valid algorithm choice. Please provide one of the following options instead: base, fsg or gspan.");
	}
	
	public static void exceptionInvalidValue(String argument){
		throw new CustomizedUncheckedException("Please provide a valid value for the option " + argument + ".");
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
