package com.uantwerp.algorithms.common;

import java.io.File;

public abstract class GraphParameters {
	
	private static int DEFAULTSUPPORT = 0;
	private static int DEFAULTMAXSIZE = 3;
	private static double DEFAULTPVALUE = 0.05;
	
	public static File graphFile;
	public static File labelsFile;
	public static File backgroundFile;
	public static File interestFile;
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
	public static String correctionMethod;
	public static boolean frequentMining;

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
		graphFile = null;
		labelsFile = null;
		backgroundFile = null;
		interestFile = null;
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
