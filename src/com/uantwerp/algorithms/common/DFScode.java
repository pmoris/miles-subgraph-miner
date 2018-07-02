package com.uantwerp.algorithms.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.javatuples.Pair;
import org.javatuples.Quintet;

import com.uantwerp.algorithms.procedures.gspan.AuxiliaryFunctions;
import com.uantwerp.algorithms.procedures.gspan.EdgeFunctions;

/*the class is a list of dfs edge*/
public class DFScode<E> extends ArrayList<E> {

	private static final long serialVersionUID = -3276835482198069067L;

	public List<Integer> rmpath;
	
	public DFScode<DFSedge> getMinDfsCode() {
		if (GraphPathParameters.verbose == 1 && GraphPathParameters.undirected == 1) System.out.println(this.dfsCodeToString() + " checking by normal root");
		Graph g = this.dfsCodeToGraph();
		DFScode<DFSedge> DFScode_min = new DFScode<>();
		HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> root = getFirstCentricProjection(g);
		Quintet<Integer, String, Integer, String,Boolean> min = minEdgeQ(root.keySet());
		DFScode_min.add(new DFSedge(min.getValue0(),min.getValue1(),min.getValue2(),min.getValue3(),min.getValue4()));
		if (GraphPathParameters.undirected==0)
			return project_get_min_directed(g, DFScode_min, root.get(min));
		else
			return project_get_min_undirected(g, DFScode_min, root.get(min));
	}
	
	private DFScode<DFSedge> project_get_min_undirected(Graph g, DFScode<DFSedge> DFScode_min, Projection<PDFS> projected) {
		DFScode_min.build_rmpath();
		List<Integer> rmpath = DFScode_min.rmpath;
		int maxtoc = DFScode_min.get(rmpath.get(0)).getSourceId() > DFScode_min.get(rmpath.get(0)).getTargetId()
				? DFScode_min.get(rmpath.get(0)).getSourceId() : DFScode_min.get(rmpath.get(0)).getTargetId();
		String minVlb = DFScode_min.get(0).getSourceLabel();
		boolean flag = false;
		/************************ Backward Edges *************************/
		HashMap<PairStrValues, Projection<PDFS>> backwardRoot = new HashMap<>();
		
		int newTo = 0;
		String lbFromBack = "";
		String lbToBack = "";
		for (int i = rmpath.size() - 1; i >= 0; i--) {
			if (flag)
				break;
			for (PDFS p : projected) {
				History history = new History(p);
				Pair<Edge, String> e = EdgeFunctions.getBackwardEdge(g, history.edges.get(rmpath.get(i)),
						history.edges.get(rmpath.get(0)), history, DFScode_min.get(rmpath.get(i)).isOrdered(), DFScode_min.get(rmpath.get(0)).isOrdered());
				if (e != null) {
					lbFromBack = g.vertexOneLabel.get(e.getValue0().from);
					lbToBack = g.vertexOneLabel.get(e.getValue0().to);
					backwardRoot = AuxiliaryFunctions.addRoot(backwardRoot, new PairStrValues("-", "-"),
							new PDFS(p.vertexId, e.getValue0(), p));
					newTo = DFScode_min.get(rmpath.get(i)).getSourceId();
					flag = true;
				}
			}
		}
		if (flag) {
			DFScode_min.add(new DFSedge(maxtoc, lbFromBack, newTo, lbToBack,true));
			return project_get_min_undirected(g, DFScode_min, backwardRoot.get(new PairStrValues("-", "-")));
		}
		/********************** Pure Fordward Edges **********************/
		String origenLabel = "";
		HashMap<PairStrValues, Projection<PDFS>> forwardRoot = new HashMap<>();
		flag = false;
		int newfrm = 0;
		for (PDFS p : projected) {
			History history = new History(p);
			List<Edge> edges = g.getForwardPureEdge(g, history.edges.get(rmpath.get(0)), minVlb, history, DFScode_min.get(rmpath.get(0)).isOrdered());
			if (edges.size() > 0) {
				origenLabel = g.vertexOneLabel.get(history.edges.get(rmpath.get(0)).to);
				flag = true;
				newfrm = maxtoc;
				for (Edge e : edges) {
					forwardRoot = AuxiliaryFunctions.addRoot(forwardRoot,
							new PairStrValues("-", g.vertexOneLabel.get(e.to)), new PDFS(p.vertexId, e, p));
				}
			}
		}
		/*************************** RMPAHT Edges ************************/
		for (int rmpath_i : rmpath) {
			if (flag)
				break;
			for (PDFS p : projected) {
				History history = new History(p);
				List<Edge> edges = g.getForwardRmpathEdge(g, history.edges.get(rmpath_i), minVlb, history, DFScode_min.get(rmpath_i).isOrdered());
				if (edges.size() > 0) {
					flag = true;
					origenLabel = DFScode_min.get(rmpath_i).getSourceLabel();
					newfrm = DFScode_min.get(rmpath_i).getSourceId();
					for (Edge e : edges) {
						forwardRoot = AuxiliaryFunctions.addRoot(forwardRoot,
								new PairStrValues("-", g.vertexOneLabel.get(e.to)), new PDFS(p.vertexId, e, p));
					}
				}
			}
		}
		if (!flag)
			return DFScode_min;
		PairStrValues fordwardMinLb = minEdge(forwardRoot.keySet());
		DFScode_min.add(new DFSedge(newfrm, origenLabel, maxtoc + 1, fordwardMinLb.val2,true));
		return project_get_min_undirected(g, DFScode_min, forwardRoot.get(fordwardMinLb));

	}

