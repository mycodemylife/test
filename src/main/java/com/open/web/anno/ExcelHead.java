package com.open.web.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHead {

	String value();
	/**
	 * 表格冻结数组
	 * @return
	 */
	int[] freeze() default {};
}
