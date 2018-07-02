package com.uantwerp.algorithms.common;

public class PairStrValues implements Comparable<PairStrValues> {

	public String val1;
	public String val2;

	public PairStrValues(String fromlb, String tolb) {
		super();
		this.val1 = fromlb;
		this.val2 = tolb;
	}

	public String getFromlb() {
		return val1;
	}

	public void setFromlb(String fromlb) {
		this.val1 = fromlb;
	}

	public String getTolb() {
		return val2;
	}

	public void setTolb(String tolb) {
		this.val2 = tolb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((val1 == null) ? 0 : val1.hashCode());
		result = prime * result + ((val2 == null) ? 0 : val2.hashCode());
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
		PairStrValues other = (PairStrValues) obj;
		if (val1 == null) {
			if (other.val1 != null)
				return false;
		} else if (!val1.equals(other.val1))
			return false;
		if (val2 == null) {
			if (other.val2 != null)
				return false;
		} else if (!val2.equals(other.val2))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PairStrValues [val1=" + val1 + ", val2=" + val2 + "]";
	}

	@Override
	public int compareTo(PairStrValues o) {
		int compFirst = this.val1.compareTo(o.val1);
		if (compFirst==0)
			return this.val2.compareTo(o.val2);
		else
			return compFirst;
	}

}
