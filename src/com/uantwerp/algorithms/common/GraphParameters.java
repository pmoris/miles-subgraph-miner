package com.uantwerp.algorithms.common;

public abstract class GraphParameters {
	
	private static int DEFAULTSUPPORT = 0;
	private static int DEFAULTMAXSIZE = 5;
	private static double DEFAULTPVALUE = 0.05;
	
	public static String graphFileContents;
	public static String labelsFileContents;
	public static String backgroundFileContents;
	public static String interestFileContents;
	public static int supportcutoff; 
	public static int singleLabel; 
	public static int undirected;
	public static int verbose;
	public static int maxsize;
	public static double pvalue;
	public static int allPValues;
	public static int nestedpval;
	public static String output;
	public static String typeAlgorithm;
	public static String typeResult;
	public static Graph graph = new Graph();
	public static String statistics;

	public static void setDefaultMaxSize(){
		GraphParameters.maxsize = DEFAULTMAXSIZE;
	}
	
	public static void setDefaultSupport(){
		GraphParameters.supportcutoff = DEFAULTSUPPORT;
	}
	
	public static void setDefaultPValue(){
		GraphParameters.pvalue = DEFAULTPVALUE;
	}
	
	public static void reset(){
		graphFileContents = null;
		labelsFileContents = null;
		backgroundFileContents= null;
		interestFileContents = null;
		setDefaultSupport();
		singleLabel = 0;
		undirected = 0;
		verbose = 0;
		setDefaultMaxSize();
		setDefaultPValue();
		nestedpval = 0;
		output = null;
		typeAlgorithm = "base";
		statistics = "";
	}
	
}
