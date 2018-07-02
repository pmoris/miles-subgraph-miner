package com.uantwerp.algorithms.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.uantwerp.algorithms.procedures.fsg.EdgeFSG;

public class Graph{

	public HashMap<String, HashSet<String>> edgeHash;
	public HashMap<String, HashSet<String>> reverseEdgeHash;
	public HashSet<String> group;
	public HashSet<String> bgnodes;
	public HashMap<String, Integer> labelHash;
	public HashMap<String, HashSet<String>> reverseVertex;
	public HashMap<String, HashSet<String>> vertex;
	public List<String> possibleLabels;
	
	public HashMap<String, String> reverseVertexOneLabel;
	public HashMap<String, String> vertexOneLabel;
	private int countWalk=0;
//	private final Cloner cloner = new Cloner();
	
	public Graph() {
		edgeHash = new HashMap<String, HashSet<String>>();
		reverseEdgeHash = new HashMap<String, HashSet<String>>();
		group = new HashSet<String>();
		bgnodes = new HashSet<String>();
		labelHash = new HashMap<String, Integer>();
		reverseVertex = new HashMap<String, HashSet<String>>();
		vertex = new HashMap<String, HashSet<String>>();
		possibleLabels = new ArrayList<>();
		
		reverseVertexOneLabel = new HashMap<String, String>();
		vertexOneLabel = new HashMap<String, String>();
	}
	
	public Graph(Edge edge) {
		edgeHash = new HashMap<String, HashSet<String>>();
		reverseEdgeHash = new HashMap<String, HashSet<String>>();
		vertexOneLabel = new HashMap<String, String>();
		addEdge("1", "2");
		addSingleLabel("1", edge.from);
		addSingleLabel("1", edge.to);
	}

	public void resetGraph(){
		edgeHash = new HashMap<String, HashSet<String>>();
		reverseEdgeHash = new HashMap<String, HashSet<String>>();
		group = new HashSet<String>();
		bgnodes = new HashSet<String>();
		labelHash = new HashMap<String, Integer>();
		reverseVertex = new HashMap<String, HashSet<String>>();
		vertex = new HashMap<String, HashSet<String>>();
		possibleLabels = new ArrayList<>();
		reverseVertexOneLabel = new HashMap<String, String>();
		vertexOneLabel = new HashMap<String, String>();
	}
	
	public List<String> getIncomingEdges(String node, Graph graph){
		List<String> result = new ArrayList<>();
		if (GraphPathParameters.undirected == 0){			
			if (reverseEdgeHash.containsKey(node)){
				for (String nodeIncoming: reverseEdgeHash.get(node)){
					if (!node.equals(nodeIncoming)){
						result.add(nodeIncoming);
					}
				}
			}
		}
		return result;
	}
	
	public List<String> getAllEdges(String node,Graph graph){
		List<String> result = new ArrayList<>();
		if (edgeHash.containsKey(node)){
			for (String nodeConnected: edgeHash.get(node)){
				if (GraphPathParameters.undirected == 1){
					if (!node.equals(nodeConnected)){
						result.add(nodeConnected);
					}
				}else
					result.add(nodeConnected); //this condition is here due to the self edges that a vertex can have		
			}
		}
		if (GraphPathParameters.undirected == 1){
			if (reverseEdgeHash.containsKey(node)){
				for  (String nodeConnected: reverseEdgeHash.get(node)){
					if (!node.equals(nodeConnected)){
						result.add(nodeConnected);
					}
				}
			}
		}
		return result;
	}
	
	public List<String> getDegreeVertex(String node,Graph graph){
		List<String> result = new ArrayList<>();
		if (edgeHash.containsKey(node)){
			for (String nodeConnected: edgeHash.get(node)){
				if (GraphPathParameters.undirected == 1){
					if (!node.equals(nodeConnected)){
						result.add(nodeConnected);
					}
				}else
					result.add(nodeConnected); //this condition is here due to the self edges that a vertex can have		
			}
		}
		if (reverseEdgeHash.containsKey(node)){
			for (String nodeConnected: reverseEdgeHash.get(node)){
				if (!node.equals(nodeConnected)){
					result.add(nodeConnected);
				}
			}
		}
		return result;
	}
	
