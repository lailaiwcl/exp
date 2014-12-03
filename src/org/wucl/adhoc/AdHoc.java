package org.wucl.adhoc;

public class AdHoc {

	public double calX1(double a, double b, double x1, double x2, double gli,
			double t, double t2) {

		double fx = (1.0 / (x1 + gli) + 1.0 / (x2 + gli)) * a;
		double fy = (1.0 / Math.pow(((t * gli) - (x1 + x2)), 2) + 1.0 / Math
				.pow(((t2 * 10) - x1), 2))
				* a;

		return fx - fy;

	}

	public double calX2(double a, double b, double x1, double x2, double gli,
			double t) {
		double fx = 1.0 / (x2 + gli) * a;
		double gx = 1.0 / Math.pow(((t * gli) - (x1 + x2)), 2) * b;
		return fx - gx;
	}

	public double calU1(double a, double b, double x1, double x2, double gli,
			double t) {
		double fx = (Math.log10(x1 / gli + 1) + Math.log10(x2 / gli + 1)) * a;
		double fy = Math.exp(Math.pow((t * gli - (x1 + x2)), 2)) - 1;
		return fx - fy;
	}
	
	public double calU2(double a, double b, double x1, double x2, double gli,
			double t) {
		double fx = Math.log10(x2 / gli + 1) * a;
		double fy = (t * gli - (x1 + x2));
		return fx - fy;
	}

	public static void main(String[] args) {
		AdHoc hoc = new AdHoc();
		double x1 = hoc.calX1(9, 8, 20.0 / 3, 7.0 / 3, 7.0, 2.0 / 3, 1.0 / 3);
		double x2 = hoc.calX2(8, 3, 20.0 / 3, 7.0 / 3, 7.0, 1.0 / 3);
		System.out.println(x1);
		System.out.println(x2);
	}
}
