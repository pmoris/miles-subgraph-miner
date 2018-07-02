package com.uantwerp.algorithms.procedures.fsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.javatuples.Quintet;

import com.rits.cloning.Cloner;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.Edge;
import com.uantwerp.algorithms.common.Graph;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.procedures.base.MatchSubgraph;
import com.uantwerp.algorithms.utilities.HashFuctions;


public class FSG {
	private final Cloner cloner = new Cloner();
	static HashMap<String, List<HashMap<Integer, String>>> root = new HashMap<>();	
	static HashMap<String, String> codeCanonical = new HashMap<>();
	static HashMap<String, DFScode<DFSedge>> canCodeDFSCode = new HashMap<>();
	static HashMap<String, DFScode<DFSedge>> canCodeDFSCodeSec = new HashMap<>();	
	static HashMap<String, DFScode<DFSedge>> codeDFSRep = new HashMap<>(); //the codes with not canonical representation
	static HashSet<String> linearGraphs = new HashSet<>();
	
	static HashMap<String, HashSet<String>> supportedNodes = new HashMap<>();
	static HashMap<String, HashSet<String>> codeCores = new HashMap<>(); //a map between canonical code and not canonical representations of its cores
	static HashMap<String, HashSet<String>> codeCoresAux = new HashMap<>(); //a map between canonical code and not canonical representations of its cores
	static HashMap<Integer, HashSet<String>> frequentGraphs = new HashMap<>(); //map between the number of edges and the canonical of the frequent graph
	
	private HashMap<Graph, String> graphToCanonical = new HashMap<>();
	private HashMap<String, Graph> canonicalToGraphs = new HashMap<>();
	
	
	
	public FSG() {
		super();
	}		
	
	private void transformToGraphs(){
		for (String code: codeDFSRep.keySet()){
			Graph g = codeDFSRep.get(code).dfsCodeToGraph();			
			String canCode = codeCanonical.get(code);
			graphToCanonical.put(g, canCode);
			if (!canonicalToGraphs.containsKey(canCode))
				canonicalToGraphs.put(canCode,g);
		}
	}
	
	public void fsg(){
		AuxFSG.oneEdgeMotifs();
		AuxFSG.generateTwoFirstEdges();
		transformToGraphs();
		resetVariables();
		int k = 3;
		boolean candidatesActive = true;
		while(candidatesActive){	
			HashMap<Graph, HashSet<String>> candidates = fsg_gen(frequentGraphs.get(k-1));
			if (candidates.isEmpty())
				candidatesActive = false;
			else{
				codeCores = new HashMap<>();
				codeCores.putAll(codeCoresAux);
				codeCoresAux = new HashMap<>();
				CANDIDATES: for (Graph candidate: candidates.keySet()){				
					String canonicalCode = getCanCode(candidate);
					if (frequentGraphs.containsKey(k))
						if (frequentGraphs.get(k).contains(canonicalCode))
							continue CANDIDATES;
					HashSet<String> support = matchCandidate(candidate, candidates.get(candidate));
					if (AuxFSG.calculateGroupSupport(support) >= GraphPathParameters.supportcutoff){
						HashFuctions.updateHashHashSet(frequentGraphs, k, canonicalCode);
						supportedNodes.put(canonicalCode, support);	
					}else
						continue CANDIDATES;
					AuxFSG.calculateSupport(canonicalCode, null, k);
				}	
				k ++;
			}
		}
	}
	
	/**
	 * 
	 * @param candidate
	 * @param supportedNodes -> the intersection of the cores of the candidate; to start matching from these supported nodes
	 * @return supported nodes which support the candidate
	 */
	private HashSet<String> matchCandidate(Graph candidate, HashSet<String> supportedNodes){ 
		HashSet<String> match = new HashSet<>();	
		DFScode<DFSedge> code = candidate.fromGraphToDFS();
		for(String sup: supportedNodes){
			HashMap<Integer,String> matchGraph = new HashMap<>();
			matchGraph.put(1, sup);
			if (GraphPathParameters.undirected == 0)
				matchGraph = MatchSubgraph.matchSubgraph(code, matchGraph);
			else
				matchGraph = MatchSubgraph.matchSubgraph_und(code, matchGraph);
			if (matchGraph != null){
				match.add(matchGraph.get(1));
			}
		}
		return match;
	}
	
