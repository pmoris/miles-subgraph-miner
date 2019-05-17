package com.uantwerp.algorithms.visualisation;

public class JsonGraphElement {
	
	@SuppressWarnings("unused")
	private String group; // 'nodes' for a node, 'edges' for an edge
	@SuppressWarnings("unused")
	private Object data;
	
    @SuppressWarnings("unused")
	private Boolean removed = false;
    @SuppressWarnings("unused")
	private Boolean selected =false;
    @SuppressWarnings("unused")
	private Boolean selectable = true;
    @SuppressWarnings("unused")
	private Boolean locked = false;
    @SuppressWarnings("unused")
	private Boolean grabbable = true;
    
	public JsonGraphElement(String group, Object data) {
		this.group = group;
		this.data = data;
	}

}
