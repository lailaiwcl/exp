package org.wucl.bp;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class BPIterator {

	public static final String IRIS = "1";
	public static final String KDDCUP99 = "2";

	/**
	 * 对数据集的80%数据进行训练，剩余20%用于测试
	 * 
	 * @param dataSetType
	 *            数据集类型
	 * @param trainTimes
	 *            网络训练迭代次数
	 * @return
	 * @throws Exception
	 */
	public static Map<Integer, Double> trainBpnn(String dataSetType,
			int trainTimes, boolean isReset) throws Exception {
		MyMultilayerPerceptron cfs = new MyMultilayerPerceptron();
		cfs.setDebug(true);
		// cfs.setReset(isReset);
		// 初始化分类器训练次数
		cfs.setTrainingTime(trainTimes);

		// 读入训练测试样本
		String filePath = "C:\\Program Files\\Weka-3-7\\data\\iris.arff";
		if (KDDCUP99.equals(dataSetType)) {
			filePath = "E:\\E\\学习资料\\数据集\\kddcup99\\corrected.arff";
		}
		File file = new File(filePath);
		ArffLoader loader = new ArffLoader();
		loader.setFile(file);
		Instances ins = loader.getDataSet();
		// 去除包含Missing属性的记录
		for (int i = 0; i < ins.numAttributes(); i++) {
			ins.deleteWithMissing(ins.attribute(i));
		}
		int length = ins.numInstances();
		ins.setClassIndex(ins.numAttributes() - 1);
		ins = ins.resample(ins.getRandomNumberGenerator(0));
		Instances trainIns = new Instances(ins, 0, (int) (length * 0.8));

		// 使用训练样本进行分类
		// System.out.println("迭代次数：" + cfs.getTrainingTime());
		long t1 = System.currentTimeMillis();
		cfs.buildClassifier(trainIns);
		// cfs = (MultilayerPerceptron) cfs.makeCopy(cfs);
		long t2 = System.currentTimeMillis();
		// System.out.println("训练用时：" + (t2 - t1));

		
		// 使用测试样本测试分类器的学习效果
		Evaluation testingEvaluation = new Evaluation(trainIns);
		for (int i = (int) (length * 0.8) + 1; i < length; i++) {
			Instance testInst = ins.instance(i);
			System.out.println(testInst + "--"+cfs.classifyInstance(testInst));
			testingEvaluation.evaluateModelOnceAndRecordPrediction(cfs,
					testInst);
		}
		// System.out.println("测试用时：" + (System.currentTimeMillis() - t2));

		// System.out.println("总用时：" + (System.currentTimeMillis() - t1));
		// 打印分类结果
		// System.out.println("分类的正确率" + (1 - testingEvaluation.errorRate()));
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		map.put(cfs.getTrainingTime(), 1 - testingEvaluation.errorRate());
		return map;
	}

	public static void main(String[] args) throws Exception {
		//for (int i = 1; i < 10; i++) {
			System.out.println(trainBpnn(BPIterator.IRIS, 1000, false));
		//}
	}

}
