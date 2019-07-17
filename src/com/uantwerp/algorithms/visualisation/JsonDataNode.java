package com.uantwerp.algorithms.visualisation;

public class JsonDataNode {
	
	@SuppressWarnings("unused")
	private String id; // mandatory (string or number) id for each element, assigned automatically on undefined
	@SuppressWarnings("unused")
	private String name;
	@SuppressWarnings("unused")
	private Double pvalue;
	@SuppressWarnings("unused")
	private String type;
	
	public JsonDataNode(String id, String name, Double pvalue, String motifSourceIndicator) {
		this.id = id;
		this.name = name;
		this.pvalue = pvalue;
		this.type = motifSourceIndicator;
	}

}
