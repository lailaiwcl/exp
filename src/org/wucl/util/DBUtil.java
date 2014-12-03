package org.wucl.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {

	/**
	 * 获取数据库表名和视图
	 * 
	 * @param conn
	 *            数据库连接
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getTablesName(Connection conn)
			throws SQLException {
		if (conn == null || conn.isClosed()) {
			return null;
		}
		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		try {
			DatabaseMetaData meta = conn.getMetaData();
			rs = meta.getTables(null, null, null, new String[] { "TABLE",
					"VIEW" });
			while (rs.next()) {
				list.add(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally {
			ConnectionUtil.closeResultSet(rs);
			ConnectionUtil.closeConnection(conn);
		}
		return list;
	}

	public static List<String> getColumnsName(String tableName)
			throws SQLException {
		Connection conn = ConnectionUtil.getConnection();
		List<String> list = new ArrayList<String>();
		try {
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getColumns(null, null, tableName, null);
			if (rs == null) {
				return null;
			}
			while (rs.next()) {
				list.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return list;
	}

	public static int getColumnType(String tableName, String columnName)
			throws SQLException {
		Connection conn = ConnectionUtil.getConnection();
		try {
			DatabaseMetaData meta = conn.getMetaData();
			ResultSet rs = meta.getColumns(null, null, tableName, null);
			if (rs == null) {
				return -1;
			}
			while (rs.next()) {
				if(rs.getString("COLUMN_NAME").equalsIgnoreCase(columnName)){
					return rs.getInt("DATA_TYPE");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		return -1;
	}

}
