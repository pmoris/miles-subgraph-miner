package com.uantwerp.algorithms.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.uantwerp.algorithms.common.GraphPathParameters;
import com.uantwerp.algorithms.procedures.base.OptimizeParameter;

public class MapUtil {

	public static List<Map.Entry<Integer, OptimizeParameter>> sortByValue(HashMap<Integer, OptimizeParameter> map) {
		List<Map.Entry<Integer, OptimizeParameter>> list = new ArrayList<Map.Entry<Integer, OptimizeParameter>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, OptimizeParameter>>() {
			public int compare(Map.Entry<Integer, OptimizeParameter> o1, Map.Entry<Integer, OptimizeParameter> o2) {
				com.uantwerp.algorithms.procedures.base.OptimizeParameter op1 = (com.uantwerp.algorithms.procedures.base.OptimizeParameter) o1
						.getValue();
				com.uantwerp.algorithms.procedures.base.OptimizeParameter op2 = (com.uantwerp.algorithms.procedures.base.OptimizeParameter) o2
						.getValue();

				if (op1.getLenghtToOne() == op2.getLenghtToOne())
					if (op1.getOutgoingEdges() == op2.getOutgoingEdges())
						if (op1.getIncomingEdges() == op2.getIncomingEdges()) {
							if (GraphPathParameters.graph.labelHash
									.get(op1.getLabel()) == GraphPathParameters.graph.labelHash.get(op2.getLabel()))
								return o1.getKey().compareTo(o2.getKey());
							else if (GraphPathParameters.graph.labelHash
									.get(op1.getLabel()) > GraphPathParameters.graph.labelHash.get(op2.getLabel()))
								return 1;
							else
								return -1;
						} else if (op1.getIncomingEdges() > op2.getIncomingEdges())
							return -1;
						else
							return 1;
					else if (op1.getOutgoingEdges() > op2.getOutgoingEdges())
						return -1;
					else
						return 1;
				else if (op1.getLenghtToOne() > op2.getLenghtToOne())
					return 1;
				else
					return -1;
			}
		});
		return list;
	}

	public static List<Map.Entry<Integer, OptimizeParameter>> sortByValue_und(HashMap<Integer, OptimizeParameter> map) {
		List<Map.Entry<Integer, OptimizeParameter>> list = new ArrayList<Map.Entry<Integer, OptimizeParameter>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<Integer, OptimizeParameter>>() {
			public int compare(Map.Entry<Integer, OptimizeParameter> o1, Map.Entry<Integer, OptimizeParameter> o2) {
				com.uantwerp.algorithms.procedures.base.OptimizeParameter op1 = (com.uantwerp.algorithms.procedures.base.OptimizeParameter) o1
						.getValue();
				com.uantwerp.algorithms.procedures.base.OptimizeParameter op2 = (com.uantwerp.algorithms.procedures.base.OptimizeParameter) o2
						.getValue();

				if (op1.getLenghtToOne() == op2.getLenghtToOne())
					if (op1.getOutgoingEdges() == op2.getOutgoingEdges()) {
						if (GraphPathParameters.graph.labelHash
								.get(op1.getLabel()) == GraphPathParameters.graph.labelHash.get(op2.getLabel()))
							return o1.getKey().compareTo(o2.getKey());
						else if (GraphPathParameters.graph.labelHash
								.get(op1.getLabel()) > GraphPathParameters.graph.labelHash.get(op2.getLabel()))
							return 1;
						else
							return -1;
					} else if (op1.getOutgoingEdges() > op2.getOutgoingEdges())
						return -1;
					else
						return 1;
				else if (op1.getLenghtToOne() > op2.getLenghtToOne())
					return 1;
				else
					return -1;
			}
		});
		return list;
	}

	public static List<Integer> sortByValueHashSet(HashSet<Integer> map, final HashMap<Integer, Integer> order) {
		List<Integer> list = new ArrayList<Integer>(map);
		Collections.sort(list, new Comparator<Integer>() {
			public int compare(Integer o1, Integer o2) {
				return order.get(o1).compareTo(order.get(o2));
			}
		});
		return list;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueIntegers(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

}