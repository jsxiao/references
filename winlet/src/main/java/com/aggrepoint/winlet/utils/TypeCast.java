package com.aggrepoint.winlet.utils;

public class TypeCast {
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object obj) {
		return (T) obj;
	}
}
