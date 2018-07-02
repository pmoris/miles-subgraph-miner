package com.uantwerp.algorithms.utilities;

public abstract class HypergeomDist {

	public static double getProbability(int m, int n, int N, int i){
		double hypercdf = 0.0;
		for (int iref = i; iref <= Math.min(N, n); iref++){
			hypercdf += hypergeom(n,m,N,iref);
		}
		return hypercdf;
	}
	
	private static double hypergeom(int n,int m,int N,int i){
		double loghyp1 = logfat(m)+logfat(n)+logfat(N)+logfat(m+n-N);
		double loghyp2 = logfat(i)+logfat(n-i)+logfat(m+i-N)+logfat(N-i)+logfat(m+n);
		return Math.exp(loghyp1 - loghyp2);
	}
	
	private static double logfat(int number) {
		return gammln(number+1);
   }
	
	private static double gammln(int number) {
		int y = number;
		Double[] cof = new Double[6];
		cof[0] = 76.18009172947146; cof[1] = -86.50532032941677; cof[2] = 24.01409824083091;cof[3] = -1.231739572450155; cof[4] = 0.12086509738661e-2; cof[5] = -0.5395239384953e-5;
		double tmp = number + 5.5;
		tmp -= (number + 0.5) * Math.log(tmp);
		double ser = 1.000000000190015;
		for (int i = 0; i<+5; i++){
			ser += cof[i]/++y;
		}
		return (-1)*tmp + Math.log(2.5066282746310005*ser/number);
	}
}
