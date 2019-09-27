package com.uantwerp.algorithms.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;

import com.uantwerp.algorithms.SubgraphMining;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphParameters;

public class PrintUtility {

	public static void printHasMap(HashMap<String,String> hashToPtint){
		PrintUtility.print2LogView("printing new hash");
		Iterator<String> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			String value = hashToPtint.get(key);
			PrintUtility.print2LogView(key+" "+value);
		}
	}
	
	public static void printHashSet(HashSet<String> hashToPtint){
		PrintUtility.print2LogView("printing new hash");
		Iterator<String> it = hashToPtint.iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			PrintUtility.print2LogView(key);
		}
	}
	
	public static void printHasMap2(HashMap<String,Integer> hashToPtint){
		PrintUtility.print2LogView("printing new hash");
		Iterator<String> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			int value = hashToPtint.get(key);
			PrintUtility.print2LogView(key+" "+value);
		}
	}
	
	public static void printHasMapHashSet(HashMap<String,HashSet<String>> hashToPtint){
		PrintUtility.print2LogView("printing new hash with hasSet");
		Iterator<String> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			HashSet<String> value = hashToPtint.get(key);
			Iterator<String> itint = value.iterator();
			while (itint.hasNext()){
				String valueIntSet = (String) itint.next();
				PrintUtility.print2LogView(key+" "+valueIntSet);
			}
		}
	}
	
	public static void printListString(List<String> list){
		for (int i=0;i < list.size(); i++){
			PrintUtility.print2LogView(list.get(i));
		}
	}
	
	public static void printHSetString(HashSet<String> hSet){
		Iterator<String> it = hSet.iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			PrintUtility.print2LogView(key);
		}
	}
	
	public static void printSummary(){
		PrintUtility.print2LogView(GraphParameters.graph.vertex.size() + " nodes in the network.");
		PrintUtility.print2LogView(GraphParameters.graph.bgnodes.size() + " background nodes.");
		PrintUtility.print2LogView(GraphParameters.graph.group.size() + " nodes of interest.");
		PrintUtility.print2LogView(GraphParameters.graph.edgeHash.size() + " nodes have targets.");
		PrintUtility.print2LogView(GraphParameters.graph.reverseEdgeHash.size() + " nodes are targets.");
		PrintUtility.print2LogView(GraphParameters.graph.labelHash.size() + " unique label(s).");
	}
	
	public static void printHashSetEdges(HashSet<DFSedge> edges){
		Iterator<DFSedge> it = edges.iterator();
		while (it.hasNext()){
			DFSedge edge = it.next();
			PrintUtility.print2LogView(edge.getSourceId()+" "+edge.getSourceLabel()+" ->"+edge.getTargetId()+" "+edge.getTargetLabel());
		}
	}
	
	public static void printHasMapEdgeInteger(HashMap<DFSedge,Integer> hashToPtint){
		Iterator<DFSedge> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			DFSedge edge = it.next();
			PrintUtility.print2LogView(edge.getSourceId()+" "+edge.getSourceLabel()+" -> "+edge.getTargetId()+" "+edge.getTargetLabel() + "\t" + hashToPtint.get(edge));
		}
	}
	
	public static void printDFScode(DFScode<DFSedge> code){
		for (int i=0; i<code.size(); i++){
			PrintUtility.print2LogView(code.get(i).getSourceId()+" "+code.get(i).getSourceLabel()+" -> "+code.get(i).getTargetId()+" "+code.get(i).getTargetLabel());
		}
	}
	
	public static void printDFScode(List<DFSedge> code){
		for (int i=0; i<code.size(); i++){
			PrintUtility.print2LogView(code.get(i).getSourceId()+" "+code.get(i).getSourceLabel()+" -> "+code.get(i).getTargetId()+" "+code.get(i).getTargetLabel());
		}
	}
	
	
	/**
	 * Helper function that checks whether the program is running in GUI or CLI mode.
	 * When in CLI, it simply prints the output to the stdout using System.out.println().
	 * In GUI mode, the print statement is run inside a separate runnable class in order
	 * to allow the main run thread to be cancelled.
	 * @param String text to print to stdout or log
	 */
	public static void print2LogView(String text) {
		if(SubgraphMining.GUI == false) {
			System.out.println(text);
		}
		else {
			Runnable  runnable = new Runnable() {
	            public void run(){
	            	System.out.println(text);
	            }
	        };
	        SwingUtilities.invokeLater(runnable);
		}
	}
}
