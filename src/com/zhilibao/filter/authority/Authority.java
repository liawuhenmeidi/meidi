package com.zhilibao.filter.authority;

 
  
import java.lang.annotation.ElementType;   
import java.lang.annotation.Retention;   
import java.lang.annotation.RetentionPolicy;   
import java.lang.annotation.Target;
import java.util.List; 
       
@Target(ElementType.METHOD)  
@Retention(RetentionPolicy.RUNTIME)
 
   
public @interface Authority {
	        
     String[] AuthorityMessage() default "{}" ;   
	     
	 boolean isverification() default false;
	   
	 boolean verification() default false;
} 
