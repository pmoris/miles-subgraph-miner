package com.uantwerp.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.procedures.fsg.VertexInvariant;

public class VertexInvariantTest {

	@Test
	public void TestUndirectedRepresentation(){
		GraphPathParameters.undirected = 1;
		DFScode<DFSedge> code = new DFScode<>();
		code.add(new DFSedge(1, "S", 2, "S"));
		code.add(new DFSedge(1, "S", 3, "C"));
		code.add(new DFSedge(2, "S", 4, "C"));
		code.add(new DFSedge(3, "C", 5, "C"));	
		
		VertexInvariant matrix = new VertexInvariant(code.dfsCodeToGraph());
		String rep = matrix.getRepresentation();
		assertEquals("1S-4C,1S-5S,2C-4C,3C-5S",rep);
		
		DFScode<DFSedge> code2 = new DFScode<>();
		code2.add(new DFSedge(1, "S", 2, "S"));
		code2.add(new DFSedge(1, "S", 3, "C"));
		code2.add(new DFSedge(2, "S", 4, "C"));
		code2.add(new DFSedge(3, "C", 6, "C"));
		code2.add(new DFSedge(4, "C", 5, "C"));
		code2.add(new DFSedge(4, "C", 7, "S"));
		code2.add(new DFSedge(4, "C", 8, "C"));
		
		VertexInvariant matrix2 = new VertexInvariant(code2.dfsCodeToGraph());
		String rep2 = matrix2.getRepresentation();
		assertEquals("1S-6C,1S-7S,2C-6C,3C-8C,4C-8C,5S-8C,7S-8C",rep2);
		
		DFScode<DFSedge> code6 = new DFScode<>();
		code6.add(new DFSedge(1, "S", 2, "C"));		
		VertexInvariant matrix6 = new VertexInvariant(code6.dfsCodeToGraph());
		String rep6 = matrix6.getRepresentation();
		assertEquals("1S-2C",rep6);
		
		DFScode<DFSedge> code7 = new DFScode<>();
		code7.add(new DFSedge(2, "C", 1, "S"));		
		VertexInvariant matrix7 = new VertexInvariant(code7.dfsCodeToGraph());
		String rep7 = matrix7.getRepresentation();
		assertEquals("1S-2C",rep7);
		
	}
	
	@Test
	public void TestDirectedRepresentation(){
		GraphPathParameters.undirected = 0;
		DFScode<DFSedge> code3 = new DFScode<>();
		code3.add(new DFSedge(1, "C", 2, "S"));
		code3.add(new DFSedge(1, "C", 3, "S"));
		VertexInvariant matrix3 = new VertexInvariant(code3.dfsCodeToGraph());
		String rep3 = matrix3.getRepresentation();
		assertEquals("1C-2S,1C-3S",rep3);
		
		DFScode<DFSedge> code4 = new DFScode<>();
		code4.add(new DFSedge(2, "C", 1, "C"));
		code4.add(new DFSedge(1, "C", 3, "S"));
		VertexInvariant matrix4 = new VertexInvariant(code4.dfsCodeToGraph());
		String rep4 = matrix4.getRepresentation();
		assertEquals("1C-3S,2C-1C",rep4);
		
		DFScode<DFSedge> code5 = new DFScode<>();
		code5.add(new DFSedge(2, "C", 1, "C"));
		VertexInvariant matrix5 = new VertexInvariant(code5.dfsCodeToGraph());
		String rep5 = matrix5.getRepresentation();
		assertEquals("2C-1C",rep5);
		
		DFScode<DFSedge> code1 = new DFScode<>();
		code1.add(new DFSedge(1, "C", 2, "S"));
		code1.add(new DFSedge(3, "S", 1, "C"));
		VertexInvariant matrix1 = new VertexInvariant(code1.dfsCodeToGraph());
		String rep1 = matrix1.getRepresentation();
		assertEquals("1C-2S,3S-1C",rep1);
		
		DFScode<DFSedge> code2 = new DFScode<>();
		code2.add(new DFSedge(2, "S", 1, "C"));
		code2.add(new DFSedge(1, "C", 3, "S"));
		VertexInvariant matrix2 = new VertexInvariant(code2.dfsCodeToGraph());
		String rep2 = matrix2.getRepresentation();
		assertEquals("1C-2S,3S-1C",rep2);
	}
}
