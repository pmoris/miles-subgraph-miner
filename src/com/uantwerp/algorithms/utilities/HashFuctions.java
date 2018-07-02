package com.uantwerp.algorithms.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.uantwerp.algorithms.common.Graph;
import com.uantwerp.algorithms.procedures.fsg.MatrixUnit;

public abstract class HashFuctions {

	public static HashMap<String, HashSet<String>> updateHashHashSet(HashMap<String, HashSet<String>> hashString, String keyHash, String value){
		if (!hashString.containsKey(keyHash)){
			HashSet<String> list = new HashSet<>();
			list.add(value);
			hashString.put(keyHash,list);
		}else{
			HashSet<String> list = hashString.get(keyHash);
			list.add(value);
			hashString.put(keyHash,list);
		}
		return hashString;
	}
	
	public static HashMap<Integer,HashSet<Integer>> updateHashHashSet(HashMap<Integer,HashSet<Integer>> graphHash, Integer key, Integer element){
		if (key != null && element != null){
			if (graphHash.containsKey(key)){
				HashSet<Integer> elementMod = graphHash.get(key);
				elementMod.add(element);
				graphHash.put(key,elementMod);
			}
			else{
				HashSet<Integer> newElement = new HashSet<Integer>();
				newElement.add(element);
				graphHash.put(key,newElement);
			}
		}
		return graphHash;
	}
	
	public static HashMap<Integer, HashSet<String>> updateHashHashSet(HashMap<Integer, HashSet<String>> hashString, Integer keyHash, String value){
		if (!hashString.containsKey(keyHash)){
			HashSet<String> list = new HashSet<>();
			list.add(value);
			hashString.put(keyHash,list);
		}else{
			HashSet<String> list = hashString.get(keyHash);
			list.add(value);
			hashString.put(keyHash,list);
		}
		return hashString;
	}
	
	public static HashMap<String, HashSet<Graph>> updateHashHashSet(HashMap<String, HashSet<Graph>> hashString, String keyHash, Graph value){
		if (!hashString.containsKey(keyHash)){
			HashSet<Graph> list = new HashSet<>();
			list.add(value);
			hashString.put(keyHash,list);
		}else{
			HashSet<Graph> list = hashString.get(keyHash);
			list.add(value);
			hashString.put(keyHash,list);
		}
		return hashString;
	}
	
	public static HashMap<Integer, List<MatrixUnit>> updateMatrixUnit(HashMap<Integer, List<MatrixUnit>> hashStringList, MatrixUnit value, Integer key){
		if (!hashStringList.containsKey(key)){
			List<MatrixUnit> values = new ArrayList<>();
			values.add(value);
			hashStringList.put(key,values);
		}else{
			List<MatrixUnit> values = hashStringList.get(key);
			values.add(value);
			hashStringList.put(key,values);
		}
		return hashStringList;
	}
	
	public static HashSet<String> returnKeySetHash(HashMap<String,HashSet<String>> hash){
		HashSet<String> hSet = new HashSet<String>();
		Iterator<String> it = hash.keySet().iterator();
		while (it.hasNext()){
			String key = (String) it.next();
			hSet.add(key);
		}
		return hSet;
	}
	
}