	private DFScode<DFSedge> project_get_min_directed(Graph g, DFScode<DFSedge> DFScode_min, Projection<PDFS> projected) {
		DFScode_min.build_rmpath();
		List<Integer> rmpath = DFScode_min.rmpath;
		int maxtoc = DFScode_min.get(rmpath.get(0)).getSourceId() > DFScode_min.get(rmpath.get(0)).getTargetId()
				? DFScode_min.get(rmpath.get(0)).getSourceId() : DFScode_min.get(rmpath.get(0)).getTargetId();
		/**************************** Self Edge *************************/
		boolean flag = false;
		String labelSelf = "";
		HashMap<PairStrValues, Projection<PDFS>> selfRoot = new HashMap<>();
		for (PDFS p : projected) {		
			History history = new History(p);
			Edge selfEdge = new Edge();
			selfEdge = EdgeFunctions.getSelfEdge(g, history.edges.get(rmpath.get(0)), history, DFScode_min.get(rmpath.get(0)).isOrdered());
			if (selfEdge != null) {
				labelSelf = g.vertexOneLabel.get(selfEdge.from);
				selfRoot = AuxiliaryFunctions.addRoot(selfRoot, new PairStrValues(labelSelf, labelSelf), new PDFS(p.vertexId, selfEdge, p));
				flag = true;
			}
		}
		if (flag) {
			DFScode_min.add(new DFSedge(maxtoc, labelSelf, maxtoc, labelSelf,true));
			return project_get_min_directed(g, DFScode_min, selfRoot.get(new PairStrValues(labelSelf, labelSelf)));
		}
		/************************ Backward Edges *************************/
		HashMap<PairStrValues, Projection<PDFS>> backwardRoot = new HashMap<>();
		int newTo = 0;
		String lbFromBack = "";
		String lbToBack = "";
		String direction = "";
		for (int i = rmpath.size() - 1; i >= 0; i--) {
			if (flag)
				break;
			for (PDFS p : projected) {
				History history = new History(p);
				Pair<Edge, String> e = EdgeFunctions.getBackwardEdge(g, history.edges.get(rmpath.get(i)),
						history.edges.get(rmpath.get(0)), history, DFScode_min.get(rmpath.get(i)).isOrdered(), DFScode_min.get(rmpath.get(0)).isOrdered());
				if (e != null) {
					lbFromBack = g.vertexOneLabel.get(e.getValue0().from);
					lbToBack = g.vertexOneLabel.get(e.getValue0().to);
					backwardRoot = AuxiliaryFunctions.addRoot(backwardRoot, new PairStrValues("-", "-"),
							new PDFS(p.vertexId, e.getValue0(), p));
					if (DFScode_min.get(rmpath.get(i)).isOrdered())
						newTo = DFScode_min.get(rmpath.get(i)).getSourceId();
					else
						newTo = DFScode_min.get(rmpath.get(i)).getTargetId();
					direction = e.getValue1();
					flag = true;
				}
			}
		}
		if (flag) {
			DFScode_min.add(new DFSedge(direction.equals("outgoing")? maxtoc: newTo, lbFromBack, direction.equals("outgoing")? newTo: maxtoc, lbToBack, true, true));
			return project_get_min_directed(g, DFScode_min, backwardRoot.get(new PairStrValues("-", "-")));
		}
		/********************** Pure Fordward Edges **********************/
		String origenLabel = "";
		HashMap<PairStrValues, Projection<PDFS>> forwardRoot = new HashMap<>();
		flag = false;
		int newfrm = 0;
		for (PDFS p : projected) {
			History history = new History(p);
			List<Edge> edges = g.getForwardPureEdge(g, history.edges.get(rmpath.get(0)), "", history, DFScode_min.get(rmpath.get(0)).isOrdered());
			if (edges.size() > 0) {
				if (DFScode_min.get(rmpath.get(0)).isOrdered())
					origenLabel = g.vertexOneLabel.get(history.edges.get(rmpath.get(0)).to);
				else
					origenLabel = g.vertexOneLabel.get(history.edges.get(rmpath.get(0)).from);
				flag = true;
				newfrm = maxtoc;
				for (Edge e : edges) {
					forwardRoot = AuxiliaryFunctions.addRoot(forwardRoot,
							new PairStrValues("-", g.vertexOneLabel.get(e.to)), new PDFS(p.vertexId, e, p));
				}
			}
		}
		if (flag){
			PairStrValues fordwardMinLb = minEdge(forwardRoot.keySet());
			DFScode_min.add(new DFSedge(newfrm, origenLabel, maxtoc + 1, fordwardMinLb.val2,true));
			return project_get_min_directed(g, DFScode_min, forwardRoot.get(fordwardMinLb));
		}
		/**************** Pure Fordward Incoming Edges *******************/
		int newFromIncoming = 0;
		HashMap<PairStrValues, Projection<PDFS>> forwardIncomingRoot = new HashMap<>();
		flag = false;
		for (PDFS p : projected) {
			History history = new History(p);
			List<Edge> edges = EdgeFunctions.getForwardPureIncomingEdge(g, history.edges.get(rmpath.get(0)), history, DFScode_min.get(rmpath.get(0)).isOrdered());
			if (edges.size() > 0) {			
				if (DFScode_min.get(rmpath.get(0)).isOrdered())
					origenLabel = g.vertexOneLabel.get(history.edges.get(rmpath.get(0)).to);
				else
					origenLabel = g.vertexOneLabel.get(history.edges.get(rmpath.get(0)).from);
				newFromIncoming = maxtoc;
				flag = true;				
				for (Edge e : edges) {
					forwardIncomingRoot = AuxiliaryFunctions.addRoot(forwardIncomingRoot,
							new PairStrValues("-",g.vertexOneLabel.get(e.from)), new PDFS(p.vertexId, e, p));
				}
			}
		}
		if (flag){
			PairStrValues fordwardMinLbIncoming = minEdge(forwardIncomingRoot.keySet());
			DFScode_min.add(new DFSedge(maxtoc + 1, fordwardMinLbIncoming.getTolb(), newFromIncoming, origenLabel,false));
			return project_get_min_directed(g, DFScode_min, forwardIncomingRoot.get(fordwardMinLbIncoming));
		}
		/*************************** RMPAHT Edges ************************/
		int indexRMPTHForward = -1;
		for (int rmpath_i : rmpath) {
			if (indexRMPTHForward != -1)
				break;
			for (PDFS p : projected) {
				History history = new History(p);
				List<Edge> edges = g.getForwardRmpathEdge(g, history.edges.get(rmpath_i), "", history, DFScode_min.get(rmpath_i).isOrdered());
				if (edges.size() > 0) {
					indexRMPTHForward = rmpath_i;
					origenLabel = DFScode_min.get(rmpath_i).getSourceLabel();
					newfrm = DFScode_min.get(rmpath_i).getSourceId();
					for (Edge e : edges) {
						AuxiliaryFunctions.addRoot(forwardRoot,
								new PairStrValues("-", g.vertexOneLabel.get(e.to)), new PDFS(p.vertexId, e, p));
					}
				}
			}
		}
		/******************** RMPAHT Incoming Edges ***********************/
		int indexRMPTHForwardIncoming = -1;
		for (int rmpath_i : rmpath) {
			if (indexRMPTHForwardIncoming != -1)
				break;
			for (PDFS p : projected) {
				History history = new History(p);
				List<Edge> edges = EdgeFunctions.getForwardRmpathIncomingEdge(g, history.edges.get(rmpath_i), history, DFScode_min.get(rmpath_i).isOrdered());				
				if (edges.size() > 0) {
					indexRMPTHForwardIncoming = rmpath_i;
					if (DFScode_min.get(rmpath_i).isOrdered()){
						origenLabel = DFScode_min.get(rmpath_i).getSourceLabel();
						newFromIncoming = DFScode_min.get(rmpath_i).getSourceId();						
					}else{
						origenLabel = DFScode_min.get(rmpath_i).getTargetLabel();
						newFromIncoming = DFScode_min.get(rmpath_i).getTargetId();
					}
					for (Edge e : edges) {
						AuxiliaryFunctions.addRoot(forwardIncomingRoot,
								new PairStrValues("-", g.vertexOneLabel.get(e.from)), new PDFS(p.vertexId, e, p));
					}
					
				}
			}
		}
		if (indexRMPTHForward != -1 && indexRMPTHForwardIncoming != -1){
			if (indexRMPTHForward ==  indexRMPTHForwardIncoming){
				PairStrValues fordwardMinLb = minEdge(forwardRoot.keySet());
				DFScode_min.add(new DFSedge(newfrm, origenLabel, maxtoc + 1, fordwardMinLb.val2,true));
				return project_get_min_directed(g, DFScode_min, forwardRoot.get(fordwardMinLb));
			}else if (indexRMPTHForward > indexRMPTHForwardIncoming){
				PairStrValues fordwardMinLb = minEdge(forwardRoot.keySet());
				DFScode_min.add(new DFSedge(newfrm, origenLabel, maxtoc + 1, fordwardMinLb.val2,true));
				return project_get_min_directed(g, DFScode_min, forwardRoot.get(fordwardMinLb));				
			}else{
				PairStrValues fordwardMinLbIncoming = minEdge(forwardIncomingRoot.keySet());
				DFScode_min.add(new DFSedge(maxtoc + 1, fordwardMinLbIncoming.getTolb(), newFromIncoming, origenLabel,false));
				return project_get_min_directed(g, DFScode_min, forwardIncomingRoot.get(fordwardMinLbIncoming));
			}
		}else if (indexRMPTHForward != -1){
			PairStrValues fordwardMinLb = minEdge(forwardRoot.keySet());
			DFScode_min.add(new DFSedge(newfrm, origenLabel, maxtoc + 1, fordwardMinLb.val2,true));
			return project_get_min_directed(g, DFScode_min, forwardRoot.get(fordwardMinLb));
		}else if (indexRMPTHForwardIncoming != -1){
			PairStrValues fordwardMinLbIncoming = minEdge(forwardIncomingRoot.keySet());
			DFScode_min.add(new DFSedge(maxtoc + 1, fordwardMinLbIncoming.getTolb(), newFromIncoming, origenLabel,false));
			return project_get_min_directed(g, DFScode_min, forwardIncomingRoot.get(fordwardMinLbIncoming));
		}else{
			return DFScode_min;
		}
	}
	
