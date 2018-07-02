package com.uantwerp.algorithms.procedures.gspan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.javatuples.Pair;

import com.uantwerp.algorithms.MiningState;
import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.Edge;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.common.History;
import com.uantwerp.algorithms.common.PDFS;
import com.uantwerp.algorithms.common.Projection;
import com.uantwerp.algorithms.utilities.AlgorithmUtility;

public abstract class GSpan {

	private static DFScode<DFSedge> dfsCode = new DFScode<>();
	private static List<DFScode<DFSedge>> firstFrequentGraphs = new ArrayList<>();
	private static HashSet<String> minDFScodes = new HashSet<>();
	
	public static void startAlgorithm() {		
		generate1edgeFrequentGraphs();
		HashMap<DFSedge, Projection<PDFS>> root = getFirstProjections();
		List<DFScode<DFSedge>> orderedDFScode = AuxiliaryFunctions.orderDFScode(firstFrequentGraphs);
		for (int i = 0; i < orderedDFScode.size(); i++) {
			DFSedge dfsEdge = orderedDFScode.get(i).get(0);		
			Projection<PDFS> projected = root.get(dfsEdge);
			dfsCode.add(dfsEdge);			
			miningCall(projected);
			dfsCode.remove(dfsCode.size() - 1);					
		}

	}
	
	private static void subGraphMiningUndirected(int prevHits,Projection<PDFS> projected) {
		String minStr = getMinStrStr();
		/*********************************Checks minimum DFS***********************************/
		if (!checkMinimumDFScode(minStr))
			return;		
		/*********************************Checks the support***********************************/
		if (!groupupportcheck(prevHits, projected, minStr)){
			return;
		}
		if (GraphPathParameters.verbose== 1) System.out.println("Number of projections = " + projected.size());
		int numVertices = dfsCode.getNumVertices();
		dfsCode.build_rmpath();
		List<Integer> rmpath = dfsCode.rmpath;
		int maxtoc = dfsCode.get(rmpath.get(0)).getSourceId() > dfsCode.get(rmpath.get(0)).getTargetId()
				? dfsCode.get(rmpath.get(0)).getSourceId() : dfsCode.get(rmpath.get(0)).getTargetId();
		dfsCode.get(0).getSourceLabel();
		
		HashMap<DFSedge, Projection<PDFS>> forwardRoot = new HashMap<>();
		HashMap<DFSedge, Projection<PDFS>> backwardRoot = new HashMap<>();
		for (PDFS p : projected) {
			String vertexId = p.vertexId;
			History history = new History(p);
			/********************** Backward ***********************/
			List<Integer> reverseRmpath = new ArrayList<>();
			reverseRmpath.addAll(rmpath);
			Collections.reverse(reverseRmpath);
			for (int rmpath_i : reverseRmpath) {
				Pair<Edge,String> e = EdgeFunctions.getBackwardEdge(GraphPathParameters.graph, history.edges.get(rmpath_i), history.edges.get(rmpath.get(0)), history,dfsCode.get(rmpath_i).isOrdered(),dfsCode.get(rmpath.get(0)).isOrdered());
				if (e != null) {
					backwardRoot = AuxiliaryFunctions.addRootEdge(backwardRoot,
							new DFSedge(dfsCode.get(rmpath.get(0)).getTargetId(),
									dfsCode.get(rmpath.get(0)).getTargetLabel(),
									dfsCode.get(rmpath_i).getSourceId(),
									dfsCode.get(rmpath_i).getSourceLabel(),true),
							new PDFS(vertexId, e.getValue0(), p));
				}
			}			
			if (numVertices >= GraphPathParameters.maxsize)
				continue;
			/********************** Pure forward **********************/
			List<Edge> edges = EdgeFunctions.getForwardPureEdge(GraphPathParameters.graph, history.edges.get(rmpath.get(0)), history,dfsCode.get(rmpath.get(0)).isOrdered());
			for (Edge e : edges) {
				for (String label: GraphPathParameters.graph.vertex.get(e.to))
					forwardRoot = AuxiliaryFunctions.addRootEdge(forwardRoot,
							new DFSedge(dfsCode.get(rmpath.get(0)).getTargetId(), 
									dfsCode.get(rmpath.get(0)).getTargetLabel(),
									maxtoc+1, 
									label,true),
							new PDFS(vertexId, e, p));
			}
			/**********************rmpath forward **********************/
			for (int rmpath_i : rmpath) {
				List<Edge> edgesF = EdgeFunctions.getForwardRmpathEdge(GraphPathParameters.graph, history.edges.get(rmpath_i), history,dfsCode.get(rmpath_i).isOrdered());
				for (Edge e : edgesF) {
					for (String label: GraphPathParameters.graph.vertex.get(e.to))
						forwardRoot =  AuxiliaryFunctions.addRootEdge(forwardRoot,
								new DFSedge(dfsCode.get(rmpath_i).getSourceId(),
										dfsCode.get(rmpath_i).getSourceLabel(),
										maxtoc+1,
										label,true),
								new PDFS(vertexId, e, p));
				}
			}
		}
		/********************** Backward **********************/
		Iterator<DFSedge> itBackward = backwardRoot.keySet().iterator();
		while (itBackward.hasNext()) {
			DFSedge key = itBackward.next();
			dfsCode.add(key);
			subGraphMiningUndirected(MiningState.checkedmotifs.get(minStr), backwardRoot.get(key));
			itBackward.remove();
			dfsCode.remove(dfsCode.size() - 1);
		}
		/********************** Forward **********************/
		Iterator<DFSedge> itForward = forwardRoot.keySet().iterator();
		while (itForward.hasNext()) {
			DFSedge key = itForward.next();
			dfsCode.add(key);
			subGraphMiningUndirected(MiningState.checkedmotifs.get(minStr), forwardRoot.get(key));
			itForward.remove();
			dfsCode.remove(dfsCode.size() - 1);
		}
	}
	
