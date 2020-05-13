package com.open.web.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class BeanToMapUtil {

	public static Map<String, Object> transform(Object object){
		if(null == object) {
			return null;
		}
		
		Map<String, Object> map = null;
		
		try {
			
			
			map = new HashMap<>();
			Field[] fields = {};
			Class clazz = object.getClass();
			while (clazz != null){
				fields = ArrayUtils.addAll(fields,clazz.getDeclaredFields());
				clazz = clazz.getSuperclass();
			}
			for(Field field : fields) {
				//关闭安全检查就可以达到提升反射速度的目的
				field.setAccessible(true);
				
				Alias alias = field.getAnnotation(Alias.class);
				if(null != alias && alias.isJoin()) {
					String text = field.getName();
					if(!StringUtils.isBlank(alias.value()) && alias.isUse()) {
						text = alias.value();
					}
					
					Object value = field.get(object);
					map.put(text, value);
				}
			}
		}catch (Exception e) {
			throw new RuntimeException("对象转换Map失败：[" + e.getMessage() + "]");
		}
		
		return map;
	}
}