	private HashMap<Graph, HashSet<String>> fsg_gen(HashSet<String> frequentGraphs){
		HashMap<Graph, HashSet<String>> candidatesAll = new HashMap<>();
		HashMap<Graph, Integer> candidatesSupport = new HashMap<>();
		/*******************************checks each pair of graphs***********************************/
		HashSet<String> checkedValues = new HashSet<>();
		for (String code1: codeCores.keySet()){
			if (linearGraphs.contains(code1)){
				for (Graph candidate: generateLinearCandidate(code1)){
					if (supportedNodes.containsKey(code1)){
						candidatesAll.put(candidate, supportedNodes.get(code1));
					}
				}
				for (Graph candidate: generateLinearSelfEdges(code1)){
					if (supportedNodes.containsKey(code1)){
						candidatesAll.put(candidate, supportedNodes.get(code1));
					}
				}				
			}
			HashSet<String> supportingCores = codeCores.get(code1); //cores of the first graph			
			LOOP2: for (String code2: cloner.deepClone(codeCores).keySet()){
				if (checkedValues.contains(code1))
					continue LOOP2;
		/*********************************************************************************************/
		/****************check the cores of the pair of frequent graphs*******************************/
				HashSet<String> suppCores = new HashSet<>();
				for (String nodeSup: codeCores.get(code2)){ //cores of the second graph
					if (supportingCores.contains(nodeSup))
						suppCores.add(nodeSup); //add when code1 and code 2 share a core
				}
		/*********************************************************************************************/
				if (suppCores.size() > 0){ //if the two graphs share more than one core then join
					HashSet<String> candidatesIntersection = getIntersection(code1, code2);
					int groupIntersection = AuxFSG.calculateGroupSupport(candidatesIntersection);  
					if (groupIntersection >= GraphPathParameters.supportcutoff){//if the intersection between the frequent graphs reaches the support threshold
						for (String core: suppCores){
		/**********adds the support of the intersection of each of the candidates which havent been checked already************/
							for(Graph candidate: fsg_join(code1, code2, core)){								
								if (!candidatesSupport.containsKey(candidate)){
									candidatesAll.put(candidate, candidatesIntersection);
									candidatesSupport.put(candidate, groupIntersection);
								}
		/*********************************************************************************************/									
							}
						}
					}
				}			
			}
			checkedValues.add(code1);
		}
		return candidatesAll;
	}
	
	/**
	 * 
	 * @param g1
	 * @param g2
	 * @param core -> common core between the graphs
	 * @return candidates size (k+1) generated grom g1 and g2
	 * @throws CloneNotSupportedException 
	 */
	private List<Graph> fsg_join(String g1Str, String g2Str, String coreStr){
		HashSet<String> candidatesCan = new HashSet<>();
		Graph g1 =canonicalToGraphs.get(g1Str);
		Graph g2 =canonicalToGraphs.get(g2Str);
		Graph core = canonicalToGraphs.get(coreStr);
		EdgeFSG e1 = checkLackingEdge(g1, getCanCode(core));
		EdgeFSG e2 = checkLackingEdge(g2, getCanCode(core));	
		if (e1 ==null || e2 == null)
			return new ArrayList<>();
		boolean doubleSideE1 = g1.checkDoubleSidedEdge(e1);
		boolean doubleSideE2 = g2.checkDoubleSidedEdge(e2);
		List<Quintet<Graph, String, Edge,Boolean,String>> provisitionalCandidates =  getProvisionalCandidates(core, e1, g1Str, candidatesCan, doubleSideE1);
		List<Quintet<Graph, String, Edge,Boolean,String>> provisitionalCandidates2 = getProvisionalCandidates(core, e2, g2Str, candidatesCan, doubleSideE2);
		List<Graph> candidates = joinCandidates(provisitionalCandidates, provisitionalCandidates2, core);
		return candidates;
	}
	
