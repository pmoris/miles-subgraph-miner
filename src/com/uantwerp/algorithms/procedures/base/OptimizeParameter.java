package com.uantwerp.algorithms.procedures.base;

public class OptimizeParameter {

	private String label;
	private double lenghtToOne;
	private int outgoingEdges;
	private int incomingEdges;
	
	public OptimizeParameter() {
		lenghtToOne = 0;
		outgoingEdges = 0;
		incomingEdges = 0;
	}
	
	public OptimizeParameter(String label) {
		lenghtToOne = 0;
		outgoingEdges = 0;
		incomingEdges = 0;
		this.label = label;
	}
	
	public double getLenghtToOne() {
		return lenghtToOne;
	}
	public void setLenghtToOne(double lenghtToOne) {
		this.lenghtToOne = lenghtToOne;
	}
	public int getOutgoingEdges() {
		return outgoingEdges;
	}
	public void setOutgoingEdges(int outgoingEdges) {
		this.outgoingEdges = outgoingEdges;
	}
	public int getIncomingEdges() {
		return incomingEdges;
	}
	public void setIncomingEdges(int incomingEdges) {
		this.incomingEdges = incomingEdges;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
}
