package com.zhilibao.token;


public @interface Token {
	 boolean save() default false;
	 
	 boolean remove() default false;
} 
