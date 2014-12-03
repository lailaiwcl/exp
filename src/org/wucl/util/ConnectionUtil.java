package org.wucl.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ConnectionUtil {
	private static Logger logger = Logger.getLogger(ConnectionUtil.class);
	private static String driver;
	private static String url;
	private static String username;
	private static String password;

	static {
		InputStream in = ConnectionUtil.class.getClassLoader()
				.getResourceAsStream("mysql.properties");
		loadResource(in);
	}

	/**
	 * 加载配置文件
	 * 
	 * @param in
	 *            配置文件流
	 */
	public static void loadResource(InputStream in) {
		Properties props = new Properties();
		try {
			props.load(in);
			if (props != null) {
				driver = props.getProperty("driver");
				url = props.getProperty("url");
				username = props.getProperty("username");
				password = props.getProperty("password");
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

	/**
	 * 获取数据库链接
	 * 
	 * @return 返回数据库链接
	 */
	public synchronized static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName(driver);
			try {
				conn = DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				logger.error(e);
			}
		} catch (ClassNotFoundException e) {
			logger.error(e);
		}
		return conn;
	}

	/**
	 * 关闭数据库链接
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 关闭 PreparedStatement
	 * 
	 * @param pstmt
	 */
	public static void closeStatement(PreparedStatement pstmt) {
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}

	}

	/**
	 * 关闭结果集
	 * 
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 关闭datasource
	 */
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
