package com.sxit.crawler.commons.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class DatatableConfig {
	
	public final static String URL_MD5_KEY = "SYS_URL_MD5";
	
	public final static String URL_KEY = "SYS_URL";

	/**
	 * 表名称
	 */
	private final String tableName;
	
	/**
	 * 唯一字段，用于标识数据唯一性
	 */
	private String uniqueColumn;
	
	/**
	 * 判断唯一值的执行SQL
	 */
	private String uniqueSql;
	
	/**
	 * 表字段
	 */
	private final List<ColumnConfig> columnItems;
	
	/**
	 * 双向MAP
	 * 数据表字段与源字段对应关系<br/>
	 * key为column，value为对应的源字段
	 * 如：<br/>
	 * 
	 */
	private BiMap<String, String> columnRelationMap;
	
	/**
	 * 保存sql
	 */
	private String insertSql;
	
	private DatatableConfig(String tableName) {
		this.tableName = tableName;
		this.columnItems = new ArrayList<DatatableConfig.ColumnConfig>();
		this.columnRelationMap = HashBiMap.create();
	}
	
	public static DatatableConfig createDatatableConfigByXml(String xmlFileName) {
		return DatatableXmlConfigHelper.parse(xmlFileName);
	}
	
	/**
	 * 创建数据表对对象配置实例
	 * @param tableName
	 * @return
	 */
	public static DatatableConfig createDatatableConfig(String tableName) {
		return new DatatableConfig(tableName);
	}

	/**
	 * 构建配置（在所有的参数设置妥当之后）：<br/>
	 * 1、构建columnRelationMap中表字段和源数据字段的双向MAP对应关系表<br/>
	 * 2、构建插入sql，依据字段配置，给出了默认值的则直接使用默认值拼如SQL，没有给出默认值的都作为参数写入数据库<br/>
	 * @return 返回对象本身
	 */
	public DatatableConfig buildConfig(){
		if (columnRelationMap.size() > 0) {
			columnRelationMap.clear();
		}
		//1、构建columnRelationMap中表字段和源数据字段的双向MAP对应关系表
		for (ColumnConfig columnConfig : columnItems) {
			if (StringUtils.isBlank(columnConfig.getDefaultValue())) {//把有默认值的列排除
//				System.out.printf("%s:%s\r\n", columnConfig.getColumnName(), columnConfig.getSrcKey());
				columnRelationMap.put(columnConfig.getColumnName(), columnConfig.getSrcKey());
			}
		}
		
		
		//2、构建插入sql
		StringBuffer commBuffer = new StringBuffer();
		StringBuffer columnBuffer = new StringBuffer();
		StringBuffer valueBuffer = new StringBuffer();
		for (int i=0; i<columnItems.size(); i++) {
			ColumnConfig columnConfig = columnItems.get(i);
			columnBuffer.append(columnConfig.getColumnName());
			
			if (StringUtils.isNotBlank(columnConfig.getDefaultValue())) {
				valueBuffer.append(columnConfig.getDefaultValue());
			} else {
				valueBuffer.append(":"+columnConfig.getColumnName());
			}
			if (i<(columnItems.size()-1)) {
				columnBuffer.append(", ");
				valueBuffer.append(", ");
			}
		}
		commBuffer.append("insert into ").append(tableName).append(" ");
		commBuffer.append("(");
		commBuffer.append(columnBuffer);
		commBuffer.append(")").append(" values (").append(valueBuffer).append(")");
		insertSql = commBuffer.toString();
		return this;
	}
	
	/**
	 * 增加字段
	 * @param columnConfig
	 * @return
	 */
	public DatatableConfig addColumn(ColumnConfig columnConfig) {
		if (!columnItems.contains(columnConfig)) {
			columnItems.add(columnConfig);
		}
		return this;
	}
	
	/**
	 * 增加字段
	 * @param columnName 字段名称
	 * @param srcKey     原始数据字段名称
	 * @return 
	 */
	public DatatableConfig addColumn(String columnName, String srcKey) {
		ColumnConfig columnConfig = buildColumnConfig(columnName, srcKey);
		if (!columnItems.contains(columnConfig)) {
			columnItems.add(columnConfig);
		}
		return this;
	}
	
	
	/**
	 * 增加字段
	 * @param columnName 字段名称
	 * @param srcKey     原始数据字段名称
	 * @param columnType 字段类型
	 * @param defaultValue 默认值（如：sysdate,seq_table_abc.nextval, 0,等）
	 * @return
	 */
	public DatatableConfig addColumn(String columnName, String srcKey, Class<?> columnType, String defaultValue) {
		ColumnConfig columnConfig = buildColumnConfig(columnName, srcKey, columnType, defaultValue);
		if (!columnItems.contains(columnConfig)) {
			columnItems.add(columnConfig);
		}
		return this;
	}
	
	/**
	 * 增加字段
	 * @param columnName 字段名称
	 * @param srcKey     原始数据字段名称
	 * @param defaultValue 默认值（如：sysdate,seq_table_abc.nextval, 0,等）
	 * @return
	 */
	public DatatableConfig addColumn(String columnName, String srcKey, String defaultValue) {
		ColumnConfig columnConfig = buildColumnConfig(columnName, srcKey, Object.class, defaultValue);
		if (!columnItems.contains(columnConfig)) {
			columnItems.add(columnConfig);
		}
		return this;
	}
	
	
	/**
	 * 简单的字段构造器
	 * @param columnName    字段名称
	 * @param srcKey        原始数据字段名称
	 * @return 返回字段配置实例
	 */
	public static ColumnConfig buildColumnConfig(String columnName, String srcKey) {
		return buildColumnConfig(columnName, srcKey, Object.class, null);
	}
	
	/**
	 * 字段构造器（所有参数）
	 * @param columnName    字段名称
	 * @param srcKey        原始数据字段名称
	 * @param columnType    字段类型
	 * @param defaultValue  默认值（如：sysdate,seq_table_abc.nextval, 0,等）
	 * @return 返回字段配置实例
	 */
	public static ColumnConfig buildColumnConfig(String columnName, String srcKey, Class<?> columnType, String defaultValue) {
		ColumnConfig columnConfig = new ColumnConfig();
		columnConfig.setColumnName(columnName);
		columnConfig.setSrcKey(srcKey);
		columnConfig.setColumnType(columnType);
		columnConfig.setDefaultValue(defaultValue);
		return columnConfig;
	}
	
	public String getTableName() {
		return tableName;
	}

	public List<ColumnConfig> getColumnItems() {
		return columnItems;
	}

	public BiMap<String, String> getColumnRelationMap() {
		return columnRelationMap;
	}

	public String getInsertSql() {
		return insertSql;
	}



	public static class ColumnConfig {
		
		/**
		 * 字段名称
		 */
		private String columnName;
		
		/**
		 * 字段类型
		 */
		private Class<?> columnType;
		
		/**
		 * 原始数据中的字段
		 */
		private String srcKey;
		
		/**
		 * 默认值（如：sysdate,seq_table_abc.nextval, 0,等）
		 */
		private String defaultValue;

		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public Class<?> getColumnType() {
			return columnType;
		}

		public void setColumnType(Class<?> columnType) {
			this.columnType = columnType;
		}

		public String getSrcKey() {
			return srcKey;
		}

		public void setSrcKey(String srcKey) {
			this.srcKey = srcKey;
		}

		public String getDefaultValue() {
			return defaultValue;
		}

		public void setDefaultValue(String defaultValue) {
			this.defaultValue = defaultValue;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((columnName == null) ? 0 : columnName.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ColumnConfig other = (ColumnConfig) obj;
			if (columnName == null) {
				if (other.columnName != null)
					return false;
			} else if (!columnName.equals(other.columnName))
				return false;
			return true;
		}
		
	}



	public String getUniqueSql() {
		return uniqueSql;
	}

	public void setUniqueSql(String uniqueSql) {
		this.uniqueSql = uniqueSql;
	}

	public String getUniqueColumn() {
		return uniqueColumn;
	}

	public void setUniqueColumn(String uniqueColumn) {
		this.uniqueColumn = uniqueColumn;
	}
	
}