	public List<Quintet<Graph, String, Edge, Boolean, String>> getProvisionalCandidates(Graph core, EdgeFSG e, String canStr, HashSet<String> candidatesCan, boolean doubleSideE){
		List<Quintet<Graph, String, Edge,Boolean, String>> provisitionalCandidates =  new ArrayList<>();
		for (EdgeFSG edge: core.getAllEdges()){						
			if (!doubleSideE){
				List<String> graphDirections = new ArrayList<>();
				if (GraphPathParameters.undirected==1){
					graphDirections.add("outgoing");
				}else{
					graphDirections.add("outgoing");
					graphDirections.add("incoming");
				}
				boolean found = false;
				DIRECTIONS: for (String direction: graphDirections){
					Quintet<Graph, String, Edge, Boolean, String> gNew = addEdge(core, edge.getSourceId(), edge.getSourceLabel(), e, canStr, candidatesCan, doubleSideE, direction);
					if (gNew != null){
						provisitionalCandidates.add(gNew);
						found = true;
					}
					Quintet<Graph, String, Edge, Boolean, String> gNew2 = addEdge(core, edge.getTargetId(), edge.getTargetLabel(), e, canStr, candidatesCan, doubleSideE, direction);
					if (gNew2 != null){
						provisitionalCandidates.add(gNew2);
						found = true;
					}
					if (found)
						break DIRECTIONS;
				}
			}else{
				Graph core2 = cloner.deepClone(core);
				for (EdgeFSG edgeTo: core2.getAllEdges()){
					if (e.getSourceId().equals(e.getTargetId()) && GraphPathParameters.undirected == 0){ //the algorithm detects a self edge on a directed configuration
						if (edge.getSourceId().equals(edgeTo.getSourceId()) && edge.getTargetId().equals(edgeTo.getTargetId())){
							Quintet<Graph, String, Edge, Boolean, String> gNew = addEdge(core, edge.getSourceId(), edgeTo.getSourceId(), edge.getSourceLabel(), edgeTo.getSourceLabel(), e, canStr, candidatesCan, doubleSideE);
							if (gNew != null){
								provisitionalCandidates.add(gNew);
							}
							Quintet<Graph, String, Edge, Boolean, String> gNew2 = addEdge(core, edge.getTargetId(), edgeTo.getTargetId(), edge.getTargetLabel(), edgeTo.getTargetLabel(), e, canStr, candidatesCan, doubleSideE);
							if (gNew2 != null){
								provisitionalCandidates.add(gNew2);
							}
						}
					}else{
						if (!edge.getSourceId().equals(edgeTo.getSourceId())){
							Quintet<Graph, String, Edge, Boolean, String> gNew = addEdge(core, edge.getSourceId(), edgeTo.getSourceId(), edge.getSourceLabel(), edgeTo.getSourceLabel(), e, canStr, candidatesCan, doubleSideE);
							if (gNew != null){
								provisitionalCandidates.add(gNew);
							}	
						}
						if (!edge.getSourceId().equals(edgeTo.getTargetId())){
							Quintet<Graph, String, Edge, Boolean, String> gNew2 = addEdge(core, edge.getSourceId(), edgeTo.getTargetId(), edge.getSourceLabel(), edgeTo.getTargetLabel(), e, canStr, candidatesCan, doubleSideE);
							if (gNew2 != null){
								provisitionalCandidates.add(gNew2);
							}
						}
						if (!edge.getTargetId().equals(edgeTo.getTargetId())){
							Quintet<Graph, String, Edge, Boolean, String> gNew3 = addEdge(core, edge.getTargetId(), edgeTo.getTargetId(), edge.getTargetLabel(),edgeTo.getTargetLabel(), e, canStr, candidatesCan, doubleSideE);
							if (gNew3 != null){
								provisitionalCandidates.add(gNew3);
							}
						}
						if (!edge.getTargetId().equals(edgeTo.getSourceId())){
							Quintet<Graph, String, Edge, Boolean, String> gNew4 = addEdge(core, edge.getTargetId(), edgeTo.getSourceId(), edge.getTargetLabel(), edgeTo.getSourceLabel(), e, canStr, candidatesCan, doubleSideE);
							if (gNew4 != null){
								provisitionalCandidates.add(gNew4);
							}
						}
					}
				}
			}			
		}
		return provisitionalCandidates;
	}
	