	public List<String> getForwardRootEdges(String node){
		List<String> result = new ArrayList<>();
		if (edgeHash.containsKey(node)){
			for (String nodeConnected: edgeHash.get(node)){
				if (GraphPathParameters.undirected == 1){
					if (vertexOneLabel.get(node).compareTo(vertexOneLabel.get(nodeConnected))<=0){
						result.add(nodeConnected);
					}
				}else{
					result.add(nodeConnected);
				}
			}
		}
		if (GraphPathParameters.undirected == 1){
			if (reverseEdgeHash.containsKey(node)){
				for (String nodeConnected: reverseEdgeHash.get(node)){
					if (vertexOneLabel.get(node).compareTo(vertexOneLabel.get(nodeConnected))<=0 && !node.equals(nodeConnected)){
						if (!result.contains(nodeConnected))
							result.add(nodeConnected);
					}
				}
			}
		}
		return result;
	}
	
	public List<Edge> getForwardPureEdge(Graph graph, Edge rmEdge, String min_vlb, History history, boolean isOrdered) {
		List<Edge> result = new ArrayList<>();
		List<String> neighbords = new ArrayList<>();
		if (isOrdered)
			neighbords = graph.getAllEdges(rmEdge.to,graph);
		else
			neighbords = graph.getAllEdges(rmEdge.from,graph);
		for (int i = 0; i < neighbords.size(); i++) {
			String nodeTo = neighbords.get(i);
			if (GraphPathParameters.undirected == 1){
				if ( /*(min_vlb.compareTo(graph.vertexOneLabel.get(nodeTo)) <= 0) &&*/ (!history.hasVertex(nodeTo))) {
					result.add(new Edge(rmEdge.to, nodeTo));
				}
			}else{
				if (!history.hasVertex(nodeTo)) {
					if (isOrdered)
						result.add(new Edge(rmEdge.to, nodeTo));
					else
						result.add(new Edge(rmEdge.from, nodeTo));
				}
			}
		}
		return result;
	}
	
