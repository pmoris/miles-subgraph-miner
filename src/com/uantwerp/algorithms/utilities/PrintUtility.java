package com.uantwerp.algorithms.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;

public class PrintUtility {

	public static void printHasMap(HashMap<String,String> hashToPtint){
		System.out.println("printing new hash");
		Iterator<String> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			String value = hashToPtint.get(key);
			System.out.println(key+" "+value);
		}
	}
	
	public static void printHashSet(HashSet<String> hashToPtint){
		System.out.println("printing new hash");
		Iterator<String> it = hashToPtint.iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			System.out.println(key);
		}
	}
	
	public static void printHasMap2(HashMap<String,Integer> hashToPtint){
		System.out.println("printing new hash");
		Iterator<String> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			int value = hashToPtint.get(key);
			System.out.println(key+" "+value);
		}
	}
	
	public static void printHasMapHashSet(HashMap<String,HashSet<String>> hashToPtint){
		System.out.println("printing new hash with hasSet");
		Iterator<String> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			HashSet<String> value = hashToPtint.get(key);
			Iterator<String> itint = value.iterator();
			while (itint.hasNext()){
				String valueIntSet = (String) itint.next();
				System.out.println(key+" "+valueIntSet);
			}
		}
	}
	
	public static void printListString(List<String> list){
		for (int i=0;i < list.size(); i++){
			System.out.println(list.get(i));
		}
	}
	
	public static void printHSetString(HashSet<String> hSet){
		Iterator<String> it = hSet.iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			System.out.println(key);
		}
	}
	
	public static void printSummary(){
		System.out.println("Found: " + GraphPathParameters.graph.vertex.size() + " nodes in graph");
		System.out.println("Of which " + GraphPathParameters.graph.bgnodes.size()+" are part of the background");
		System.out.println("Of which " + GraphPathParameters.graph.group.size()+" were selected");
		System.out.println("Of which " + GraphPathParameters.graph.edgeHash.size()+" have targets");
		System.out.println("Of which " + GraphPathParameters.graph.reverseEdgeHash.size()+" are targets");
		System.out.println("With " + GraphPathParameters.graph.labelHash.size() + " possible labels");
	}
	
	public static void printHashSetEdges(HashSet<DFSedge> edges){
		Iterator<DFSedge> it = edges.iterator();
		while (it.hasNext()){
			DFSedge edge = it.next();
			System.out.println(edge.getSourceId()+" "+edge.getSourceLabel()+" ->"+edge.getTargetId()+" "+edge.getTargetLabel());
		}
	}
	
	public static void printHasMapEdgeInteger(HashMap<DFSedge,Integer> hashToPtint){
		Iterator<DFSedge> it = hashToPtint.keySet().iterator();
		while (it.hasNext()){
			DFSedge edge = it.next();
			System.out.println(edge.getSourceId()+" "+edge.getSourceLabel()+" -> "+edge.getTargetId()+" "+edge.getTargetLabel() + "\t" + hashToPtint.get(edge));
		}
	}
	
	public static void printDFScode(DFScode<DFSedge> code){
		for (int i=0; i<code.size(); i++){
			System.out.println(code.get(i).getSourceId()+" "+code.get(i).getSourceLabel()+" -> "+code.get(i).getTargetId()+" "+code.get(i).getTargetLabel());
		}
	}
	
	public static void printDFScode(List<DFSedge> code){
		for (int i=0; i<code.size(); i++){
			System.out.println(code.get(i).getSourceId()+" "+code.get(i).getSourceLabel()+" -> "+code.get(i).getTargetId()+" "+code.get(i).getTargetLabel());
		}
	}
	
}
