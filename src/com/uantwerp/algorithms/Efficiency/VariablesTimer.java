package com.uantwerp.algorithms.Efficiency;

import com.uantwerp.algorithms.utilities.FileUtility;

public abstract class VariablesTimer {
	
	public static long MaxHeapSize = 0;	
	public static String statistics = "";
	private static int mb;
	public static boolean stateFinish = false;
	private static final String SEPARATOR = ";";
	private static int startTime;
	
	public static void initializeVariable(){
		stateFinish = false;
		mb = 1024*1024;
		Runtime runtime = Runtime.getRuntime();
		MaxHeapSize = runtime.maxMemory() / mb;
		startTime = (int) (System.currentTimeMillis()/1000);
	}
	
	public static void addStatistic(){
		int endTime = (int) (System.currentTimeMillis()/1000);
		Runtime runtime = Runtime.getRuntime();
		//			time of execution, 					Print free memory,						Print total available memory
		statistics += (endTime - startTime) + SEPARATOR + runtime.freeMemory() / mb + SEPARATOR + runtime.totalMemory() / mb +"\n";
	}
	
	public static void finishProcess(){
		stateFinish = true;
	}
	
	public static void writeResults(String pathFile){
		if (!pathFile.equals(""))
			FileUtility.writeFile(pathFile, statistics);
	}
	
}
