package com.open.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.core.io.support.PropertiesLoaderUtils;

/** 
* @author: wkn
* @date：2019年9月23日 上午11:14:15 
* 读取properties工具类
*/
public class PropertiesUtil {

	private PropertiesUtil() {
	}
	 /**
     * 根据key读取value
     * @param filePath
     * @param key
     * @return
     */
    public static String readValue(String filePath,String key) {
		Properties prop = null;
		String value = null;
		try {
			// 通过Spring中的PropertiesLoaderUtils工具类进行获取
			prop = PropertiesLoaderUtils.loadAllProperties(filePath);
			// 根据关键字查询相应的值
			value = prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
    }

	/**
	 * 加载整个配置文件
	 * @return
	 */
	private static Properties getProperties(String propertiesName) {
		Properties p = new Properties();
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
			p.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return p;
	}

	/**
	 * 获取全部的配置信息map
	 */
	public static Map<String,String> getValues(String propertiesName){
		Properties properties = getProperties(propertiesName);
		return new HashMap<>((Map)properties);
	}
}
