package com.uantwerp.algorithms.procedures.gspan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.javatuples.Pair;

import com.uantwerp.algorithms.common.Edge;
import com.uantwerp.algorithms.common.Graph;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.common.History;

public abstract class EdgeFunctions {

	public static Pair<Edge, String> getBackwardEdge(Graph graph, Edge e1, Edge e2, History history, boolean isOrderedE1, boolean isOrderedE2) {
		String nodeToCompare = "";
		if (isOrderedE1)
			nodeToCompare = e1.from;
		else
			nodeToCompare = e1.to;
		if (GraphPathParameters.undirected == 1 && e1.equals(e2))
			return null;		
		List<String> neighbords = new ArrayList<>();
		if (isOrderedE2)
			neighbords = graph.getAllEdges(e2.to,graph);
		else
			neighbords = graph.getAllEdges(e2.from,graph);
		for (String nodeTo: neighbords){
			if (isOrderedE2){
				if (history.hasEdge(e2.to, nodeTo) || !nodeTo.equals(nodeToCompare))
					continue; 
				return new Pair<Edge,String>(new Edge(e2.to, nodeTo),"outgoing");
			}else{
				if (history.hasEdge(e2.from, nodeTo) || !nodeTo.equals(nodeToCompare))
					continue; 
				return new Pair<Edge,String>(new Edge(e2.from, nodeTo),"outgoing");
			}
		}
		if (GraphPathParameters.undirected == 0){
			if (isOrderedE2)
				neighbords = graph.getIncomingEdges(e2.to,graph);
			else
				neighbords = graph.getIncomingEdges(e2.from,graph);
			for (String nodeTo: neighbords){
				if (isOrderedE2){
					if (history.hasEdge(nodeTo, e2.to) || !nodeTo.equals(nodeToCompare))
						continue; 
					return new Pair<Edge,String>(new Edge(nodeTo, e2.to),"incoming");
				}else{
					if (history.hasEdge(nodeTo, e2.from) || !nodeTo.equals(nodeToCompare))
						continue; 
					return new Pair<Edge,String>(new Edge(nodeTo, e2.from),"incoming");
				}
			}
		}
		return null;
	}

	public static List<Edge> getForwardPureEdge(Graph graph, Edge rm_edge, History history, boolean isOrdered) {
		List<Edge> result = new ArrayList<>();
		List<String> neighbords = new ArrayList<>();
		if (isOrdered)
			neighbords = graph.getAllEdges(rm_edge.to,graph);
		else
			neighbords = graph.getAllEdges(rm_edge.from,graph);
		for (int i = 0; i < neighbords.size(); i++) {
			String nodeTo = neighbords.get(i);
			if (history.hasVertex(nodeTo)) {
				continue;
			}
			if (isOrdered)
				result.add(new Edge(rm_edge.to, nodeTo));
			else
				result.add(new Edge(rm_edge.from, nodeTo));
		}
		return result;
	}
	
	public static Edge getSelfEdge(Graph graph, Edge rmEdge, History history, boolean isOrdered) {
		if (isOrdered){
			for (String nodeTo: graph.getAllEdges(rmEdge.to,graph)) {
				if (rmEdge.to.equals(nodeTo) && !history.hasEdge(rmEdge.to,nodeTo)){
					return new Edge(rmEdge.to, nodeTo);
				}				
			}
		}
		else{
			for (String nodeTo: graph.getAllEdges(rmEdge.from,graph)) {
				if (rmEdge.from.equals(nodeTo) && !history.hasEdge(rmEdge.from,nodeTo)){
					return new Edge(rmEdge.from, nodeTo);
				}				
			}
		}
		return null;
	}

	public static List<Edge> getForwardRmpathEdge(Graph graph, Edge rmEdge, History history, boolean isOrdered) {
		List<Edge> result = new ArrayList<>();
		List<String> neighbords = new ArrayList<>();
		if (isOrdered)
			neighbords = graph.getAllEdges(rmEdge.from,graph);
		else
			neighbords = graph.getAllEdges(rmEdge.to,graph);
		Iterator<String> it = neighbords.iterator();
		while (it.hasNext()) {
			String nodeTo = it.next();
			if (isOrdered){
				if (rmEdge.to.equals(nodeTo) || history.hasVertex(nodeTo))
					continue;
				result.add(new Edge(rmEdge.from, nodeTo));
			}else{
				if (rmEdge.from.equals(nodeTo) || history.hasVertex(nodeTo))
					continue;
				result.add(new Edge(rmEdge.to, nodeTo));
			}
		}
		return result;
	}
	
	public static List<Edge> getForwardPureIncomingEdge(Graph graph, Edge rmEdge, History history, boolean isOrdered) {		
		List<Edge> result = new ArrayList<>();
		List<String> neighbords = new ArrayList<>();
		if (isOrdered)
			neighbords = graph.getIncomingEdges(rmEdge.to,graph);
		else
			neighbords = graph.getIncomingEdges(rmEdge.from,graph);
		for (int i = 0; i < neighbords.size(); i++) {
			String nodeTo = neighbords.get(i);
			if (history.hasVertex(nodeTo)) {
				continue;
			}
			if (isOrdered)
				result.add(new Edge(nodeTo, rmEdge.to));
			else
				result.add(new Edge(nodeTo, rmEdge.from));
		}
		return result;
	}
	
	public static List<Edge> getForwardRmpathIncomingEdge(Graph graph, Edge rmEdge, History history, boolean isOrdered) {
		List<Edge> result = new ArrayList<>();
		List<String> neighbords = new ArrayList<>();
		if (isOrdered)
			neighbords = graph.getIncomingEdges(rmEdge.from,graph);
		else
			neighbords = graph.getIncomingEdges(rmEdge.to,graph);
		Iterator<String> it = neighbords.iterator();
		while (it.hasNext()) {
			String nodeTo = it.next();
			if (history.hasVertex(nodeTo)) {
				continue;
			}
			if (isOrdered){
				if (rmEdge.to.equals(nodeTo) || history.hasVertex(nodeTo))
					continue;
				result.add(new Edge(nodeTo, rmEdge.from));
			}else{
				if (rmEdge.from.equals(nodeTo) || history.hasVertex(nodeTo))
					continue;
				result.add(new Edge(nodeTo, rmEdge.to));
			}
		}			
		return result;
	}
	
}
