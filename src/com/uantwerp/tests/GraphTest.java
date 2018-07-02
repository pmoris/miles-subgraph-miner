package com.uantwerp.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;

public class GraphTest {

	@Test
	public void TestIsConnectedGraphUndirected(){
		GraphPathParameters.undirected = 1;
		DFScode<DFSedge> code = new DFScode<>();
		code.add(new DFSedge(1, "S", 2, "S"));
		code.add(new DFSedge(1, "S", 3, "C"));
		code.add(new DFSedge(2, "S", 4, "C"));
		code.add(new DFSedge(5, "C", 3, "C"));
		assertTrue(code.dfsCodeToGraph().isConnected());
		
		DFScode<DFSedge> code2 = new DFScode<>();
		code2.add(new DFSedge(1, "S", 2, "S"));
		code2.add(new DFSedge(1, "S", 3, "C"));
		code2.add(new DFSedge(2, "S", 4, "C"));
		code2.add(new DFSedge(3, "C", 6, "C"));
		code2.add(new DFSedge(4, "C", 5, "C"));
		code2.add(new DFSedge(4, "C", 7, "S"));
		code2.add(new DFSedge(8, "C", 4, "C"));
		assertTrue(code2.dfsCodeToGraph().isConnected());

		DFScode<DFSedge> code3 = new DFScode<>();
		code3.add(new DFSedge(1, "S", 2, "S"));
		code3.add(new DFSedge(1, "S", 3, "C"));
		code3.add(new DFSedge(3, "C", 6, "C"));
		code3.add(new DFSedge(4, "C", 5, "C"));
		code3.add(new DFSedge(4, "C", 7, "S"));
		code3.add(new DFSedge(8, "C", 4, "C"));
		assertFalse(code3.dfsCodeToGraph().isConnected());
		
		DFScode<DFSedge> code4 = new DFScode<>();
		code4.add(new DFSedge(1, "S", 2, "S"));
		code4.add(new DFSedge(2, "S", 4, "C"));
		code4.add(new DFSedge(5, "C", 3, "C"));
		assertFalse(code4.dfsCodeToGraph().isConnected());
	}
	
	@Test
	public void TestIsConnectedGraphDirected(){
		GraphPathParameters.undirected = 0;
		DFScode<DFSedge> code = new DFScode<>();
		code.add(new DFSedge(1, "S", 2, "S"));
		code.add(new DFSedge(1, "S", 3, "C"));
		code.add(new DFSedge(2, "S", 4, "C"));
		code.add(new DFSedge(5, "C", 3, "C"));
		assertTrue(code.dfsCodeToGraph().isConnected());

		DFScode<DFSedge> code2 = new DFScode<>();
		code2.add(new DFSedge(1, "S", 2, "S"));
		code2.add(new DFSedge(1, "S", 3, "C"));
		code2.add(new DFSedge(2, "S", 4, "C"));
		code2.add(new DFSedge(3, "C", 6, "C"));
		code2.add(new DFSedge(4, "C", 5, "C"));
		code2.add(new DFSedge(4, "C", 7, "S"));
		code2.add(new DFSedge(8, "C", 4, "C"));
		assertTrue(code2.dfsCodeToGraph().isConnected());
		
		DFScode<DFSedge> code3 = new DFScode<>();
		code3.add(new DFSedge(1, "S", 2, "S"));
		code3.add(new DFSedge(1, "S", 3, "C"));
		code3.add(new DFSedge(3, "C", 6, "C"));
		code3.add(new DFSedge(4, "C", 5, "C"));
		code3.add(new DFSedge(4, "C", 7, "S"));
		code3.add(new DFSedge(8, "C", 4, "C"));
		assertFalse(code3.dfsCodeToGraph().isConnected());
		
		DFScode<DFSedge> code4 = new DFScode<>();
		code4.add(new DFSedge(1, "S", 2, "S"));
		code4.add(new DFSedge(2, "S", 4, "C"));
		code4.add(new DFSedge(5, "C", 3, "C"));
		GraphPathParameters.undirected = 1;
		assertFalse(code4.dfsCodeToGraph().isConnected());
	}
}