	public List<Edge> getForwardRmpathEdge(Graph graph, Edge rmEdge, String minVlb, History history, boolean isOrdered) {
		List<Edge> result = new ArrayList<>();
		String toVlb = graph.vertexOneLabel.get(rmEdge.to);
		List<String> neighbords = new ArrayList<>();
		if (isOrdered)
			neighbords = graph.getAllEdges(rmEdge.from,graph);
		else
			neighbords = graph.getAllEdges(rmEdge.to,graph);
		Iterator<String> it = neighbords.iterator();
		while (it.hasNext()) {
			String nodeTo = it.next();
			String newToVlb = graph.vertexOneLabel.get(nodeTo);
			if (GraphPathParameters.undirected == 1){
				if (rmEdge.to.equals(nodeTo) || /*minVlb.compareTo(newToVlb) > 0 ||*/ history.hasVertex(nodeTo))
					continue;
				if (toVlb.compareTo(newToVlb) <= 0){
					result.add(new Edge(rmEdge.from, nodeTo));
				}
			}else{
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
			
		}
		return result;
	}
	
	public HashSet<String> getForwardVertices(String node){
		if (edgeHash.containsKey(node))
			return edgeHash.get(node);
		return new HashSet<>();
	}
	
	public HashSet<String> getBackwardVertices(String node){
		if (reverseEdgeHash.containsKey(node))
			return reverseEdgeHash.get(node);
		return new HashSet<>();
	}
	
	public HashSet<String> getLabelsVertex(String node){
		if (vertex.containsKey(node))
			return vertex.get(node);
		return new HashSet<>();
	}
	
	public HashSet<String> getNodesLabel(String label){
		if (reverseVertex.containsKey(label))
			return reverseVertex.get(label);
		return new HashSet<>();
	}
	
	public void addEdge(String v1, String v2){
		if (edgeHash.containsKey(v1)){
			HashSet<String> values =  edgeHash.get(v1);
			values.add(v2);
			edgeHash.put(v1,values);
		}else{
			HashSet<String> values =  new HashSet<>();
			values.add(v2);
			edgeHash.put(v1,values);
		}
		if (reverseEdgeHash.containsKey(v2)){
			HashSet<String> values =  reverseEdgeHash.get(v2);
			values.add(v1);
			reverseEdgeHash.put(v2,values);
		}else{
			HashSet<String> values =  new HashSet<>();
			values.add(v1);
			reverseEdgeHash.put(v2,values);
		}
	}
	
	public void addSingleLabel(String v, String l){
		if (!vertexOneLabel.containsKey(v))
			vertexOneLabel.put(v, l);
	}	
	
	public void addEdgeLabels(DFSedge edge){
		addEdge(String.valueOf(edge.getSourceId()), String.valueOf(edge.getTargetId()));
		addSingleLabel(String.valueOf(edge.getSourceId()), edge.getSourceLabel());
		addSingleLabel(String.valueOf(edge.getTargetId()), edge.getTargetLabel());
	}

	@Override
	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
		DFScode<DFSedge> code =  new DFScode<>();
		HashMap<String, Integer> matches = new HashMap<>();
		matches.put("1", 1);
		maxId = 1;
		return generateDFSCode("1", matches, new HashSet<>(), code).dfsCodeToString().hashCode();
//		for (String v1: edgeHash.keySet()){
//			for (String v2: edgeHash.get(v1)){
//				result = prime * result + (v1.hashCode() * vertexOneLabel.get(v1).hashCode()) +  (v2.hashCode() * vertexOneLabel.get(v2).hashCode());
//			}
//		}
//		result = prime * result + ((edgeHash == null) ? 0 : edgeHash.hashCode());
//		result = prime * result + ((vertexOneLabel == null) ? 0 : vertexOneLabel.hashCode());
//		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Graph other = (Graph) obj;
		if (edgeHash == null) {
			if (other.edgeHash != null)
				return false;
		} else if (!edgeHash.equals(other.edgeHash))
			return false;
		if (vertexOneLabel == null) {
			if (other.vertexOneLabel != null)
				return false;
		} else if (!vertexOneLabel.equals(other.vertexOneLabel))
			return false;
		return true;
	}
	
	public boolean isConnected(){
		int totalEdges = 0;
		HashSet<EdgeFSG> edges = getAllEdges();
		totalEdges = edges.size();
		boolean isConnected = true;
		if (!edgeHash.containsKey("1") && !reverseEdgeHash.containsKey("1") && !edgeHash.containsValue("1") && reverseEdgeHash.containsValue("1")){
			return false;
		}else{
			visitedEdge = new HashSet<>();
			countWalk = 0;
			walkGraph("1");
		}
		if (totalEdges != countWalk)
			return false;
		return isConnected;
	}
	
	public List<String> getneighbords(String node){
		List<String> neighbords = new ArrayList<>();
		if (edgeHash.containsKey(node)){
			neighbords.addAll(edgeHash.get(node));
		}
		if (reverseEdgeHash.containsKey(node)){
			neighbords.addAll(reverseEdgeHash.get(node));
		}
		return neighbords;
	}
	
	private HashSet<String> visitedEdge =  new HashSet<>();
	private void walkGraph(String vertex){
		List<String> neighbords = getneighbords(vertex);
		for (String neighbord: neighbords){	
			if (!visitedEdge.contains(vertex+"@"+neighbord)){
				visitedEdge.add(vertex+"@"+neighbord);
				visitedEdge.add(neighbord+"@"+vertex);
				walkGraph(neighbord);
				countWalk += 1;
			}
		}
	}
	