	private HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> getFirstCentricProjection(Graph g){
		HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> root = new HashMap<>();
		HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> rootSelf = new HashMap<>();
		String vertex  = "1";
		List<String> outgoingVertices = g.getAllEdges(vertex, g);
		for (String e: outgoingVertices){
			if (!vertex.equals(e))
				root = AddEdgeRoot(1,g,root,vertex,e);
			else
				rootSelf = AddEdgeRoot(0,g,root,vertex,e);
		}
		if (GraphPathParameters.undirected==0)
			if (!rootSelf.isEmpty())
				return rootSelf;
		if (!root.isEmpty())
			return root;
		if (GraphPathParameters.undirected==0){
			List<String> incomingVertices = g.getIncomingEdges(vertex, g);
			for (String e: incomingVertices){
				if (!vertex.equals(e))
					root = AddEdgeRoot(2,g,root,vertex,e);
				else
					rootSelf = AddEdgeRoot(0,g,root,vertex,e);
			}
			if (!rootSelf.isEmpty())
				return rootSelf;
			if (!root.isEmpty())
				return root;
		}
		return null;
		
	}
	
	private HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> AddEdgeRoot(int type, Graph g, HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> root, String vertex, String e){
		PDFS pdfs;		
		Quintet<Integer, String, Integer, String, Boolean> dfsEdge;
		if (type==0){
			pdfs= new PDFS(vertex, new Edge(vertex, e), null);
			dfsEdge  = Quintet.with(1, g.vertexOneLabel.get(vertex), 1, g.vertexOneLabel.get(e),true);
		}
		else if (type==1){
			pdfs= new PDFS(vertex, new Edge(vertex, e), null);
			dfsEdge  = Quintet.with(1, g.vertexOneLabel.get(vertex), 2, g.vertexOneLabel.get(e),true);
		}
		else{
			pdfs= new PDFS(vertex, new Edge(e, vertex), null);
			dfsEdge  = Quintet.with(2, g.vertexOneLabel.get(e), 1, g.vertexOneLabel.get(vertex),false);
		}
		root = AuxiliaryFunctions.addRoot(root, dfsEdge, pdfs);
		return root;
	}
	