	private static void subGraphMiningDirected(int prevHits, Projection<PDFS> projected) {
		String minStr = getMinStrStr();
		/*********************************Checks minimum DFS***********************************/
		if (!checkMinimumDFScode(minStr))
			return;		
		/*********************************Checks the support***********************************/
		if (!groupupportcheck(prevHits, projected, minStr)){
			return;
		}
		if (GraphPathParameters.verbose== 1) System.out.println("Number of projections = " + projected.size());
		int numVertices = dfsCode.getNumVertices();
		dfsCode.build_rmpath();
		List<Integer> rmpath = dfsCode.rmpath;
		int maxtoc = dfsCode.get(rmpath.get(0)).getSourceId() > dfsCode.get(rmpath.get(0)).getTargetId()
				? dfsCode.get(rmpath.get(0)).getSourceId() : dfsCode.get(rmpath.get(0)).getTargetId();
		dfsCode.get(0).getSourceLabel();
		
		HashMap<DFSedge, Projection<PDFS>> selfRoot = new HashMap<>();
		HashMap<DFSedge, Projection<PDFS>> forwardRoot = new HashMap<>();
		HashMap<DFSedge, Projection<PDFS>> forwardRootRmpth = new HashMap<>();
		HashMap<DFSedge, Projection<PDFS>> forwardRootIncoming = new HashMap<>();
		HashMap<DFSedge, Projection<PDFS>> forwardRmpthRootIncoming = new HashMap<>();
		HashMap<DFSedge, Projection<PDFS>> backwardRoot = new HashMap<>();
		for (PDFS p : projected) {
			String vertexId = p.vertexId;
			History history = new History(p);
			/****** Self Edge ******/
			Edge selfEdge = EdgeFunctions.getSelfEdge(GraphPathParameters.graph, history.edges.get(rmpath.get(0)), history,dfsCode.get(rmpath.get(0)).isOrdered());
			if (selfEdge != null) {
				selfRoot = AuxiliaryFunctions.addRootEdge(selfRoot, dfsCode.get(rmpath.get(0)).isOrdered() ?
						new DFSedge(dfsCode.get(rmpath.get(0)).getTargetId(),  dfsCode.get(rmpath.get(0)).getTargetLabel(), dfsCode.get(rmpath.get(0)).getTargetId(), dfsCode.get(rmpath.get(0)).getTargetLabel()):
							new DFSedge(dfsCode.get(rmpath.get(0)).getSourceId(),  dfsCode.get(rmpath.get(0)).getSourceLabel(), dfsCode.get(rmpath.get(0)).getSourceId(), dfsCode.get(rmpath.get(0)).getSourceLabel()), 
						new PDFS(vertexId, selfEdge, p));
			}
			/********************** Backward ***********************/
			List<Integer> reverseRmpath = new ArrayList<>();
			reverseRmpath.addAll(rmpath);
			Collections.reverse(reverseRmpath);
			for (int rmpath_i : reverseRmpath) {
				Pair<Edge, String> e = EdgeFunctions.getBackwardEdge(GraphPathParameters.graph, history.edges.get(rmpath_i), history.edges.get(rmpath.get(0)), history,dfsCode.get(rmpath_i).isOrdered(),dfsCode.get(rmpath.get(0)).isOrdered());
				if (e != null) {
					DFSedge edge = null;
					if (e.getValue1().equals("outgoing")){
						if (dfsCode.get(rmpath_i).isOrdered() && dfsCode.get(rmpath.get(0)).isOrdered()){
							edge = new DFSedge(dfsCode.get(rmpath.get(0)).getTargetId(), dfsCode.get(rmpath.get(0)).getTargetLabel(),
									dfsCode.get(rmpath_i).getSourceId(), dfsCode.get(rmpath_i).getSourceLabel(), false, true);
						}else if (dfsCode.get(rmpath_i).isOrdered()){
							edge = new DFSedge(dfsCode.get(rmpath.get(0)).getSourceId(), dfsCode.get(rmpath.get(0)).getSourceLabel(),
									dfsCode.get(rmpath_i).getSourceId(), dfsCode.get(rmpath_i).getSourceLabel(), false, true);
						}else if (dfsCode.get(rmpath.get(0)).isOrdered()){
							edge = new DFSedge(dfsCode.get(rmpath.get(0)).getTargetId(), dfsCode.get(rmpath.get(0)).getTargetLabel(),
									dfsCode.get(rmpath_i).getTargetId(), dfsCode.get(rmpath_i).getTargetLabel(), false, true);
						}else{
							edge = new DFSedge(dfsCode.get(rmpath.get(0)).getSourceId(), dfsCode.get(rmpath.get(0)).getSourceLabel(),
									dfsCode.get(rmpath_i).getTargetId(), dfsCode.get(rmpath_i).getTargetLabel(), false, true);
						}
						
					}else{
						if (dfsCode.get(rmpath_i).isOrdered() && dfsCode.get(rmpath.get(0)).isOrdered()){
							edge = new DFSedge(dfsCode.get(rmpath_i).getSourceId(), dfsCode.get(rmpath_i).getSourceLabel(), 
									dfsCode.get(rmpath.get(0)).getTargetId(), dfsCode.get(rmpath.get(0)).getTargetLabel(), true, true);
						}else if (dfsCode.get(rmpath_i).isOrdered()){
							edge = new DFSedge(dfsCode.get(rmpath_i).getSourceId(), dfsCode.get(rmpath_i).getSourceLabel(), 
									dfsCode.get(rmpath.get(0)).getSourceId(), dfsCode.get(rmpath.get(0)).getSourceLabel(), true, true);
						}else if (dfsCode.get(rmpath.get(0)).isOrdered()){
							edge = new DFSedge(dfsCode.get(rmpath_i).getTargetId(), dfsCode.get(rmpath_i).getTargetLabel(), 
									dfsCode.get(rmpath.get(0)).getTargetId(), dfsCode.get(rmpath.get(0)).getTargetLabel(), true, true);
						}else{
							edge = new DFSedge(dfsCode.get(rmpath_i).getTargetId(), dfsCode.get(rmpath_i).getTargetLabel(), 
									dfsCode.get(rmpath.get(0)).getSourceId(), dfsCode.get(rmpath.get(0)).getSourceLabel(), true, true);
						}
					}
					backwardRoot = AuxiliaryFunctions.addRootEdge(backwardRoot, edge, new PDFS(vertexId, e.getValue0(), p));
				}
			}			
			if (numVertices >= GraphPathParameters.maxsize)
				continue;
			/********************** Pure forward **********************/
			List<Edge> edges = EdgeFunctions.getForwardPureEdge(GraphPathParameters.graph, history.edges.get(rmpath.get(0)), history,dfsCode.get(rmpath.get(0)).isOrdered());
			for (Edge e : edges) {				
				for (String label: GraphPathParameters.graph.vertex.get(e.to)){
					forwardRoot = AuxiliaryFunctions.addRootEdge(forwardRoot, dfsCode.get(rmpath.get(0)).isOrdered() ?
							new DFSedge(dfsCode.get(rmpath.get(0)).getTargetId(), dfsCode.get(rmpath.get(0)).getTargetLabel(), maxtoc+1, label,true):
								new DFSedge(dfsCode.get(rmpath.get(0)).getSourceId(), dfsCode.get(rmpath.get(0)).getSourceLabel(), maxtoc+1, label,true),
							new PDFS(vertexId, e, p));
				}
			}
			/********************** Pure forward incoming**********************/
			List<Edge> edgesIncoming = EdgeFunctions.getForwardPureIncomingEdge(GraphPathParameters.graph, history.edges.get(rmpath.get(0)), history,dfsCode.get(rmpath.get(0)).isOrdered());
			for (Edge e : edgesIncoming) {	
				for (String label: GraphPathParameters.graph.vertex.get(e.from))
					forwardRootIncoming = AuxiliaryFunctions.addRootEdge(forwardRootIncoming, dfsCode.get(rmpath.get(0)).isOrdered() ?
							new DFSedge(maxtoc+1, label, dfsCode.get(rmpath.get(0)).getTargetId(), dfsCode.get(rmpath.get(0)).getTargetLabel(),false):
								new DFSedge(maxtoc+1, label, dfsCode.get(rmpath.get(0)).getSourceId(), dfsCode.get(rmpath.get(0)).getSourceLabel(),false),
							new PDFS(vertexId, e, p));
			}
			/**********************rmpath forward **********************/
			for (int rmpath_i : rmpath) {
				List<Edge> edgesF = EdgeFunctions.getForwardRmpathEdge(GraphPathParameters.graph, history.edges.get(rmpath_i), history,dfsCode.get(rmpath_i).isOrdered());
				for (Edge e : edgesF) 
					for (String label: GraphPathParameters.graph.vertex.get(e.to))
						forwardRootRmpth =  AuxiliaryFunctions.addRootEdge(forwardRootRmpth, dfsCode.get(rmpath_i).isOrdered() ?
								new DFSedge(dfsCode.get(rmpath_i).getSourceId(), dfsCode.get(rmpath_i).getSourceLabel(), maxtoc+1, label,true):
									new DFSedge(dfsCode.get(rmpath_i).getTargetId(), dfsCode.get(rmpath_i).getTargetLabel(), maxtoc+1, label,true),
								new PDFS(vertexId, e, p));
			}			
			/**********************rmpath forward incoming**********************/
			for (int rmpath_i : rmpath) {
				List<Edge> edgesRMPIncoming = EdgeFunctions.getForwardRmpathIncomingEdge(GraphPathParameters.graph, history.edges.get(rmpath_i), history,dfsCode.get(rmpath_i).isOrdered());
				for (Edge e : edgesRMPIncoming) {
					for (String label: GraphPathParameters.graph.vertex.get(e.from))
						forwardRmpthRootIncoming = AuxiliaryFunctions.addRootEdge(forwardRmpthRootIncoming, dfsCode.get(rmpath_i).isOrdered() ?
								new DFSedge(maxtoc+1, label, dfsCode.get(rmpath_i).getSourceId(), dfsCode.get(rmpath_i).getSourceLabel(),false):
								new DFSedge(maxtoc+1, label, dfsCode.get(rmpath_i).getTargetId(), dfsCode.get(rmpath_i).getTargetLabel(),false),
								new PDFS(vertexId, e, p));
				}
			}
		}
		/********************** Self Edge **********************/
		if (!selfRoot.isEmpty()){
			Iterator<DFSedge> itSelf = selfRoot.keySet().iterator();
			while (itSelf.hasNext()) {
				DFSedge key = itSelf.next();
				dfsCode.add(key);
				subGraphMiningDirected(MiningState.checkedmotifs.get(minStr), selfRoot.get(key));
				itSelf.remove();
				dfsCode.remove(dfsCode.size() - 1);
			}
		}
		/********************** Backward **********************/
		Iterator<DFSedge> itBackward = backwardRoot.keySet().iterator();
		while (itBackward.hasNext()) {
			DFSedge key = itBackward.next();
			dfsCode.add(key);
			subGraphMiningDirected(MiningState.checkedmotifs.get(minStr), backwardRoot.get(key));
			itBackward.remove();
			dfsCode.remove(dfsCode.size() - 1);
		}
		/********************** Forward **********************/
		Iterator<DFSedge> itForward = forwardRoot.keySet().iterator();
		while (itForward.hasNext()) {			
			DFSedge key = itForward.next();
			dfsCode.add(key);
			subGraphMiningDirected(MiningState.checkedmotifs.get(minStr), forwardRoot.get(key));
			itForward.remove();
			dfsCode.remove(dfsCode.size() - 1);
		}
		/********************** Incoming **********************/
		Iterator<DFSedge> itForwardIncoming = forwardRootIncoming.keySet().iterator();
		while (itForwardIncoming.hasNext()) {
			DFSedge key = itForwardIncoming.next();			
			dfsCode.add(key);
			subGraphMiningDirected(MiningState.checkedmotifs.get(minStr), forwardRootIncoming.get(key));
			itForwardIncoming.remove();
			dfsCode.remove(dfsCode.size() - 1);
		}
		/********************** Forward RMPTH **********************/
		Iterator<DFSedge> itForwardRmpth = forwardRootRmpth.keySet().iterator();
		while (itForwardRmpth.hasNext()) {
			DFSedge key = itForwardRmpth.next();
			dfsCode.add(key);
			subGraphMiningDirected(MiningState.checkedmotifs.get(minStr), forwardRootRmpth.get(key));
			itForwardRmpth.remove();
			dfsCode.remove(dfsCode.size() - 1);
		}
		/********************** Incoming RMPTH**********************/
		Iterator<DFSedge> itForwardIncomingRmpth = forwardRmpthRootIncoming.keySet().iterator();
		while (itForwardIncomingRmpth.hasNext()) {
			DFSedge key = itForwardIncomingRmpth.next();			
			dfsCode.add(key);
			subGraphMiningDirected(MiningState.checkedmotifs.get(minStr), forwardRmpthRootIncoming.get(key));
			itForwardIncomingRmpth.remove();
			dfsCode.remove(dfsCode.size() - 1);
		}
	}
	
