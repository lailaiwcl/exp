package org.wucl.adhoc;

public class AdHoc2 {

	public double calU1(double a, double b, double x1, double x2, double gli,
			double t) {
		double fx = (Math.log10(x1 / gli + 1) + Math.log10(x2 / gli + 1)) * a;
		double fy = (t * gli - (x1 + x2));
		return fx + fy;
	}

	public double calU2(double a, double b, double x1, double x2, double gli,
			double t) {
		double fx = Math.log10(x2 / gli + 1) * a;
		double fy = (t * gli - (x1 + x2));
		return fx + fy;
	}

	public double sigmoid(double val, double between) {
		if(val< 0 || val > between){
			return 1.0 * between / (1d + Math.exp(-val));
		}
		return val;
	}

	public static void main(String[] args) {
		AdHoc2 hoc = new AdHoc2();
		double x1 = 20.0 / 3;
		double x2 = 7.0 / 3;
		x1 = hoc
				.calU1(1, 1, 0.6732902807446262, 2.2998534011995324, 7, 1.0 / 3);
		x2 = hoc.calU2(1, 1, x1, x2, 7, 1.0 / 3);
		x1 = hoc.sigmoid(0.6732902807446262 + x1, 6.6);
		System.out.println("(" + x1 + "," + x2 + ")");
	}

}
