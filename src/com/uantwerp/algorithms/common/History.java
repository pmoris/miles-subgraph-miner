package com.uantwerp.algorithms.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class History {
	
	public List<Edge> edges = new ArrayList<>();
	public HashSet<String> verticesUsed = new HashSet<>();
	public HashSet<String> edgesUsed = new HashSet<>();
	
	public History(PDFS pdfs) {
		if (pdfs == null)
			return;
		while (pdfs != null){
			Edge e = pdfs.current;
			edges.add(e);
			verticesUsed.add(e.from);
			verticesUsed.add(e.to);
			edgesUsed.add(e.from + "@" + e.to);
			if (GraphPathParameters.undirected ==1)
				edgesUsed.add(e.to + "@"  + e.from);
			pdfs = pdfs.prev;
		}
		Collections.reverse(edges);
	}
	
	public History(DFScode<DFSedge> dfscode, HashMap<Integer, String> matches) {
		for (DFSedge efsEdge: dfscode){
			Edge e = new Edge(matches.get(efsEdge.getSourceId()), matches.get(efsEdge.getTargetId()));
			edges.add(e);			
			verticesUsed.add(e.from);
			verticesUsed.add(e.to);
			edgesUsed.add(e.from + "@" + e.to);
			if (GraphPathParameters.undirected ==1)
				edgesUsed.add(e.to + "@"  + e.from);
		}
		Collections.reverse(edges);
	}
	public boolean hasVertex(String vId){
		return verticesUsed.contains(vId);
	}
	
	public boolean hasEdge(String frmId, String toId){
		return edgesUsed.contains(frmId + "@" + toId);
	}

}
