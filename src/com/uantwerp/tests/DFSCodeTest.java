package com.uantwerp.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.uantwerp.algorithms.common.DFScode;
import com.uantwerp.algorithms.common.DFSedge;
import com.uantwerp.algorithms.common.GraphPathParameters;

public class DFSCodeTest {

	@Test
	public void TestUndirectedRepresentation(){
		GraphPathParameters.undirected = 1;
		DFScode<DFSedge> code = new DFScode<>();
		code.add(new DFSedge(1, "S", 2, "S"));
		code.add(new DFSedge(1, "S", 3, "C"));
		code.add(new DFSedge(2, "S", 4, "C"));
		code.add(new DFSedge(3, "C", 5, "C"));	
		
		assertEquals("1S-2C,2C-3C,1S-4S,4S-5C",code.getMinDfsCode().dfsCodeToString());
		
		DFScode<DFSedge> code1 = new DFScode<>();
		code1.add(new DFSedge(1, "C", 2, "C"));
		code1.add(new DFSedge(1, "C", 3, "C"));
		code1.add(new DFSedge(2, "C", 4, "C"));
		code1.add(new DFSedge(3, "C", 5, "C"));	
		
		assertEquals("1C-2C,2C-3C,1C-4C,4C-5C",code1.getMinDfsCode().dfsCodeToString());
		
		DFScode<DFSedge> code2 = new DFScode<>();
		code2.add(new DFSedge(1, "C", 2, "C"));
		code2.add(new DFSedge(2, "C", 3, "C"));
		code2.add(new DFSedge(3, "C", 4, "C"));
		
		assertEquals("1C-2C,2C-3C,1C-4C",code2.getMinDfsCode().dfsCodeToString());
	}
	
	@Test
	public void TestDirectedRepresentation(){
		GraphPathParameters.undirected = 0;
		DFScode<DFSedge> code = new DFScode<>();
		code.add(new DFSedge(1, "S", 2, "S"));
		code.add(new DFSedge(1, "S", 3, "C"));
		code.add(new DFSedge(2, "S", 4, "C"));
		code.add(new DFSedge(3, "C", 5, "C"));	
		
		assertEquals("1S-2C,2C-3C,1S-4S,4S-5C",code.getMinDfsCode().dfsCodeToString());
		
		DFScode<DFSedge> code2 = new DFScode<>();
		code2.add(new DFSedge(1, "C", 2, "C"));
		code2.add(new DFSedge(1, "C", 3, "S"));
		code2.add(new DFSedge(3, "S", 2, "C", false));
		
		assertEquals("1C-2C,3S-2C,1C-3S",code2.getMinDfsCode().dfsCodeToString());

		DFScode<DFSedge> code3 = new DFScode<>();
		code3.add(new DFSedge(1, "C", 2, "C"));
		code3.add(new DFSedge(2, "C", 5, "S"));
		code3.add(new DFSedge(4, "S", 2, "C"));
		code3.add(new DFSedge(1, "C", 3, "S"));
		assertEquals("1C-2C,2C-3S,4S-2C,1C-5S",code3.getMinDfsCode().dfsCodeToString());

		DFScode<DFSedge> code4 = new DFScode<>();
		code4.add(new DFSedge(2, "C", 1, "C"));
		code4.add(new DFSedge(1, "C", 3, "C"));
		code4.add(new DFSedge(4, "S", 3, "C"));
		code4.add(new DFSedge(5, "C", 4, "S"));
		assertEquals("1C-2C,3S-2C,4C-3S,5C-1C",code4.getMinDfsCode().dfsCodeToString());

		DFScode<DFSedge> code5 = new DFScode<>();
		code5.add(new DFSedge(2, " ", 1, " ", true));
		code5.add(new DFSedge(2, " ", 3, " ", false));
		code5.add(new DFSedge(3, " ", 1, " ", true));
		code5.add(new DFSedge(1, " ", 4, " ", false));
		assertEquals("1 -2 ,3 -1 ,3 -4 ,4 -1 ",code5.getMinDfsCode().dfsCodeToString());

		DFScode<DFSedge> code6 = new DFScode<>();
		code6.add(new DFSedge(1, " ", 2, " ", true));
		code6.add(new DFSedge(3, " ", 2, " ", false));
		code6.add(new DFSedge(3, " ", 1, " ", true));
		code6.add(new DFSedge(1, " ", 4, " ", false));
		assertEquals("1 -2 ,3 -2 ,3 -1 ,1 -4 ",code6.getMinDfsCode().dfsCodeToString());

		DFScode<DFSedge> code7 = new DFScode<>();
		code7.add(new DFSedge(2, "GLY", 1, "ASP", true));
		code7.add(new DFSedge(2, "GLY", 3, " ", false));
		code7.add(new DFSedge(4, "GLY", 3, " ", true));
		code7.add(new DFSedge(4, "GLY", 1, "ASP", false));
		code7.add(new DFSedge(4, "GLY", 2, "GLY", false));
		assertEquals("2GLY-1ASP,2GLY-3 ,4GLY-3 ,4GLY-1ASP,2GLY-4GLY",code7.getMinDfsCode().dfsCodeToString());
	}
}
