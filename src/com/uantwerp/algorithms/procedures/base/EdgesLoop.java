package com.uantwerp.algorithms.procedures.base;

import java.util.HashMap;
import java.util.HashSet;

public class EdgesLoop {

	HashMap<Integer,Integer> edgecountbynode = new HashMap<>();
	HashMap<Integer,Integer> edgecountbytarget = new HashMap<>();
	HashMap<Integer, HashSet<Integer>> fowloopedge = new HashMap<>();
	HashMap<Integer, HashSet<Integer>> backloopedge = new HashMap<>();
	
	public EdgesLoop() {
		super();
	}
	
	public HashMap<Integer, Integer> getEdgecountbynode() {
		return edgecountbynode;
	}
	
	public void setEdgecountbynode(HashMap<Integer, Integer> edgecountbynode) {
		this.edgecountbynode = edgecountbynode;
	}
	
	public HashMap<Integer, Integer> getEdgecountbytarget() {
		return edgecountbytarget;
	}
	
	public void setEdgecountbytarget(HashMap<Integer, Integer> edgecountbytarget) {
		this.edgecountbytarget = edgecountbytarget;
	}

	public HashMap<Integer, HashSet<Integer>> getFowloopedge() {
		return fowloopedge;
	}

	public void setFowloopedge(HashMap<Integer, HashSet<Integer>> fowloopedge) {
		this.fowloopedge = fowloopedge;
	}

	public HashMap<Integer, HashSet<Integer>> getBackloopedge() {
		return backloopedge;
	}

	public void setBackloopedge(HashMap<Integer, HashSet<Integer>> backloopedge) {
		this.backloopedge = backloopedge;
	}
	
	
}