	/*check the support of the pair of values given that it is gonna prune the edges with no required support*/
	private static void generate1edgeFrequentGraphs() {
		HashSet<DFSedge> vvlbEdges = new HashSet<>();
		for (String vertex: GraphPathParameters.graph.getAllNodes(GraphPathParameters.graph)){		
			for (String vertex2: GraphPathParameters.graph.getAllEdges(vertex,GraphPathParameters.graph)){
				if (GraphPathParameters.undirected == 1 && vertex.equals(vertex2))
					continue;
				if (GraphPathParameters.singleLabel == 1){
					String lblVertex = GraphPathParameters.graph.vertexOneLabel.get(vertex);
					String lbl2Vertex = GraphPathParameters.graph.vertexOneLabel.get(vertex2);
					vvlbEdges.add(new DFSedge(1, lblVertex, vertex.equals(vertex2) ? 1 : 2, lbl2Vertex, true));
				}else{
					for (String lbl1: GraphPathParameters.graph.vertex.get(vertex)){
						for (String lbl2: GraphPathParameters.graph.vertex.get(vertex2)){
							if (GraphPathParameters.undirected == 0 ){
								vvlbEdges.add(new DFSedge(1, lbl1, vertex.equals(vertex2) ? 1 : 2, vertex.equals(vertex2) ? lbl1 : lbl2, true));
							}else{
								vvlbEdges.add(new DFSedge(1, lbl1, 2, lbl2,true));
							}
						}
					}
				}
			}
			if (GraphPathParameters.graph.reverseEdgeHash.containsKey(vertex)) {					
				for (String vertexNeighbor: GraphPathParameters.graph.reverseEdgeHash.get(vertex)){					
					if (vertex.equals(vertexNeighbor))
						continue;
					if (GraphPathParameters.singleLabel == 1){
						String lblVertex = GraphPathParameters.graph.vertexOneLabel.get(vertex);
						String lbl2Vertex = GraphPathParameters.graph.vertexOneLabel.get(vertexNeighbor);
						if (GraphPathParameters.undirected == 1)
							vvlbEdges.add(new DFSedge(1, lblVertex, 2, lbl2Vertex, true));
						else
							vvlbEdges.add(new DFSedge(2, lbl2Vertex, 1, lblVertex, false));
					}else{
						for (String lbl1: GraphPathParameters.graph.vertex.get(vertex)){
							for (String lbl2: GraphPathParameters.graph.vertex.get(vertexNeighbor)){
								if (GraphPathParameters.undirected == 1)
									vvlbEdges.add(new DFSedge(1, lbl1, 2, lbl2, true));
								else
									vvlbEdges.add(new DFSedge(2, lbl2, 1, lbl1, false));
							}
						}
					}					
				}
			}
		}
		Iterator<DFSedge> it = vvlbEdges.iterator();
		while (it.hasNext()) {
			DFSedge edge = it.next();
			DFScode<DFSedge> dfsCod = new DFScode<>();
			dfsCod.add(edge);
			firstFrequentGraphs.add(dfsCod);
		}
	}
	
