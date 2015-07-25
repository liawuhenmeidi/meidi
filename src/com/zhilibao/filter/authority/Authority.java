package com.zhilibao.filter.authority;


 
import java.lang.annotation.ElementType;   
import java.lang.annotation.Retention;   
import java.lang.annotation.RetentionPolicy;   
import java.lang.annotation.Target; 
      
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
 
 
public @interface Authority { 
	 boolean isverification() default false;
	   
	 boolean verification() default false;
} 
