package com.uantwerp.algorithms.common;

public abstract class GraphPathParameters {
	
	private static int DEFAULTSUPPORT = 0;
	private static int DEFAULTMAXSIZE = 5;
	private static double DEFAULTPVALUE = 0.05;
	
	public static String pathGraph;
	public static String pathLabels;
	public static String pathBgNodes;
	public static String pathGroupFile;
	public static int supportcutoff; 
	public static int singleLabel; 
	public static int undirected;
	public static int verbose;
	public static int maxsize;
	public static double pvalue;
	public static int nestedpval;
	public static String output;
	public static String typeAlgorithm;
	public static String typeResult;
	public static Graph graph = new Graph();
	public static String statistics;

	public static void setDefaultMaxSize(){
		GraphPathParameters.maxsize = DEFAULTMAXSIZE;
	}
	
	public static void setDefaultSupport(){
		GraphPathParameters.supportcutoff = DEFAULTSUPPORT;
	}
	
	public static void setDefaultPValue(){
		GraphPathParameters.pvalue = DEFAULTPVALUE;
	}
	
	public static void reset(){
		pathGraph = null;
		pathLabels = null;
		pathBgNodes=null;
		pathGroupFile = null;
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
