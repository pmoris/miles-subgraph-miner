package com.uantwerp.algorithms.procedures.gspan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.javatuples.Quintet;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.Edge;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.common.PDFS;
import com.uantwerp.algorithms.common.PairStrValues;
import com.uantwerp.algorithms.common.Projection;
import com.uantwerp.algorithms.exceptions.SubGraphMiningExceptionGspan;

public abstract class AuxiliaryFunctions {
	
	public static int compareDFSCode(DFScode<DFSedge> X, DFScode<DFSedge> Y){
		if (X.size() != Y.size()){
			SubGraphMiningExceptionGspan.exceptionDFSCodesDifferenSizes();
		}
		if(X.containsAll(Y)){
			return 0;
		}
		for (int i=0; i< X.size(); i++){
			boolean e1Forward = X.get(i).getSourceId() < X.get(i).getTargetId();
			boolean e2Forward = Y.get(i).getSourceId() < Y.get(i).getTargetId();
//			Check each of the following rules; if one applies, set X < Y and exit the loop
//			Is X a backward edge and Y a forward edge?
			if (!e1Forward && e2Forward){
				return -1;
//			Is X a backward edge, Y a backward edge, 
			}else if (!e1Forward && !e2Forward){
//			and the 2nd vertex of X < 2nd vertex of Y?			
				if (X.get(i).getTargetId() < Y.get(i).getTargetId()){
					return -1;
				}
//			Is X a forward edge, Y a forward edge, 
			}else if (e1Forward && e2Forward){				
//				the 1st vertex of X = the 1st vertex of Y, and the label for the first vertex of X is less than the label for the first vertex of Y?
				if (Y.get(i).getSourceId() == X.get(i).getSourceId()){
					if (X.get(i).getSourceLabel().compareTo(Y.get(i).getSourceLabel()) < 0){
							return -1;
					}
					else if (X.get(i).getSourceLabel().compareTo(Y.get(i).getSourceLabel()) == 0){
//						the 1st vertex of X = the 1st vertex of Y, the first vertex label of X = the first vertex label of Y, and the 2nd vertex label of X < the 2nd vertex label of Y?									
						if (X.get(i).getTargetLabel().compareTo(Y.get(i).getTargetLabel()) < 0){
							return -1;
						}
					}
	//				the 1st vertex of Y < the 1st vertex of X					
				}else if (Y.get(i).getSourceId() < X.get(i).getSourceId()){
					return -1;
				}
			}
		}
		return 1;
	}
	
