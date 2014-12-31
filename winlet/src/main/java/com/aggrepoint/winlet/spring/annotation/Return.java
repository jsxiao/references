package com.aggrepoint.winlet.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author Jiangming Yang (yangjm@gmail.com)
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Return {
	static final String NOT_SPECIFIED = "!!NOT_SPECIFIED!!";

	Code[] value() default {};

	String update() default "";

	boolean dialog() default false;

	boolean cache() default false;

	String log() default "";

	String msg() default "";

	String logexclude() default "";

	String title() default "";

	String view() default NOT_SPECIFIED;
}
