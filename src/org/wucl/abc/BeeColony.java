package org.wucl.abc;

import org.wucl.Site;

public class BeeColony {
	// 蜂群数量
	int np = 20;
	// 食物源数量
	public int foodNumber = np / 2;
	// 如果食物源达到limit次数没有被更新，该食物源就被放弃
	int limit = 100;

	// 目标个数
	public int boundNum = 5;
	// 目标地址
	Site[] goalSite = new Site[10];

	// 食物源
	Site[][] foods = new Site[foodNumber][boundNum];

	// 适应度
	double[] f = new double[foodNumber];
	// 适应值
	double[] fitness = new double[foodNumber];
	// 更新次数
	double[] trial = new double[foodNumber];

	//
	Site[] solution = new Site[boundNum];
	// 食物源更新概率
	double[] prob = new double[foodNumber];

	// 全局最优解
	public double globalMin;
	// 全局最优状态
	public Site[] globalParams = new Site[boundNum];

	double objValSol;
	double fitnessSol;

	public double[] xx;
	public double[] yy;

	/**
	 * 初始化
	 */
	public void initial() {
		// 变量初始化
		foods = new Site[foodNumber][boundNum];
		f = new double[foodNumber];
		fitness = new double[foodNumber];
		trial = new double[foodNumber];
		solution = new Site[boundNum];
		prob = new double[foodNumber];
		globalParams = new Site[boundNum];
		goalSite = new Site[foodNumber];

		// 初始位置
		for (int i = 0; i < goalSite.length; i++) {
			Site site = new Site("", xx[i], yy[i]);
			goalSite[i] = site;
		}
		for (int i = 0; i < foodNumber; i++) {
			init(i);
		}
		// 假设全局最优为第一个食物源
		globalMin = f[0];
		for (int i = 0; i < boundNum; i++) {
			globalParams[i] = foods[0][i];
		}

	}

	/**
	 * 记住食物源，找到最优食物源
	 */
	public void memorizeBestSource() {
		for (int i = 0; i < foodNumber; i++) {
			if (f[i] < globalMin) {
				globalMin = f[i];
				for (int j = 0; j < boundNum; j++)
					globalParams[j] = foods[i][j];
			}
		}
	}

	/**
	 * 发送雇佣蜂进行领域搜索
	 */
	public void sendEmployedBees() {
		for (int i = 0; i < foodNumber; i++) {
			searchNeighborhood(i);
		}
	}

	public void calculateProbabilities() {
		double maxfit = fitness[0];
		for (int i = 1; i < foodNumber; i++) {
			if (fitness[i] > maxfit)
				maxfit = fitness[i];
		}

		for (int i = 0; i < foodNumber; i++) {
			prob[i] = (0.9 * (fitness[i] / maxfit)) + 0.1;
		}
	}

	public void sendOnlookerBees() {
		int i = 0, t = 0;
		while (t < foodNumber) {
			if (ramdon() < prob[i]) {
				t++;
				searchNeighborhood(i);
				if (fitnessSol > fitness[i]) {
					trial[i] = 0;
					for (int j = 0; j < boundNum; j++)
						foods[i][j] = solution[j];
					f[i] = objValSol;
					fitness[i] = fitnessSol;
				} else {
					trial[i] = trial[i] + 1;
				}

			}
			i++;
			if (i == foodNumber) {
				i = 0;
			}
		}
	}

	/**
	 * 发送侦查蜂，如果食物源的在指定搜索次数类没有被更新，<br/>
	 * 则放弃该食物源，全局随机寻找下一个食物源
	 */
	public void sendScoutBees() {
		int maxtrialindex = 0;
		for (int i = 1; i < foodNumber; i++) {
			if (trial[i] > trial[maxtrialindex])
				maxtrialindex = i;
		}
		if (trial[maxtrialindex] >= limit) {
			init(maxtrialindex);
		}
	}

	/**
	 * 进行邻域搜索更新
	 * 
	 * @param index
	 *            更新的食物源位置下标
	 */
	private void searchNeighborhood(int index) {
		int changedIndex = (int) (ramdon() * boundNum);
		int neighbourIndex = (int) (ramdon() * foodNumber);
		while (neighbourIndex == index) {
			neighbourIndex = (int) (ramdon() * foodNumber);
		}
		for (int j = 0; j < boundNum; j++) {
			solution[j] = foods[index][j];
		}

		solution[changedIndex].x = foods[index][changedIndex].x
				+ (foods[index][changedIndex].x - foods[neighbourIndex][changedIndex].x)
				* (ramdon() - 0.5) * 2;
		solution[changedIndex].y = foods[index][changedIndex].y
				+ (foods[index][changedIndex].y - foods[neighbourIndex][changedIndex].y)
				* (ramdon() - 0.5) * 2;

		// 对越界数据的处理
		solution[changedIndex].x = (solution[changedIndex].x < 0) ? 0
				: solution[changedIndex].x;
		solution[changedIndex].x = (solution[changedIndex].x > 100) ? 100
				: solution[changedIndex].x;
		solution[changedIndex].y = (solution[changedIndex].y < 0) ? 0
				: solution[changedIndex].y;
		solution[changedIndex].y = (solution[changedIndex].y > 100) ? 100
				: solution[changedIndex].y;

		objValSol = calculateFunction(solution);
		fitnessSol = calculateFitness(objValSol);
	}

	private void init(int index) {
		for (int j = 0; j < boundNum; j++) {
			Site site = new Site(j + "", 100 * ramdon(), 100 * ramdon());
			foods[index][j] = site;
			// solution[j] = foods[index][j];
		}
		f[index] = calculateFunction(foods[index]);
		fitness[index] = calculateFitness(f[index]);
		trial[index] = 0;
	}

	private double calculateFunction(Site site[]) {
		double sum = 0;
		for (int i = 0; i < site.length; i++) {
			for (int j = 0; j < goalSite.length; j++) {
				sum += Math.sqrt(Math.pow((site[i].x - goalSite[j].x), 2)
						+ Math.pow((site[i].y - goalSite[j].y), 2));
			}
		}
		// System.out.println(sum);
		return sum;
	}

	/**
	 * 计算适应度函数值
	 * 
	 * @param fun
	 * @return
	 */
	private double calculateFitness(double fun) {
		double result = 0;
		if (fun >= 0) {
			result = 1 / (fun + 1);
		} else {
			result = 1 + Math.abs(fun);
		}
		return result;
	}

	/**
	 * 产生[0,1)之间的随机数
	 * 
	 * @return
	 */
	private double ramdon() {
		return Math.random();
	}

	public static void main(String[] args) {
		BeeColony bee = new BeeColony();
		double[] xx = { 0, 10, 20, 30, 40, 50, 60, 70, 80, 90 };
		double[] yy = { 20, 60, 80, 100, 50, 30, 90, 10, 0, 60 };
		bee.xx = xx;
		bee.yy = yy;
		bee.foodNumber = xx.length;
		for (int i = 0; i < 30; i++) {
			bee.initial();
			bee.memorizeBestSource();
			for (int j = 0; j < 3000; j++) {
				bee.sendEmployedBees();
				bee.calculateProbabilities();
				bee.sendOnlookerBees();
				bee.memorizeBestSource();
				bee.sendScoutBees();
			}
		}
		for (int i = 0; i < bee.globalParams.length; i++) {
			System.out.println(bee.globalParams[i]);
		}
		System.out.println("globalMin：" + bee.globalMin);
	}
}
