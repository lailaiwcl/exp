package org.wucl.bp;

import java.util.Random;

public class BPMain {
	public final BP bp = new BP(32, 15, 4);

	//public final BP bp2 = new BP(32, 15, 4);

	public static void main(String[] args) {
		BPMain main = new BPMain();
		main.train(200, 1);
		System.out.println(main.autoTest(100000));
		//main.reLearn(1);
		System.out.println(main.autoTest(100000));
	}

	/**
	 * 输入样本训练网络
	 * 
	 * @param trainCount
	 *            输入样本的个数
	 * @param reaptTimes
	 *            输入样本重复训练次数
	 */
	public String train(int trainCount, int reaptTimes) {
		int[] values = new int[trainCount];
		Random random = new Random();
		for (int i = 0; i < values.length; i++) {
			values[i] = random.nextInt();
		}
		StringBuffer err = new StringBuffer();
		for (int i = 0; i < reaptTimes; i++) {
			for (int j = 0; j < values.length; j++) {
				double[] real = new double[4];
				real[jundge(values[j])] = 1;
				double[] binary = normalize(values[j]);
				double[] input = new double[33];
				double[] target = new double[4];
				input = binary;
				target = real;
				bp.train(input, target);
				err.append(String.format("%.6f", bp.optErrSum));
				err.append(",");
			}
		}
		String str = err.substring(0, err.length() - 1);
		return str;
	}


	public String autoTest(int count) {
		int correctNum = 0;
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			int value = random.nextInt();
			double[] binary = new double[32];
			binary = normalize(value);
			double[] result = bp.test(binary);
			if (jundge(value) == outputJundge(result)) {
				correctNum++;
			}
		}
		return correctNum + "/" + count + "=" + (correctNum * 1.0 / count);
	}
	
	public String autoTest2(int count) {
		int correctNum = 0;
		Random random = new Random();
		for (int i = 0; i < count; i++) {
			int value = random.nextInt();
			double[] binary = new double[32];
			binary = normalize(value);
			double[] result = bp.test(binary);
			if (jundge(value) == outputJundge(result)) {
				correctNum++;
			}
		}
		return correctNum + "/" + count + "=" + (correctNum * 1.0 / count);
	}

	public int test(int value) {
		double[] binary = new double[32];
		binary = normalize(value);
		double[] result = bp.test(binary);
		return outputJundge(result);
	}

	public void reLearn(int count) {
		Random random = new Random(33478);
		for (int i = 0; i < count; i++) {
			int n = random.nextInt();
			int index = test(n);
			double[] real = new double[4];
			real[index] = 1;
			double[] binary = normalize(n);
			double[] input = new double[33];
			double[] target = new double[4];
			input = binary;
			target = real;
			bp.train(input, target);
		}
	}
	
	/**
	 * 返回数据输出下标
	 * @param value
	 * @return
	 */
	public static int jundge(int value) {
		if (value >= 0) {
			if ((value & 1) == 1) {
				// 正奇数
				return 0;
			} else {
				// 正偶数
				return 1;
			}
		} else if ((value & 1) == 1) {
			// 负奇数
			return 2;
		} else {
			// 负偶数
			return 3;
		}
	}

	public static int outputJundge(double[] result) {
		double max = -Integer.MIN_VALUE;
		int idx = -1;

		for (int i = 0; i != result.length; i++) {
			if (result[i] > max) {
				max = result[i];
				idx = i;
			}
		}
		return idx;
	}

	/**
	 * 归一化
	 * 
	 * @param value
	 * @return
	 */
	public static double[] normalize(int value) {
		double[] binary = new double[32];
		int index = 31;
		do {
			binary[index--] = (value & 1);
			value >>>= 1;
		} while (value != 0);
		return binary;
	}

}