	public DFScode() {
		super();
	}

	public DFScode(Collection<? extends E> c) {
		super(c);
	}

	public DFScode(int initialCapacity) {
		super(initialCapacity);
	}

	private PairStrValues minEdge(Set<PairStrValues> values) {
		return Collections.min(values);
	}
	
	private Quintet<Integer, String, Integer, String, Boolean> minEdgeQ(Set<Quintet<Integer, String, Integer, String,Boolean>> values) {
		Quintet<Integer, String, Integer, String, Boolean> min = null;
		for (Quintet<Integer, String, Integer, String, Boolean> val: values){
			if (val.getValue0()==val.getValue2()){
				if (min==null){
					min = Quintet.with(val.getValue0(), val.getValue1(), val.getValue2(), val.getValue3(), val.getValue4());
				}else{
					if (val.getValue3().compareTo(min.getValue1())  < 0){
						min = Quintet.with(val.getValue0(), val.getValue1(), val.getValue2(), val.getValue3(), val.getValue4());
					}
				}
			}else if (val.getValue0() < val.getValue2()){
				if (min==null){
					min = Quintet.with(val.getValue0(), val.getValue1(), val.getValue2(), val.getValue3(), val.getValue4());
				}else{
					if (val.getValue3().compareTo(min.getValue3())  < 0){
						min = Quintet.with(val.getValue0(), val.getValue1(), val.getValue2(), val.getValue3(), val.getValue4());
					}
				}
			}else{
				if (min==null){
					min = Quintet.with(val.getValue0(), val.getValue1(), val.getValue2(), val.getValue3(), val.getValue4());
				}else{
					if (val.getValue1().compareTo(min.getValue1())  < 0){
						min = Quintet.with(val.getValue0(), val.getValue1(), val.getValue2(), val.getValue3(), val.getValue4());
					}
				}
			}
		}
		return min;
	}
	
