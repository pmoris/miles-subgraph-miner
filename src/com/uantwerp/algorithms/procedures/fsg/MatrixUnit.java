package com.uantwerp.algorithms.procedures.fsg;

public class MatrixUnit implements Comparable<MatrixUnit> {

	private String label;
	private String identifier;

	public MatrixUnit(String label, String identifier) {
		super();
		this.label = label;
		this.identifier = identifier;
	}

	
	public String getLabel() {
		return label;
	}


	public void setLabel(String label) {
		this.label = label;
	}


	public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MatrixUnit other = (MatrixUnit) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}

	@Override
	public int compareTo(MatrixUnit o) {
//		if (this.identifier.equals("1") && o.identifier.equals("1")) {
//			return 0;
//		} else if (this.identifier.equals("1") && !o.identifier.equals("1")) {
//			return -1;
//		} else {
			return this.label.compareTo(o.label);
//		}
	}

}
