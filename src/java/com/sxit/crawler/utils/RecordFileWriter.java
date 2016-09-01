package com.sxit.crawler.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class RecordFileWriter {

	private static Logger log = LoggerFactory.getLogger(RecordFileWriter.class);
	public static final int SPLIT_MARK = 0xFF98; 
	
	/**
	 * 对象读取回调
	 * @author Administrator
	 *
	 */
	public static interface ObjectConvertCallBack {
		
		void convertObject(Object obj);
		
	}
	public synchronized static void wrietToTextFile(File file, String line, boolean append) {
		try {
			FileUtils.writeStringToFile(file, line+"\r\n", append);
		} catch (Exception e) {
			log.warn("数据写入错误, File:{}", file.getPath());
			if (log.isDebugEnabled()) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public synchronized static void wrietToTextFile(File file, Collection<?> lines, boolean append) {
		try {
			FileUtils.writeLines(file, lines, append);
		} catch (Exception e) {
			log.warn("数据写入错误, File:{}", file.getPath());
			if (log.isDebugEnabled()) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	/**
	 * 将HashMap写入文件中记录
	 * 文件结构：<br/>
	 * 4字节长度（len）
	 * len字节长数据
	 * 4字节分隔符
	 * 
	 * @param file
	 * @param data
	 */
	public synchronized static void writeToFile(File file, Map<?, ?> data) {
		if (CollectionUtils.isEmpty(data)) {
			return;
		}
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			byte dataContentByts[] = baos.toByteArray();//数据内容
			baos.close();
			byte dataContentLengthByts[] = ByteUtils.int2byte(dataContentByts.length);//数据长度
			byte splitMarkByts[] = ByteUtils.int2byte(SPLIT_MARK);//分隔符
			
			byte dataByts[] = new byte[dataContentByts.length+dataContentLengthByts.length+splitMarkByts.length];
			System.arraycopy(dataContentLengthByts, 0, dataByts, 0, dataContentLengthByts.length);
			System.arraycopy(dataContentByts, 0, dataByts, dataContentLengthByts.length, dataContentByts.length);
			System.arraycopy(splitMarkByts, 0, dataByts, dataContentLengthByts.length+dataContentByts.length, splitMarkByts.length);
			FileUtils.writeByteArrayToFile(file, dataByts, true);
		} catch (Exception e) {
			log.warn("数据写入错误, File:{}", file.getPath());
			if (log.isDebugEnabled()) {
				log.error(e.getMessage(), e);
			}
		}
	}
	
	public static void readFromFile(File file, ObjectConvertCallBack convertCallBack) {
		if (!file.exists() || !file.isFile() || !file.canRead()) {
			return;
		}
		
		try {
			RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
			boolean isEnd = false;
			while (!isEnd) {
				try {
					int dataLength = randomAccessFile.readInt();
					byte dataByts[] = new byte[dataLength];
					int readTotal = randomAccessFile.read(dataByts);
					while (readTotal < dataByts.length) {//如果不够，则需要进行二次读取
						int tmp = randomAccessFile.read(dataByts, readTotal, dataLength-readTotal);
						readTotal += tmp;
						if (tmp < 0 ) {
							isEnd = true;
							break;
						}
					}
					randomAccessFile.readInt();//读取4字节的分隔符
					ByteArrayInputStream bais = new ByteArrayInputStream(dataByts);
					ObjectInputStream ois = new ObjectInputStream(bais);
					Object obj = ois.readObject();
					convertCallBack.convertObject(obj);
				} catch (EOFException e) {
					isEnd = true;
				} catch (Exception e) {
					log.warn("读取数据出错,File:{}, Error:{}", file.getPath(), e.getMessage());
					e.printStackTrace();
					if (log.isDebugEnabled()) {
						log.error(e.getMessage(), e);
					}
					isEnd = true;
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 写数据
	 * @param args
	 * @throws Exception
	 */
	public static void main1(String[] args) throws Exception{
		File file = new File("d:/test.dat");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("x", 1);
		data.put("y", 2);
		data.put("z", "cccccc");
		writeToFile(file, data);
	}
	
	/**
	 * 读数据
	 * @param args
	 */
	public static void main(String[] args) {
		Set<String> keySet = new LinkedHashSet<String>();
// 		File file = new File("E:\\workspaces\\sxit\\crawl\\mobilephone_module\\data\\zol\\data\\zol_com_cn_mobileparam.data");
//		File file = new File("E:\\workspaces\\sxit\\crawl\\mobilephone_module\\data\\pconline\\data\\pconline_com_cn_mobileparam.data");
		File file = new File("E:\\workspaces\\sxit\\crawl\\mobilephone_module\\data\\pcpop\\data\\pcpop_com_mobileparam.data");
		final List<Map<String, Object>> listMap = new ArrayList<Map<String,Object>>();
		readFromFile(file, new ObjectConvertCallBack() {
			//@Override
			public void convertObject(Object obj) {
				Map<String, Object> map = (Map<String, Object>)obj;
				listMap.add(map);
			}
		});
		System.out.println(listMap.toString());
		System.out.println("=================================");
		int i=0;
		for (Map<String, Object> map : listMap) {
			i++;
			keySet.addAll(map.keySet());
		}
		System.out.println(i);
		System.out.println(keySet);
//		for (String key : keySet) {
//			try {
//				String str = new String(key.getBytes("GB18030"), "UTF-8");
//				System.out.println(str);
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}
}
