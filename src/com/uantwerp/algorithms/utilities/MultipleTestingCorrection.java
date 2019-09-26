package com.uantwerp.algorithms.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class MultipleTestingCorrection {

////	the raw p-values
//	private double[] pValues;
//	
////	the adjusted p-values
//	private double[] adjustedPValues;
//	
//public double[] getAdjustedPValues() {
//		return adjustedPValues;
//	}
//
//	//	the number of tests
//	private int m;
//	
////	the type of correction
//	private String type;

//	public MultipleTestingCorrection(HashMap<String,Double> supportedMotifsPValues, String type) {
////        this.pValues = p;
//        this.m = pValues.length;
//        this.adjustedPValues = new double[m];
//        this.type = type;
//	}

	/**
	 * @param supportedMotifsPValues HashMap of motif Strings mapping to p-value
	 *                               Doubles.
	 * @param type                   String specifying the chosen multiple testing
	 *                               correction method: "bonferroni, BH, BY or
	 *                               holm".
	 * @return HashMap of motif Strings mapping to adjusted p-value Doubles.
	 */
	public static HashMap<String, Double> adjustPValues(HashMap<String, Double> supportedMotifsPValues, String type) {
//		order p-values
		List<Entry<String, Double>> sortedMotif2PValue = sortMotifsOnPValue(supportedMotifsPValues);
//		number of tests/motifs
		int m = sortedMotif2PValue.size();
//		create hashmap to store adjusted p-values
		HashMap<String, Double> motifs2AdjustedPValues = new HashMap<>();

		if (type == "BH") {
			BenjaminiHochbergCorrection(sortedMotif2PValue, motifs2AdjustedPValues, m);
		} else if (type == "BY") {
			BenjaminiYekutieliCorrection(sortedMotif2PValue, motifs2AdjustedPValues, m);
		} else if (type == "holm") {
			HolmCorrection(sortedMotif2PValue, motifs2AdjustedPValues, m);
		} else {
			BonferroniCorrection(sortedMotif2PValue, motifs2AdjustedPValues, m);
		}
		return motifs2AdjustedPValues;
	}

	/**
	 * @param supportedMotifsPValues
	 * @return List of Entry<motif String, p-value Double> elements, sorted by
	 *         ascending p-value
	 * 
	 *         https://stackoverflow.com/questions/21105413/java-java-util-map-entry-and-collection-sortlist-comparator
	 *         https://stackoverflow.com/questions/26717422/converting-hashmap-to-sorted-arraylist
	 */
	private static List<Entry<String, Double>> sortMotifsOnPValue(HashMap<String, Double> supportedMotifsPValues) {
		List<Entry<String, Double>> supportedMotifsPValuesEntryList = new ArrayList<>(
				supportedMotifsPValues.entrySet());
		Collections.sort(supportedMotifsPValuesEntryList, new Comparator<Entry<String, Double>>() {
			@Override
			public int compare(Entry<String, Double> a, Entry<String, Double> b) {
				return a.getValue().compareTo(b.getValue());
			}
		});
		return supportedMotifsPValuesEntryList;
	}

	private static void BonferroniCorrection(List<Entry<String, Double>> sortedMotif2PValue,
			HashMap<String, Double> motifs2AdjustedPValues, int m) {
		for (Entry<String, Double> motif2PValuePair : sortedMotif2PValue) {
			String motif = motif2PValuePair.getKey();
			double pValue = motif2PValuePair.getValue();
			double adjustedPValue = pValue * m;
			motifs2AdjustedPValues.put(motif, Math.min(adjustedPValue, 1.0));
		}
	}

	private static void HolmCorrection(List<Entry<String, Double>> sortedMotif2PValue,
			HashMap<String, Double> motifs2AdjustedPValues, int m) {

		double maxOfSmallerPValues = 0;
		for (int i = 1; i < m + 1; i++) {
			String motif = sortedMotif2PValue.get(i - 1).getKey();
			double pValue = sortedMotif2PValue.get(i - 1).getValue();

			double adjustedPValue = pValue * (m - i + 1);

			// if the new adjusted p-value is larger than a previously encountered one,
			// replace it with the smallest encountered one
			if (adjustedPValue < maxOfSmallerPValues) {
				adjustedPValue = maxOfSmallerPValues;
			}

			// store adjusted p-value in hashmap under its associated motif key
			motifs2AdjustedPValues.put(motif, Math.min(adjustedPValue, 1.0));
			maxOfSmallerPValues = adjustedPValue;
		}

	}

	private static void BenjaminiHochbergCorrection(List<Entry<String, Double>> sortedMotif2PValue,
			HashMap<String, Double> motifs2AdjustedPValues, int m) {
		// the FDR adjusted p-value q_i is the smallest value q_k for k >= i, where q_k
		// is the FDR corrected value ( p_i / (m i) )* cM
		// initialise q at 1 and go from largest to smallest p-value
		// this ensures a monotonic adjusted FDR function
		double minQ = 1;
		for (int i = m; i > 0; i--) {
			String motif = sortedMotif2PValue.get(i - 1).getKey();
			double pValue = sortedMotif2PValue.get(i - 1).getValue();

			double adjustedPValue = ((pValue * m) / i) * 1; // cM = 1 for BH fdr

			// if the new adjusted p-value is larger than a previously encountered one,
			// replace it with the smallest encountered one
			if (adjustedPValue > minQ) {
				adjustedPValue = minQ;
			}

			// store adjusted p-value in hashmap under its associated motif key
			motifs2AdjustedPValues.put(motif, Math.min(adjustedPValue, 1.0));
			minQ = adjustedPValue;
		}
	}

	private static void BenjaminiYekutieliCorrection(List<Entry<String, Double>> sortedMotif2PValue,
			HashMap<String, Double> motifs2AdjustedPValues, int m) {
		double minQ = 1;
		for (int i = m; i > 0; i--) {
			String motif = sortedMotif2PValue.get(i - 1).getKey();
			double pValue = sortedMotif2PValue.get(i - 1).getValue();

			double adjustedPValue = ((pValue * m) / i) * cMSum(m); // cM = sum_j_to_m 1/j for BY fdr

			if (adjustedPValue > minQ) {
				adjustedPValue = minQ;
			}

			motifs2AdjustedPValues.put(motif, Math.min(adjustedPValue, 1.0));
			minQ = adjustedPValue;
		}
	}

	private static double cMSum(int m) {
		double cM = 0;
		for (double i = 1; i <= m; i++) {
			cM = cM + (1 / i);
		}
		return cM;
	}
}
