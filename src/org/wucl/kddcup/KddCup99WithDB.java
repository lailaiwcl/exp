package org.wucl.kddcup;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Sqls;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.impl.NutIoc;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.ioc.loader.json.JsonLoader;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Param;
import org.wucl.bean.KddCupBean;
import org.wucl.util.DBUtil;

@At("/kddcup99")
@IocBean
public class KddCup99WithDB {

	public static NutDao dao = null;
	{
		Ioc ioc = new NutIoc(new JsonLoader("ioc.js"));
		dao = ioc.get(NutDao.class, "dao");
	}
	static String[] attributeText = { "连接持续时间", "协议类型", "网络服务类型", "网络连接状态",
			"源到目标字节数", "目标到源字节数", "是否同一主机", "错误分段数", "加急包数", " 访问敏感文件数",
			"登录尝试失败数", "是否成功登陆", "compromised次数", "是否获得root", "是否若出现su root",
			"root用户访问数", "文件创建次数", "使用shell命令数", "访问控制文件数", "FTP会话中出站数",
			"登录是否属于hot", "是否guest登录", "同机连接数", "同目标服务连接数", "前2s同机SYN错误比",
			"同服务SYN错误比", "同主机REJ错误比", "同服务REJ错误比", "同机同服务比", "同机不同服务比",
			"同服务不同机比", "前100同机数", "前100同服务数", "前100同机同服务比", "同机不同服务", "同机同端口",
			"同目标不同源", "同机SYN错误比", "同服务SYN错误比", "同机REJ错误比", "同服务REJ错误比", "入侵类型" };

	@At("/listLabel")
	public List<Map<String, String>> getclassLabel() {
		String sqlStr = "select distinct label as c1 from kddcup99_corrected";
		Sql sql = Sqls.create(sqlStr.toString());
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next())
					list.add(rs.getString(1));
				return list;
			}
		});
		dao.execute(sql);
		List<String> list = sql.getList(String.class);
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		for (String text : list) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("text", text);
			mapList.add(map);
		}
		return mapList;
	}

	@At("/listAttribute")
	public List<KeyValueBean> getAttribute() throws SQLException {
		List<String> list = DBUtil.getColumnsName("kddcup99_corrected");
		List<KeyValueBean> mapList = new ArrayList<KeyValueBean>();
		for (int i = 0; i < list.size() - 1; i++) {
			mapList.add(new KeyValueBean(list.get(i + 1), attributeText[i]));
		}
		return mapList;
	}

	@At("/getAttributeType")
	public int getAttributeType(@Param("attributeName") String attributeName)
			throws SQLException {
		return DBUtil.getColumnType("kddcup99_corrected", attributeName);
	}

	@At("/getUnContinueValue")
	public List<KddCupBean> getUnContinueValue(
			@Param("attributeName") String attributeName,
			@Param("labels") String labels) {
		labels = labels.replaceAll(",", "','");
		String sqlStr = "select " + attributeName + ",count(" + attributeName
				+ ") from kddcup99_corrected where label in('" + labels
				+ "') group by " + attributeName;
		// System.out.println("sqlStr--" + sqlStr);
		Sql sql = Sqls.create(sqlStr.toString());
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				List<KddCupBean> list = new ArrayList<KddCupBean>();
				while (rs.next()) {
					KddCupBean bean = new KddCupBean();
					bean.setKey(rs.getString(1));
					bean.setValue(rs.getInt(2));
					list.add(bean);
				}
				return list;
			}
		});
		dao.execute(sql);
		List<KddCupBean> list = sql.getList(KddCupBean.class);
		return list;
	}

	@At("/getContinueValue")
	public List<KddCupBean> getContinueValue(
			@Param("attributeName") String attributeName,
			@Param("valueStep") String valueStep, @Param("labels") String labels) {
		labels = labels.replaceAll(",", "','");
		List<Double> listStep = new ArrayList<Double>();
		String[] value = valueStep.split(",");
		for (int i = 0; i < 11; i++) {
			listStep.add(Double.parseDouble(value[i]));
		}
		List<Integer> listValue = new ArrayList<Integer>();
		for (int i = 0; i < listStep.size() - 1; i++) {
			String op = "<";
			if (i == 9) {
				op = "<=";
			}
			String sqlCalc = "select count(*)  from kddcup99_corrected where "
					+ attributeName + " >= " + listStep.get(i) + " and "
					+ attributeName + op + listStep.get(i + 1)
					+ "and label in('" + labels + "')";
			Sql sql3 = Sqls.create(sqlCalc.toString());
			sql3.setCallback(new SqlCallback() {
				public Object invoke(Connection conn, ResultSet rs, Sql sql)
						throws SQLException {
					List<Integer> list = new ArrayList<Integer>();
					while (rs.next())
						list.add(rs.getInt(1));
					// System.out.println("list" + list);
					return list;
				}
			});
			dao.execute(sql3);
			listValue.add(sql3.getList(Integer.class).get(0));

		}
		List<KddCupBean> list = new ArrayList<KddCupBean>();
		for (int j = 0; j < 10; j++) {
			String op = ")";
			if (j == 9) {
				op = "]";
			}
			KddCupBean bean = new KddCupBean();
			bean.setKey("[" + listStep.get(j) + "-" + listStep.get(j + 1) + op);
			bean.setValue(listValue.get(j));
			list.add(bean);
		}
		System.out.println(list);
		return list;
	}

	@At("/getAttributeStep")
	public List<Double> getAttributeStep(
			@Param("attributeName") String attributeName,
			@Param("labels") String labels) {
		labels = labels.replaceAll(",", "','");
		String sqlMin = "select min(" + attributeName
				+ ") from kddcup99_corrected where label in('" + labels + "')";
		Sql sql = Sqls.create(sqlMin.toString());
		sql.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next())
					list.add(rs.getString(1));
				return list;
			}
		});
		dao.execute(sql);
		Double min = Double.parseDouble(sql.getList(String.class).get(0));

		String sqlMax = "select max(" + attributeName
				+ ") from kddcup99_corrected where label in('" + labels + "')";
		Sql sql2 = Sqls.create(sqlMax.toString());
		sql2.setCallback(new SqlCallback() {
			public Object invoke(Connection conn, ResultSet rs, Sql sql)
					throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next())
					list.add(rs.getString(1));
				// System.out.println("list" + list);
				return list;
			}
		});
		dao.execute(sql2);
		Double max = Double.parseDouble(sql2.getList(String.class).get(0));

		double step = (max - min) / 10;
		List<Double> listStep = new ArrayList<Double>();
		// double currentValue = min;
		BigDecimal bd = new BigDecimal(min + "");
		for (int i = 0; i <= 10; i++) {
			listStep.add(bd.doubleValue());
			// currentValue += step;
			bd = bd.add(new BigDecimal(step + ""));
		}
		return listStep;
	}

	public static void main(String[] args) throws SQLException {
		KddCup99WithDB k = new KddCup99WithDB();
		// System.out.println(k.getContinueValue("rerror_rate",
		// "0.0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0"));
	}

}
