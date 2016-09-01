package com.sxit.crawler.commons.jdbc;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import com.sxit.crawler.commons.exception.SystemException;

/**
 * <strong>Spring JDBC DataRow映射器</strong><br>
 * 列名与对象动态映射类<br>
 * <p>
 * 该类能够提供列名到对象的动态映射，但是其所对应的类的字段名称必须和数据库中的字段名称保持一致，
 * 且该类的定义必须符合JavaBean规范（默认提供一个公共的无参构造函数，必须对所有需要保持映射的字段
 * 提供一个规范的getter和setter方法）。
 * </p>
 * @since 1.0
 * @version $Id: ColumnObjectRowMapper.java,v 1.1 2009/07/19 02:06:07 liangexiang Exp $
 */
public class ColumnObjectRowMapper implements RowMapper{
//	private Log log = LogFactory.getLog(ColumnObjectRowMapper.class);
	private Class<?> clazz;
	private Map<String, Method> methodMap;
	public ColumnObjectRowMapper(Class<?> clazz) {
		this.clazz = clazz;
		setFiledMethod();
	}
	
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		Object instance = null;
		try {
			instance = clazz.newInstance();
		} catch (Exception e) {
			throw new SystemException(e);
		}
		for (int i=1; i<=columnCount; i++) {
			String key = getColumnKey(JdbcUtils.lookupColumnName(rsmd, i));
			key = key.replaceAll("_", "");
			Method method = methodMap.get(key.toLowerCase());
			if (null == method) {
				continue;
			} else {
				Type types[] = method.getGenericParameterTypes();
				if (types.length <=0 || types.length > 1) {
					throw new SystemException(key+" is not bean.");
				} else {
					Type type = types[0];
					Object obj = JdbcUtils.getResultSetValue(rs, i, (Class<?>) type);
					if (null != obj) {
						try {
							method.invoke(instance, obj);
						} catch (Exception e) {
							throw new SystemException(e);
						}
					}
				}
			}
		}
		return instance;
	}
	
	
	public String getSetMethodName(String name) {
		return "set"+name.substring(0, 1).toUpperCase()+name.substring(1, name.length());
	}

	private String getColumnKey(String columnName) {
		return columnName;
	}
	
	private void setFiledMethod() {
		methodMap = new HashMap<String, Method>();
		Method methods[] = clazz.getMethods();
		for (int i=0; i<methods.length; i++) {
			Method m = methods[i];
			String methodName = (m.getName());
			if (methodName.startsWith("set")) {
				String filedStr = methodName.substring(3, methodName.length());
				filedStr = filedStr.substring(0, 1).toLowerCase()+filedStr.substring(1, filedStr.length());
				methodMap.put(filedStr.toLowerCase(), m);
			}
		}
	}
}
