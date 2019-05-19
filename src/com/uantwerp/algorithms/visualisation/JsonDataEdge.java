package com.uantwerp.algorithms.visualisation;

public class JsonDataEdge {
	
	@SuppressWarnings("unused")
	private String id; // mandatory (string or number) id for each element, assigned automatically on undefined
	@SuppressWarnings("unused")
	private String source;
	@SuppressWarnings("unused")
	private String target;
	@SuppressWarnings("unused")
	private Double pvalue;
	
	public JsonDataEdge(String id, String idSource, String idTarget, Double pvalue) {
		this.id = id;
		this.source = idSource;
		this.target = idTarget;
		this.pvalue = pvalue;		
	}

}
