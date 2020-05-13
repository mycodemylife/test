package com.open.web.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


/**
 * 属性文件读取
 * @author liubin
 *
 */
public class PropertiesReader {

	public static String PROPERTY_FILE = "";

	private static PropertiesReader instance = null;

	private static Properties paraProps = new Properties();
	
	//读取配置文件
	private PropertiesReader() {

		//InputStream is = getClass().getResourceAsStream(PROPERTY_FILE);
		
		try {
			File propFile = new File(PROPERTY_FILE);
			FileInputStream fis = new FileInputStream(propFile);
			BufferedInputStream in = new BufferedInputStream(fis);
			
			paraProps.load(in);
			
		}catch (Exception e) {
			
			System.err.println("Can not read properties file!");
		
		}
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static synchronized PropertiesReader getInstance(String fileName) {
		
		if (instance == null) {
			PROPERTY_FILE = fileName;
			instance = new PropertiesReader();
		}
		return instance;

	}

	/**
	 * 获取属性
	 * @param paraName
	 * @return
	 */
	public String getProperty(String paraName) {
		
		String property = "";
		try {
			property = paraProps.getProperty(paraName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return property;
	}
}