	public List<Graph> joinCandidates(List<Quintet<Graph, String, Edge, Boolean, String>> provCan1, List<Quintet<Graph, String, Edge, Boolean, String>> provCan2, Graph core){
		int coreNroVertices = core.getNumberVertices();
		List<Graph> candidates =  new ArrayList<>();
		for (Quintet<Graph, String, Edge, Boolean, String> candidate1: provCan1){			
			String sourceVertex = candidate1.getValue1();
			Edge edgeSource = candidate1.getValue2();
			LOOP2: for (Quintet<Graph, String, Edge, Boolean, String> candidate2: provCan2){
				String sourceVertex2 = candidate2.getValue1();
				Edge edgeSource2 = candidate2.getValue2();
				if (!candidate1.getValue3() && !candidate2.getValue3()){ //when both of the graphs add a single edge
					if (sourceVertex.equals(sourceVertex2)){ //check if the candidates start from the same vertex, then just add one edge
						if ((coreNroVertices + 2) > GraphPathParameters.maxsize)
							continue LOOP2;
						joinHelper(candidate1, candidate2, sourceVertex, edgeSource2, candidates);
					}else{
						if (edgeSource.getTo().equals(edgeSource2.getTo())){ //check if the candidates start from different vertices , then add one independent edge and one edge connecting the the source with the new vertex
							if ((coreNroVertices + 1) > GraphPathParameters.maxsize)
								continue LOOP2;
							Graph candidateComplete1 = cloner.deepClone(candidate1.getValue0());
							addEdgeJoinedUnion(candidateComplete1, sourceVertex2, candidateComplete1.getMaxIdVertex());		
							candidateChecker(candidateComplete1, candidate1.getValue0(), candidate2.getValue0(), candidates);
							if ((coreNroVertices + 2) > GraphPathParameters.maxsize)
								continue LOOP2;
							joinHelper(candidate1, candidate2, sourceVertex2, edgeSource2, candidates);
						}else{//when the candidates have different source vertices and different labels, then just adds one independent edge
							if ((coreNroVertices + 2) > GraphPathParameters.maxsize)
								continue LOOP2;
							joinHelper(candidate1, candidate2, sourceVertex2, edgeSource2, candidates);
						}
					}
				}else if (candidate1.getValue3() && !candidate2.getValue3()){
					if (candidate1.getValue2().from.equals(candidate1.getValue2().to) || candidate2.getValue2().from.equals(candidate2.getValue2().to)){
						if ((coreNroVertices + 1) > GraphPathParameters.maxsize)
							continue LOOP2;
						if (candidate2.getValue2().from.equals(candidate2.getValue2().to)){
							Graph candidateComplete = cloner.deepClone(candidate1.getValue0());
							addEdgeJoinedUnion(candidateComplete, edgeSource2.from, edgeSource2.to);
							candidateChecker(candidateComplete, candidate1.getValue0(), candidate2.getValue0(), candidates);
						}else{
							joinHelper(candidate1, candidate2, sourceVertex2, edgeSource2, candidates);
						}
					}else{
						if ((coreNroVertices + 2) > GraphPathParameters.maxsize)
							continue LOOP2;
						joinHelper(candidate1, candidate2, sourceVertex2, edgeSource2, candidates);
					}
				}else if (!candidate1.getValue3() && !candidate2.getValue3()){
					if (candidate1.getValue2().from.equals(candidate1.getValue2().to) || candidate2.getValue2().from.equals(candidate2.getValue2().to)){
						if ((coreNroVertices + 1) > GraphPathParameters.maxsize)
							continue LOOP2;
						if (candidate2.getValue2().from.equals(candidate2.getValue2().to)){
							Graph candidateComplete = cloner.deepClone(candidate1.getValue0());
							addEdgeJoinedUnion(candidateComplete, edgeSource2.from, edgeSource2.to);
							candidateChecker(candidateComplete, candidate1.getValue0(), candidate2.getValue0(), candidates);
						}else{
							joinHelper(candidate1, candidate2, sourceVertex2, edgeSource2, candidates);
						}
					}else{
						if ((coreNroVertices + 2) > GraphPathParameters.maxsize)
							continue LOOP2;
						joinHelper(candidate2, candidate1, sourceVertex, edgeSource, candidates);
					}
				}else{ //both of the graphs add an connected and different edge
					if (!(edgeSource.from.equals(edgeSource2.from) && edgeSource.to.equals(edgeSource2.to)) &&
							!(edgeSource.to.equals(edgeSource2.from) && (edgeSource.from.equals(edgeSource2.to)))){
						if ((coreNroVertices + 2) > GraphPathParameters.maxsize)
							continue LOOP2;
						Graph candidateComplete = cloner.deepClone(candidate1.getValue0());
						addEdgeJoinedUnion(candidateComplete, edgeSource2.from, edgeSource2.to);
						candidateChecker(candidateComplete, candidate1.getValue0(), candidate2.getValue0(), candidates);
					}
				}
			}
		}
		return candidates;
	}
	
