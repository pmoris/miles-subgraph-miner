package com.uantwerp.algorithms.visualisation;

public class JsonDataNode {
	
	@SuppressWarnings("unused")
	private String id; // mandatory (string or number) id for each element, assigned automatically on undefined
	@SuppressWarnings("unused")
	private String name;
	@SuppressWarnings("unused")
	private Double pValue;
	
	public JsonDataNode(String id, String name, Double pValue) {
		this.id = id;
		this.name = name;
		this.pValue = pValue;		
	}

}
