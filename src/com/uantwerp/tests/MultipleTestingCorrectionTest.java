package com.uantwerp.tests;

import java.util.HashMap;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

import com.uantwerp.algorithms.utilities.MultipleTestingCorrection;

public class MultipleTestingCorrectionTest {

	private static final double DELTA = 1e-7;

	static HashMap<String, Double> supportedMotifsPValues;

	static {
		supportedMotifsPValues = new HashMap<String, Double>();
		supportedMotifsPValues.put("motif1", 0.01);
		supportedMotifsPValues.put("motif2", 0.02);
		supportedMotifsPValues.put("motif3", 0.09);
		supportedMotifsPValues.put("motif4", 0.8);
		supportedMotifsPValues.put("motif5", 1.0);
	}

	@Test
	public void TestBonferroni() {

//		initialise hashmap containing expected bonferroni-adjusted p-values
		HashMap<String, Double> bonferroniAdjustedMap = new HashMap<String, Double>();
		bonferroniAdjustedMap.put("motif1", 0.05);
		bonferroniAdjustedMap.put("motif2", 0.10);
		bonferroniAdjustedMap.put("motif3", 0.45);
		bonferroniAdjustedMap.put("motif4", 1.0);
		bonferroniAdjustedMap.put("motif5", 1.0);

//		adjusts p-values with bonferroni method
		HashMap<String, Double> motifs2AdjustedPValues = MultipleTestingCorrection.adjustPValues(supportedMotifsPValues,
				"bonferroni");

//		test equality
		assertThat(motifs2AdjustedPValues.get("motif1"), is(closeTo(bonferroniAdjustedMap.get("motif1"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif2"), is(closeTo(bonferroniAdjustedMap.get("motif2"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif3"), is(closeTo(bonferroniAdjustedMap.get("motif3"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif4"), is(closeTo(bonferroniAdjustedMap.get("motif4"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif5"), is(closeTo(bonferroniAdjustedMap.get("motif5"), DELTA)));

//		test equality while ignoring order
//		assertThat(motifs2AdjustedPValues, IsCloseTo(bonferroniAdjustedMap));

//		test size
		assertThat(motifs2AdjustedPValues.size(), is(supportedMotifsPValues.size()));
	}

	@Test
	public void TestBH() {
//		initialise hashmap containing expected bh-adjusted p-values
		HashMap<String, Double> BHAdjustedMap = new HashMap<String, Double>();
		BHAdjustedMap.put("motif1", 0.05);
		BHAdjustedMap.put("motif2", 0.05);
		BHAdjustedMap.put("motif3", 0.15);
		BHAdjustedMap.put("motif4", 1.0);
		BHAdjustedMap.put("motif5", 1.0);

//		adjusts p-values with bh method
		HashMap<String, Double> motifs2AdjustedPValues = MultipleTestingCorrection.adjustPValues(supportedMotifsPValues,
				"BH");

//		test equality
		assertThat(motifs2AdjustedPValues.get("motif1"), is(closeTo(BHAdjustedMap.get("motif1"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif2"), is(closeTo(BHAdjustedMap.get("motif2"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif3"), is(closeTo(BHAdjustedMap.get("motif3"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif4"), is(closeTo(BHAdjustedMap.get("motif4"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif5"), is(closeTo(BHAdjustedMap.get("motif5"), DELTA)));

//		test size
		assertThat(motifs2AdjustedPValues.size(), is(supportedMotifsPValues.size()));
	}

	@Test
	public void TestBY() {
//		initialise hashmap containing expected by-adjusted p-values
		HashMap<String, Double> BYAdjustedMap = new HashMap<String, Double>();
		BYAdjustedMap.put("motif1", 0.1141667);
		BYAdjustedMap.put("motif2", 0.1141667);
		BYAdjustedMap.put("motif3", 0.3425000);
		BYAdjustedMap.put("motif4", 1.0);
		BYAdjustedMap.put("motif5", 1.0);

//		adjusts p-values with by method
		HashMap<String, Double> motifs2AdjustedPValues = MultipleTestingCorrection.adjustPValues(supportedMotifsPValues,
				"BY");

//		test equality
		assertThat(motifs2AdjustedPValues.get("motif1"), is(closeTo(BYAdjustedMap.get("motif1"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif2"), is(closeTo(BYAdjustedMap.get("motif2"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif3"), is(closeTo(BYAdjustedMap.get("motif3"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif4"), is(closeTo(BYAdjustedMap.get("motif4"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif5"), is(closeTo(BYAdjustedMap.get("motif5"), DELTA)));

//		test size
		assertThat(motifs2AdjustedPValues.size(), is(supportedMotifsPValues.size()));
	}

	@Test
	public void TestHolm() {
//		initialise hashmap containing expected holm-adjusted p-values
		HashMap<String, Double> HolmAdjustedMap = new HashMap<String, Double>();
		HolmAdjustedMap.put("motif1", 0.05);
		HolmAdjustedMap.put("motif2", 0.08);
		HolmAdjustedMap.put("motif3", 0.27);
		HolmAdjustedMap.put("motif4", 1.0);
		HolmAdjustedMap.put("motif5", 1.0);

//		adjusts p-values with holm method
		HashMap<String, Double> motifs2AdjustedPValues = MultipleTestingCorrection.adjustPValues(supportedMotifsPValues,
				"holm");

//		test equality
		assertThat(motifs2AdjustedPValues.get("motif1"), is(closeTo(HolmAdjustedMap.get("motif1"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif2"), is(closeTo(HolmAdjustedMap.get("motif2"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif3"), is(closeTo(HolmAdjustedMap.get("motif3"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif4"), is(closeTo(HolmAdjustedMap.get("motif4"), DELTA)));
		assertThat(motifs2AdjustedPValues.get("motif5"), is(closeTo(HolmAdjustedMap.get("motif5"), DELTA)));

//		test size
		assertThat(motifs2AdjustedPValues.size(), is(supportedMotifsPValues.size()));

		// bigger test
		HashMap<String, Double> supportedMotifsPValues2 = new HashMap<String, Double>();
		supportedMotifsPValues2.put("motif1", 0.01);
		supportedMotifsPValues2.put("motif2", 0.02);
		supportedMotifsPValues2.put("motif3", 0.09);
		supportedMotifsPValues2.put("motif4", 0.8);
		supportedMotifsPValues2.put("motif5", 0.12);
		supportedMotifsPValues2.put("motif6", 0.00012);
		supportedMotifsPValues2.put("motif7", 0.1);
		supportedMotifsPValues2.put("motif8", 0.1);
		supportedMotifsPValues2.put("motif9", 0.1);
		supportedMotifsPValues2.put("motif10", 1.0);

		HashMap<String, Double> HolmAdjustedMap2 = new HashMap<String, Double>();
		HolmAdjustedMap2.put("motif1", 0.09);
		HolmAdjustedMap2.put("motif2", 0.16);
		HolmAdjustedMap2.put("motif3", 0.63);
		HolmAdjustedMap2.put("motif4", 1.0);
		HolmAdjustedMap2.put("motif5", 0.63);
		HolmAdjustedMap2.put("motif6", 0.0012);
		HolmAdjustedMap2.put("motif7", 0.63);
		HolmAdjustedMap2.put("motif8", 0.63);
		HolmAdjustedMap2.put("motif9", 0.63);
		HolmAdjustedMap2.put("motif10", 1.0);

//		adjusts p-values with holm method
		HashMap<String, Double> motifs2AdjustedPValues2 = MultipleTestingCorrection
				.adjustPValues(supportedMotifsPValues2, "holm");

//		test equality
		assertThat(motifs2AdjustedPValues2.get("motif1"), is(closeTo(HolmAdjustedMap2.get("motif1"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif2"), is(closeTo(HolmAdjustedMap2.get("motif2"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif3"), is(closeTo(HolmAdjustedMap2.get("motif3"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif4"), is(closeTo(HolmAdjustedMap2.get("motif4"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif5"), is(closeTo(HolmAdjustedMap2.get("motif5"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif6"), is(closeTo(HolmAdjustedMap2.get("motif6"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif7"), is(closeTo(HolmAdjustedMap2.get("motif7"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif8"), is(closeTo(HolmAdjustedMap2.get("motif8"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif9"), is(closeTo(HolmAdjustedMap2.get("motif9"), DELTA)));
		assertThat(motifs2AdjustedPValues2.get("motif10"), is(closeTo(HolmAdjustedMap2.get("motif10"), DELTA)));

	}

}
