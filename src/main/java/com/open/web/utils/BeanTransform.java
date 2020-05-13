package com.open.web.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= {ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface BeanTransform {

	/**
	 * 实体类转化为Map时的别名
	 */
	String value();
	/**
	 * 实体类转化为Map时，是否使用别名作为key
	 */
	boolean isUse() default true;
	/**
	 * 实体类转化为Map时，是否参与转化
	 */
	boolean isJoin() default true;
	/**
	 * 将数组中的内容赋值给bean时，对应的数组下标，-1时表示不对其进行赋值
	 * @return
	 */
	int index() default -1;
}
