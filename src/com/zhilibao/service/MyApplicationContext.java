package com.zhilibao.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyApplicationContext { 
          
    public static ApplicationContext getInstance(){
    	return  new ClassPathXmlApplicationContext("ApplicationContext.xml");   
    } 
 
}
 