	private static int calculateGroupSupport(HashSet<String> valueSet){
		valueSet.retainAll(GraphPathParameters.graph.group);
		return valueSet.size();
	}
	
	private static boolean groupupportcheck(int prevHits, Projection<PDFS> projected, String minDfScode){
		/*********************************Measures the support***********************************/
		HashSet<String> support = AuxiliaryFunctions.getTotalSupport(projected);
		int totalSupport = support.size();
		int groupSupport = calculateGroupSupport(support);			
		MiningState.checkedmotifs.put(minDfScode, groupSupport);
		if (groupSupport < GraphPathParameters.supportcutoff)
			return false;
		else{
			MiningState.freqmotifs.put(minDfScode, totalSupport);
			double prob = AlgorithmUtility.getProbability(prevHits, GraphPathParameters.graph.group.size(), totalSupport, groupSupport);
			if (GraphPathParameters.verbose == 1)
				System.out.println(minDfScode + "\t" + groupSupport + "\t" + totalSupport + "\t" + prob);
			MiningState.sigmotifs.put(minDfScode, prob);
			return true;
		}
		/****************************************************************************************/
	}
	
	private static String getMinStrStr(){
		DFScode<DFSedge> minDFS = new DFScode<>();
		minDFS = dfsCode.getMinDfsCode();
		return minDFS.dfsCodeToString();		
	}
	