	public int getNumVertices() {
		HashSet<Integer> vertices = new HashSet<>();
		for (int i = 0; i < this.size(); i++) {
			DFSedge edge = (DFSedge) this.get(i);
			vertices.add(edge.getSourceId());
			vertices.add(edge.getTargetId());
		}
		return vertices.size();
	}

	public String dfsCodeToString() {
		String dfsCode = "";
		for (int i = 0; i < this.size(); i++) {
			if (dfsCode.equals(""))
				dfsCode = this.get(i).toString();
			else
				dfsCode += "," + this.get(i).toString();
		}
		return dfsCode;
	}

	public int maxVertexIndexNaive() {
		int max = 1;
		for (int i = 0; i < this.size(); i++) {
			DFSedge edge = (DFSedge) this.get(i);
			if (edge.getSourceId() > max)
				max = edge.getSourceId();
			if (edge.getTargetId() > max)
				max = edge.getTargetId();
		}
		return max;
	}

	public Graph dfsCodeToGraph() {
		Graph g = new Graph();
		for (int i = 0; i < this.size(); i++) {
			DFSedge edge = (DFSedge) this.get(i);
			String sourceVertex = String.valueOf(edge.getSourceId());
			String targetVertex = String.valueOf(edge.getTargetId());
			g.addEdge(sourceVertex, targetVertex);
			g.addSingleLabel(sourceVertex, edge.getSourceLabel());
			g.addSingleLabel(targetVertex, edge.getTargetLabel());
		}
		return g;
	}