	public void removeEdge(String nodeFrom, String nodeTo){
		if (edgeHash.containsKey(nodeFrom)){
			if (edgeHash.get(nodeFrom).size() > 1){
				if (edgeHash.get(nodeFrom).contains(nodeTo)){
					edgeHash.get(nodeFrom).remove(nodeTo);
				}
			}else{
					edgeHash.remove(nodeFrom);
			}
		}
		if (reverseEdgeHash.containsKey(nodeTo)){
			if (reverseEdgeHash.get(nodeTo).size() > 1){
				if (reverseEdgeHash.get(nodeTo).contains(nodeFrom)){
					reverseEdgeHash.get(nodeTo).remove(nodeFrom);
				}
			}else{
				reverseEdgeHash.remove(nodeTo);
			}
		}
		if (!edgeHash.containsKey(nodeFrom) && !reverseEdgeHash.containsKey(nodeFrom))
			vertexOneLabel.remove(nodeFrom);
		if (!edgeHash.containsKey(nodeTo) && !reverseEdgeHash.containsKey(nodeTo))
			vertexOneLabel.remove(nodeTo);
	}
	
	public HashSet<EdgeFSG> getAllEdges(){
		HashSet<EdgeFSG> edges =  new HashSet<>();
		for (String nodeFrom: edgeHash.keySet()){
			for (String nodeTo: edgeHash.get(nodeFrom)){
				edges.add(new EdgeFSG(nodeFrom, vertexOneLabel.get(nodeFrom), nodeTo, vertexOneLabel.get(nodeTo)));
			}
		}
		if (GraphPathParameters.undirected == 1){
			for (String nodeFrom: reverseEdgeHash.keySet()){
				for (String nodeTo: reverseEdgeHash.get(nodeFrom)){
					if (!edges.contains(new EdgeFSG(nodeTo, vertexOneLabel.get(nodeTo),nodeFrom, vertexOneLabel.get(nodeFrom))))
						edges.add(new EdgeFSG(nodeFrom, vertexOneLabel.get(nodeFrom), nodeTo, vertexOneLabel.get(nodeTo)));
				}
			}
		}
		return edges;
	}
	
	public boolean checkDoubleSidedEdge(EdgeFSG edge){
		if (!vertexStandsAlone(edge.getSourceId()) && !vertexStandsAlone(edge.getTargetId()))
			return true;
		return false;
	}
	
	public boolean vertexStandsAlone(String vertex){
		boolean hasOutgoing = false;
		if (edgeHash.containsKey(vertex)){
			hasOutgoing = true;
			if (edgeHash.get(vertex).size() > 1){
				return false;
			}
		}
		boolean hasIncoming = false;
		if (reverseEdgeHash.containsKey(vertex)){
			hasIncoming = true;
			if (reverseEdgeHash.get(vertex).size() > 1){
				return false;
			}
		}
		if (hasIncoming && hasOutgoing)
			return false;
		return true;
	}
	
	public void addEdge(String vertexFrom, String labelTo, String direction){
		String maxId = String.valueOf(Integer.valueOf(getMaxIdVertex()) + 1);
		if (direction.equals("outgoing"))		
			addEdge(vertexFrom, maxId);
		else if (direction.equals("incoming"))
			addEdge(maxId,vertexFrom);
		vertexOneLabel.put(maxId, labelTo);
	}
	
	public void addSelfEdge(String vertex){
		addEdge(vertex, vertex);
	}
	
	public String getMaxIdVertex(){
		String maxIdEdge = Collections.max(edgeHash.keySet());
		String reverseMaxIdEdge = Collections.max(reverseEdgeHash.keySet());
		if (maxIdEdge.compareTo(reverseMaxIdEdge) > 0){
			return maxIdEdge;
		}else{
			return reverseMaxIdEdge;
		}
	}
	
	public int getNumberVertices(){
		return vertexOneLabel.size();
	}
	public boolean checkOrigigExist(){
		if (edgeHash.containsKey("1") || reverseEdgeHash.containsKey("1"))
			return true;
		else
			return false;
	}
	
	public DFScode<DFSedge> fromGraphToDFS(){
		if (GraphPathParameters.undirected == 0){
			DFScode<DFSedge> code =  new DFScode<>();
			for(String node1: edgeHash.keySet()){
				for(String node2: edgeHash.get(node1)){
					code.add(new DFSedge(Integer.valueOf(node1), vertexOneLabel.get(node1), Integer.valueOf(node2), vertexOneLabel.get(node2)));
				}
			}
			return code;
		}else{
			DFScode<DFSedge> code =  new DFScode<>();
			HashMap<String, Integer> matches = new HashMap<>();
			matches.put("1", 1);
			maxId = 1;
			generateDFSCode("1", matches, new HashSet<>(), code);
			return code;
		}
	}
	
