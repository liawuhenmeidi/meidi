package com.zhilibao.utill.bean;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.zhilibao.model.tax.TaxBasicMessage;
 
public class BeanUtill { 
	protected static Log logger = LogFactory.getLog(BeanUtill.class);   
     
	public static void main(String args[]){
		Object bean = new TaxBasicMessage(); 
		 
		//logger.info(getBeanName(TaxBasicMessage));  
  
		logger.info(getBeanPropertyList(bean));   
		logger.info(getBeanFilesList(bean));  
	}
	public static String getBeanName(Object bean){  
            
            Class clz = bean.getClass();  
            String clzStr =clz.getName();
            logger.info(clzStr); 
            
            //得到类名  
           // String beanName = clzStr.substring(clzStr.lastIndexOf(".")+1).toLowerCase();  
            return clzStr;   
    }  
  
    
      
    public static List<String> getBeanPropertyList(Object bean){   
            Class clz = bean.getClass();    
            Field[] strs = clz.getDeclaredFields();  
            List<String> propertyList = new ArrayList<String>();  
            for (int i = 0; i < strs.length; i++) {  
                String protype = strs[i].getType().toString();  
                propertyList.add(protype.substring(protype.lastIndexOf(".")+1)+"`"+strs[i].getName());  
            }  
            return propertyList;  
      
    }   
     
    public static String getBeanFilesList(Object bean){  
            Class clz = bean.getClass();  
            Field[] strs = clz.getDeclaredFields();  
            StringBuffer sb = new StringBuffer();  
            for (int i = 0; i < strs.length; i++) {  
                String protype = strs[i].getType().toString();  
                if (!strs[i].getName().equals("tableName")&&!strs[i].getType().equals("List")) {  
                   sb.append(strs[i].getName()+",");  
                }  
            }  
            sb.deleteCharAt(sb.toString().lastIndexOf(","));  
            return sb.toString();  
        
    }  
}
