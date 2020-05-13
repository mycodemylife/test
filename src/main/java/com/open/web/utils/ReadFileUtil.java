package com.open.web.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取文件使用的工具类
 * @author 程佳伟
 */
public class ReadFileUtil {

	/**
	 * 按行读取文件，返回集合
	 * @param path
	 * @return
	 */
	public static List<String> readFileLines(String path) {
		return readFileLines(path,"GB2312");
	}
	
	/**
	 * 按行读取指定编码方式的文件,返回集合
	 * @param path
	 * @return
	 */
	public static List<String> readFileLines(String path,String encoding) {
		File file = new File(path);
		if(!file.exists()) {
			return null;
		}
		if(!file.isFile()) {
			return null;
		}
		
		List<String> list = new ArrayList<>();
		BufferedReader bf  = null;
		try {

			bf = new BufferedReader(new InputStreamReader(new FileInputStream(file),encoding));
			String str;
			// 按行读取字符串
			while ((str = bf.readLine()) != null) {
				list.add(new String(str));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(null != bf) {
				try {
					bf.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return list;
	}
}
