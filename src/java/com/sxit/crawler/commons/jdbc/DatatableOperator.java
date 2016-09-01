package com.sxit.crawler.commons.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.BiMap;

/**
 * 数据表操作类：
 * 
 * @author Administrator
 *
 */
public class DatatableOperator {
	
	private Logger log = LoggerFactory.getLogger(DatatableOperator.class);

	/**
	 * 双向MAP
	 * 数据表字段与源字段对应关系<br/>
	 * key为column，value为对应的源字段
	 * 如：<br/>
	 * 
	 */
	private final BiMap<String, String> columnRelationMap;
	
	private DatatableConfig datatableConfig;
	
	private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public DatatableOperator(DatatableConfig datatableConfig, JdbcTemplate jdbcTemplate) {
		super();
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(jdbcTemplate);
		this.datatableConfig = datatableConfig;
		this.columnRelationMap = datatableConfig.getColumnRelationMap();
	}
	
	
	/**
	 * 将源字段与值的key-value对应关系翻译成column-value的对应关系
	 * @param row
	 * @return
	 */
	public Map<String, Object> transRow(Map<String, Object> row) {
		Map<String, Object> columnValueMap = new HashMap<String, Object>();
		for (String column : columnRelationMap.keySet()) {
			String srcKey = columnRelationMap.get(column);//字段对应的原始数据字段
			Object srcVal = row.get(srcKey);//原始数据字段值
			columnValueMap.put(column, srcVal);
		}
		return columnValueMap;
	}
	
	/**
	 * 将源字段与值的key-value对应关系翻译成column-value的对应关系
	 * @param row
	 * @return
	 */
	public List<Map<String, Object>> transRow(List<Map<String, Object>> rows) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		if (!CollectionUtils.isEmpty(rows)) {
			for (Map<String, Object> row : rows) {
				resultList.add(transRow(row));
			}
		}
		return resultList;
	}
	
	/**
	 * 保存一行原始数据（没有经过key-->column字段翻译的
	 * @param sourceRow
	 * @return
	 */
	public boolean saveData(Map<String, Object> sourceRow) {
		return saveColumnData(transRow(sourceRow));
	}
	
	public synchronized boolean saveData(Map<String, Object> sourceRow, boolean checkUnique) {
		return saveColumnData(transRow(sourceRow), checkUnique);
	}

	/**
	 * 保存多行原始数据（没有经过key-->column字段翻译的
	 * @param sourceRow
	 * @return
	 */
	public boolean saveData(List<Map<String, Object>> sourceRows) {
		return saveColumnData(transRow(sourceRows));
	}
	/**
	 * 保存多行原始数据（没有经过key-->column字段翻译的
	 * @param sourceRow
	 * @return
	 */
	public boolean saveData(List<Map<String, Object>> sourceRows, boolean checkUnique) {
		return saveColumnData(transRow(sourceRows), checkUnique);
	}
	
	/**
	 * 保存一行数据
	 * @param row key为字段，value为字段对应的值
	 * @return
	 */
	public boolean saveColumnData(Map<String, Object> row) {
		return saveColumnData(row, false);
	}
	
	/**
	 * 保存一行数据
	 * @param row key为字段，value为字段对应的值
	 * @param checkUnique检查唯一性，如果为true，则对数据做唯一性校验，如果已经存在相同数据，则不插入。
	 * @return
	 */
	public boolean saveColumnData(Map<String, Object> row, boolean checkUnique) {
		if (CollectionUtils.isEmpty(row))
			return false;
		
		if(checkUnique) {
			if (StringUtils.isNotBlank(datatableConfig.getUniqueSql()) && existsData(row)) {
				log.warn("数据已经存在：{}:{}", datatableConfig.getUniqueColumn(), row.get(datatableConfig.getUniqueColumn()));
				return false;
			}else{
				//if (log.isDebugEnabled()) {
					log.info("保存数据:{}", row);
				//}
				
			}
		}
		
		String sql = datatableConfig.getInsertSql();
		return simpleJdbcTemplate.update(sql, row)>0;
	}
	
	/**
	 * 保存多行数据(不保证数据唯一性)
	 * @param rows row key为字段，value为字段对应的值
	 * @return
	 */
	public boolean saveColumnData(List<Map<String, Object>> rows) {
		return saveColumnData(rows, false);
	}
	
	/**
	 * 保存多行数据
	 * @param rows row key为字段，value为字段对应的值
	 * @param checkUnique检查唯一性，如果为true，则对数据做唯一性校验，如果已经存在相同数据，则不插入。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public boolean saveColumnData(List<Map<String, Object>> rows, boolean checkUnique) {
		if (CollectionUtils.isEmpty(rows)) 
			return false;
		if (log.isDebugEnabled()) {
			log.info("保存数据:{}", rows);
		}
		
		if (checkUnique) {
			rows = filterExistsData(rows);//过滤已经存在的数据
		}
		if(rows.size()>0){
			log.info("save uniqe data size:"+rows.size());
			String sql = datatableConfig.getInsertSql();
			Map<String, Object>[] batchValues = new Map[rows.size()]; 
			batchValues = rows.toArray(batchValues);
			int[] resultRows = simpleJdbcTemplate.batchUpdate(sql, batchValues);
			return resultRows.length > 0;
		}else{
			return true;
		}
	}
	
	private List<Map<String, Object>> filterExistsData(List<Map<String, Object>> rows) {
		if (StringUtils.isNotBlank(datatableConfig.getUniqueSql())) {
			List<Map<String, Object>> uniqueRows = new ArrayList<Map<String,Object>>();
			//过滤已经存在的数据
			for (Map<String, Object> row : rows) {
				if (existsData(row)) {
					log.warn("数据已经存在：{}:{}", datatableConfig.getUniqueColumn(), row.get(datatableConfig.getUniqueColumn()));
					continue;
				} else {
					uniqueRows.add(row);
				}
			}
			rows = uniqueRows;
		}
		return rows;
	}
	
	/**
	 * 判断是否存在该条记录，依据unique_sql配置进行查找
	 * @param row
	 * @return 存在返回true，不存在返回false
	 */
	public boolean existsData(Map<String, Object> row) {
		Long count = simpleJdbcTemplate.queryForLong(datatableConfig.getUniqueSql(), row);
		if (count == null || count <= 0) {
			return false;
		} else {
			return true;
		}
	}
	/**
	 * 判断是否存在该条记录，依据sql进行查找
	 * @param row
	 * @return 存在返回true，不存在返回false
	 */
	public boolean existsData(String sql,Object[] object) {
		Long count = simpleJdbcTemplate.queryForLong(sql,object);
		if (count == null || count <= 0) {
			return false;
		} else {
			return true;
		}
	}
}
