package com.uantwerp.algorithms.common;

import java.util.ArrayList;
import java.util.Collection;

public class Projection<E> extends ArrayList<E>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7737479861741270641L;

	public Projection() {
		super();
	}

	public Projection(Collection<? extends E> c) {
		super(c);
	}

	public Projection(int initialCapacity) {
		super(initialCapacity);
	}

	
}
