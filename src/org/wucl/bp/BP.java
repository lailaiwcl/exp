package org.wucl.bp;

import java.util.Random;

import org.apache.log4j.Logger;

/**
 * BP神经网络实现
 * 
 * @author wucl
 * 
 */
public class BP {
	/**
	 * 输入层
	 */
	private final double[] input;
	/**
	 * 隐藏层
	 */
	private final double[] hidden;
	/**
	 * 输出层
	 */
	private final double[] output;
	/**
	 * 目标结果
	 */
	private final double[] target;

	/**
	 * 隐藏层误差信号
	 */
	private final double[] hidDelta;
	/**
	 * 输出层误差信号
	 */
	private final double[] optDelta;

	/**
	 * 学习率
	 */
	private final double eta;

	/**
	 * 学习动量
	 */
	private final double momentum;

	/**
	 * 输入和隐藏层之间权重矩阵
	 */
	private final double[][] iptHidWeights;
	/**
	 * 隐藏层和输出层之间权重矩阵
	 */
	private final double[][] hidOptWeights;

	/**
	 * 前一次输入和隐藏层之间权重矩阵
	 */
	private final double[][] iptHidPrevUptWeights;
	/**
	 * 前一次隐藏层和输出层之间权重矩阵
	 */
	private final double[][] hidOptPrevUptWeights;

	public double optErrSum = 0d;

	public double hidErrSum = 0d;

	private final Random random;

	private static Logger logger = Logger.getLogger(BP.class);

	/**
	 * 构造函数
	 * 
	 * @param inputSize
	 * @param hiddenSize
	 * @param outputSize
	 * @param eta
	 * @param momentum
	 */
	public BP(int inputSize, int hiddenSize, int outputSize, double eta,
			double momentum) {

		input = new double[inputSize + 1];
		hidden = new double[hiddenSize + 1];
		output = new double[outputSize + 1];
		target = new double[outputSize + 1];

		hidDelta = new double[hiddenSize + 1];
		optDelta = new double[outputSize + 1];

		iptHidWeights = new double[inputSize + 1][hiddenSize + 1];
		hidOptWeights = new double[hiddenSize + 1][outputSize + 1];

		random = new Random(19881211);
		randomizeWeights(iptHidWeights);
		randomizeWeights(hidOptWeights);

		iptHidPrevUptWeights = new double[inputSize + 1][hiddenSize + 1];
		hidOptPrevUptWeights = new double[hiddenSize + 1][outputSize + 1];

		this.eta = eta;
		this.momentum = momentum;
	}

	/**
	 * 随机初始化权重数组（-1~1之间的随机数）
	 * 
	 * @param matrix
	 */
	private void randomizeWeights(double[][] matrix) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[i].length; j++) {
				double real = random.nextDouble();
				matrix[i][j] = random.nextDouble() > 0.5 ? real : -real;
			}
	}

	/**
	 * 默认参数构造 eta = 0.25 and momentum = 0.9
	 * 
	 * @param inputSize
	 * @param hiddenSize
	 * @param outputSize
	 */
	public BP(int inputSize, int hiddenSize, int outputSize) {
		this(inputSize, hiddenSize, outputSize, 0.25, 0.9);
	}

	/**
	 * 训练网络
	 * 
	 * @param trainData
	 * @param target
	 */
	public void train(double[] real, double[] target) {
		loadInput(real);
		loadTarget(target);
		forward();
		calculateDelta();
		adjustWeight();
	}

	/**
	 * 测试神经网络
	 * 
	 * @param inData
	 * @return
	 */
	public double[] test(double[] inData) {
		if (inData.length != input.length - 1) {
			throw new IllegalArgumentException("Size Do Not Match.");
		}
		System.arraycopy(inData, 0, input, 1, inData.length);
		forward();
		return getNetworkOutput();
	}

	/**
	 * 获取输出层结果
	 * 
	 * @return
	 */
	private double[] getNetworkOutput() {
		int len = output.length;
		double[] temp = new double[len - 1];
		for (int i = 1; i < len; i++)
			temp[i - 1] = output[i];
		return temp;
	}

	/**
	 * 加载训练结果数据
	 * 
	 * @param arg
	 */
	private void loadTarget(double[] arg) {
		if (arg.length != target.length - 1) {
			throw new IllegalArgumentException("Size Do Not Match.");
		}
		System.arraycopy(arg, 0, target, 1, arg.length);
	}

	/**
	 * 加载输入数据
	 * 
	 * @param inData
	 *            待输入的数据
	 */
	private void loadInput(double[] inData) {
		if (inData.length != input.length - 1) {
			throw new IllegalArgumentException("Size Do Not Match.");
		}
		System.arraycopy(inData, 0, input, 1, inData.length);
	}

	/**
	 * 计算当前层节点值
	 * 
	 * @param layer0
	 * @param layer1
	 * @param weight
	 */
	private void forward(double[] layer0, double[] layer1, double[][] weight) {
		layer0[0] = 1.0;
		for (int j = 1; j < layer1.length; ++j) {
			double sum = 0;
			for (int i = 0; i < layer0.length; ++i) {
				sum += weight[i][j] * layer0[i];
			}
			layer1[j] = sigmoid(sum);
		}
	}

	/**
	 * 计算输入层和隐藏层之间、隐藏层和输入层之间的值
	 */
	private void forward() {
		forward(input, hidden, iptHidWeights);
		forward(hidden, output, hidOptWeights);
	}

	/**
	 * 计算输出层误差
	 */
	private void outputErr() {
		double errSum = 0;
		for (int i = 1; i < optDelta.length; ++i) {
			double o = output[i];
			optDelta[i] = o * (1d - o) * (target[i] - o);
			errSum += Math.abs(optDelta[i]);
		}
		optErrSum = errSum;
		// logger.info(optErrSum);
	}

	/**
	 * 计算隐藏层误差
	 */
	private void hiddenErr() {
		double errSum = 0;
		for (int j = 1; j < hidDelta.length; ++j) {
			double o = hidden[j];
			double sum = 0;
			for (int k = 1; k < optDelta.length; ++k)
				sum += hidOptWeights[j][k] * optDelta[k];
			hidDelta[j] = o * (1d - o) * sum;
			errSum += Math.abs(hidDelta[j]);
		}
		hidErrSum = errSum;
	}

	/**
	 * 计算输出层和隐藏层误差
	 */
	private void calculateDelta() {
		outputErr();
		hiddenErr();
	}

	/**
	 * 调整权值矩阵
	 * 
	 * @param delta
	 * @param layer
	 * @param weight
	 * @param prevWeight
	 */
	private void adjustWeight(double[] delta, double[] layer,
			double[][] weight, double[][] prevWeight) {

		layer[0] = 1;
		for (int i = 1, len = delta.length; i != len; ++i) {
			for (int j = 0; j < layer.length; ++j) {
				double newVal = momentum * prevWeight[j][i] + eta * delta[i]
						* layer[j];
				weight[j][i] += newVal;
				prevWeight[j][i] = newVal;
			}
		}
	}

	/**
	 * 调整权值矩阵
	 */
	private void adjustWeight() {
		adjustWeight(optDelta, hidden, hidOptWeights, hidOptPrevUptWeights);
		adjustWeight(hidDelta, input, iptHidWeights, iptHidPrevUptWeights);
	}

	/**
	 * 单极性变换
	 * 
	 * @param val
	 *            待变换的数
	 * @return 变换结果
	 */
	private double sigmoid(double val) {
		return 1d / (1d + Math.exp(-val));
	}
}