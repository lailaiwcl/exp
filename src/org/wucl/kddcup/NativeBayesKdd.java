package org.wucl.kddcup;

import java.io.File;
import java.io.IOException;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class NativeBayesKdd {
	private static Instances ins;
	private static Instances testIns;
	private static Classifier classifier;
	static {
		// 读入训练测试样本
		String filePath = "E:\\E\\学习资料\\数据集\\kddcup99\\corrected.arff";
		File file = new File(filePath);
		ArffLoader loader = new ArffLoader();
		try {
			loader.setFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ins = loader.getDataSet();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 去除包含Missing属性的记录
		for (int i = 0; i < ins.numAttributes(); i++) {
			ins.deleteWithMissing(ins.attribute(i));
		}
		ins.setClassIndex(ins.numAttributes() - 1);
		ins = ins.resample(ins.getRandomNumberGenerator(0));
		testIns = new Instances(ins, 100000, 110000);
	}

	public static void main(String[] args) throws Exception {
		
		classifier = new NaiveBayes();
		long t1 = System.currentTimeMillis();
		run(classifier,ins,1000);
		long t2 = System.currentTimeMillis();
		System.out.println("run time:" + (t2-t1));
		System.out.println("-----------------------");
		
		classifier = new NaiveBayes();
		t1 = System.currentTimeMillis();
		run(classifier,ins,5000);
		t2 = System.currentTimeMillis();
		System.out.println("run time:" + (t2-t1));
		System.out.println("-----------------------");
		
		classifier = new NaiveBayes();
		t1 = System.currentTimeMillis();
		run(classifier,ins,10000);
		t2 = System.currentTimeMillis();
		System.out.println("run time:" + (t2-t1));
		System.out.println("-----------------------");
		
		classifier = new NaiveBayes();
		t1 = System.currentTimeMillis();
		run(classifier,ins,100000);
		t2 = System.currentTimeMillis();
		System.out.println("run time:" + (t2-t1));
		System.out.println("-----------------------");
	}

	/**
	 * 测试误差
	 * @param classifier
	 * @param instances
	 * @param trainNumber
	 * @throws Exception
	 */
	public static void run(Classifier classifier, Instances instances,
			int trainNumber) throws Exception {
		// Classifier bayes = new NaiveBayes();
		Instances trainIns = new Instances(ins, 0, trainNumber);
		classifier = train(classifier, trainIns);
		Evaluation testingEvaluation;
		testingEvaluation = new Evaluation(trainIns);
		int length = testIns.numInstances();
		for (int i = 0; i < length; i++) {
			Instance inst = testIns.instance(i);
			testingEvaluation.evaluateModelOnceAndRecordPrediction(classifier,
					inst);
		}
		double error = testingEvaluation.errorRate();
		System.out.println("trainNum:" + trainNumber + "    error Rate:"
				+ error);
	}
	
	/**
	 * 训练误差
	 * @param classifier
	 * @param instances
	 * @param trainNumber
	 * @throws Exception
	 */
	public static void run2(Classifier classifier, Instances instances,
			int trainNumber) throws Exception {
		// Classifier bayes = new NaiveBayes();
		Instances trainIns = new Instances(ins, 0, trainNumber);
		classifier = train(classifier, trainIns);
		Evaluation testingEvaluation;
		testingEvaluation = new Evaluation(trainIns);
		int length = trainIns.numInstances();
		for (int i = 0; i < length; i++) {
			Instance inst = trainIns.instance(i);
			testingEvaluation.evaluateModelOnceAndRecordPrediction(classifier,
					inst);
		}
		double error = testingEvaluation.errorRate();
		System.out.println("trainNum:" + trainNumber + "    error Rate:"
				+ error);
	}

	public static Classifier train(Classifier classifier, Instances instances) {
		if (classifier == null || instances == null) {
			return null;
		}
		try {
			classifier.buildClassifier(instances);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return classifier;
	}

}
