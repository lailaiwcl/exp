package org.wucl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.wucl.abc.BeeColony;
import org.wucl.adhoc.AdHoc;
import org.wucl.adhoc.AdHoc2;
import org.wucl.bp.BPMain;

public class DataServlet extends HttpServlet {

	private static final long serialVersionUID = 2049044557490555723L;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf8");
		// response.setContentType("text/html;charset=utf8");
		String str = request.getRequestURI();
		String url = str.substring(str.lastIndexOf("/") + 1, str
				.lastIndexOf("."));
		if ("bpnn".equals(url)) {
			// 神经网络
			bpnn(request, response);
		} else if ("abc".equals(url)) {
			// 人工蜂群算法
			abc(request, response);
		} else if ("adhoc".equals(url)) {
			adhoc(request, response);
		} else if ("adhoc2".equals(url)) {
			adhoc2(request, response);
		} else if ("initadhoc".equals(url)) {
			initAdhoc();
		} else if ("evaluate".equals(url)) {
			evaluate(request, response);
		} else if ("initevaluate".equals(url)) {
			initevaluate();
		} else if ("readfromfileevaluate".equals(url)) {
			readfromfileevaluate(request, response);
		}
	}

	double c = 0;
	double o = 0;
	int count = 0;
	Reader reader = new InputStreamReader(DataServlet.class
			.getClassLoader().getResourceAsStream("a.txt"));
	BufferedReader buf = new BufferedReader(reader);

	public void evaluate(HttpServletRequest request,
			HttpServletResponse response) {
		double v0 = Double.parseDouble(request.getParameter("v0"));
		double v1 = Double.parseDouble(request.getParameter("v1"));
		double v2 = Double.parseDouble(request.getParameter("v2"));
		double v3 = Double.parseDouble(request.getParameter("v3"));
		double v4 = Double.parseDouble(request.getParameter("v4"));
		double v5 = Double.parseDouble(request.getParameter("v5"));
		double v6 = Double.parseDouble(request.getParameter("v6"));
		double v7 = Double.parseDouble(request.getParameter("v7"));
		double v8 = Double.parseDouble(request.getParameter("v8"));
		double v = 0.147 * v1 + 0.457 * v2 + 0.062 * v3 + 0.046 * v4 + 0.207
				* v6 + 0.080 * v5;
		c += 0.5 * (v0 + 0.2 * v) * v7 * v8;
		o += v0;
		count++;
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		out.write(count + "," + c + "," + o);
		out.flush();
		out.close();
		System.out.println(count + "," + c + "," + o);
		// System.out.println(v0 + ":" + v1 + ":" + v2 + ":" + v3 + ":" + v4 +
		// ":"
		// + v5 + ":" + v6 + ":" + v7 + ":" + v8);

	}

	public void readfromfileevaluate(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String r;
		while ((r = buf.readLine()) != null) {
			String[] sa = r.split("\t");
			System.out.println(sa.length);
			if (sa.length == 9) {
				double v0 = Double.parseDouble(sa[0]);
				double v1 = Double.parseDouble(sa[3]);
				double v2 = Double.parseDouble(sa[4]);
				double v3 = Double.parseDouble(sa[5]);
				double v4 = Double.parseDouble(sa[6]);
				double v5 = Double.parseDouble(sa[7]);
				double v6 = Double.parseDouble(sa[8]);
				double v7 = Double.parseDouble(sa[1]);
				double v8 = Double.parseDouble(sa[2]);
				double v = 0.147 * v1 + 0.457 * v2 + 0.062 * v3 + 0.046 * v4 + 0.207
						* v6 + 0.080 * v5;
				c += 0.5 * (v0 + 0.2 * v) * v7 * v8;
				o += v0;
				count++;
				PrintWriter out = null;
				try {
					out = response.getWriter();
				} catch (IOException e) {
					e.printStackTrace();
				}
				out.write(count + "," + c + "," + o);
				out.flush();
				out.close();
				System.out.println(count + "," + c + "," + o);
				return;
			}

		}
	}

	public void initevaluate() {
		c = 0;
		o = 0;
		count = 0;
		reader = new InputStreamReader(DataServlet.class
				.getClassLoader().getResourceAsStream("a.txt"));
		buf = new BufferedReader(reader);
	}

	AdHoc2 hoc = new AdHoc2();
	double x1 = 20.0 / 3;
	double x2 = 7.0 / 3;
	double u1 = 0;
	double u2 = 0;

	private void adhoc(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(x1 + "," + x2);
		out.write(x1 + "," + x2);
		out.flush();
		out.close();
		u1 = hoc.calU1(1, 1, x1, x2, 7, 1.0 / 3);
		u2 = hoc.calU2(1, 1, x1, x2, 7, 1.0 / 3);
		x1 = hoc.sigmoid(x1 + u1, 6.6);
		x2 = hoc.sigmoid(x2 + u2, 2.3);
	}

	AdHoc adhoc = new AdHoc();

	private void adhoc2(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(x1 + "," + x2);
		out.write(x1 + "," + x2);
		out.flush();
		out.close();
		u1 = adhoc.calU1(1, 1, x1, x2, 7, 1.0 / 3);
		u2 = adhoc.calU2(1, 1, x1, x2, 7, 1.0 / 3);
		x1 = hoc.sigmoid(x1 + u1, 6.6);
		x2 = hoc.sigmoid(x2 + u2, 2.3);
	}

	private void initAdhoc() {
		hoc = new AdHoc2();
		x1 = 20.0 / 3;
		x2 = 7.0 / 3;
		u1 = 0;
		u2 = 0;
	}

	private void bpnn(HttpServletRequest request, HttpServletResponse response) {
		try {
			int trainCount = Integer.parseInt(request
					.getParameter("trainCount"));
			int reaptTimes = Integer.parseInt(request
					.getParameter("reaptTimes"));
			BPMain bp = new BPMain();
			String errStr = bp.train(trainCount, reaptTimes);
			String corrSum = bp.autoTest(100000);
			PrintWriter out = response.getWriter();
			out.write(errStr + "," + corrSum);
			out.flush();
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void abc(HttpServletRequest request, HttpServletResponse response) {
		try {
			int boundNum = Integer.parseInt(request.getParameter("boundNum"));
			String orginalSite = request.getParameter("orginalSite");
			String[] s1 = orginalSite.trim().split(",");
			System.out.println(orginalSite);
			BeeColony bee = new BeeColony();
			int k = 0;
			bee.xx = new double[s1.length / 2];
			bee.yy = new double[s1.length / 2];
			for (int i = 0; i < s1.length;) {
				bee.xx[k] = Double.parseDouble(s1[i]);
				bee.yy[k] = Double.parseDouble(s1[++i]);
				++i;
				++k;
			}
			bee.boundNum = boundNum;
			bee.foodNumber = bee.xx.length;
			bee.initial();
			bee.memorizeBestSource();
			for (int j = 0; j < 10000; j++) {
				bee.sendEmployedBees();
				bee.calculateProbabilities();
				bee.sendOnlookerBees();
				bee.memorizeBestSource();
				bee.sendScoutBees();
			}

			PrintWriter out = response.getWriter();
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			DecimalFormat df = new DecimalFormat("#.0");
			for (int i = 0; i < bee.globalParams.length; i++) {
				sb.append("" + df.format(bee.globalParams[i].x) + ","
						+ df.format(bee.globalParams[i].y) + ",");
			}
			sb.append(df.format(bee.globalMin));
			sb.append("]");
			System.out.println(sb);
			out.write(sb.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