	public static List<DFScode<DFSedge>> orderDFScode(List<DFScode<DFSedge>> codes){
		Collections.sort(codes, new Comparator<DFScode<DFSedge>>() {
            @Override
            public int compare(DFScode<DFSedge> X, DFScode<DFSedge> Y) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return compareDFSCode(X,Y);
            }
        });
		return codes;
	}
	
	public static HashMap<PairStrValues, Projection<PDFS>> addRoot(HashMap<PairStrValues, Projection<PDFS>> root, PairStrValues edgelb, PDFS pdfs){
		if (root.containsKey(edgelb)){					
			Projection<PDFS> projection = root.get(edgelb);
			projection.add(pdfs);
			root.put(edgelb,projection);
		}else{
			Projection<PDFS> projection = new Projection<>();
			projection.add(pdfs);
			root.put(edgelb,projection);
		}
		return root;
	}
	
	public static HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> addRoot(HashMap<Quintet<Integer, String, Integer, String, Boolean>, Projection<PDFS>> root, Quintet<Integer, String, Integer, String, Boolean> edgelb, PDFS pdfs){
		if (root.containsKey(edgelb)){					
			Projection<PDFS> projection = root.get(edgelb);
			projection.add(pdfs);
			root.put(edgelb,projection);
		}else{
			Projection<PDFS> projection = new Projection<>();
			projection.add(pdfs);
			root.put(edgelb,projection);
		}
		return root;
	}
	
	public static HashMap<DFSedge, Projection<PDFS>> addRootEdge(HashMap<DFSedge, Projection<PDFS>> root, DFSedge edgelb, PDFS pdfs){
		if (root.containsKey(edgelb)){					
			Projection<PDFS> projection = root.get(edgelb);
			projection.add(pdfs);
			root.put(edgelb,projection);
		}else{
			Projection<PDFS> projection = new Projection<>();
			projection.add(pdfs);
			root.put(edgelb,projection);
		}
		return root;
	}
	
	public static HashMap<DFScode<DFSedge>, Projection<PDFS>> addRootDFScode(HashMap<DFScode<DFSedge>, Projection<PDFS>> root, DFScode<DFSedge> edgelb, PDFS pdfs){
		if (root.containsKey(edgelb)){					
			Projection<PDFS> projection = root.get(edgelb);
			projection.add(pdfs);
			root.put(edgelb,projection);
		}else{
			Projection<PDFS> projection = new Projection<>();
			projection.add(pdfs);
			root.put(edgelb,projection);
		}
		return root;
	}
	
	public static HashMap<DFScode<DFSedge>, List<HashMap<Integer, String>>> addRootDFScode(HashMap<DFScode<DFSedge>, List<HashMap<Integer, String>>> root, DFScode<DFSedge> dfsCode, HashMap<Integer, String> pdfs){
		if (root.containsKey(dfsCode)){					
			List<HashMap<Integer, String>> projection = root.get(dfsCode);
			projection.add(pdfs);
			root.put(dfsCode,projection);
		}else{
			List<HashMap<Integer, String>> projection = new ArrayList<>();
			projection.add(pdfs);
			root.put(dfsCode,projection);
		}
		return root;
	}
	
	public static HashMap<String, List<HashMap<Integer, String>>> addRootString(HashMap<String, List<HashMap<Integer, String>>> root, String dfsCode, HashMap<Integer, String> pdfs){
		if (root.containsKey(dfsCode)){					
			List<HashMap<Integer, String>> projection = root.get(dfsCode);
			projection.add(pdfs);
			root.put(dfsCode,projection);
		}else{
			List<HashMap<Integer, String>> projection = new ArrayList<>();
			projection.add(pdfs);
			root.put(dfsCode,projection);
		}
		return root;
	}
	
	public static PairStrValues orderEdgesFirstIt(String sourceLabel, String targetLabel){
		if (GraphPathParameters.undirected == 1){
			if (sourceLabel.compareTo(targetLabel) > 0){ // if the label of the first node is greater than the label of the target node							
				return null;				
			}else{
				return new PairStrValues(sourceLabel, targetLabel);
			}
		}else
			return new PairStrValues(sourceLabel, targetLabel);
	}
	
	public static HashSet<String> getTotalSupport(Projection<PDFS> projected){
		HashSet<String> vertices = new HashSet<>();
		if (projected != null){			
			for (int i=0; i < projected.size(); i++){
				vertices.add(projected.get(i).vertexId);
			}
		}
		return vertices;
	}
	
	public static Projection<PDFS> getInvertex(HashMap<Edge, String> compValues, Projection<PDFS> projected){
		Projection<PDFS> newProj = new Projection<>();
		for (PDFS p: projected){
			PDFS newP = new PDFS(compValues.get(p.current), p.current, p.prev);
			newProj.add(newP);
		}
		return newProj;
	}
	
	public static String refactorDFScodeInv(DFScode<DFSedge> dfsCode){
		DFScode<DFSedge> newDFS = new DFScode<>(dfsCode.size());
		
		for (int i = 0; i < dfsCode.size(); i++){
			if (i==0)
				if (GraphPathParameters.undirected == 1){
					newDFS.add(new DFSedge(1, dfsCode.get(i).getTargetLabel(), 2, dfsCode.get(i).getSourceLabel(),true));
				}else{
					newDFS.add(new DFSedge(2, dfsCode.get(i).getSourceLabel(), 1, dfsCode.get(i).getTargetLabel(),true));
				}
			else{
				DFSedge edge = dfsCode.get(i);
				int source = 0;
				int target = 0;
				if (edge.getSourceId() == 1){
					source =  2;
				}else if (edge.getSourceId() == 2){
					source =  1;
				}else{
					source = edge.getSourceId();
				}
				if (edge.getTargetId() == 1){
					target = 2;
				}else if (edge.getTargetId() == 2){
					target = 1;
				}else{
					target = edge.getTargetId();
				}
				newDFS.add(new DFSedge(source, edge.getSourceLabel(), target, edge.getTargetLabel(),true));
			}
		}	

		return newDFS.dfsCodeToString();
	}
	
}