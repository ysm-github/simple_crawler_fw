package com.sxit.crawler.commons.jdbc;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Throwables;
import com.sxit.crawler.commons.jdbc.DatatableConfig.ColumnConfig;

/**
 * 依据XML文件构建数据表配置
 * @author Administrator
 *
 */
public class DatatableXmlConfigHelper {

	private Document document;
	
	private DatatableXmlConfigHelper(String fileName) {
		ClassPathResource resource = new ClassPathResource(fileName);
		try {
			document = Jsoup.parse(resource.getFile(), "UTF-8");
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}
	
	public static DatatableConfig parse(String fileName) {
		DatatableXmlConfigHelper helper = new DatatableXmlConfigHelper(fileName);
		String tableName = helper.getTableName();
		DatatableConfig datatableConfig = DatatableConfig.createDatatableConfig(tableName);
		datatableConfig.setUniqueSql(helper.getUniqueSql());
		datatableConfig.setUniqueColumn(helper.getUniqueColumn());
		List<ColumnConfig> columnConfigs = helper.getColumnConfigs();
		if (!CollectionUtils.isEmpty(columnConfigs)) {
			for (ColumnConfig columnConfig : columnConfigs) {
				datatableConfig.addColumn(columnConfig);
			}
		}
		return datatableConfig;
		
	}
	
	public String getTableName() {
		return document.select("dataTableConfig>tableName").first().text();
	}
	
	public String getUniqueColumn() {
		Element elt = document.select("dataTableConfig>unique_column").first();
		return elt.attr("value");
	}
	public String getUniqueSql() {
		Element elt = document.select("dataTableConfig>unique_sql").first();
		if (null != elt) {
			return elt.text();
		} else {
			return null;
		}
	}
	
	public List<DatatableConfig.ColumnConfig> getColumnConfigs() {
		List<DatatableConfig.ColumnConfig> list = new ArrayList<DatatableConfig.ColumnConfig>();
		Elements elts = document.select("dataTableConfig>columns>column");
		if (!CollectionUtils.isEmpty(elts)) {
			for (Element elt : elts) {
				String columnName = elt.attr("columnName");
				String srcKey = elt.attr("srcKey");
				String columnType = elt.attr("columnType");
				String defaultValue = elt.attr("defaultValue");
				Class<?> columnTypeClass = null;
				if (StringUtils.isNotBlank(columnType)) {
					try {
						columnTypeClass = ClassUtils.getClass(columnType);
					} catch (Exception e) {
						Throwables.propagate(e);
					}
				}
				list.add(DatatableConfig.buildColumnConfig(columnName, srcKey, columnTypeClass, defaultValue));
			}
		}
		return list;
	}
}
