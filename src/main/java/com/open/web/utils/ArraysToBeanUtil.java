package com.open.web.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 使用数组内容和对象字节码创建对应的对象
 * 
 * @author 程佳伟
 */
public class ArraysToBeanUtil {

	private static List<String> packageClasses = Arrays.asList("java.lang.Short","java.lang.Integer","java.lang.Long",
			"java.lang.Char","java.lang.Byte","java.lang.Float","java.lang.Double","java.lang.Boolean");
	private static List<String> dateClasses = Arrays.asList("java.util.Date");
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object transform(Object[] arr,Class clazz) {
		
		if(null == arr || arr.length == 0 || null == clazz) {
			return null;
		}
		
		Object obj = null;
		Constructor constructor = null;
		
		try {
			constructor = clazz.getConstructor();
			obj = constructor.newInstance();
			
			List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
			
			for(Field field : fields) {
				BeanTransform btf = field.getAnnotation(BeanTransform.class);
				if(null == btf || btf.index() < 0) {
					continue;
				}
				
				int index = btf.index();
				if(index >= 0 && index < arr.length) {
					field.set(obj, arr[index]);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return obj;
	}
	
	/**
	 * 将字符串数据数组转换为对象，目前仅仅支持基本类型的包装类、Date类和String类型数组
	 * @param arr
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T transform(String[] arr,Class<T> clazz) {
		
		if(null == arr || arr.length == 0 || null == clazz) {
			return null;
		}
		
		T t = null;
		Constructor constructor = null;
		
		try {
			constructor = clazz.getConstructor();
			t = (T) constructor.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
		
		for(Field field : fields) {
			BeanTransform btf = field.getAnnotation(BeanTransform.class);
			if(null == btf || btf.index() < 0) {
				continue;
			}
			
			try {
				field.setAccessible(true);
				int index = btf.index();
				String fieldTypeName = null;
				if(index >= 0 && index < arr.length) {
					
					Class fieldType = field.getType();
					fieldTypeName = fieldType.getTypeName();
					if(null != fieldTypeName && packageClasses.contains(fieldTypeName)) {
						if(StringUtils.isNotBlank(arr[index])) {
							Method valueOfMethod = fieldType.getMethod("valueOf", String.class);
							field.set(t, valueOfMethod.invoke(null, arr[index]));
						}else {
							field.set(t,null);
						}
					}else if(null != fieldTypeName && "java.lang.String".equals(fieldTypeName)) {
						field.set(t, arr[index]);
					}else if(null != fieldTypeName && dateClasses.contains(fieldTypeName)) {
						if(StringUtils.isNotBlank(arr[index])) {
							DateTimeFormat dtf = field.getAnnotation(DateTimeFormat.class);
							String pattern = dtf.pattern();
							SimpleDateFormat sdf = null;
							if(StringUtils.isNotBlank(pattern)) {
								sdf = new SimpleDateFormat(pattern);
							}else {
								sdf = new SimpleDateFormat("yyyy-MM-dd");
							}
						
							field.set(t, sdf.parse(arr[index]));
						}else {
							field.set(t,null);
						}
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return t;
	}
	
	public static void main(String[] args) {
		
		String path = "D:\\Test\\botnet_trojon.txt";
		
		List<String> list = ReadFileUtil.readFileLines(path);
//		List<WaBotnetTrojon> botnetTrojons = new ArrayList<WaBotnetTrojon>();
//		WaBotnetTrojon botnetTrojon = null;
		if(null != list) {
			for(String str : list) {
				String[] arr = str.split("\t");
//				System.out.println(Arrays.toString(arr));
//				botnetTrojon = ArraysToBeanUtil.transform(arr, WaBotnetTrojon.class);
//				botnetTrojons.add(botnetTrojon);
			}
		}
		
//		for(WaBotnetTrojon bt : botnetTrojons) {
//			System.out.println("botnetTrojons : " + bt);
//		}
	}
}