	private int maxId = 0;
	private DFScode<DFSedge> generateDFSCode(String node, HashMap<String,Integer> mapNodes, HashSet<String> visited, DFScode<DFSedge> dfsCode){		
		List<String> neighbords = getneighbords(node);
		for (String neighbor: neighbords){			
			if (!visited.contains(node+"@"+neighbor)){
				if (GraphPathParameters.undirected == 1){
					visited.add(node+"@"+neighbor);
					visited.add(neighbor+"@"+node);
				}else
					visited.add(node+"@"+neighbor);
				if (!mapNodes.containsKey(neighbor)){
					maxId ++;
					mapNodes.put(neighbor, maxId);
					dfsCode.add(new DFSedge(mapNodes.get(node), vertexOneLabel.get(node), maxId, vertexOneLabel.get(neighbor)));
					dfsCode = generateDFSCode(neighbor, mapNodes, visited, dfsCode);
				}else{
					if (GraphPathParameters.undirected == 1)
						if (mapNodes.get(node) < mapNodes.get(neighbor))
							dfsCode.add(new DFSedge(mapNodes.get(node), vertexOneLabel.get(node), mapNodes.get(neighbor), vertexOneLabel.get(neighbor)));
						else
							dfsCode.add(new DFSedge(mapNodes.get(neighbor), vertexOneLabel.get(neighbor), mapNodes.get(node), vertexOneLabel.get(node)));
					else
						dfsCode.add(new DFSedge(mapNodes.get(node), vertexOneLabel.get(node), mapNodes.get(neighbor), vertexOneLabel.get(neighbor)));
					dfsCode = generateDFSCode(neighbor, mapNodes, visited, dfsCode);
				}
			}
		}
		return dfsCode;
	}
	
	public String getLinearLastNode(String node, HashSet<String> visitedNodes){
		visitedNodes.add(node);
		List<String> neighbors = getneighbords(node);
		for (String neighbor: neighbors){
			if (!visitedNodes.contains(neighbor)){
				node = getLinearLastNode(neighbor, visitedNodes);
			}
		}
		return node;
	}
	
	public int getNumberIncomingEdges(String node){
		if (edgeHash.containsKey(node))
			return edgeHash.get(node).size();
		return 0;
	}
	
	public int getNumberOutgoingEdges(String node){
		if (reverseEdgeHash.containsKey(node))
			return reverseEdgeHash.get(node).size();
		return 0;
	}
	
	public List<String> getSortedNodesGraph(Graph g, List<String> codes){
		Collections.sort(codes, new Comparator<String>() {
			@Override
            public int compare(String X,String Y){
				if (getDegreeVertex(X, g).size() == getDegreeVertex(Y, g).size()){					
					if (getNumberIncomingEdges(X) == getNumberIncomingEdges(Y)){
						if (getNumberOutgoingEdges(X) == getNumberOutgoingEdges(Y)){
							if (vertexOneLabel.get(X).equals(vertexOneLabel.get(Y))){
								return 0;
							}else{
								return X.compareTo(Y);
							}
						}else{
							return getNumberOutgoingEdges(X) < getNumberOutgoingEdges(Y) ? -1: 1;
						}
					}else{
						return getNumberIncomingEdges(X) < getNumberIncomingEdges(Y) ? -1: 1;						
					}
				}else{
					return getDegreeVertex(X, g).size() < getDegreeVertex(Y, g).size() ? -1: 1;					
				}
			}
		});
		return codes;
	}
		
	public boolean isOutgoing(String from, String to){
		if (edgeHash.containsKey(from))
			if (edgeHash.get(from).contains(to))
				return true;
		return false;
	}
	
	public HashSet<String> getAllNodes(Graph graph){
		HashSet<String> allNodes = new HashSet<>();
		for (String node: graph.edgeHash.keySet()){
			allNodes.add(node);
			for (String nodeTo: graph.edgeHash.get(node)){
				allNodes.add(nodeTo);
			}
		}
		return allNodes;
	}
}
		