	private void joinHelper(Quintet<Graph, String, Edge, Boolean, String> candidate1, Quintet<Graph, String, Edge, Boolean, String> candidate2, String sourceVertex, Edge edgeSource2, List<Graph> candidates){
		Graph candidateComplete = cloner.deepClone(candidate1.getValue0());
		addEdgeSingleUnion(candidateComplete, sourceVertex, edgeSource2.to, candidate2.getValue4());
		candidateChecker(candidateComplete, candidate1.getValue0(), candidate2.getValue0(), candidates);
	}
	
	private void candidateChecker(Graph candidateComplete, Graph candidate1, Graph candidate2, List<Graph> candidates){
		if (candidateComplete.getNumberVertices() <= GraphPathParameters.maxsize){
			HashFuctions.updateHashHashSet(codeCoresAux, getCanCode(candidateComplete), getCanCode(candidate1));
			HashFuctions.updateHashHashSet(codeCoresAux, getCanCode(candidateComplete), getCanCode(candidate2));
			candidates.add(candidateComplete);
		}
	}
	
	private Quintet<Graph, String, Edge, Boolean, String> addEdge(Graph core, String sourceId, String sourceLabel, EdgeFSG e, String g1Str, HashSet<String> candidatesCan,Boolean doubleEdge, String direction){
		Graph c = cloner.deepClone(core);
		Edge edge = new Edge();
		if (sourceLabel.equals(e.getSourceLabel())){//if from label of the core edge is equal to from label of the new edge	
			addEdgeSingleUnion(c, sourceId, e.getTargetLabel(), direction);
			edge.setFrom(e.getTargetId());
			edge.setTo(e.getTargetLabel());
		}else if (sourceLabel.equals(e.getTargetLabel())){//if from label of the core edge is equal to target label of the new edge
			addEdgeSingleUnion(c, sourceId, e.getSourceLabel(), direction);
			edge.setFrom(e.getSourceId());
			edge.setTo(e.getSourceLabel());
		}
		String canCode = getCanCode(c);
		if (!candidatesCan.contains(canCode))
			candidatesCan.add(canCode);		
		if (g1Str.equals(canCode))					
			return new Quintet<Graph, String, Edge, Boolean, String>(c, sourceId, edge, doubleEdge, direction);
		return null;
	}
	
	private Quintet<Graph, String, Edge, Boolean, String> addEdge(Graph core, String fromId, String toId, String sourceLabel, String targetLabel,  EdgeFSG e, String g1Str, HashSet<String> candidatesCan, Boolean doubleEdge){		
		Graph c = cloner.deepClone(core);
		boolean foundEdge = false;
		if (e.getSourceId().equals(e.getTargetId()) && GraphPathParameters.undirected == 1)
			return null;
		else if (e.getSourceId().equals(e.getTargetId()) && GraphPathParameters.undirected == 0){
			if (fromId.equals(toId) && sourceLabel.equals(targetLabel)){
				if (sourceLabel.equals(e.getSourceLabel())){
					addEdgeJoinedUnion(c,fromId,toId);
					foundEdge = true;
				}				
			}
		}			
		if (sourceLabel.equals(e.getSourceLabel()) && targetLabel.equals(e.getTargetLabel()) && !foundEdge){//if from label of the core edge is equal to from label of the new edge
			addEdgeJoinedUnion(c,fromId,toId);
			foundEdge = true;
		}
		if (sourceLabel.equals(e.getTargetLabel()) && targetLabel.equals(e.getSourceLabel()) && !foundEdge){//if from label of the core edge is equal to from label of the new edge			
			addEdgeJoinedUnion(c,toId,fromId);
		}
		String canCode = getCanCode(c);
		if (!candidatesCan.contains(canCode))
			candidatesCan.add(canCode);		
		if (g1Str.equals(canCode))					
			return new Quintet<Graph, String, Edge, Boolean, String>(c, fromId, new Edge(fromId, toId), doubleEdge, "");
		return null;
	}
	