	private static boolean checkMinimumDFScode(String minStr){		
		if (!dfsCode.dfsCodeToString().equals(minStr))
			return false;
		else{
			if(minDFScodes.contains(minStr)){
				if (GraphPathParameters.verbose == 1) System.out.println(dfsCode.dfsCodeToString()+ " already saved with min: " + minStr);
				return false;
			}else{
				if (GraphPathParameters.verbose == 1) System.out.println(dfsCode.dfsCodeToString() + " saved with min DFS: " + minStr);
				minDFScodes.add(minStr);
			}
			return true;
		}
	}
	
	private static HashMap<DFSedge, Projection<PDFS>> getFirstProjections(){
		HashMap<DFSedge, Projection<PDFS>> root = new HashMap<>();
		for (String node: GraphPathParameters.graph.bgnodes){
			for (String edge : GraphPathParameters.graph.getAllEdges(node,GraphPathParameters.graph)) {
				Edge newEdge = new Edge(node, edge);
				PDFS pdfs = new PDFS(node, newEdge, null);	
				if (GraphPathParameters.singleLabel == 1){
					DFSedge edgeDFS =  GraphPathParameters.undirected == 1 ? 
							new DFSedge(1, GraphPathParameters.graph.vertexOneLabel.get(node), 2, GraphPathParameters.graph.vertexOneLabel.get(edge),true) : 
								new DFSedge(1, GraphPathParameters.graph.vertexOneLabel.get(node), (node.equals(edge) ? 1: 2), GraphPathParameters.graph.vertexOneLabel.get(edge), true);
					root = AuxiliaryFunctions.addRootEdge(root, edgeDFS, pdfs);
				}else{
					for (String lbl1: GraphPathParameters.graph.vertex.get(node)){
						for (String lbl2: GraphPathParameters.graph.vertex.get(edge)){
							DFSedge edgeDFS =  GraphPathParameters.undirected == 1 ? 
									new DFSedge(1, lbl1, 2, lbl2,true) : 
										new DFSedge(1, lbl1, (node.equals(edge) ? 1: 2), lbl2, true);
							root = AuxiliaryFunctions.addRootEdge(root, edgeDFS, pdfs);
						}
					}
				}
			}
			if (GraphPathParameters.undirected == 0){
				for (String edge : GraphPathParameters.graph.getIncomingEdges(node,GraphPathParameters.graph)) {
					if (node.equals(edge))
						continue;
					if (!node.equals(edge)){
						Edge newEdge = new Edge(edge, node);
						PDFS pdfs = new PDFS(node, newEdge, null);
						if (GraphPathParameters.singleLabel == 1){
							DFSedge edgeDFS =  new DFSedge(2, GraphPathParameters.graph.vertexOneLabel.get(edge), 1, GraphPathParameters.graph.vertexOneLabel.get(node), false);
							root = AuxiliaryFunctions.addRootEdge(root, edgeDFS, pdfs);
						}else{
							for (String lbl1: GraphPathParameters.graph.vertex.get(node)){
								for (String lbl2: GraphPathParameters.graph.vertex.get(edge)){
									DFSedge edgeDFS =  new DFSedge(2, lbl2, 1, lbl1, false);
									root = AuxiliaryFunctions.addRootEdge(root, edgeDFS, pdfs);
								}
							}
						}
					}
				}
			}
		}
		return root;
	}
	
	private static void miningCall(Projection<PDFS> projected){
		if (GraphPathParameters.undirected ==1)
			subGraphMiningUndirected(GraphPathParameters.graph.bgnodes.size(), projected);
		else
			subGraphMiningDirected(GraphPathParameters.graph.bgnodes.size(), projected);
	}
	

}