	public void build_rmpath() {
		rmpath = new ArrayList<>();
		int old_frm = -1;
		for (int i = this.size() - 1; i >= 0; i--) {
			DFSedge edge = (DFSedge) this.get(i);
			if (GraphPathParameters.undirected == 1){
				if (edge.getSourceId() < edge.getTargetId() && (old_frm == -1 || edge.getTargetId() == old_frm)) {
					rmpath.add(i);
					old_frm = edge.getSourceId();
				}
			}else{
				if (old_frm == -1 || (edge.isOrdered()? edge.getTargetId(): edge.getSourceId()) == old_frm){
					if (edge.getSourceId() == edge.getTargetId()){ // self edge
						if (this.size() == 1){
							rmpath.add(i);
							old_frm = 1;
						}
					}else if (edge.getSourceId() > edge.getTargetId() && !edge.isBackward()){
							rmpath.add(i);
							old_frm = edge.getTargetId();
					}else if (edge.getSourceId() < edge.getTargetId() && !edge.isBackward()) {
						rmpath.add(i);
						old_frm = edge.getSourceId();
					}
				}
			}
		}
	}

	public void removeEdge(DFSedge edge){
		Iterator<E> it = this.iterator();
		while (it.hasNext()){
			DFSedge edgeThis = (DFSedge) it.next();
			if (edge.equals(edgeThis)){
				it.remove();
				return;
			}
		}
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		for (int i=0; i < this.size(); i++){
			result += this.get(i).hashCode();
		}		
		return result;
	}
	
	@Override
	public String toString() {
		String elements = "";
		for (int i = 0; i < this.size(); i++) {
			if (i == 0)
				elements = this.get(i).toString();
			else
				elements += "," + this.get(i).toString();
		}
		return "DFScode [elementData=[" + elements + "], modCount=" + modCount + "]";
	}

}