	private void addEdgeSingleUnion(Graph g, String vertexToJoin, String newLabel, String direction){
		g.addEdge(vertexToJoin, newLabel, direction);
	}
	
	private void addEdgeJoinedUnion(Graph g, String vertexFrom, String vertexTo){
		g.addEdge(vertexFrom, vertexTo);
	}
	
	private EdgeFSG checkLackingEdge(Graph g, String canCore){
		Graph gTest = cloner.deepClone(g);
		HashSet<EdgeFSG> edges = g.getAllEdges();
		for (EdgeFSG edge: edges){
			gTest.removeEdge(edge.getSourceId(), edge.getTargetId());
			if (gTest.isConnected()){
				if (canCore.equals(getCanCode(gTest))){
					
					return edge;
				}
			}
			gTest = cloner.deepClone(g);
		}
		return null;
	}
		
	
	/**
	 * Tests if two candidates are gonna have the necessary support to be combined in one graph, measure intersection of the interesting nodes
	 */
	private HashSet<String> getIntersection(String canonicalG1, String canonicalG2){ 
		HashSet<String> intersect = new HashSet<>();
		if (!supportedNodes.containsKey(canonicalG1))
			return intersect;
		if (!supportedNodes.containsKey(canonicalG2))
			return intersect;		
		for (String code: supportedNodes.get(canonicalG1)){
			if (supportedNodes.get(canonicalG2).contains(code))
				intersect.add(code);
		}
		return intersect;
	}
	
	private void resetVariables(){
		root = null;
		canCodeDFSCodeSec = null;
		codeCanonical = null;
		canCodeDFSCode = null;
		codeDFSRep = null;
	}
	
	/**
	 * Looks for the canonical code, if this is not yet created generates the matrix and use vertex invariants
	 */
	private String getCanCode(Graph g){		
		String canCode = "";
		if (graphToCanonical.containsKey(g)){
			canCode = graphToCanonical.get(g);			
		}else{
			VertexInvariant matrixRepresentation = new VertexInvariant(g);
			canCode = matrixRepresentation.getRepresentation();
			if (!canonicalToGraphs.containsKey(canCode))
				canonicalToGraphs.put(canCode,g);
			graphToCanonical.put(g, canCode);
		}
		return canCode;
	}
		
	/**
	 * for linear graphs generates all their possible candidates given that the current approach can not generate them by mixing 2 subgraphs 
	 * @param code
	 * @return
	 */
	private List<Graph> generateLinearCandidate(String code){
		List<Graph> candidates = new ArrayList<>();
		Graph core = canonicalToGraphs.get(code);
		for (String label: GraphPathParameters.graph.possibleLabels){
			Graph c = cloner.deepClone(core);
			String lastNode = c.getLinearLastNode("1", new HashSet<>());
			c.addEdge(lastNode, label, "outgoing");			
			linearGraphs.add(getCanCode(c));
			candidateChecker(c,canonicalToGraphs.get(code),canonicalToGraphs.get(code),candidates);
			if (GraphPathParameters.undirected==0){
				Graph c2 = cloner.deepClone(core);
				c2.addEdge(lastNode, label, "incoming");
				linearGraphs.add(getCanCode(c2));
				candidateChecker(c2,canonicalToGraphs.get(code),canonicalToGraphs.get(code),candidates);
			}
		}
		return candidates;
	}
	
	private List<Graph> generateLinearSelfEdges(String code){
		List<Graph> candidates = new ArrayList<>();
		Graph core = canonicalToGraphs.get(code);
		Graph c = cloner.deepClone(core);
		String lastNode = c.getLinearLastNode("1", new HashSet<>());
		c.addSelfEdge(lastNode);		
		candidateChecker(c,canonicalToGraphs.get(code),canonicalToGraphs.get(code),candidates);
		return candidates;
	}

}
