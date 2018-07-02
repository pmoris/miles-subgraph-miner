package com.uantwerp.algorithms.exceptions;

public abstract class SubGraphMiningExceptionGspan {
	
	public static void exceptionDFSCodesDifferenSizes(){
		try {
			throw new Exception("When determining the minimum DFS code the two motif lists have a different size");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
